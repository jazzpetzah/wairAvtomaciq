package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SketchPage extends AndroidPage {

    private static final String idCanvas = "dcv__canvas";
    @FindBy(id = idCanvas)
    private WebElement canvas;

    private static final String idColorPicker = "cpdl__color_layout";
    @FindBy(id = idColorPicker)
    private WebElement colorPicker;

    private static final String idSendButton = "tv__send_button";
    @FindBy(id = idSendButton)
    private WebElement sendButton;

    // Colors should be in the order they appear in the color picker
    public static final String[] colors = {"white", "black", "blue", "green",
            "yellow", "red", "orange", "pink", "purple"};

    private int selectedColorIndex = 0; // default to white

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

    public void setColor(int colorIndex) throws Exception {
        this.selectedColorIndex = colorIndex;
        selectColorFromChooser(colorPicker);
    }

    public void setColor(String color) throws Exception {
        color = color.toLowerCase().trim();
        this.selectedColorIndex = ArrayUtils.indexOf(colors, color);
        selectColorFromChooser(colorPicker);
    }

    private void selectColorFromChooser(WebElement colorPicker)
            throws Exception {
        int numColors = colors.length;
        double colorPickerElementWidth = colorPicker.getSize().width;
        // the actual select area is a bit smaller than the width of the element
        double colorPickerSelectorWidth = colorPickerElementWidth - 2
                * colorPickerPadding;

        double colorWidth = colorPickerSelectorWidth / numColors;

        double colorPosition = colorWidth * selectedColorIndex
                + ((0.5) * colorWidth);
        double percentX = (colorPickerPadding + colorPosition)
                / colorPickerElementWidth * 100;
        int percentY = 50;

        DriverUtils.tapOnPercentOfElement(this.getDriver(), colorPicker,
                (int) percentX, percentY);

        // This is needed according to current control algorithm
        Thread.sleep(1000);
    }

    public void drawLinesOnCanvas(int percentStartX, int percentStartY,
                                  int percentEndX, int percentEndY) throws Exception {
        int swipeDuration = 300;
        if (getDriver().getOSVersionString().compareTo("4.3") < 0) {
            swipeDuration = 1500;
        }
        DriverUtils.swipeElementPointToPoint(this.getDriver(), canvas,
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
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), sendButton);
        DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(idSendButton), 5);
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idSendButton), 1)) {
            try {
                getDriver().tap(1, sendButton.getLocation().getX(), sendButton.getLocation().getY(), 1);
            } catch (NoSuchElementException e) {
                log.debug("Can't find send sketch button. Page source: " + getDriver().getPageSource());
            }
        }
    }

    public Optional<BufferedImage> getCanvasScreenshot() throws Exception {
        return this.getElementScreenshot(canvas);
    }
}
