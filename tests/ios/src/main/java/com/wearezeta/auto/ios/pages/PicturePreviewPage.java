package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class PicturePreviewPage extends IOSPage {
    private static final By nameSketchButton = MobileBy.AccessibilityId("sketchButton");

    private static final By nameEmojiButton = MobileBy.AccessibilityId("emojiButton");

    private static final By nameRetakeButton = MobileBy.AccessibilityId("Retake");

    private static final By nameUsePhotoButton = MobileBy.AccessibilityId("Use Photo");

    private static final By nameConfirmButton = MobileBy.AccessibilityId("OK");

    public PicturePreviewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "sketch":
                return nameSketchButton;
            case "confirm":
                return nameConfirmButton;
            case "cancel":
                return xpathCancelButton;
            case "retake":
                return nameRetakeButton;
            case "use photo":
                return nameUsePhotoButton;
            case "emoji":
                return nameEmojiButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        if (locator.equals(nameConfirmButton) || locator.equals(xpathCancelButton)) {
            selectVisibleElements(locator).get(0).click();
        } else {
            getElement(locator).click();
        }
    }
}
