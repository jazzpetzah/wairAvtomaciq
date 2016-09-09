package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class NewDeviceOverlay extends IOSPage {
    private static final Function<String, String> xpathStrLabelByText = text ->
            String.format("//StaticText[contains(@name, '%s')]", text);

    private static final By nameSendAnywayButton = MobileBy.AccessibilityId("SEND ANYWAY");

    private static final By nameShowDeviceButton = MobileBy.AccessibilityId("SHOW DEVICE");

    private static final By nameCloseOverlayButton = MobileBy.AccessibilityId("RightButton");

    public NewDeviceOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isContainingLabel(String expectedLabel) throws Exception {
        final By locator = By.xpath(xpathStrLabelByText.apply(expectedLabel));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapShowDeviceButton() throws Exception {
        getElement(nameShowDeviceButton).click();
    }

    public void tapSendAnywayButton() throws Exception {
        getElement(nameSendAnywayButton).click();
    }

    public void closeNewDeviceOverlay() throws Exception {
        getElement(nameCloseOverlayButton).click();
    }
}