package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingOutgoingConnectionPage extends IOSPage {
    private static final By xpathConnectOtherUserButton =
            By.xpath("//XCUIElementTypeButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']");

    private static final By xpathCancelRequestButton = By.xpath("//XCUIElementTypeButton[@label='CANCEL REQUEST']");

    public PendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "connect":
                return xpathConnectOtherUserButton;
            case "cancel request":
                return xpathCancelRequestButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        if (locator.equals(xpathConnectOtherUserButton)) {
            this.tapElementWithRetryIfStillDisplayed(xpathConnectOtherUserButton);
            // Wait for animation
            Thread.sleep(2000);
        } else {
            getElement(locator).click();
        }
    }

    public boolean isButtonVisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return isLocatorDisplayed(locator);
    }

    public boolean isButtonInvisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return isLocatorInvisible(locator);
    }
}
