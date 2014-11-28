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

public class App {
	private static final String FEATURE_EXTENSION = "feature";
	private static final String MUTE_TAG = Testcase.MAGIC_TAG_PREFIX + "mute";

	private static final String ALL_TESTCASES_HEADER = "Testcases in .feature Files";
	private static final String ALL_EXECUTED_TESTCASES_HEADER = "Executed Testcases";
	private static final String MISSING_IDS_TESTCASES_HEADER = "Testcases Without IDs";
	private static final String CORRUPTED_IDS_TESTCASES_HEADER = "Testcases With Corrupted IDs";
	private static final String OLD_MUTED_TESTCASES = "Existing Muted Testcases";
	private static final String NEWLY_MUTED_TESTCASES = "Newly Muted Testcases in Zephyr";
	private static final String NEWLY_UNMUTED_TESTCASES = "Newly Unmuted Testcases in Zephyr";
	private static final String NON_MUTED_TESTCASES = "Non-Muted Testcases (All Other)";

	private static final String EXECUTION_TYPE_FEATURES_SYNC = "features_sync";
	private static final String EXECUTION_TYPE_RESULTS_SYNC = "results_sync";
	private static final String EXECUTION_TYPE_VERIFICATION_SYNC = "verification_sync";

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

	private static void syncCucumberTCWithZephyr(CucumberTestcase cucumberTC,
			ZephyrTestcase zephyrTC) {
		if (!zephyrTC.getIsAutomated()) {
			zephyrTC.setIsAutomated(true);
		}
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

	private static ReportModel syncFeatureFiles(String root, String zephyrServer)
			throws Throwable {
		ZephyrDB zephyrDB = new ZephyrDB(zephyrServer);
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

	private static Boolean syncZephyrMutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedTestcase executedTC) {
		if (!zephyrTC.getIsAutomated()) {
			zephyrTC.setIsAutomated(true);
		}
		Set<String> zephyrTCTags = zephyrTC.getTags();
		if (!zephyrTCTags.contains(MUTE_TAG) && executedTC.getIsFailed()) {
			zephyrTCTags.add(MUTE_TAG);
			zephyrTC.setTags(zephyrTCTags);
			return true;
		}
		return false;
	}

	private static ReportModel syncTestExecutionResults(String jsonReportPath,
			String zephyrServer) throws Throwable {
		ZephyrDB zephyrDB = new ZephyrDB(zephyrServer);
		final List<ZephyrTestcase> zephyrTestcases = zephyrDB.getTestcases();

		ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedTestcase> executedTestcases = resultJSON
				.getTestcases();

		List<ExecutedTestcase> testcasesWithMissingIds = new ArrayList<ExecutedTestcase>();
		List<ExecutedTestcase> testcasesWithCorruptedIds = new ArrayList<ExecutedTestcase>();
		List<ZephyrTestcase> newlyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<ZephyrTestcase> previouslyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<CucumberTestcase> nonMutedTestcases = new ArrayList<CucumberTestcase>();
		for (ExecutedTestcase executedTC : executedTestcases) {
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
								executedTC)) {
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

	private static Boolean syncZephyrUnmutedWithExecutedTC(
			ZephyrTestcase zephyrTC, ExecutedTestcase executedTC) {
		if (!zephyrTC.getIsAutomated()) {
			zephyrTC.setIsAutomated(true);
		}
		if (executedTC.getIsPassed()) {
			Set<String> currentZephyrTCTags = zephyrTC.getTags();
			if (currentZephyrTCTags.contains(MUTE_TAG)) {
				currentZephyrTCTags.remove(MUTE_TAG);
				zephyrTC.setTags(currentZephyrTCTags);
				return true;
			}
		}
		return false;
	}

	private static ReportModel syncTestVerificationResults(
			String jsonReportPath, String zephyrServer) throws Throwable {
		ZephyrDB zephyrDB = new ZephyrDB(zephyrServer);
		final List<ZephyrTestcase> zephyrTestcases = zephyrDB.getTestcases();

		ResultJSON resultJSON = new ResultJSON(jsonReportPath);
		final List<ExecutedTestcase> executedTestcases = resultJSON
				.getTestcases();

		List<ExecutedTestcase> testcasesWithMissingIds = new ArrayList<ExecutedTestcase>();
		List<ExecutedTestcase> testcasesWithCorruptedIds = new ArrayList<ExecutedTestcase>();
		List<ZephyrTestcase> newlyUnmutedTestcases = new ArrayList<ZephyrTestcase>();
		List<ZephyrTestcase> previouslyMutedTestcases = new ArrayList<ZephyrTestcase>();
		List<CucumberTestcase> nonMutedTestcases = new ArrayList<CucumberTestcase>();
		for (ExecutedTestcase executedTC : executedTestcases) {
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
								executedTC)) {
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
		reportData.put(OLD_MUTED_TESTCASES, previouslyMutedTestcases);
		reportData.put(NEWLY_UNMUTED_TESTCASES, newlyUnmutedTestcases);
		reportData.put(NON_MUTED_TESTCASES, nonMutedTestcases);

		final String title = String.format(
				"[%s] Muted Tests Verification Report",
				getCurrentDateTimeStamp());
		return prapareReportingModel(title, reportData);
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
								+ EXECUTION_TYPE_VERIFICATION_SYNC).hasArg()
				.isRequired().create());
		options.addOption(OptionBuilder
				.withLongOpt("zephyr-server")
				.withType(String.class)
				.withDescription(
						"[mandatory] ip/domain name of Zephyr server (MySQL access should be already enabled for the current host)")
				.hasArg().isRequired().create());
		options.addOption(OptionBuilder
				.withLongOpt("html-report-path")
				.withType(String.class)
				.withDescription(
						"[mandatory] full path to resulting html report")
				.hasArg().isRequired().create());
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
		return options;
	}

	public static void main(String[] args) throws Throwable {
		final Options options = createCmdlineOptions();
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new PosixParser();

		try {
			CommandLine line = parser.parse(options, args);

			final String executionType = line.getOptionValue("type");
			final String zephyrServer = line.getOptionValue("zephyr-server");
			final String htmlReportPath = line
					.getOptionValue("html-report-path");

			ReportModel reportDataModel = null;
			if (executionType.equals(EXECUTION_TYPE_FEATURES_SYNC)) {
				if (!line.hasOption("features-root")) {
					System.out.println("Please provide 'features-root' option");
					System.exit(1);
				}
				final String featuresRoot = line
						.getOptionValue("features-root");
				if (!new File(featuresRoot).exists()) {
					System.out
							.println("Please provide valid 'features-root' option value");
					System.exit(1);
				}
				reportDataModel = syncFeatureFiles(featuresRoot, zephyrServer);
			} else if (executionType.equals(EXECUTION_TYPE_RESULTS_SYNC)
					|| executionType.equals(EXECUTION_TYPE_VERIFICATION_SYNC)) {
				if (!line.hasOption("report-path")) {
					System.out.println("Please provide 'report-path' option");
					System.exit(1);
				}
				final String reportPath = line.getOptionValue("report-path");
				if (!new File(reportPath).exists()) {
					System.out
							.println("Please provide valid 'report-path' option value");
					System.exit(1);
				}
				if (executionType.equals(EXECUTION_TYPE_RESULTS_SYNC)) {
					reportDataModel = syncTestExecutionResults(reportPath,
							zephyrServer);
				} else {
					reportDataModel = syncTestVerificationResults(reportPath,
							zephyrServer);
				}
			} else {
				formatter.printHelp("zephyr_sync", options);
				System.exit(1);
			}
			ReportGenerator.generate(reportDataModel, htmlReportPath);
			System.out
					.println("Execution report has been successfully saved as "
							+ htmlReportPath);
			System.exit(0);
		} catch (ParseException exp) {
			formatter.printHelp("zephyr_sync", options);
			System.out.println("Unexpected exception:" + exp.getMessage());
		}
	}
}
