package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.Future;

public class FirstTimeOverlay extends IOSPage {
    private static final By nameOKButton = MobileBy.AccessibilityId("OK");

    public FirstTimeOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameOKButton);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameOKButton);
    }

    public void acceptIfVisible(int timeoutSeconds) throws Exception {
        final Optional<WebElement> gotItButton = getElementIfDisplayed(nameOKButton, timeoutSeconds);
        if (gotItButton.isPresent()) {
            gotItButton.get().click();
        }
    }
}