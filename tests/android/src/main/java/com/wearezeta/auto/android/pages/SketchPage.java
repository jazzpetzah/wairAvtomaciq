package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SketchPage extends AndroidPage {

    private static final By idCanvas = By.id("dcv__canvas");

    private static final By idColorPicker = By.id("cpdl__color_layout");

    private static final By idSendButton = By.id("tv__send_button");
    private static final By idSketchButton = By.id("gtv__drawing_button__sketch");
    private static final By idEmojiButton = By.id("gtv__drawing_button__emoji");
    private static final By idTextButton = By.id("gtv__drawing_button__text");
    private static final By idTextSketchInput = By.id("et__sketch_text");

    // Colors should be in the order they appear in the color picker
    public enum SketchColor {
        WHITE(0), BLACK(1), BLUE(2), GREEN(3), YELLOW(4), RED(5), ORANGE(6), PINK(7), PURPLE(8);

        private int index;

        SketchColor(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }


    private SketchColor selectedColor = SketchColor.WHITE; // default to white

    /**
     * The padding value on either sides of the color picker. Taken from the
     * file fragment_drawing.xml in the client project resource files under the
     * ColorPickerCircleLayout view element
     */
    private final int COLOR_PICKER_PADDING_DP = 24;

    private int colorPickerPadding = (int) (COLOR_PICKER_PADDING_DP * AndroidCommonUtils
            .getScreenDensity());

    public SketchPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void setColor(SketchColor color) throws Exception {
        this.selectedColor = color;
        selectColorFromChooser(getElement(idColorPicker));
    }

    public void tapOnModeButton(String buttonName) throws Exception {
        final By locator = getModeButtonLocator(buttonName);
        getElement(locator).click();
    }

    public void typeTextOnSketch(String text) throws Exception {
        AndroidCommonUtils.type(text);
        pressKeyboardSendButton();
    }

    private static By getModeButtonLocator(String buttonName) {
        switch(buttonName.toLowerCase()) {
            case "emoji":
                return idEmojiButton;
            case "sketch":
                return idSketchButton;
            case "text":
                return idTextButton;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify mode button name '%s'", buttonName));
        }
    }

    private void selectColorFromChooser(WebElement colorPicker)
            throws Exception {
        int numColors = SketchColor.values().length;
        double colorPickerElementWidth = colorPicker.getSize().width;
        // the actual select area is a bit smaller than the width of the element
        double colorPickerSelectorWidth = colorPickerElementWidth - 2 * colorPickerPadding;

        double colorWidth = colorPickerSelectorWidth / numColors;

        double colorPosition = colorWidth * selectedColor.getIndex() + ((0.5) * colorWidth);
        double percentX = (colorPickerPadding + colorPosition) / colorPickerElementWidth * 100;
        int percentY = 50;

        DriverUtils.tapOnPercentOfElement(this.getDriver(), colorPicker, (int) percentX, percentY);

        // This is needed according to current control algorithm
        Thread.sleep(1000);
    }

    public void drawLinesOnCanvas(int percentStartX, int percentStartY,
                                  int percentEndX, int percentEndY) throws Exception {
        int swipeDuration = 300;
        if (getDriver().getOSVersion().compareTo(new DefaultArtifactVersion("4.3")) < 0) {
            swipeDuration = 1500;
        }
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(idCanvas),
                swipeDuration, percentStartX, percentStartY, percentEndX,
                percentEndY);
    }

    private static final Random random = new Random();

    public void drawRandomLines(int numLines) throws Exception {
        for (int i = 0; i < numLines; i++) {
            int startX = random.nextInt(25);
            int startY = random.nextInt(25);
            int endX = 50 + random.nextInt(50);
            int endY = 50 + random.nextInt(50);
            drawLinesOnCanvas(startX, startY, endX, endY);
        }
    }

    public void tapSendButton() throws Exception {
        final WebElement sendButton = getElement(idSendButton);
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), sendButton);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), idSendButton, 5)) {
            try {
                getDriver().tap(1, sendButton.getLocation().getX(), sendButton.getLocation().getY(),
                        DriverUtils.SINGLE_TAP_DURATION);
            } catch (NoSuchElementException e) {
                log.debug("Can't find send sketch button. Page source: " + getDriver().getPageSource());
            }
        }
        // Wait for animation
        Thread.sleep(2000);
    }

    public Optional<BufferedImage> getCanvasScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idCanvas));
    }

    public void drawEmojiOnCanvas() throws Exception {
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), getElement(idCanvas));
    }
}
