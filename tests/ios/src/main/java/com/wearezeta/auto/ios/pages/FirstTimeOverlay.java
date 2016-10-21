package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class FirstTimeOverlay extends IOSPage {
    private static final By nameOKButton = MobileBy.AccessibilityId("OK");

    public FirstTimeOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return isLocatorDisplayed(nameOKButton);
    }

    public boolean waitUntilInvisible() throws Exception {
        return isLocatorInvisible(nameOKButton);
    }

    public void accept() throws Exception {
        getElement(nameOKButton).click();
    }
}