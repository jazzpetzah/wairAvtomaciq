package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class TabletManageDevicesOverlay extends AndroidTabletPage {

    private static final By idManageDevicesButton = By.id("zb__otr_device_limit__manage_devices");

    public TabletManageDevicesOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idManageDevicesButton);
    }

    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idManageDevicesButton);
    }

    public void tapManageDevicesButton() throws Exception {
        getElement(idManageDevicesButton).click();
    }
}
