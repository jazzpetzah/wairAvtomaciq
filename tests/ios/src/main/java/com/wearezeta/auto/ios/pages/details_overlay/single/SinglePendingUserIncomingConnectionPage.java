package com.wearezeta.auto.ios.pages.details_overlay.single;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.ios.pages.details_overlay.BasePendingIncomingConnectionPage;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SinglePendingUserIncomingConnectionPage extends BasePendingIncomingConnectionPage {
    private static final Function<String, String> nameStrUserNameByValue =
            value -> String.format("Connect to %s", value);

    public SinglePendingUserIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "ignore":
                return xpathPendingRequestIgnoreButton;
            case "connect":
                return xpathPendingRequestConnectButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s' for Pending requests ",
                        name));
        }
    }

    @Override
    public boolean isNameVisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrUserNameByValue.apply(value));
        return isLocatorDisplayed(locator);
    }

    @Override
    public boolean isNameInvisible(String value) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrUserNameByValue.apply(value));
        return isLocatorInvisible(locator);
    }
}
