package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class DeviceDetailsPage extends IOSPage {
    private final static By nameVerifySwitcher = By.xpath("//UIASwitch");

//    private final static By nameShowDeviceFingerprint = By.name("SHOW MY DEVICE FINGERPRINT");

    public DeviceDetailsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapVerifySwitcher() throws Exception {
        getElement(nameVerifySwitcher).click();
    }

    public void tapBackButton() throws Exception {
        // FIXME: Find a way to detect back button
        getDriver().tap(1, 25, 25, DriverUtils.SINGLE_TAP_DURATION);
    }
}
