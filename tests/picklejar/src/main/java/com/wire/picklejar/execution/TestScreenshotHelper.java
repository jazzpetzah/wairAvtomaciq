package com.wire.picklejar.execution;

import static com.wire.picklejar.Config.SCREENSHOT_PATH;
import com.wire.picklejar.gherkin.model.Feature;
import com.wire.picklejar.gherkin.model.Scenario;
import com.wire.picklejar.gherkin.model.Step;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for saving screenshots to the right place for jenkins cucumber report plugin. This class should actually reside on the
 * test execution implementation but for testing purposes it's included in PickleJar for now.
 *
 */
public class TestScreenshotHelper {

    protected static final Logger LOG = LoggerFactory.getLogger(TestScreenshotHelper.class.getSimpleName());

    private static final int MAX_SCREENSHOT_WIDTH = 1440;
    private static final int MAX_SCREENSHOT_HEIGHT = 800;

    public void saveScreenshot(Step step, Scenario scenario, Feature feature, byte[] screenshot) throws IOException {
        Path path = Paths.get(
                Paths.get(SCREENSHOT_PATH).toAbsolutePath().toString(),
                getReportFeatureName(feature.getName()),
                getReportScenarioName(scenario.getName()));
        path.toFile().mkdirs();
        int index = 1;
        Path desiredPicturePath = Paths.get(path.toString(), getReportStepName(step.getName()) + "_" + index + ".png");
        // we abort the while loop when index exceeds 20. 
        // That means you have the same step 20 times in one scenario which is unlikely to happen
        while (desiredPicturePath.toFile().exists() && index < 20) {
            index++;
            desiredPicturePath = Paths.get(path.toString(), getReportStepName(step.getName()) + "_" + index + ".png");
        }
        screenshot = adjustScreenshotSize(screenshot, MAX_SCREENSHOT_WIDTH, MAX_SCREENSHOT_HEIGHT);
        Files.write(desiredPicturePath, screenshot);
        printScreenshot(
                getReportFeatureName(feature.getName()),
                getReportScenarioName(scenario.getName()),
                getReportStepName(step.getName()) + "_" + index + ".png");
    }

    private void printScreenshot(String reportFeatureName, String reportScenarioName, String reportStepName) {
        final String relativePathTemplate = "../../../../cucumber-html-reports/Images/%s/%s/%s";
        final String imgTemplate = "<a href=\"%s\" target=\"_blank\"><img src=\"%s\" alt=\"%s\" width=\"40%%\" height=\"40%%\"></a>";
        String relativePath = String.format(relativePathTemplate, reportFeatureName, reportScenarioName, reportStepName);
        LOG.info(String.format(imgTemplate, relativePath, relativePath, reportStepName));
    }

    protected String getReportFeatureName(String featureName) {
        return reduceDoubleUnderscores(featureName.replaceAll("[^a-zA-Z0-9]", "_"));
    }

    protected String getReportScenarioName(String scenarioName) {
        // cucumber does not replace characters like " with _ but just removes them beforehand
        // needs more investigation what characters are removed and which are replaced by _
        return reduceDoubleUnderscores(scenarioName
                .replaceAll("[\"!,]", "")
                .replaceAll("[^a-zA-Z0-9]", "_"));
    }

    protected String getReportStepName(String stepName) {
        // cucumber does not replace characters like " with _ but just removes them beforehand
        // needs more investigation what characters are removed and which are replaced by _
        return stripLastUnderscore(reduceDoubleUnderscores(stepName
                .replaceAll("[\"!']", "")
                .replaceAll("[^a-zA-Z0-9]", "_")));
    }

    private String reduceDoubleUnderscores(String input) {
        String output = input;
        while (output.contains("__")) {
            output = output.replaceAll("__", "_");
        }
        return output;
    }

    private String stripLastUnderscore(String string) {
        if (string.charAt(string.length() - 1) == '_') {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    private byte[] adjustScreenshotSize(byte[] screenshot, final int maxWidth, final int maxHeight) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(screenshot);
//        Arrays.asList(ImageIO.getReaderFormatNames()).stream().forEach((String r) -> LOG.debug(r));
        BufferedImage imgScreenshot = ImageIO.read(in);
        if (imgScreenshot != null) {
            try {
                imgScreenshot = scaleTo(imgScreenshot, maxWidth, maxHeight);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(imgScreenshot, "png", baos);
                return baos.toByteArray();
            } catch (Exception e) {
                LOG.warn("Could not resize image", e);
                return screenshot;
            }
        }
        return screenshot;
    }

    private static BufferedImage scaleTo(BufferedImage originalImage, final int maxWidth, final int maxHeight) throws
            IOException {
        final int height = originalImage.getHeight();
        final int width = originalImage.getWidth();
        float resizeRatio = 1;
        if (width > maxWidth || height > maxHeight) {
            final float resizeRatioW1 = (float) maxWidth / width;
            final float resizeRatioW2 = (float) maxWidth / height;
            final float resizeRatioH1 = (float) maxHeight / width;
            final float resizeRatioH2 = (float) maxHeight / height;
            float resizeRatioH = (resizeRatioH1 < resizeRatioH2) ? resizeRatioH1 : resizeRatioH2;
            float resizeRatioW = (resizeRatioW1 < resizeRatioW2) ? resizeRatioW1 : resizeRatioW2;
            final float resizeRatioLimitedW = (resizeRatioH > resizeRatioW) ? resizeRatioH : resizeRatioW;
            resizeRatioH = (resizeRatioH1 > resizeRatioH2) ? resizeRatioH1 : resizeRatioH2;
            resizeRatioW = (resizeRatioW1 > resizeRatioW2) ? resizeRatioW1 : resizeRatioW2;
            final float resizeRatioLimitedH = (resizeRatioH < resizeRatioW) ? resizeRatioH : resizeRatioW;
            resizeRatio = (resizeRatioLimitedW < resizeRatioLimitedH) ? resizeRatioLimitedW : resizeRatioLimitedH;
        }
        final int scaledW = Math.round(width * resizeRatio);
        final int scaledH = Math.round(height * resizeRatio);
        BufferedImage resizedImage = new BufferedImage(scaledW, scaledH, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(originalImage, 0, 0, scaledW, scaledH, null);
        return resizedImage;
    }

}
