package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraPage extends IOSPage {
    private static final By nameCameraRollButton = MobileBy.AccessibilityId("Camera Roll");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("PhotoCapture");

    private static final By nameSketchButton = MobileBy.AccessibilityId("editNotConfirmedImageButton");

    // private static final By nameCancelButton = MobileBy.AccessibilityId("Cancel");

    private boolean isTestImageUploaded = false;

    public CameraPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(nameTakePhotoButton).click();
    }

    public void tapCameraRollButton() throws Exception {
        if (!isTestImageUploaded && CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
            isTestImageUploaded = true;
        }
        getElement(nameCameraRollButton).click();
    }

    public void tapSketchButton() throws Exception {
        getElement(nameSketchButton).click();
    }
}
