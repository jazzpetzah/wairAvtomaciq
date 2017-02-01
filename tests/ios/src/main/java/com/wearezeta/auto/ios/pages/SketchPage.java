package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBSwipeDirection;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SketchPage extends IOSPage {
    private static final By nameSendButton = MobileBy.AccessibilityId("sendButton");
    private static final By nameDrawButton = MobileBy.AccessibilityId("drawButton");
    private static final By nameEmojiButton = MobileBy.AccessibilityId("emojiButton");
    private static final String strNameOpenGalleryButton = "photoButton";
    private static final By nameOpenGalleryButton = MobileBy.AccessibilityId(strNameOpenGalleryButton);
    private static final By nameUndoButton = MobileBy.AccessibilityId("undoButton");
    private static final By fbXpathCanvas = FBBy.xpath(
            String.format("//XCUIElementTypeButton[@name='%s']/parent::*[1]/preceding-sibling::*[1]",
                    strNameOpenGalleryButton));

    public SketchPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    private static final Random rand = new Random();

    public void sketchRandomLines() throws Exception {
        final int directionsCount = FBSwipeDirection.values().length;
        for (int times = 0; times < 2; ++times) {
            ((FBElement) getElement(fbXpathCanvas)).swipe(
                    FBSwipeDirection.values()[rand.nextInt(directionsCount)]
            );
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
