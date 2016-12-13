package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDragArguments;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.Dimension;

public class SketchPage extends IOSPage {
    private static final By nameSendButton = MobileBy.AccessibilityId("sendButton");
    private static final By nameDrawButton = MobileBy.AccessibilityId("drawButton");
    private static final By nameEmojiButton = MobileBy.AccessibilityId("emojiButton");
    private static final By nameOpenGalleryButton = MobileBy.AccessibilityId("photoButton");
    private static final By nameUndoButton = MobileBy.AccessibilityId("undoButton");

    public SketchPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    private static final Random rand = new Random();

    public void sketchRandomLines() throws Exception {
        final double startXPercent = 10.0;
        final double startYPercent = 20.0;
        for (int i = 0; i < 2; i++) {
            final double endXPercent = startXPercent + rand.nextInt(80);
            final double endYPercent = startYPercent + rand.nextInt(30);
            if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
                IOSSimulatorHelpers.swipe(startXPercent / 100.0, startYPercent / 100.0,
                        endXPercent / 100.0, endYPercent / 100.0);
            } else {
                final Dimension screenSize = getDriver().manage().window().getSize();
                getDriver().dragFromToForDuration(
                        new FBDragArguments(
                                startXPercent * screenSize.getWidth() / 100,
                                startYPercent * screenSize.getHeight() / 100,
                                endXPercent * screenSize.getWidth() / 100,
                                endYPercent * screenSize.getHeight() / 100,
                                3)
                );
            }
        }
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "send":
                return nameSendButton;
            case "draw":
                return nameDrawButton;
            case "emoji":
                return nameEmojiButton;
            case "open gallery":
                return nameOpenGalleryButton;
            case "undo":
                return nameUndoButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        getElement(locator).click();
    }
}
