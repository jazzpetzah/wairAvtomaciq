package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ManageDevicesOverlay extends AndroidPage {

    private static final By idManageDevicesButton = By.id("zb__otr_device_limit__manage_devices");

    public ManageDevicesOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
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
