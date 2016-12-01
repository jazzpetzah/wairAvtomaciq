package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class UniqueUsernameTakeoverPage extends IOSPage {
    private static final By nameChooseYoursButton = MobileBy.AccessibilityId("CHOOSE YOURS");
    private static final By nameKeepThisOneButton = MobileBy.AccessibilityId("KEEP THIS ONE");

    private static final By nameTitleLabel = MobileBy.AccessibilityId("Usernames are here.");

    private static final int VISIBILITY_TIMEOUT_SECONDS = 15;

    public UniqueUsernameTakeoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return isLocatorDisplayed(nameTitleLabel, VISIBILITY_TIMEOUT_SECONDS);
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "choose yours":
                return nameChooseYoursButton;
            case "keep this one":
                return nameKeepThisOneButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        getElement(locator,
                String.format("No '%s' button has been shown after %s seconds", name, VISIBILITY_TIMEOUT_SECONDS),
                VISIBILITY_TIMEOUT_SECONDS).click();
    }
}
