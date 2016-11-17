package com.wearezeta.auto.ios.pages;

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

     private By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "not now":
                return nameNotNowButton;
            case "share contacts":
                return xpathShareContactsButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        this.tapElementWithRetryIfStillDisplayed(getButtonByName(name));
    }
}