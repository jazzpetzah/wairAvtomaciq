package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BasePendingIncomingConnectionPage extends BaseUserDetailsOverlay {
    private static final By xpathPendingRequestIgnoreButton =
            By.xpath("(//XCUIElementTypeButton[@name='IGNORE'])[last()]");

    private static final By xpathPendingRequestConnectButton =
            By.xpath("(//XCUIElementTypeButton[@name='CONNECT'])[last()]");

    private static final Function<String, String> xpathStrPendingRequesterByName = name ->
            String.format("//XCUIElementTypeStaticText[contains(@name, 'Connect to %s')]", name);

    public BasePendingIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
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
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(2000);
    }

    public boolean isConnectToNameExist(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrPendingRequesterByName.apply(expectedName));
        return isLocatorDisplayed(locator);
    }
}
