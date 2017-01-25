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
    private static final By nameShowDeviceButton = MobileBy.AccessibilityId("SHOW DEVICE");
    private static final By nameSendAnywayButton = MobileBy.AccessibilityId("SEND ANYWAY");

    public NewDeviceOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isContainingLabel(String expectedLabel) throws Exception {
        final By locator = By.xpath(xpathStrLabelByText.apply(expectedLabel));
        return isLocatorDisplayed(locator);
    }

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "x":
                return nameCloseOverlayButton;
            case "show device":
                return nameShowDeviceButton;
            case "send anyway":
                return nameSendAnywayButton;
            default:
                throw new IllegalArgumentException(String.format("There is no '%s' button on Search UI page", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }
}