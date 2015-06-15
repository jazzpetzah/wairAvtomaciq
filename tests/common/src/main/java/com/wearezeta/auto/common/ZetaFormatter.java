package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
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

public class ZetaFormatter implements Formatter, Reporter {
	private String feature = "";
	private String scenario = "";
	private Queue<String> step = new LinkedList<String>();

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

	@Override
	public void scenario(Scenario arg0) {
		scenario = arg0.getName();
		log.debug("\n\nScenario: " + scenario);
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {
	}

	@Override
	public void step(Step arg0) {
		step.add(arg0.getName());
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
		final String stepName = step.poll();
		final String stepStatus = arg0.getStatus();
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

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {

	}

	public static String getBuildNumber() {
		return buildNumber;
	}

	public static void setBuildNumber(String buildNumber) {
		ZetaFormatter.buildNumber = buildNumber;
	}

}
