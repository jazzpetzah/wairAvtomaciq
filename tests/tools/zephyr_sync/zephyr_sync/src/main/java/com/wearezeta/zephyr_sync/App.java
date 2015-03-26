package com.wearezeta.zephyr_sync;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.wearezeta.zephyr_sync.reporting.ReportGenerator;
import com.wearezeta.zephyr_sync.reporting.ReportModel;
import com.wearezeta.zephyr_sync.reporting.ReportModel.TestcaseGroup;
import com.wearezeta.zephyr_sync.storages.CucumberExecutionStatus;
import com.wearezeta.zephyr_sync.storages.GherkinFile;
import com.wearezeta.zephyr_sync.storages.ResultJSON;
import com.wearezeta.zephyr_sync.storages.ZephyrDB;
import com.wearezeta.zephyr_sync.storages.ZephyrExecutionStatus;
import com.wearezeta.zephyr_sync.storages.ZephyrTestCycle;
import com.wearezeta.zephyr_sync.storages.ZephyrTestPhase;

public class App {
	private static final String FEATURE_EXTENSION = "feature";
	private static final String MUTE_TAG = Testcase.MAGIC_TAG_PREFIX + "mute";

	private static final String ALL_TESTCASES_HEADER = "Testcases in .feature Files";
	private static final String ALL_EXECUTED_TESTCASES_HEADER = "Executed Testcases";
	private static final String MISSING_IDS_TESTCASES_HEADER = "Testcases Without IDs";
	private static final String CORRUPTED_IDS_TESTCASES_HEADER = "Testcases With Corrupted IDs";
	private static final String OLD_MUTED_TESTCASES = "Existing Muted Testcases";
	private static final String OLD_MUTED_TESTCASES_IN_ZEPHYR = "Existing Muted Testcases in Zephyr";
	private static final String NEWLY_MUTED_TESTCASES = "Newly Muted Testcases in Zephyr";
	private static final String NEWLY_UNMUTED_TESTCASES = "Newly Unmuted Testcases in Zephyr";
	private static final String NON_MUTED_TESTCASES = "Non-Muted Testcases (All Other)";

	private static final String JOB_URL_MUTED_TEMPLATE = "Muted by %s";
	private static final String JOB_URL_UNMUTED_TEMPLATE = "Unmuted by %s";
	private static final String JOB_URL_CHANGED_TEMPLATE = "Changed by %s";

	private static final String EXECUTION_TYPE_FEATURES_SYNC = "features_sync";
	private static final String EXECUTION_TYPE_RESULTS_SYNC = "results_sync";
	private static final String EXECUTION_TYPE_VERIFICATION_SYNC = "verification_sync";
	private static final String EXECUTION_TYPE_PHASE_SYNC = "phase_sync";
	private static final String EXECUTION_TYPE_PHASE_VERIFICATION = "phase_verification";
	private static final String EXECUTION_TYPE_ZEPHYR_DB_FIX = "zephyr_db_fix";

	private static String transformURLIntoLinks(String text) {
		final String urlValidationRegex = "(https?|ftp)://(www\\d?|[a-zA-Z0-9]+)?.[a-zA-Z0-9-]+(\\:|.)([a-zA-Z0-9.]+|(\\d+)?)([/?:].*)?";
		Pattern p = Pattern.compile(urlValidationRegex);
		Matcher m = p.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String found = m.group(0);
			m.appendReplacement(sb, "<a href='" + found + "'>"
					+ StringEscapeUtils.escapeXml(found) + "</a>");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static final String ID_LINK_TEMPLATE = "<a href='http://%s/flex?tcid=%s'>%s</a>";

	private static String getZephyrTcDetailsAsHTML(Testcase tc,
			String zephyrServer) {
		List<String> idLinks = new ArrayList<String>();
		Set<String> idsSet = Testcase.splitIds(tc.getId());
		for (String id : idsSet) {
			idLinks.add(String.format(ID_LINK_TEMPLATE, zephyrServer, id, id));
		}

		if ((tc instanceof ZephyrTestcase)
				&& ((ZephyrTestcase) tc).getAutomatedScriptPath() != null) {
			return String.format("[%s] %s -> <em>%s</em>", StringUtils.join(
					idLinks, " "), StringEscapeUtils.escapeXml(tc.getName()),
					transformURLIntoLinks(((ZephyrTestcase) tc)
							.getAutomatedScriptPath()));
		} else {
			return String.format("[%s] %s", StringUtils.join(idLinks, " "),
					StringEscapeUtils.escapeXml(tc.getName()));
		}
	}

	private static ReportModel prapareReportingModel(String title,
			Map<String, List<? extends Testcase>> data, String zephyrServer) {
		List<TestcaseGroup> tcGroups = new ArrayList<TestcaseGroup>();
		for (Map.Entry<String, List<? extends Testcase>> entry : data
				.entrySet()) {
			Map<String, Testcase> byNameTestcasesMapping = new HashMap<String, Testcase>();
			for (Testcase tc : entry.getValue()) {
				byNameTestcasesMapping.put(tc.getName(), tc);
			}
			List<String> details = new ArrayList<String>();
			List<String> sortedByName = new ArrayList<String>(
					byNameTestcasesMapping.keySet());
			Collections.sort(sortedByName);
			for (String name : sortedByName) {
				details.add(getZephyrTcDetailsAsHTML(
						byNameTestcasesMapping.get(name), zephyrServer));
			}
			tcGroups.add(new TestcaseGroup(entry.getKey(), details, details
					.size()));
		}
		return new ReportModel(title, tcGroups);
	}

	private static void syncAutomatedState(CucumberTestcase cucumberTC,
			ZephyrTestcase zephyrTC) {
		if (!zephyrTC.getIsAutomated()) {
			zephyrTC.setIsAutomated(true);
			zephyrTC.setAutomatedScriptName(cucumberTC.getName());
		}
		if (zephyrTC.getIsAutomated()
				&& !zephyrTC.getAutomatedScriptName().equals(
						cucumberTC.getName())) {
			zephyrTC.setAutomatedScriptName(cucumberTC.getName());
		}
	}

	private static void syncCucumberTCWithZephyr(CucumberTestcase cucumberTC,
			ZephyrTestcase zephyrTC) {
		syncAutomatedState(cucumberTC, zephyrTC);
		Set<String> currentCucumberTCTags = cucumberTC.getTags();
		final Set<String> currentZephyrTCTags = zephyrTC.getTags();
		if (currentZephyrTCTags.contains(MUTE_TAG)) {
			currentCucumberTCTags.add(MUTE_TAG);
			cucumberTC.setTags(currentCucumberTCTags);
		} else if (currentCucumberTCTags.contains(MUTE_TAG)) {
			currentCucumberTCTags.remove(MUTE_TAG);
			cucumberTC.setTags(currentCucumberTCTags);
		}
	}

	private static ReportModel syncFeatureFiles(String root, ZephyrDB zephyrDB)
			throws Exception {
		final List<ZephyrTestcase> zephyrTestcases = zephyrDB.getTestcases();

		File folder = new File(root);
		File[] listOfFiles = folder.listFiles();
		final List<CucumberTestcase> allGherkinTestcases = new ArrayList<CucumberTestcase>();
		List<CucumberTestcase> testcasesWithMissingIds = new ArrayList<CucumberTestcase>();
		List<CucumberTestcase> testcasesWithCorruptedIds = new ArrayList<CucumberTestcase>();
		List<ZephyrTestcase> mutedTestcases = new ArrayList<ZephyrTestcase>();
		List<CucumberTestcase> nonMutedTestcases = new ArrayList<CucumberTestcase>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isFile()
					|| !listOfFiles[i].getName().endsWith(
							"." + FEATURE_EXTENSION)) {
				continue;
			}
			GherkinFile gherkinScenarious = new GherkinFile(new File(root,
					listOfFiles[i].getName()).getAbsolutePath());
			final List<CucumberTestcase> currentTestcases = gherkinScenarious
					.getTestcases();
			allGherkinTestcases.addAll(gherkinScenarious.getTestcases());

			for (CucumberTestcase cucumberTC : currentTestcases) {
				if (cucumberTC.getId().length() > 0) {
					boolean isTCUntouched = true;
					for (String tcId : Testcase.splitIds(cucumberTC.getId())) {
						try {
							Long.parseLong(tcId);
						} catch (NumberFormatException e) {
							testcasesWithCorruptedIds.add(cucumberTC);
							isTCUntouched = false;
							break;
						}

						boolean zephyrTCFound = false;
						for (ZephyrTestcase zephyrTC : zephyrTestcases) {
							if (!zephyrTC.getId().equals(tcId)) {
								continue;
							}
							zephyrTCFound = true;
							syncCucumberTCWithZephyr(cucumberTC, zephyrTC);
							if (zephyrTC.getTags().contains(MUTE_TAG)) {
								mutedTestcases.add(zephyrTC);
								isTCUntouched = false;
							}
							break;
						}
						if (!zephyrTCFound) {
							testcasesWithCorruptedIds.add(cucumberTC);
							isTCUntouched = false;
							break;
						}
					}
					if (isTCUntouched) {
						nonMutedTestcases.add(cucumberTC);
					}
				} else {
					testcasesWithMissingIds.add(cucumberTC);
				}
			}
			gherkinScenarious.syncTestcases(currentTestcases);
		}
		zephyrDB.syncTestcases(zephyrTestcases);

		Map<String, List<? extends Testcase>> reportData = new LinkedHashMap<String, List<? extends Testcase>>();
		reportData.put(ALL_TESTCASES_HEADER, allGherkinTestcases);
		reportData.put(MISSING_IDS_TESTCASES_HEADER, testcasesWithMissingIds);
		reportData.put(CORRUPTED_IDS_TESTCASES_HEADER,
				testcasesWithCorruptedIds);
		reportData.put(OLD_MUTED_TESTCASES, mutedTestcases);
		reportData.put(NON_MUTED_TESTCASES, nonMutedTestcases);

		final String title = String.format("[%s] Pre-Execution Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData, zephyrDB.getServer());
	}

	private static boolean syncZephyrMutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedCucumberTestcase executedTC,
			String jobURL) {
		syncAutomatedState(executedTC, zephyrTC);
		Set<String> zephyrTCTags = zephyrTC.getTags();
		if (!zephyrTCTags.contains(MUTE_TAG)
				&& executedTC.getStatus() == CucumberExecutionStatus.Failed) {
			zephyrTCTags.add(MUTE_TAG);
			zephyrTC.setTags(zephyrTCTags);
			if (jobURL != null) {
				zephyrTC.setAutomatedScriptPath(String.format(
						JOB_URL_MUTED_TEMPLATE, jobURL));
			}
			return true;
		}
		return false;
	}

	private static ReportModel syncTestExecutionResults(String jsonReportPath,
			ZephyrDB zephyrDB, String jobURL) throws Exception {
		final List<ZephyrTestcase> zephyrTestcases = zephyrDB.getTestcases();

		ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedCucumberTestcase> executedTestcases = resultJSON
				.getTestcases();

		List<ExecutedCucumberTestcase> testcasesWithMissingIds = new ArrayList<ExecutedCucumberTestcase>();
		List<ExecutedCucumberTestcase> testcasesWithCorruptedIds = new ArrayList<ExecutedCucumberTestcase>();
		List<ZephyrTestcase> newlyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<ZephyrTestcase> previouslyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<CucumberTestcase> nonMutedTestcases = new ArrayList<CucumberTestcase>();
		for (ExecutedCucumberTestcase executedTC : executedTestcases) {
			if (executedTC.getId().length() > 0) {
				boolean isTCUntouched = true;
				for (String tcId : Testcase.splitIds(executedTC.getId())) {
					try {
						Long.parseLong(tcId);
					} catch (NumberFormatException e) {
						testcasesWithCorruptedIds.add(executedTC);
						isTCUntouched = false;
						break;
					}

					boolean zephyrTCFound = false;
					for (ZephyrTestcase zephyrTC : zephyrTestcases) {
						if (!zephyrTC.getId().equals(tcId)) {
							continue;
						}
						zephyrTCFound = true;
						if (zephyrTC.getTags().contains(MUTE_TAG)) {
							previouslyMutedTestcases.add(zephyrTC);
							isTCUntouched = false;
						} else if (syncZephyrMutedWithExecutedTC(zephyrTC,
								executedTC, jobURL)) {
							newlyMutedTestcases.add(zephyrTC);
							isTCUntouched = false;
						}
						break;
					}
					if (!zephyrTCFound) {
						testcasesWithCorruptedIds.add(executedTC);
						isTCUntouched = false;
						break;
					}
				}
				if (isTCUntouched) {
					nonMutedTestcases.add(executedTC);
				}
			} else {
				testcasesWithMissingIds.add(executedTC);
			}
		}
		zephyrDB.syncTestcases(zephyrTestcases);

		Map<String, List<? extends Testcase>> reportData = new LinkedHashMap<String, List<? extends Testcase>>();
		reportData.put(ALL_EXECUTED_TESTCASES_HEADER, executedTestcases);
		reportData.put(MISSING_IDS_TESTCASES_HEADER, testcasesWithMissingIds);
		reportData.put(CORRUPTED_IDS_TESTCASES_HEADER,
				testcasesWithCorruptedIds);
		reportData.put(OLD_MUTED_TESTCASES, previouslyMutedTestcases);
		reportData.put(NEWLY_MUTED_TESTCASES, newlyMutedTestcases);
		reportData.put(NON_MUTED_TESTCASES, nonMutedTestcases);

		printBriefReport(executedTestcases);

		final String title = String.format("[%s] Post-Execution Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData, zephyrDB.getServer());
	}

	private static void printBriefReport(
			final List<ExecutedCucumberTestcase> executedTestcases) {
		final int executedTCsCount = executedTestcases.size();
		int passedTCsCount = 0;
		int failedTCsCount = 0;
		int skippedTCsCount = 0;
		for (ExecutedCucumberTestcase tc : executedTestcases) {
			switch (tc.getStatus()) {
			case Failed:
				failedTCsCount++;
				break;
			case Passed:
				passedTCsCount++;
				break;
			case Skipped:
				skippedTCsCount++;
				break;
			default:
				break;
			}
		}
		System.out.println(String.format("%d tests passed out of %d",
				passedTCsCount, executedTCsCount));
		System.out.println(String.format("%d tests failed out of %d",
				failedTCsCount, executedTCsCount));
		System.out.println(String.format("%d tests skipped out of %d",
				skippedTCsCount, executedTCsCount));
	}

	private static boolean syncZephyrUnmutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedCucumberTestcase executedTC,
			String jobURL) {
		syncAutomatedState(executedTC, zephyrTC);
		if (executedTC.getStatus() == CucumberExecutionStatus.Passed) {
			Set<String> currentZephyrTCTags = zephyrTC.getTags();
			if (currentZephyrTCTags.contains(MUTE_TAG)) {
				currentZephyrTCTags.remove(MUTE_TAG);
				zephyrTC.setTags(currentZephyrTCTags);
				if (jobURL != null) {
					zephyrTC.setAutomatedScriptPath(String.format(
							JOB_URL_UNMUTED_TEMPLATE, jobURL));
				}
				return true;
			}
		}
		return false;
	}

	private static ReportModel syncTestVerificationResults(
			String jsonReportPath, ZephyrDB zephyrDB, String jobURL)
			throws Exception {
		final List<ZephyrTestcase> zephyrTestcases = zephyrDB.getTestcases();

		ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedCucumberTestcase> executedTestcases = resultJSON
				.getTestcases();

		List<ExecutedCucumberTestcase> testcasesWithMissingIds = new ArrayList<ExecutedCucumberTestcase>();
		List<ExecutedCucumberTestcase> testcasesWithCorruptedIds = new ArrayList<ExecutedCucumberTestcase>();
		List<ZephyrTestcase> newlyUnmutedTestcases = new ArrayList<ZephyrTestcase>();
		List<ZephyrTestcase> previouslyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<CucumberTestcase> nonMutedTestcases = new ArrayList<CucumberTestcase>();
		for (ExecutedCucumberTestcase executedTC : executedTestcases) {
			if (executedTC.getId().length() > 0) {
				boolean isTCUntouched = true;
				for (String tcId : Testcase.splitIds(executedTC.getId())) {
					try {
						Long.parseLong(tcId);
					} catch (NumberFormatException e) {
						testcasesWithCorruptedIds.add(executedTC);
						isTCUntouched = false;
						break;
					}

					boolean zephyrTCFound = false;
					for (ZephyrTestcase zephyrTC : zephyrTestcases) {
						if (!zephyrTC.getId().equals(tcId)) {
							continue;
						}
						zephyrTCFound = true;
						if (zephyrTC.getTags().contains(MUTE_TAG)) {
							previouslyMutedTestcases.add(zephyrTC);
							isTCUntouched = false;
						}
						if (syncZephyrUnmutedWithExecutedTC(zephyrTC,
								executedTC, jobURL)) {
							newlyUnmutedTestcases.add(zephyrTC);
							isTCUntouched = false;
						}
						break;
					}
					if (!zephyrTCFound) {
						testcasesWithCorruptedIds.add(executedTC);
						isTCUntouched = false;
						break;
					}
				}
				if (isTCUntouched) {
					nonMutedTestcases.add(executedTC);
				}
			} else {
				testcasesWithMissingIds.add(executedTC);
			}
		}
		zephyrDB.syncTestcases(zephyrTestcases);

		Map<String, List<? extends Testcase>> reportData = new LinkedHashMap<String, List<? extends Testcase>>();
		reportData.put(ALL_EXECUTED_TESTCASES_HEADER, executedTestcases);
		reportData.put(MISSING_IDS_TESTCASES_HEADER, testcasesWithMissingIds);
		reportData.put(CORRUPTED_IDS_TESTCASES_HEADER,
				testcasesWithCorruptedIds);
		reportData.put(OLD_MUTED_TESTCASES_IN_ZEPHYR, previouslyMutedTestcases);
		reportData.put(NEWLY_UNMUTED_TESTCASES, newlyUnmutedTestcases);
		reportData.put(NON_MUTED_TESTCASES, nonMutedTestcases);

		printBriefReport(executedTestcases);

		final String title = String.format(
				"[%s] Muted Tests Verification Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData, zephyrDB.getServer());
	}

	private static int syncPhaseResults(ZephyrDB zephyrDB,
			String jsonReportPath, ZephyrTestPhase dstPhase, String jobUrl)
			throws Exception {
		List<ExecutedZephyrTestcase> phaseTestcases = dstPhase.getTestcases();

		final ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedCucumberTestcase> executedCucumberTestcases = resultJSON
				.getTestcases();

		for (ExecutedZephyrTestcase phaseTC : phaseTestcases) {
			for (ExecutedCucumberTestcase executedCucumberTC : executedCucumberTestcases) {
				final Set<String> executedTCIds = Testcase
						.splitIds(executedCucumberTC.getId());
				if (executedTCIds.contains(phaseTC.getId())) {
					if (executedCucumberTC.getStatus() == CucumberExecutionStatus.Passed) {
						phaseTC.setExecutionStatus(ZephyrExecutionStatus.Pass);
					} else if (executedCucumberTC.getStatus() == CucumberExecutionStatus.Failed) {
						phaseTC.setExecutionStatus(ZephyrExecutionStatus.Fail);
					}
					if (jobUrl != null) {
						phaseTC.setExecutionComment(String.format(
								JOB_URL_CHANGED_TEMPLATE, jobUrl));
					}
					break;
				}
			}
		}

		return zephyrDB.syncPhaseResults(dstPhase);
	}

	private static String getCurrentDateTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date now = new Date();
		String strDate = sdf.format(now);
		return strDate;
	}

	private static final String PARAM_ZEPHYR_SERVER = "zephyr-server";
	private static final String PARAM_TYPE = "type";
	private static final String PARAM_HTML_REPORT_PATH = "html-report-path";
	private static final String PARAM_FEATURES_ROOT = "features-root";
	private static final String PARAM_JOB_URL = "job-url";
	private static final String PARAM_CYCLE_NAME = "cycle-name";
	private static final String PARAM_PHASE_NAME = "phase-name";
	private static final String PARAM_REPORT_PATH = "report-path";

	@SuppressWarnings("static-access")
	private static Options createCmdlineOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_TYPE)
				.withType(String.class)
				.withDescription(
						"[mandatory] sets execution type: either "
								+ EXECUTION_TYPE_FEATURES_SYNC + " or "
								+ EXECUTION_TYPE_RESULTS_SYNC + " or "
								+ EXECUTION_TYPE_VERIFICATION_SYNC + " or "
								+ EXECUTION_TYPE_PHASE_SYNC + " or "
								+ EXECUTION_TYPE_PHASE_VERIFICATION + " or "
								+ EXECUTION_TYPE_ZEPHYR_DB_FIX).hasArg()
				.isRequired().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_ZEPHYR_SERVER)
				.withType(String.class)
				.withDescription(
						"[mandatory] ip/domain name of Zephyr server (MySQL access should be already enabled for the current host)")
				.hasArg().isRequired().create());
		options.addOption(OptionBuilder.withLongOpt(PARAM_HTML_REPORT_PATH)
				.withType(String.class)
				.withDescription("full path to resulting html report").hasArg()
				.create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_FEATURES_ROOT)
				.withType(String.class)
				.withDescription(
						"the path to root folder, where Cucumber .feature files are located")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_REPORT_PATH)
				.withType(String.class)
				.withDescription(
						"the path to JSON report conatining suite execution resulsts")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_JOB_URL)
				.withType(String.class)
				.withDescription(
						"URL of Jenkins job, which generated this report (if available)")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_CYCLE_NAME)
				.withType(String.class)
				.withDescription(
						"the name of Zephyr execution cycle (make sure it is spelled correctly)")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_PHASE_NAME)
				.withType(String.class)
				.withDescription(
						"the name of Zephyr execution phase (make sure it is spelled correctly)")
				.hasArg().create());
		return options;
	}

	private static void verifyParameterExists(CommandLine cmdLine,
			String paramName) {
		if (!cmdLine.hasOption(paramName)) {
			System.out.println(String.format("Please provide '%s' option",
					paramName));
			System.exit(1);
		}
	}

	private static void verifyFileParameterExists(CommandLine cmdLine,
			String paramName) {
		verifyParameterExists(cmdLine, paramName);
		if (!new File(cmdLine.getOptionValue(paramName)).exists()) {
			System.out.println(String.format(
					"Please provide valid '%s' option value", paramName));
			System.exit(1);
		}
	}

	private static ReportModel executeSyncFeaturesAction(CommandLine cmdLine,
			ZephyrDB zephyrDB) throws Exception {
		verifyFileParameterExists(cmdLine, PARAM_FEATURES_ROOT);
		return syncFeatureFiles(cmdLine.getOptionValue(PARAM_FEATURES_ROOT),
				zephyrDB);
	}

	private static ReportModel executeSyncResultsAction(CommandLine cmdLine,
			ZephyrDB zephyrDB) throws Exception {
		verifyFileParameterExists(cmdLine, PARAM_REPORT_PATH);
		if (cmdLine.getOptionValue(PARAM_TYPE).equals(
				EXECUTION_TYPE_RESULTS_SYNC)) {
			return syncTestExecutionResults(
					cmdLine.getOptionValue(PARAM_REPORT_PATH), zephyrDB,
					cmdLine.getOptionValue(PARAM_JOB_URL));
		} else {
			return syncTestVerificationResults(
					cmdLine.getOptionValue(PARAM_REPORT_PATH), zephyrDB,
					cmdLine.getOptionValue(PARAM_JOB_URL));
		}
	}

	private static int executeSyncPhaseAction(CommandLine cmdLine,
			ZephyrDB zephyrDB) throws Exception {
		verifyFileParameterExists(cmdLine, PARAM_REPORT_PATH);

		return syncPhaseResults(zephyrDB,
				cmdLine.getOptionValue(PARAM_REPORT_PATH),
				executeVerifyPhaseAction(cmdLine, zephyrDB),
				cmdLine.getOptionValue(PARAM_JOB_URL));
	}

	private static ZephyrTestPhase executeVerifyPhaseAction(
			CommandLine cmdLine, ZephyrDB zephyrDB) throws Exception {
		verifyParameterExists(cmdLine, PARAM_CYCLE_NAME);
		verifyParameterExists(cmdLine, PARAM_PHASE_NAME);

		ZephyrTestCycle dstCycle = zephyrDB.getTestCycle(cmdLine
				.getOptionValue(PARAM_CYCLE_NAME));
		return dstCycle
				.getPhaseByName(cmdLine.getOptionValue(PARAM_PHASE_NAME));
	}

	private static Map<String, Integer> executeFixZephyrDBAction(
			CommandLine cmdLine, ZephyrDB zephyrDB) throws Exception {
		return zephyrDB.fixLostTestcases();
	}

	public static void main(String[] args) throws Exception {
		final Options options = createCmdlineOptions();
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new PosixParser();

		try {
			CommandLine line = parser.parse(options, args);
			final String executionType = line.getOptionValue(PARAM_TYPE);
			final ZephyrDB zephyrDB = new ZephyrDB(
					line.getOptionValue(PARAM_ZEPHYR_SERVER));
			if (executionType.equals(EXECUTION_TYPE_FEATURES_SYNC)
					|| executionType.equals(EXECUTION_TYPE_RESULTS_SYNC)
					|| executionType.equals(EXECUTION_TYPE_VERIFICATION_SYNC)) {
				verifyParameterExists(line, PARAM_HTML_REPORT_PATH);
				final String htmlReportPath = line
						.getOptionValue(PARAM_HTML_REPORT_PATH);
				if (executionType.equals(EXECUTION_TYPE_FEATURES_SYNC)) {
					ReportGenerator.generate(
							executeSyncFeaturesAction(line, zephyrDB),
							htmlReportPath);
				} else {
					ReportGenerator.generate(
							executeSyncResultsAction(line, zephyrDB),
							htmlReportPath);
				}
				System.out
						.println("Execution report has been successfully saved as "
								+ htmlReportPath);
			} else if (executionType.equals(EXECUTION_TYPE_PHASE_SYNC)) {
				final int updatedTestcasesCount = executeSyncPhaseAction(line,
						zephyrDB);
				if (updatedTestcasesCount > 0) {
					System.out
							.println(String
									.format("Successfully updated %d test case(s) in Zephyr phase '%s' "
											+ "with automated test execution results taken from '%s'",
											updatedTestcasesCount,
											line.getOptionValue(PARAM_PHASE_NAME),
											line.getOptionValue(PARAM_REPORT_PATH)));
				} else {
					System.out
							.println(String
									.format("There are no automated test cases in Zephyr phase '%s' to update",
											line.getOptionValue(PARAM_PHASE_NAME)));
				}
			} else if (executionType.equals(EXECUTION_TYPE_PHASE_VERIFICATION)) {
				ZephyrTestPhase phase = executeVerifyPhaseAction(line, zephyrDB);
				System.out
						.println(String
								.format("The phase '%s' belonging to execution cycle '%s' "
										+ "has been successfully detected in Zephyr and is scheduled to '%s'",
										line.getOptionValue(PARAM_PHASE_NAME),
										line.getOptionValue(PARAM_CYCLE_NAME),
										phase.getScheduledTo().toString()));
			} else if (executionType.equals(EXECUTION_TYPE_ZEPHYR_DB_FIX)) {
				final Map<String, Integer> countOfFixedTestcases = executeFixZephyrDBAction(
						line, zephyrDB);
				boolean isDBOk = true;
				for (Entry<String, Integer> statsEntry : countOfFixedTestcases
						.entrySet()) {
					final String statsEntryName = statsEntry.getKey();
					final int fixedTCCount = statsEntry.getValue();
					if (fixedTCCount > 0) {
						System.out
								.println(String
										.format("Successfully restored %d test case(s) in '%s' module of Zephyr database",
												fixedTCCount, statsEntryName));
						isDBOk = false;
					}
				}
				if (isDBOk) {
					System.out
							.println("Zephyr database is in a good shape and does not require any fixes");
				}
			} else {
				formatter.printHelp("zephyr_sync", options);
				System.exit(1);
			}
			System.exit(0);
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage()
					+ "\n");
			formatter.printHelp("zephyr_sync", options);
			System.exit(1);
		}
	}
}
