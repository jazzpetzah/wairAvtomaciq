package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ShareContactsOverlay extends IOSPage {
    private static final By xpathShareContactsButton = By.xpath("//*[@label='SHARE CONTACTS']");

    private static final By nameNotNowButton = MobileBy.AccessibilityId("NOT NOW");

    public ShareContactsOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathShareContactsButton);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathShareContactsButton);
    }

    public void tapShareContactsButton() throws Exception {
        getElement(xpathShareContactsButton).click();
    }

    public void tapNotNowButton() throws Exception {
        getElement(nameNotNowButton).click();
    }
}