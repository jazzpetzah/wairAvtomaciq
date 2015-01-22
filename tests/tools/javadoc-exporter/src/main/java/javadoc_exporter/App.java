package javadoc_exporter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javadoc_exporter.confluence_rest_api.ConfluenceAPIWrappers;
import javadoc_exporter.model.StepsContainer;
import javadoc_exporter.transformers.InputTransformer;
import javadoc_exporter.transformers.OutputTransformer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class App {
	private static final String PARAM_NAME_MODE = "mode";

	private static final String MODE_TRANSFORM = "convert";
	private static final String PARAM_NAME_SRC_HTML = "src-html";
	private static final String PARAM_NAME_DST_ROOT = "dst-root";

	private static final String MODE_PUBLISH = "publish";
	private static final String PARAM_NAME_SRC_ROOT = "src-root";
	private static final String PARAM_NAME_SPACE_KEY = "space-key";
	private static final String PARAM_NAME_PARENT_PAGE_ID = "parent-page-id";

	private static void transformJavadocToConfluence(String srcPath,
			String dstRoot) throws Exception {
		WebDriver driver = new HtmlUnitDriver();
		StepsContainer container = null;
		try {
			driver.get(String.format("file:///%s", srcPath));
			container = new InputTransformer(driver).transform();
		} finally {
			driver.quit();
		}

		new OutputTransformer(container, dstRoot).transform();
	}

	private static String extractPageTitle(final String path)
			throws UnsupportedEncodingException {
		final String baseName = FilenameUtils.getBaseName(path);
		return URLDecoder.decode(baseName, OutputTransformer.DEFAULT_ENCODING);
	}

	private static String extractPageBody(final String path) throws Exception {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, OutputTransformer.DEFAULT_ENCODING);
	}

	private static int publishStepsContainer(long parentPageId,
			String spaceKey, String containerFilePath,
			List<String> stepsFilesPaths) throws Exception {
		final String pageTitle = extractPageTitle(containerFilePath);
		final String pageBody = extractPageBody(containerFilePath);
		final long containerPageId = ConfluenceAPIWrappers.createChildPage(
				parentPageId, spaceKey, pageTitle, pageBody);
		System.out.println(String.format("Created container page '%s'",
				pageTitle));

		Map<String, String> stepsToPublish = new LinkedHashMap<String, String>();
		for (String stepFilePath : stepsFilesPaths) {
			final String stepPageTitle = extractPageTitle(stepFilePath);
			final String stepPageBody = extractPageBody(stepFilePath);
			stepsToPublish.put(stepPageTitle, stepPageBody);
		}

		final SortedSet<String> sortedStepNames = new TreeSet<String>(
				stepsToPublish.keySet());
		for (String stepName : sortedStepNames) {
			ConfluenceAPIWrappers.createChildPage(containerPageId, spaceKey,
					stepName, stepsToPublish.get(stepName));
			System.out.println(String.format("\tCreated step page '%s'",
					stepName));
		}
		return stepsToPublish.size() + 1;
	}

	private static int syncConfluenceContent(long parentPageId,
			String spaceKey, Map<String, List<String>> stepsMapping)
			throws Exception {
		System.out.println("Cleaning existing hierachy...");
		ConfluenceAPIWrappers.removeChildren(parentPageId, true);
		System.out.println("Done");

		int synchronizedPagesCount = 0;
		final SortedSet<String> sortedContainerNames = new TreeSet<String>(
				stepsMapping.keySet());
		for (String containerName : sortedContainerNames) {
			synchronizedPagesCount += publishStepsContainer(parentPageId,
					spaceKey, containerName, stepsMapping.get(containerName));
		}
		return synchronizedPagesCount;
	}

	private static int publishToConfluence(String srcRoot, String spaceKey,
			long parentPageId) throws Exception {
		// ! all module names inside file names are urlencoded
		Map<String, List<String>> stepsMapping = new LinkedHashMap<String, List<String>>();

		final File srcFolder = new File(srcRoot);
		for (final File containerEntry : srcFolder.listFiles()) {
			if (containerEntry.isDirectory()) {
				continue;
			}
			final String containerFilePath = containerEntry.getCanonicalPath();
			if (!containerFilePath.endsWith("."
					+ OutputTransformer.TRANSFORMED_FILE_EXT)) {
				continue;
			}

			final String stepsFolderPath = String.format("%s%s",
					FilenameUtils.removeExtension(containerFilePath),
					OutputTransformer.STEPS_SUBFOLDER_NAME_SUFFIX);
			final File stepsFolder = new File(stepsFolderPath);
			List<String> stepsPaths = new ArrayList<String>();
			if (stepsFolder.exists() && stepsFolder.isDirectory()) {
				for (final File stepEntry : stepsFolder.listFiles()) {
					if (stepEntry.isDirectory()) {
						continue;
					}
					final String stepFilePath = stepEntry.getCanonicalPath();
					if (stepFilePath.endsWith("."
							+ OutputTransformer.TRANSFORMED_FILE_EXT)) {
						stepsPaths.add(stepFilePath);
					}
				}
			}
			stepsMapping.put(containerFilePath, stepsPaths);
		}

		return syncConfluenceContent(parentPageId, spaceKey, stepsMapping);
	}

	@SuppressWarnings("static-access")
	private static Options createCmdlineOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_MODE)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory] available modes are: %s and %s",
								MODE_TRANSFORM, MODE_PUBLISH)).hasArg()
				.isRequired().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_SRC_HTML)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory for '%s' mode] path to the source javadoc html",
								MODE_TRANSFORM)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_DST_ROOT)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory for '%s' mode] path to the destination folder",
								MODE_TRANSFORM)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_SRC_ROOT)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory for '%s' mode] path to the source folder containing Confluence-compatible files hierachy",
								MODE_PUBLISH)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_SPACE_KEY)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory for '%s' mode] confluence space key, where destination pages root is located",
								MODE_PUBLISH)).hasArg().create());
		options.addOption(OptionBuilder
				.withLongOpt(PARAM_NAME_PARENT_PAGE_ID)
				.withType(String.class)
				.withDescription(
						String.format(
								"[mandatory for '%s' mode] the id number of root page",
								MODE_PUBLISH)).hasArg().create());
		return options;
	}

	public static void main(String[] args) throws Exception {
		final Options options = createCmdlineOptions();
		HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new PosixParser();

		try {
			CommandLine line = parser.parse(options, args);

			final String mode = line.getOptionValue(PARAM_NAME_MODE);
			if (mode.toLowerCase().equals(MODE_TRANSFORM)) {
				final String srcPath = line.getOptionValue(PARAM_NAME_SRC_HTML);
				if (!new File(srcPath).exists()) {
					throw new IOException("Source file should be existing file");
				}
				final String dstPath = line.getOptionValue(PARAM_NAME_DST_ROOT);
				transformJavadocToConfluence(srcPath, dstPath);
				System.out.println(String.format(
						"Successfully transformed %s to %s", srcPath, dstPath));
			} else if (mode.toLowerCase().equals(MODE_PUBLISH)) {
				final String srcRoot = line.getOptionValue(PARAM_NAME_SRC_ROOT);
				if (!new File(srcRoot).exists()) {
					throw new IOException(
							String.format(
									"Source root '%s' should exist on local file system",
									srcRoot));
				}
				final String spaceKey = line
						.getOptionValue(PARAM_NAME_SPACE_KEY);
				if (spaceKey == null) {
					throw new RuntimeException(
							String.format(
									"Command line parameter '%s' must be provided for mode '%s'",
									PARAM_NAME_SPACE_KEY, mode));
				}
				final String parentPageId = line
						.getOptionValue(PARAM_NAME_PARENT_PAGE_ID);
				if (parentPageId == null) {
					throw new RuntimeException(
							String.format(
									"Command line parameter '%s' must be provided for mode '%s'",
									PARAM_NAME_PARENT_PAGE_ID, mode));
				}
				final int countOfPublishedPages = publishToConfluence(srcRoot,
						spaceKey, Long.parseLong(parentPageId));
				System.out.println(String.format(
						"Successfully published %d page(s) located in '%s'",
						countOfPublishedPages, srcRoot));
			} else {
				throw new RuntimeException(String.format(
						"Mode '%s' is unknown", mode));
			}
			System.exit(0);
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage()
					+ "\n");
			formatter.printHelp("javadoc-exporter", options);
			System.exit(1);
		}
	}
}
