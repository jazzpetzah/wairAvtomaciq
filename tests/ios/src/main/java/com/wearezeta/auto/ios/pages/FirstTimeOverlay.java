package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.Future;

public class FirstTimeOverlay extends IOSPage {
    private static final By nameGotItButton = By.name("GOT IT");

    public FirstTimeOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntiVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGotItButton);
    }

    public boolean waitUntiInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameGotItButton);
    }

    public void acceptIfVisible(int timeoutSeconds) throws Exception {
        final Optional<WebElement> gotItButton = getElementIfDisplayed(nameGotItButton, timeoutSeconds);
        if (gotItButton.isPresent()) {
            gotItButton.get().click();
        }
    }
}