package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class IncomingPendingRequestsPage extends IOSPage {

    private static final By xpathPendingRequestIgnoreButton =
            By.xpath("(//XCUIElementTypeButton[@name='IGNORE'])[last()]");

    private static final By xpathPendingRequestConnectButton =
            By.xpath("(//XCUIElementTypeButton[@name='CONNECT'])[last()]");

    private static final Function<String, String> xpathStrPendingRequesterByName = name ->
            String.format("//XCUIElementTypeStaticText[contains(@name, 'Connect to %s')]", name);

    public IncomingPendingRequestsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocatorByName(String name) {
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

    public void tapButton(String name, int times) throws Exception {
        assert times > 0 : String.format("Cannot tap '%s' button %s times", name, times);
        final By locator = getButtonLocatorByName(name);
        for (int i = 0; i < times; i++) {
            getElement(locator).click();
            // Wait for animation
            Thread.sleep(1000);
        }
    }

    public void tapButton(String name) throws Exception {
        tapButton(name, 1);
    }

    public boolean isConnectButtonDisplayed() throws Exception {
        return isDisplayed(xpathPendingRequestConnectButton);
    }

    public boolean isConnectToNameExist(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrPendingRequesterByName.apply(expectedName));
        return isDisplayed(locator);
    }
}
