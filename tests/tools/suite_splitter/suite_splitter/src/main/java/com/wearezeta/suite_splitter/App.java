package com.wearezeta.suite_splitter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.wearezeta.suite_splitter.reporting.ReportGenerator;
import com.wearezeta.suite_splitter.reporting.ReportModel;
import com.wearezeta.suite_splitter.storages.GherkinFile;
import com.wearezeta.suite_splitter.storages.ResultJSON;

public class App {
	private static final String FEATURE_EXTENSION = "feature";
	private static final String TAG_MARKER = "@";

	private static final String MODE_SPLIT = "split";
	private static final String MODE_JOIN = "join";

	private static final String PART_NAME_TEMPLATE = "%s_part_%s";
	private static final String FOLDER_PART_NAME_TEMPLATE = "feature_set_%s";

	private static List<GherkinFile> getFeatures(final String root)
			throws Throwable {
		List<GherkinFile> result = new ArrayList<GherkinFile>();
		File folder = new File(root);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isFile()
					|| !listOfFiles[i].getName().endsWith(
							"." + FEATURE_EXTENSION)) {
				continue;
			}
			result.add(new GherkinFile(listOfFiles[i].getCanonicalPath()));
		}

		return result;
	}

	private static Map<String, Integer> splitFeaturesIntoFixedSizeContainers(
			List<GherkinFile> existingFeatures, String dstRoot,
			Set<String> includeTags, Set<String> excludeTags, int divider)
			throws Throwable {
		int testcasesCount = 0;
		for (GherkinFile existingFeature : existingFeatures) {
			testcasesCount += existingFeature.getTestcases(includeTags,
					excludeTags).size();
		}
		final int maxTestcasesCountPerFeature = testcasesCount / divider + 1;

		Map<String, Integer> splittedFeaturesMapping = new LinkedHashMap<String, Integer>();
		for (GherkinFile existingFeature : existingFeatures) {
			final String currentPath = existingFeature.getPath();
			List<? extends Testcase> currentTestcases = existingFeature
					.getTestcases(includeTags, excludeTags);
			if (currentTestcases.size() == 0) {
				continue;
			}
			if (currentTestcases.size() <= maxTestcasesCountPerFeature) {
				final String splittedFilePath = new File(dstRoot, new File(
						currentPath).getName()).getCanonicalPath();
				existingFeature.saveTestcasesAs(currentTestcases,
						splittedFilePath);
				splittedFeaturesMapping.put(splittedFilePath,
						currentTestcases.size());
			} else {
				int partsCount;
				if (currentTestcases.size() % maxTestcasesCountPerFeature == 0) {
					partsCount = currentTestcases.size()
							/ maxTestcasesCountPerFeature;
				} else {
					partsCount = currentTestcases.size()
							/ maxTestcasesCountPerFeature + 1;
				}
				for (int partNumber = 0; partNumber < partsCount; partNumber++) {
					final String splittedFilePath = new File(dstRoot,
							String.format(PART_NAME_TEMPLATE, FilenameUtils
									.removeExtension(new File(currentPath)
											.getName()), partNumber + 1)
									+ "." + FEATURE_EXTENSION)
							.getCanonicalPath();
					int endIndex;
					if ((partNumber + 1) * maxTestcasesCountPerFeature > currentTestcases
							.size()) {
						endIndex = currentTestcases.size();
					} else {
						endIndex = (partNumber + 1)
								* maxTestcasesCountPerFeature;
					}
					List<? extends Testcase> testcasesToSave = currentTestcases
							.subList(partNumber * maxTestcasesCountPerFeature,
									endIndex);
					existingFeature.saveTestcasesAs(testcasesToSave,
							splittedFilePath);
					splittedFeaturesMapping.put(splittedFilePath,
							testcasesToSave.size());
				}
			}
		}
		return splittedFeaturesMapping;
	}

	private static void updateStatistics(String containerPath,
			List<String> containingItems, ReportModel model) throws Throwable {
		Map<String, List<? extends Testcase>> testcasesInContainer = new LinkedHashMap<String, List<? extends Testcase>>();
		for (String containingItem : containingItems) {
			testcasesInContainer.put(containingItem, new GherkinFile(
					containingItem).getTestcases());
		}
		model.addFeaturesPart(containerPath, testcasesInContainer);
	}

	private static Set<String> normalizeTags(final String tags) {
		if (tags == null) {
			return null;
		}
		Set<String> result = new LinkedHashSet<String>();
		for (String tag : tags.split(",")) {
			if (tag.startsWith(TAG_MARKER)) {
				result.add(tag.trim());
			} else {
				result.add(TAG_MARKER + tag.trim());
			}
		}
		return result;
	}

	private static ReportModel splitFeatures(
			List<GherkinFile> existingFeatures, String dstRoot,
			String includeTags, String excludeTags, int divider)
			throws Throwable {
		ReportModel model = new ReportModel();

		Map<String, Integer> splittedFeatures = splitFeaturesIntoFixedSizeContainers(
				existingFeatures, dstRoot, normalizeTags(includeTags),
				normalizeTags(excludeTags), divider);
		int testcasesCount = 0;
		for (Entry<String, Integer> entry : splittedFeatures.entrySet()) {
			testcasesCount += entry.getValue();
		}
		int folderNumber = 0;
		final int maxTestcasesCountInFolder = testcasesCount / divider + 1;
		
		Set<String> processedFeatures = new HashSet<String>();
		while (processedFeatures.size() < splittedFeatures.size()) {
			final File currentFolder = new File(dstRoot, String.format(
					FOLDER_PART_NAME_TEMPLATE, folderNumber + 1));
			if (currentFolder.isDirectory()) {
				FileUtils.deleteDirectory(currentFolder);
			}
			if (!currentFolder.mkdirs()) {
				throw new RuntimeException("Failed to create "
						+ currentFolder.getCanonicalPath() + " folder");
			}
			int testcasesCountInFolder = 0;
			List<String> featuresInFolder = new ArrayList<String>();
			for (String srcPath : splittedFeatures.keySet()) {
				if (processedFeatures.contains(srcPath)) {
					continue;
				}
				final int tcCount = splittedFeatures.get(srcPath);
				// Do not create too much parted folders
				if (folderNumber + 1 < divider
						&& testcasesCountInFolder + tcCount > maxTestcasesCountInFolder) {
					continue;
				}
				File srcFile = new File(srcPath);
				File dstFile = new File(currentFolder.getCanonicalPath(),
						srcFile.getName());
				srcFile.renameTo(dstFile);
				featuresInFolder.add(dstFile.getCanonicalPath());
				processedFeatures.add(srcPath);

				testcasesCountInFolder += tcCount;
			}
			updateStatistics(currentFolder.getCanonicalPath(),
					featuresInFolder, model);

			folderNumber++;
		}

		return model;
	}

	private static Set<String> normalizeFilesList(String inList) {
		Set<String> result = new LinkedHashSet<String>();
		for (String rawPath : inList.split(",")) {
			String path = rawPath.trim();
			if (!new File(path).exists()) {
				System.out.println(String.format(
						"The file '%s' does not exist", path));
				System.exit(1);
			}
			result.add(path);
		}
		return result;
	}

	@SuppressWarnings("static-access")
	private static Options createCmdlineOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withLongOpt("mode")
				.withType(String.class)
				.withDescription(
						String.format(
								"sets working mode. Possible values are: %s (default) and %s",
								MODE_SPLIT, MODE_JOIN)).hasArg().create());
		options.addOption(OptionBuilder.withLongOpt("verbose")
				.withDescription("Be more tolkative").create());
		// split mode options
		options.addOption(OptionBuilder
				.withLongOpt("features-root")
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory in %s mode] the path to root folder, where Cucumber .feature files are located",
								MODE_SPLIT)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("dst-root")
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory in %s mode] the path to destination folder, where splitted Cucumber .feature files should be located",
								MODE_SPLIT)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("devices-count")
				.withType(Integer.class)
				.withDescription(
						String.format(
								"[mandatory in %s mode] the number of devices to split feature files between. Should be greater than zero",
								MODE_SPLIT)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("include-tags")
				.withType(String.class)
				.withDescription(
						String.format(
								"[optional in %s mode] comma separated list of tags. All test cases, which are not marked by these tags, will be excluded",
								MODE_SPLIT)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("exclude-tags")
				.withType(String.class)
				.withDescription(
						String.format(
								"[optional in %s mode] comma separated list of tags. All test cases, which are marked by these tags, will be excluded. This parameter has priority over include-tags",
								MODE_SPLIT)).hasArg().create());
		// join mode options
		options.addOption(OptionBuilder
				.withLongOpt("result-report")
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory in %s mode] full path to resulting JSON report",
								MODE_JOIN)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt("report-parts")
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory in %s mode] comma separated list of paths to parted JSON reports",
								MODE_JOIN)).hasArg().create());
		return options;
	}

	private static String readRequiredOption(CommandLine line, String optionName) {
		String value = line.getOptionValue(optionName);
		if (value == null) {
			System.out.println(String.format(
					"Please provide valid value to %s option", optionName));
			System.exit(1);
		}
		return value;
	}

	public static void main(String[] args) throws Throwable {
		final Options options = createCmdlineOptions();
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new PosixParser();

		try {
			CommandLine line = parser.parse(options, args);

			if (line.getOptionValue("mode") == null
					|| line.getOptionValue("mode").equals(MODE_SPLIT)) {
				final String featuresRoot = line
						.getOptionValue("features-root");
				if (featuresRoot == null
						|| !new File(featuresRoot).isDirectory()) {
					System.out
							.println("Please provide valid value to features-root option");
					System.exit(1);
				}
				final String dstRoot = readRequiredOption(line, "dst-root");
				final String devicesCountStr = readRequiredOption(line,
						"devices-count");
				final int devicesCount = Integer.parseInt(devicesCountStr);
				String includeTags = line.getOptionValue("include-tags");
				if (includeTags.equals("null")) {
					includeTags = null;
				}
				String excludeTags = line.getOptionValue("exclude-tags");
				if (excludeTags.equals("null")) {
					excludeTags = null;
				}

				ReportModel reportDataModel = splitFeatures(
						getFeatures(featuresRoot), dstRoot, includeTags,
						excludeTags, devicesCount);
				final int partsCount = reportDataModel.getSplittedFeatures()
						.size();
				if (partsCount > 0) {
					if (line.hasOption("verbose")) {
						ReportGenerator.generate(reportDataModel);
					} else {
						System.out.println(String.format(
								"Successfully split features into %d parts",
								partsCount));
					}
				} else {
					System.out
							.println("There are no any test cases to split in the given scenarios");
				}
			} else if (line.getOptionValue("mode") != null
					&& line.getOptionValue("mode").equals(MODE_JOIN)) {
				final String resultReportPath = readRequiredOption(line,
						"result-report");
				final String srcFilesPaths = readRequiredOption(line,
						"report-parts");
				Set<String> reportsToJoin = normalizeFilesList(srcFilesPaths);
				ResultJSON bigReport = ResultJSON.createFromSetOfResults(
						resultReportPath, reportsToJoin);
				System.out.println(String.format(
						"Successfully joined %d parted reports into %s",
						reportsToJoin.size(), bigReport.getPath()));
			} else {
				System.out.println(String.format(
						"Unexpected mode parameter value '%s'",
						line.getOptionValue("mode")));
				formatter.printHelp("suite_splitter", options);
				System.exit(1);
			}
			System.exit(0);
		} catch (ParseException exp) {
			System.out.println("Unexpected exception: " + exp.getMessage());
			formatter.printHelp("suite_splitter", options);
			System.exit(1);
		}
	}
}
