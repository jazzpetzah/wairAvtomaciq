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
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.wearezeta.zephyr_sync.reporting.ReportGenerator;
import com.wearezeta.zephyr_sync.reporting.ReportModel;
import com.wearezeta.zephyr_sync.reporting.ReportModel.TestcaseGroup;
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

	private static ReportModel prapareReportingModel(String title,
			Map<String, List<? extends Testcase>> data) {
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
				details.add(String.format("[%s] %s", byNameTestcasesMapping
						.get(name).getId(), name));
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
		List<CucumberTestcase> mutedTestcases = new ArrayList<CucumberTestcase>();
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
					for (String tcId : cucumberTC.getId().split("\\s+")) {
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
								mutedTestcases.add(cucumberTC);
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
		return prapareReportingModel(title, reportData);
	}

	private static boolean syncZephyrMutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedCucumberTestcase executedTC,
			String jobURL) {
		syncAutomatedState(executedTC, zephyrTC);
		Set<String> zephyrTCTags = zephyrTC.getTags();
		if (!zephyrTCTags.contains(MUTE_TAG) && executedTC.getIsFailed()) {
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
				for (String tcId : executedTC.getId().split("\\s+")) {
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

		final String title = String.format("[%s] Post-Execution Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData);
	}

	private static boolean syncZephyrUnmutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedCucumberTestcase executedTC,
			String jobURL) {
		syncAutomatedState(executedTC, zephyrTC);
		if (executedTC.getIsPassed()) {
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
				for (String tcId : executedTC.getId().split("\\s+")) {
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

		final String title = String.format(
				"[%s] Muted Tests Verification Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData);
	}

	private static void syncPhaseResults(ZephyrDB zephyrDB,
			String jsonReportPath, ZephyrTestPhase dstPhase, String jobUrl)
			throws Exception {
		List<ExecutedZephyrTestcase> phaseTestcases = dstPhase.getTestcases();

		final ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedCucumberTestcase> executedCucumberTestcases = resultJSON
				.getTestcases();

		for (ExecutedCucumberTestcase executedCucumberTC : executedCucumberTestcases) {
			for (ExecutedZephyrTestcase phaseTC : phaseTestcases) {
				if (executedCucumberTC.getId().equals(phaseTC.getId())) {
					if (executedCucumberTC.getIsPassed()) {
						phaseTC.setExecutionStatus(ZephyrExecutionStatus.Pass);
					} else if (executedCucumberTC.getIsFailed()) {
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

		zephyrDB.syncPhaseResults(dstPhase);
	}

	private static String getCurrentDateTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date now = new Date();
		String strDate = sdf.format(now);
		return strDate;
	}

	@SuppressWarnings("static-access")
	private static Options createCmdlineOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withLongOpt("type")
				.withType(String.class)
				.withDescription(
						"[mandatory] sets execution type: either "
								+ EXECUTION_TYPE_FEATURES_SYNC + " or "
								+ EXECUTION_TYPE_RESULTS_SYNC + " or "
								+ EXECUTION_TYPE_VERIFICATION_SYNC + " or "
								+ EXECUTION_TYPE_PHASE_SYNC + " or "
								+ EXECUTION_TYPE_PHASE_VERIFICATION).hasArg()
				.isRequired().create());
		options.addOption(OptionBuilder
				.withLongOpt("zephyr-server")
				.withType(String.class)
				.withDescription(
						"[mandatory] ip/domain name of Zephyr server (MySQL access should be already enabled for the current host)")
				.hasArg().isRequired().create());
		options.addOption(OptionBuilder.withLongOpt("html-report-path")
				.withType(String.class)
				.withDescription("full path to resulting html report").hasArg()
				.create());
		options.addOption(OptionBuilder
				.withLongOpt("features-root")
				.withType(String.class)
				.withDescription(
						"the path to root folder, where Cucumber .feature files are located")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("report-path")
				.withType(String.class)
				.withDescription(
						"the path to JSON report conatining suite execution resulsts")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("job-url")
				.withType(String.class)
				.withDescription(
						"URL of Jenkins job, which generated this report (if available)")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("cycle-name")
				.withType(String.class)
				.withDescription(
						"the name of Zephyr execution cycle (make sure it is spelled correctly)")
				.hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("phase-name")
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
		verifyFileParameterExists(cmdLine, "features-root");
		return syncFeatureFiles(cmdLine.getOptionValue("features-root"),
				zephyrDB);
	}

	private static ReportModel executeSyncResultsAction(CommandLine cmdLine,
			ZephyrDB zephyrDB) throws Exception {
		verifyFileParameterExists(cmdLine, "report-path");
		if (cmdLine.getOptionValue("type").equals(EXECUTION_TYPE_RESULTS_SYNC)) {
			return syncTestExecutionResults(
					cmdLine.getOptionValue("report-path"), zephyrDB,
					cmdLine.getOptionValue("job-url"));
		} else {
			return syncTestVerificationResults(
					cmdLine.getOptionValue("report-path"), zephyrDB,
					cmdLine.getOptionValue("job-url"));
		}
	}

	private static void executeSyncPhaseAction(CommandLine cmdLine,
			ZephyrDB zephyrDB) throws Exception {
		verifyFileParameterExists(cmdLine, "report-path");

		syncPhaseResults(zephyrDB, cmdLine.getOptionValue("report-path"),
				executeVerifyPhaseAction(cmdLine, zephyrDB),
				cmdLine.getOptionValue("job-url"));
	}

	private static ZephyrTestPhase executeVerifyPhaseAction(
			CommandLine cmdLine, ZephyrDB zephyrDB) throws Exception {
		verifyParameterExists(cmdLine, "cycle-name");
		verifyParameterExists(cmdLine, "phase-name");

		ZephyrTestCycle dstCycle = zephyrDB.getTestCycle(cmdLine
				.getOptionValue("cycle-name"));
		return dstCycle.getPhaseByName(cmdLine.getOptionValue("phase-name"));
	}

	public static void main(String[] args) throws Exception {
		final Options options = createCmdlineOptions();
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new PosixParser();

		try {
			CommandLine line = parser.parse(options, args);
			final String executionType = line.getOptionValue("type");
			final ZephyrDB zephyrDB = new ZephyrDB(
					line.getOptionValue("zephyr-server"));
			if (executionType.equals(EXECUTION_TYPE_FEATURES_SYNC)
					|| executionType.equals(EXECUTION_TYPE_RESULTS_SYNC)
					|| executionType.equals(EXECUTION_TYPE_VERIFICATION_SYNC)) {
				verifyParameterExists(line, "html-report-path");
				final String htmlReportPath = line
						.getOptionValue("html-report-path");
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
				executeSyncPhaseAction(line, zephyrDB);
				System.out.println(String.format(
						"Successfully updated Zephyr phase '%s' "
								+ "with automated test execution results",
						line.getOptionValue("phase-name")));
			} else if (executionType.equals(EXECUTION_TYPE_PHASE_VERIFICATION)) {
				ZephyrTestPhase phase = executeVerifyPhaseAction(line, zephyrDB);
				System.out
						.println(String
								.format("The phase named '%s' has been successfully detected in Zephyr and is scheduled to '%s'",
										line.getOptionValue("phase-name"),
										phase.getScheduledTo().toString()));
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
