package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class NewDeviceOverlay extends IOSPage {
    private static final Function<String, String> xpathStrLabelByText = text ->
            String.format("//XCUIElementTypeStaticText[contains(@name, '%s')]", text);

    private static final By nameCloseOverlayButton = MobileBy.AccessibilityId("RightButton");

    public NewDeviceOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isContainingLabel(String expectedLabel) throws Exception {
        final By locator = By.xpath(xpathStrLabelByText.apply(expectedLabel));
        return isLocatorDisplayed(locator);
    }

    public void closeNewDeviceOverlay() throws Exception {
        getElement(nameCloseOverlayButton).click();
    }
}