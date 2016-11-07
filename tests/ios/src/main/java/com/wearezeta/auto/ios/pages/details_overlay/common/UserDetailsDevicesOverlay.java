package com.wearezeta.auto.ios.pages.details_overlay.common;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.ios.pages.details_overlay.ISupportsTabSwitching;
import com.wearezeta.auto.ios.pages.devices_overlay.BaseUserDevicesOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class UserDetailsDevicesOverlay extends BaseUserDevicesOverlay implements ISupportsTabSwitching {
    private static final String xpathStrDevicesRoot =
            "//XCUIElementTypeButton[@name='DEVICES']/following::XCUIElementTypeTable";

    private static final Function<String, String> xpathStrLinkBlockByText = text ->
            String.format("//*[contains(@value, '%s')]", text);

    public UserDetailsDevicesOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected String getDevicesListRootXPath() {
        return xpathStrDevicesRoot;
    }

    public void tapLink(String expectedLink) throws Exception {
        final By locator = FBBy.xpath(xpathStrLinkBlockByText.apply(expectedLink));
        this.tapByPercentOfElementSize((FBElement) getElement(locator), 15, 95);
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
        // Wait for animation
        Thread.sleep(1000);
    }
}
