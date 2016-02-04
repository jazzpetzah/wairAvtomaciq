package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.RCTestcase;
import com.wearezeta.auto.common.rc.TestcaseResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;

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
import io.appium.java_client.ios.IOSDriver;

public class ZetaFormatter implements Formatter, Reporter {
    private static String feature = "";
    private static String scenario = "";
    private static Map<Step, String> steps = new LinkedHashMap<>();
    private static Optional<Iterator<Step>> stepsIterator = Optional.empty();

    private static final Logger log = ZetaLogger.getLog(ZetaFormatter.class.getSimpleName());

    private long stepStartedTimestamp;

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
            result.append(tag.getName()).append(" ");
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public void scenario(Scenario arg0) {
        scenario = arg0.getName();
        log.debug(String.format("\n\nScenario: %s %s", scenario, formatTags(arg0.getTags())));
    }

    @Override
    public void scenarioOutline(ScenarioOutline arg0) {
    }

    // This will prefill all the step names before any 'result' method is called
    @Override
    public void step(Step arg0) {
        steps.put(arg0, null);
    }

    @Override
    public void syntaxError(String arg0, String arg1, List<String> arg2, String arg3, Integer arg4) {

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

    private static final int MAX_SCREENSHOT_WIDTH = 1600;
    private static final int MAX_SCREENSHOT_HEIGHT = 900;

    private static BufferedImage adjustScreenshotSize(BufferedImage originalImage) {
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        float resizeRatio = 1;
        if (width > MAX_SCREENSHOT_WIDTH || height > MAX_SCREENSHOT_HEIGHT) {
            float resizeRatioW = (float) MAX_SCREENSHOT_WIDTH / width;
            float resizeRatioH = (float) MAX_SCREENSHOT_HEIGHT / height;
            resizeRatio = (resizeRatioH > resizeRatioW) ? resizeRatioW : resizeRatioH;
        }
        try {
            return ImageUtil.resizeImage(originalImage, resizeRatio);
        } catch (IOException e) {
            e.printStackTrace();
            return originalImage;
        }
    }

    private void takeStepScreenshot(final Result stepResult, final String stepName) throws Exception {
        final ZetaDriver driver = getDriver().orElse(null);
        if (driver != null) {
            if (stepResult.getStatus().equals(Result.SKIPPED.getStatus())) {
                // Don't make screenshots for skipped steps to speed up
                // suite execution
                return;
            }
            int index = 1;
            boolean isExist;
            String tmpScreenshotPath;
            do {
                tmpScreenshotPath = String.format("%s/%s/%s/%s_%s.png",
                        CommonUtils.getPictureResultsPathFromConfig(this.getClass()), feature.replaceAll("\\W+", "_"),
                        scenario.replaceAll("\\W+", "_"), stepName.replaceAll("\\W+", "_"), index);
                isExist = new File(tmpScreenshotPath).exists();
                index++;
            } while (isExist);
            final String screenshotPath = tmpScreenshotPath;
            if (driver instanceof IOSDriver && CommonUtils.getIsSimulatorFromConfig(ZetaFormatter.class)) {
                try {
                    CommonUtils.takeIOSSimulatorScreenshot(screenshotPath);
                } catch (Exception e) {
                    log.error("Failed to take iOS simulator screenshot:");
                    e.printStackTrace();
                }
            } else if (driver instanceof ZetaAndroidDriver) {
                try {
                    CommonUtils.takeAndroidScreenshot((ZetaAndroidDriver) driver, new File(screenshotPath));
                } catch (Exception e) {
                    log.error("Failed to take Android screenshot:");
                    e.printStackTrace();
                }
            } else {
                final Optional<BufferedImage> screenshot = DriverUtils.takeFullScreenShot(driver);
                if (!screenshot.isPresent()) {
                    return;
                }
                screenshotSavers.execute(() -> storeScreenshot(screenshot.get(), screenshotPath));
            }
        } else {
            log.debug(String.format("Selenium driver is not ready yet. Skipping screenshot creation for step '%s'", stepName));
        }
    }

    private static boolean isScreenshotingEnabled = true;

    static {
        try {
            isScreenshotingEnabled = CommonUtils.getMakeScreenshotsFromConfig(ZetaFormatter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isScreenshotingOnPassedStepsEnabled = true;

    static {
        try {
            isScreenshotingOnPassedStepsEnabled = CommonUtils.getMakeScreenshotOnPassedStepsFromConfig(ZetaFormatter.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void result(Result result) {
        if (!stepsIterator.isPresent()) {
            stepsIterator = Optional.of(steps.keySet().iterator());
        }
        final Step currentStep = stepsIterator.get().next();
        final String stepName = currentStep.getName();
        final String stepStatus = result.getStatus();
        steps.put(currentStep, stepStatus);
        final long stepFinishedTimestamp = new Date().getTime();
        if (isScreenshotingEnabled) {
            if (!isScreenshotingOnPassedStepsEnabled && (result.getStatus().equals(Result.PASSED))) {
                log.debug("Skip screenshot for passed step....");
            } else {
                try {
                    takeStepScreenshot(result, stepName);
                } catch (Exception e) {
                    // Ignore screenshoting exceptions
                    e.printStackTrace();
                }
            }
            final long screenshotFinishedTimestamp = new Date().getTime();
            log.debug(String.format("%s (status: %s, step duration: %s ms + screenshot duration: %s ms)", stepName, stepStatus,
                    stepFinishedTimestamp - stepStartedTimestamp, screenshotFinishedTimestamp - stepFinishedTimestamp));
        } else {
            log.debug(String.format("%s (status: %s, step duration: %s ms)", stepName, stepStatus,
                    stepFinishedTimestamp - stepStartedTimestamp));
        }
    }

    private void storeScreenshot(final BufferedImage screenshot, final String path) {
        try {
            final File outputFile = new File(path);
            if (!outputFile.getParentFile().exists()) {
                // noinspection ResultOfMethodCallIgnored
                outputFile.getParentFile().mkdirs();
            }
            ImageIO.write(adjustScreenshotSize(screenshot), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String arg0) {
    }

    private static Optional<ZetaDriver> getDriver() throws Exception {
        if (lazyDriver != null && lazyDriver.isDone() && !lazyDriver.isCancelled()) {
            return Optional.of((ZetaDriver) lazyDriver.get());
        } else {
            return Optional.empty();
        }
    }

    private static Future<? extends RemoteWebDriver> lazyDriver = null;

    public synchronized static void setLazyDriver(Future<? extends RemoteWebDriver> lazyDriver) {
        ZetaFormatter.lazyDriver = lazyDriver;
    }

    private static final ExecutorService screenshotSavers = Executors.newFixedThreadPool(3);

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
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
        try {
            final Set<String> normalizedTags = normalizeTags(scenario.getTags());

            TestrailSyncUtilities.syncExecutedScenarioWithTestrail(scenario,
                    new TestcaseResultToTestrailTransformer(steps).transform(), normalizedTags);
        } finally {
            steps.clear();
            stepsIterator = Optional.empty();
        }
    }

    public static String getFeature() {
        return feature;
    }

    public static String getScenario() {
        return scenario;
    }
}
