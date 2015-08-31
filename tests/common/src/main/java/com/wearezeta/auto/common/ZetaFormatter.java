package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.IRCTestcasesStorage;
import com.wearezeta.auto.common.rc.RCTestcase;
import com.wearezeta.auto.common.rc.TestcaseResultAnalyzer;
import com.wearezeta.auto.zephyr.ExecutedZephyrTestcase;
import com.wearezeta.auto.zephyr.ZephyrDB;
import com.wearezeta.auto.zephyr.ZephyrExecutionStatus;
import com.wearezeta.auto.zephyr.ZephyrTestCycle;
import com.wearezeta.auto.zephyr.ZephyrTestPhase;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

public class ZetaFormatter implements Formatter, Reporter {

	private static String feature = "";
	private static String scenario = "";
	private static Map<Step, String> steps = new LinkedHashMap<>();
	private static IRCTestcasesStorage storage = null;
	private static ZephyrTestPhase phase = null;
	private static ZephyrTestCycle cycle = null;
	@SuppressWarnings("unused")
	private static String jenkinsJobUrl = null;

	private Step getLastStep() {
		Step out = null;
		for (Step step : steps.keySet()) {
			out = step;
		}
		return out;
	}

	private static final Logger log = ZetaLogger.getLog(ZetaFormatter.class
			.getSimpleName());

	private long stepStartedTimestamp;

	private static String buildNumber = "unknown";

	@Override
	public void background(Background arg0) {

	}

	@Override
	public void close() {

	}

	@Override
	public void done() {

	}

	@Override
	public void eof() {
	}

	@Override
	public void examples(Examples arg0) {

	}

	@Override
	public void feature(Feature arg0) {
		feature = arg0.getName();
		log.debug("Feature: " + feature);
	}

	private static String formatTags(List<Tag> tags) {
		final StringBuilder result = new StringBuilder();
		result.append("[ ");
		for (Tag tag : tags) {
			result.append(tag.getName() + " ");
		}
		result.append("]");
		return result.toString();
	}

	@Override
	public void scenario(Scenario arg0) {
		scenario = arg0.getName();
		log.debug(String.format("\n\nScenario: %s %s", scenario,
				formatTags(arg0.getTags())));
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {
	}

	@Override
	public void step(Step arg0) {
		steps.put(arg0, null);
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, Integer arg4) {

	}

	@Override
	public void uri(String arg0) {
	}

	@Override
	public void after(Match arg0, Result arg1) {
	}

	@Override
	public void before(Match arg0, Result arg1) {
	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
	}

	@Override
	public void match(Match arg0) {
		stepStartedTimestamp = new Date().getTime();
	}

	private void takeStepScreenshot(final Result stepResult,
			final String stepName) throws Exception {
		final ZetaDriver driver = getDriver(
				stepResult.getStatus().equals(Result.FAILED)).orElse(null);
		if (driver != null) {
			if (stepResult.getStatus().equals(Result.SKIPPED.getStatus())) {
				// Don't make screenshots for skipped steps to speed up
				// suite execution
				return;
			}
			final String screenshotPath = String
					.format("%s/%s/%s/%s.png", CommonUtils
							.getPictureResultsPathFromConfig(this.getClass()),
							feature.replaceAll("\\W+", "_"), scenario
									.replaceAll("\\W+", "_"), stepName
									.replaceAll("\\W+", "_"));
			final Optional<BufferedImage> screenshot = DriverUtils
					.takeFullScreenShot(driver);
			if (!screenshot.isPresent()) {
				return;
			}
			screenshotSavers.execute(() -> storeScreenshot(screenshot.get(),
					screenshotPath));
		} else {
			log.debug(String
					.format("Selenium driver is not ready yet. Skipping screenshot creation for step '%s'",
							stepName));
		}
	}

	@Override
	public void result(Result arg0) {
		final String stepName = getLastStep().getName();
		final String stepStatus = arg0.getStatus();
		steps.put(getLastStep(), stepStatus);
		final long stepFinishedTimestamp = new Date().getTime();
		boolean isScreenshotingEnabled = true;
		try {
			isScreenshotingEnabled = CommonUtils
					.getMakeScreenshotsFromConfig(this.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isScreenshotingEnabled) {
			try {
				takeStepScreenshot(arg0, stepName);
			} catch (Exception e) {
				// Ignore screenshoting exceptions
				e.printStackTrace();
			}
			final long screenshotFinishedTimestamp = new Date().getTime();
			log.debug(String
					.format("%s (status: %s, step duration: %s ms + screenshot duration: %s ms)",
							stepName, stepStatus, stepFinishedTimestamp
									- stepStartedTimestamp,
							screenshotFinishedTimestamp - stepFinishedTimestamp));
		} else {
			log.debug(String.format("%s (status: %s, step duration: %s ms)",
					stepName, stepStatus, stepFinishedTimestamp
							- stepStartedTimestamp));
		}
	}

	private void storeScreenshot(final BufferedImage screenshot,
			final String path) {
		try {
			final File outputfile = new File(path);
			if (!outputfile.getParentFile().exists()) {
				outputfile.getParentFile().mkdirs();
			}
			ImageIO.write(screenshot, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String arg0) {
	}

	private static Optional<ZetaDriver> getDriver(boolean forceWait)
			throws Exception {
		if (lazyDriver.isDone() || forceWait) {
			return Optional.of((ZetaDriver) lazyDriver
					.get(ZetaDriver.INIT_TIMEOUT_MILLISECONDS,
							TimeUnit.MILLISECONDS));
		} else {
			return Optional.empty();
		}
	}

	private static Future<? extends RemoteWebDriver> lazyDriver = null;

	public synchronized static void setLazyDriver(
			Future<? extends RemoteWebDriver> lazyDriver) {
		ZetaFormatter.lazyDriver = lazyDriver;
	}

	private static final ExecutorService screenshotSavers = Executors
			.newFixedThreadPool(3);

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
	}

	private synchronized void syncCurrentTestResult(Set<String> normalizedTags,
			String cycleName, String phaseName, String jenkinsJobUrl)
			throws Exception {
		if (phase == null) {
			storage = new ZephyrDB(
					CommonUtils.getZephyrServerFromConfig(getClass()));
			cycle = ((ZephyrDB) storage).getTestCycle(cycleName);
			phase = cycle.getPhaseByName(phaseName);
		}
		final ZephyrExecutionStatus actualTestResult = TestcaseResultAnalyzer
				.analyzeSteps(steps);
		final List<ExecutedZephyrTestcase> rcTestCases = phase.getTestcases();
		boolean isAnyTestChanged = false;
		for (ExecutedZephyrTestcase rcTestCase : rcTestCases) {
			if (!Collections.disjoint(rcTestCase.getTags(), normalizedTags)) {
				if (rcTestCase.getExecutionStatus() != actualTestResult) {
					log.info(String
							.format("--> Setting execution result to '%s' for RC test case #%s (Cycle '%s', Phase '%s')",
									actualTestResult.toString(),
									rcTestCase.getId(), cycle.getName(),
									phase.getName()));
					rcTestCase.setExecutionStatus(actualTestResult);
					if (jenkinsJobUrl != null && jenkinsJobUrl.length() > 0) {
						rcTestCase.setExecutionComment(jenkinsJobUrl);
					}
					isAnyTestChanged = true;
				}
			}
		}
		if (isAnyTestChanged) {
			((ZephyrDB) storage).syncPhaseResults(phase);
		}
	}

	private static Set<String> normalizeTags(List<Tag> tags) {
		Set<String> result = new LinkedHashSet<>();
		for (Tag tag : tags) {
			if (tag.getName().startsWith(RCTestcase.MAGIC_TAG_PREFIX)) {
				result.add(tag.getName());
			} else {
				result.add(RCTestcase.MAGIC_TAG_PREFIX + tag.getName());
			}
		}
		return result;
	}

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		String zephyrCycleName;
		String zephyrPhaseName;
		String jenkinsJobUrl = null;
		try {
			try {
				zephyrCycleName = CommonUtils
						.getZephyrCycleNameFromConfig(getClass());
				zephyrPhaseName = CommonUtils
						.getZephyrPhaseNameFromConfig(getClass());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			try {
				jenkinsJobUrl = CommonUtils
						.getJenkinsJobUrlFromConfig(getClass());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (zephyrCycleName.length() > 0 && zephyrPhaseName.length() > 0) {
				final Set<String> normalizedTags = normalizeTags(scenario
						.getTags());
				if (normalizedTags.contains(RCTestcase.RC_TAG)) {
					try {
						syncCurrentTestResult(normalizedTags, zephyrCycleName,
								zephyrPhaseName, jenkinsJobUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			steps.clear();
		}
	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static void setBuildNumber(String buildNumber) {
		ZetaFormatter.buildNumber = buildNumber;
	}

	public static String getFeature() {
		return feature;
	}

	public static String getScenario() {
		return scenario;
	}
}
