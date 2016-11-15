package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class CallKitOverlayPage extends IOSPage {
    private static final By nameAcceptButton = MobileBy.AccessibilityId("Accept");

    private static final By nameDeclineButton = MobileBy.AccessibilityId("Decline");

    private static final By nameRemindMeButton = MobileBy.AccessibilityId("Remind Me");

    private static final By nameMessageButton = MobileBy.AccessibilityId("Message");

    private static final Function<String, String> nameStrCallLabelByType =
            callType -> String.format("Wire %s…", callType);

    public CallKitOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static By getButtonByName(String name) {
        switch (name.toLowerCase()) {
            case "decline":
                return nameDeclineButton;
            case "accept":
                return nameAcceptButton;
            case "remind me":
                return nameRemindMeButton;
            case "message":
                return nameMessageButton;
            default:
                throw new IllegalArgumentException(String.format("Button name '%s' is unknown", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonByName(name);
        getElement(locator).click();
    }

    public boolean isButtonVisible(String name) throws Exception {
        final By locator = getButtonByName(name);
        return isLocatorDisplayed(locator);
    }

    public boolean isButtonInvisible(String name) throws Exception {
        final By locator = getButtonByName(name);
        return isLocatorInvisible(locator);
    }

    public boolean isVisible(String overlayType) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrCallLabelByType.apply(overlayType));
        return isLocatorDisplayed(locator,15);
    }

    public boolean isInvisible(String overlayType) throws Exception {
        final By locator = MobileBy.AccessibilityId(nameStrCallLabelByType.apply(overlayType));
        return isLocatorInvisible(locator);
    }
}
