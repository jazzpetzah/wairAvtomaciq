package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ManageDevicesOverlay extends IOSPage{
    private static final By nameManageDevicesButton = MobileBy.AccessibilityId("MANAGE DEVICES");

    public ManageDevicesOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntiVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameManageDevicesButton);
    }

    public boolean waitUntiInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameManageDevicesButton);
    }
}
