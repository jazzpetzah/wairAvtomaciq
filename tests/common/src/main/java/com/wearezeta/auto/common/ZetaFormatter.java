package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.File;
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
import com.wearezeta.auto.common.driver.HasParallelScreenshotsFeature;
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

	private long startDate;
	private long endDate;

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void after(Match arg0, Result arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void before(Match arg0, Result arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void match(Match arg0) {
		startDate = new Date().getTime();
	}

	@Override
	public void result(Result arg0) {
		endDate = new Date().getTime();
		final String currentStep = step.poll();

		log.debug(currentStep + " (status: " + arg0.getStatus() + ", time: "
				+ (endDate - startDate) + "ms)");
<<<<<<< HEAD

		// take screenshot
		try {
			if (CommonUtils.getMakeScreenshotsFromConfig(this.getClass())) {
				final ZetaDriver driver = getDriver(arg0.getStatus().equals(
						Result.FAILED));
				if (driver != null) {
					if (arg0.getStatus().equals(Result.SKIPPED.getStatus())) {
						// Don't make screenshots for skipped steps to speed up
						// suite execution
						return;
					}
					Optional<BufferedImage> screenshot = DriverUtils
							.takeFullScreenShot(driver);
					if (!screenshot.isPresent()) {
						log.info(String
								.format("No screenshot could be taken for the step '%s' (skipped)",
										currentStep));
						return;
					}
					String picturePath = CommonUtils
							.getPictureResultsPathFromConfig(this.getClass());
					// FIXME: some characters in steps/captions may not be
					// acceptable for file names
					File outputfile = new File(picturePath + feature + "/"
							+ scenario + "/" + currentStep + ".png");

					if (!outputfile.getParentFile().exists()) {
						outputfile.getParentFile().mkdirs();
					}
					ImageIO.write(screenshot.get(), "png", outputfile);
				} else {
					log.debug(String
							.format("Selenium driver is not ready yet. Skipping screenshot creation for step '%s'",
									currentStep));
				}
		try {
			final ZetaDriver driver = getDriver(
					arg0.getStatus().equals(Result.FAILED)).orElse(null);
			if (driver != null) {
				if (arg0.getStatus().equals(Result.SKIPPED.getStatus())) {
					// Don't make screenshots for skipped steps to speed up
					// suite execution
					return;
				}
				initScreenshotMakers(driver);
				final String screenshotPath = String.format("%s/%s/%s/%s.png",
						CommonUtils.getPictureResultsPathFromConfig(this
								.getClass()), feature.replaceAll("\\W+", "_"),
						scenario.replaceAll("\\W+", "_"), currentStep
								.replaceAll("\\W+", "_"));
				if (screenshotMakers.isPresent()) {
					final Runnable task = new Thread(() -> storeScreenshot(
							driver, screenshotPath));
					screenshotMakers.get().execute(task);
				} else {
					storeScreenshot(driver, screenshotPath);
				}
			} else {
				log.debug(String
						.format("Selenium driver is not ready yet. Skipping screenshot creation for step '%s'",
								currentStep));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void storeScreenshot(ZetaDriver driver, String path) {
		try {
			final Optional<BufferedImage> screenshot = DriverUtils
					.takeFullScreenShot(driver);
			if (!screenshot.isPresent()) {
				return;
			}
			final File outputfile = new File(path);
			if (!outputfile.getParentFile().exists()) {
				outputfile.getParentFile().mkdirs();
			}
			ImageIO.write(screenshot.get(), "png", outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String arg0) {
		// TODO Auto-generated method stub
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

	private void initScreenshotMakers(ZetaDriver driver) {
		if (screenshotMakers.isPresent()) {
			return;
		}
		if (driver instanceof HasParallelScreenshotsFeature) {
			final int threadsCount = ((HasParallelScreenshotsFeature) driver)
					.getMaxScreenshotMakersCount();
			log.info(String
					.format("%s declares support of multithreaded screenshots. Will do screenshots in %s parallel threads.",
							driver.getClass().getSimpleName(), threadsCount));
			screenshotMakers = Optional.of(Executors
					.newFixedThreadPool(threadsCount));
		} else {
			screenshotMakers = Optional.empty();
		}
	}

	private static Future<? extends RemoteWebDriver> lazyDriver = null;

	public synchronized static void setLazyDriver(
			Future<? extends RemoteWebDriver> lazyDriver) {
		ZetaFormatter.lazyDriver = lazyDriver;
		if (screenshotMakers.isPresent()) {
			screenshotMakers.get().shutdownNow();
			try {
				screenshotMakers.get().awaitTermination(0, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// ignore silently
			}
			screenshotMakers = Optional.empty();
		}
	}

	private static Optional<ExecutorService> screenshotMakers = Optional
			.empty();

	public static Optional<ExecutorService> getScreenshotMakers() {
		return screenshotMakers;
	}

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
