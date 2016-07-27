package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraPage extends IOSPage {
    private static final By nameCameraRollButton = MobileBy.AccessibilityId("cameraLibraryButton");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("cameraShutterButton");

    private static final By nameCloseButton = MobileBy.AccessibilityId("cameraCloseButton");

    private boolean isTestImageUploaded = false;

    public CameraPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "take photo":
                return nameTakePhotoButton;
            case "camera roll":
                return nameCameraRollButton;
            case "close":
                return nameCloseButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        if (!isTestImageUploaded && locator.equals(nameCameraRollButton) &&
                CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
            isTestImageUploaded = true;
        }
        getElement(getButtonByName(name)).click();
    }
}
