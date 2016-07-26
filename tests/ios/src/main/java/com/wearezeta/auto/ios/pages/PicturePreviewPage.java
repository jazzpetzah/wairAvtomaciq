package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class PicturePreviewPage extends IOSPage {
    private static final By nameSketchButton = MobileBy.AccessibilityId("editNotConfirmedImageButton");

    public PicturePreviewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapSketchButton() throws Exception {
        getElement(nameSketchButton).click();
    }
}
