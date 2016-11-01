package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.DriverUtils;

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
        final int startX = 10;
        final int startY = 20;
        for (int i = 0; i < 2; i++) {
            final int endX = startX + rand.nextInt(80);
            final int endY = startY + rand.nextInt(30);
            if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
                IOSSimulatorHelpers.swipe(startX / 100.0, startY / 100.0, endX / 100.0, endY / 100.0);
            } else {
                DriverUtils.swipeByCoordinates(getDriver(), 1000, startX, startY, endX, endY);
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
