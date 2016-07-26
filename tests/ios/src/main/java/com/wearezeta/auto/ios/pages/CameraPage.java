package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraPage extends IOSPage {
    private static final By nameCameraRollButton = MobileBy.AccessibilityId("Camera Roll");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("cameraButton");

    public CameraPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);

        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
        }
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(nameTakePhotoButton).click();
    }

    public void tapCameraRollButton() throws Exception {
        getElement(nameCameraRollButton).click();
    }
}
