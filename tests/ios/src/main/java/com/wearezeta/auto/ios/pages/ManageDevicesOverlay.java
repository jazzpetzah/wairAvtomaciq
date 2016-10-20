package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ManageDevicesOverlay extends IOSPage{
    private static final By nameManageDevicesButton = MobileBy.AccessibilityId("MANAGE DEVICES");

    public ManageDevicesOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return isDisplayed(nameManageDevicesButton);
    }

    public boolean waitUntilInvisible() throws Exception {
        return isInvisible(nameManageDevicesButton);
    }
}
