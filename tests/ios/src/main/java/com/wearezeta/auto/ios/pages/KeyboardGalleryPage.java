package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class KeyboardGalleryPage extends IOSPage {

    private static final By xpathOpenCameraRollButton = MobileBy.AccessibilityId("cameraRollButton");

    private static final By nameTakePictureButton = MobileBy.AccessibilityId("takePictureButton");

    private static final String nameStrToggleCameraButton = "changeCameraButton";
    private static final By nameToggleCameraButton = MobileBy.AccessibilityId(nameStrToggleCameraButton);

    private static final By nameFullscreenCameraButton = MobileBy.AccessibilityId("fullscreenCameraButton");

    private static final By xpathFirstPicture = By.xpath(String.format(
            "//XCUIElementTypeCollectionView[ .//XCUIElementTypeButton[@name='%s'] ]" +
                    "/XCUIElementTypeCell[2]", nameStrToggleCameraButton));

    private static final By nameBackButton = MobileBy.AccessibilityId("goBackButton");

    public KeyboardGalleryPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void selectFirstPicture() throws Exception {
        getElement(xpathFirstPicture).click();
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "camera shutter":
                return nameTakePictureButton;
            case "camera roll":
                return xpathOpenCameraRollButton;
            case "toggle camera":
                return nameToggleCameraButton;
            case "fullscreen camera":
                return nameFullscreenCameraButton;
            case "back":
                return nameBackButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
        if (locator.equals(xpathOpenCameraRollButton)) {
            // Wait for Camera Roll initialization
            Thread.sleep(3000);
        }
    }

    public boolean isButtonVisible(String name) throws Exception {
        return isLocatorDisplayed(getButtonLocatorByName(name));
    }

    public boolean isButtonInvisible(String name) throws Exception {
        return isLocatorInvisible(getButtonLocatorByName(name));
    }
}
