package javadoc_exporter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javadoc_exporter.confluence_rest_api.RESTMethods;
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
import org.json.JSONArray;
import org.json.JSONObject;
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

	private static JSONObject getPageInfo(JSONObject children, String pageTitle)
			throws Exception {
		final int size = children.getInt("size");
		if (size > 0) {
			JSONArray foundPages = children.getJSONArray("results");
			for (int pageIdx = 0; pageIdx < size; pageIdx++) {
				final JSONObject currentPage = foundPages
						.getJSONObject(pageIdx);
				final String currentTitle = currentPage.getString("title");
				if (currentTitle.equals(pageTitle)) {
					return currentPage;
				}
			}
		}
		return null;
	}

	private static long updateConfluenceChildPageIfNecessary(long parentPageId,
			String spaceKey, String pageTitle, String pageBody)
			throws Exception {
		JSONObject children = RESTMethods.getChildren(parentPageId,
				new String[] { "version" });
		JSONObject pageInfo = getPageInfo(children, pageTitle);
		if (pageInfo == null) {
			pageInfo = RESTMethods.createChildPage(parentPageId, spaceKey,
					pageTitle, pageBody);
		} else {
			String currentBodyHash = "";
			if (pageInfo.getJSONObject("version").has("message")) {
				currentBodyHash = pageInfo.getJSONObject("version").getString(
						"message");
			}
			if (!currentBodyHash.equals(RESTMethods.getBodyHash(pageBody))) {
				final int currentPageVersion = pageInfo
						.getJSONObject("version").getInt("number");
				RESTMethods.updateChildPage(parentPageId, spaceKey, pageTitle,
						pageBody, currentPageVersion + 1);
			}
		}
		return pageInfo.getLong("id");
	}

	private static String extractPageTitle(final String path)
			throws UnsupportedEncodingException {
		String baseName = FilenameUtils.getBaseName(path);
		String fNameWOExt = FilenameUtils.removeExtension(baseName);
		return URLDecoder
				.decode(fNameWOExt, OutputTransformer.DEFAULT_ENCODING);
	}

	private static String extractPageBody(final String path) throws Exception {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, OutputTransformer.DEFAULT_ENCODING);
	}

	private static void publishStepsContainer(long parentPageId,
			String spaceKey, String containerFilePath,
			List<String> stepsFilesPaths) throws Exception {
		final String pageTitle = extractPageTitle(containerFilePath);
		final String pageBody = extractPageBody(containerFilePath);
		final long containerPageId = updateConfluenceChildPageIfNecessary(
				parentPageId, spaceKey, pageTitle, pageBody);

		Map<String, String> stepsToPublish = new LinkedHashMap<String, String>();
		for (String stepFilePath : stepsFilesPaths) {
			final String stepPageTitle = extractPageTitle(stepFilePath);
			final String stepPageBody = extractPageBody(stepFilePath);
			stepsToPublish.put(stepPageTitle, stepPageBody);
		}
		removeExtraChildren(containerPageId, stepsToPublish.keySet());

		for (Map.Entry<String, String> stepInfo : stepsToPublish.entrySet()) {
			updateConfluenceChildPageIfNecessary(containerPageId, spaceKey,
					stepInfo.getKey(), stepInfo.getValue());
		}
	}

	private static void syncConfluenceContent(long parentPageId,
			String spaceKey, Map<String, List<String>> stepsMapping)
			throws Exception {
		Set<String> realContainerNames = new HashSet<String>();
		for (String path : stepsMapping.keySet()) {
			final String pageTitle = extractPageTitle(path);
			realContainerNames.add(pageTitle);
		}
		removeExtraChildren(parentPageId, realContainerNames);

		for (Map.Entry<String, List<String>> containerInfo : stepsMapping
				.entrySet()) {
			publishStepsContainer(parentPageId, spaceKey,
					containerInfo.getKey(), containerInfo.getValue());
		}
	}

	private static void removeExtraChildren(long parentPageId,
			Set<String> exstingChildrenTitles) throws Exception {
		JSONObject children = RESTMethods.getChildren(parentPageId,
				new String[] { "version" });
		Map<String, Long> confluenceChildren = new HashMap<String, Long>();
		final int size = children.getInt("size");
		if (size > 0) {
			JSONArray foundPages = children.getJSONArray("results");
			for (int pageIdx = 0; pageIdx < size; pageIdx++) {
				final JSONObject currentPage = foundPages
						.getJSONObject(pageIdx);
				confluenceChildren.put(currentPage.getString("title"),
						currentPage.getLong("id"));
			}
		}
		Set<Long> pagesToDelete = new HashSet<Long>();
		for (Map.Entry<String, Long> entry : confluenceChildren.entrySet()) {
			if (!exstingChildrenTitles.contains(entry.getKey())) {
				pagesToDelete.add(entry.getValue());
			}
		}
		for (Long id : pagesToDelete) {
			RESTMethods.removeChildPage(id.longValue());
		}
	}

	/**
	 * http://stackoverflow.com/questions/24504631/how-to-update-a-page-in-
	 * confluence-5-5-1-via-rest-call
	 */
	private static void publishToConfluence(String srcRoot, String spaceKey,
			long parentPageId) throws Exception {
		// TODO : encapsulate Confluence-specific stuff into separate module
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

		syncConfluenceContent(parentPageId, spaceKey, stepsMapping);
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
				publishToConfluence(srcRoot, spaceKey,
						Long.parseLong(parentPageId));
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
