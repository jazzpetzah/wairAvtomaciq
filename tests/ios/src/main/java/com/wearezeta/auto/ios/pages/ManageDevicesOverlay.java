package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.Future;

public class ManageDevicesOverlay extends IOSPage{
    private static final By nameManageDevicesButton = By.name("MANAGE DEVICES");

    public ManageDevicesOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntiVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameManageDevicesButton);
    }

    public boolean waitUntiInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameManageDevicesButton);
    }

    public void acceptIfVisible(int timeoutSeconds) throws Exception {
        final Optional<WebElement> manageDevicesButton = getElementIfDisplayed(nameManageDevicesButton, timeoutSeconds);
        if (manageDevicesButton.isPresent()) {
            manageDevicesButton.get().click();
        }
    }
}
