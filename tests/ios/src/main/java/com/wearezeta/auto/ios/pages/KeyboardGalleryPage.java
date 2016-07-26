package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class KeyboardGalleryPage extends IOSPage {

    private static final By xpathOpenCameraRollButton = MobileBy.AccessibilityId("cameraRollButton");

    private static final By nameTakePictureButton = MobileBy.AccessibilityId("takePictureButton");

    private static final By nameToggleCameraButton = MobileBy.AccessibilityId("changeCameraButton");

    private static final By nameFullscreenCameraButton = MobileBy.AccessibilityId("fullscreenCameraButton");

    private static final By xpathFirstPicture =
            By.xpath("//UIACollectionCell[@name='changeCameraButton']/following-sibling::UIACollectionCell");

    public KeyboardGalleryPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapCameraRollButton() throws Exception {
        getElement(xpathOpenCameraRollButton).click();
    }

    public void selectFirstPicture() throws Exception {
        getElement(xpathFirstPicture).click();
    }

    public void tapTakePictureButton() throws Exception {
        getElement(nameTakePictureButton).click();
    }

    public void tapToggleCameraButton() throws Exception {
        getElement(nameToggleCameraButton).click();
    }

    public void tapFullscreenButton() throws Exception {
        getElement(nameFullscreenCameraButton).click();
    }
}
