package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class SheetOverlay extends IOSPage {

    private static final By nameSheetOverlayCancelButton = MobileBy.AccessibilityId("Cancel");

    public SheetOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "cancel":
                return nameSheetOverlayCancelButton;
            default:
                throw new IllegalArgumentException(
                        String.format("The '%s' button is unknown to Sheet overlay", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }
}
