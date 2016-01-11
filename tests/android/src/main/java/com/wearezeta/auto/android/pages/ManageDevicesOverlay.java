package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class ManageDevicesOverlay extends AndroidPage {

    private static final String idManageDevicesButton = "zb__otr_device_limit__manage_devices";
    @FindBy(id = idManageDevicesButton)
    private WebElement manageDevicesBtn;

    public ManageDevicesOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idManageDevicesButton));
    }

    public boolean isInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idManageDevicesButton));
    }

    public void tapManageDevicesButton() {
        manageDevicesBtn.click();
    }
}
