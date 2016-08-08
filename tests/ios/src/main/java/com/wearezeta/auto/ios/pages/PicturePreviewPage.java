package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class PicturePreviewPage extends IOSPage {
    private static final By nameSketchButton = MobileBy.AccessibilityId("editNotConfirmedImageButton");

    private static final By nameRetakeButton = MobileBy.AccessibilityId("Retake");

    private static final By nameUsePhotoButton = MobileBy.AccessibilityId("Use Photo");

    public PicturePreviewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "sketch":
                return nameSketchButton;
            case "confirm":
                return xpathConfirmButton;
            case "cancel":
                return xpathCancelButton;
            case "retake":
                return nameRetakeButton;
            case "use photo":
                return nameUsePhotoButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }
}
