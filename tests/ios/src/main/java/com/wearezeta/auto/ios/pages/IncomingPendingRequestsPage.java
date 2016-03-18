package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class IncomingPendingRequestsPage extends IOSPage {

    private static final By xpathPendingRequestIgnoreButton = By.xpath("(//UIAButton[@name='IGNORE'])[last()]");

    private static final By xpathPendingRequestConnectButton = By.xpath("(//UIAButton[@name='CONNECT'])[last()]");

    private static final Function<String, String> xpathStrPendingRequesterByName = name ->
            String.format("//UIAStaticText[contains(@name, 'Connect to %s')]", name);

    public IncomingPendingRequestsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickIgnoreButton() throws Exception {
        getElement(xpathPendingRequestIgnoreButton, "Ignore button is not visible").click();
    }

    public void clickIgnoreButtonMultiple(int clicks) throws Exception {
        for (int i = 0; i < clicks; i++) {
            getElement(xpathPendingRequestIgnoreButton, "Ignore button is not visible").click();
            // Wait for animation
            Thread.sleep(1000);
        }
    }

    public void clickConnectButton() throws Exception {
        getElement(xpathPendingRequestConnectButton, "Connect button is not visible").click();
    }

    public void clickConnectButtonMultiple(int clicks) throws Exception {
        for (int i = 0; i < clicks; i++) {
            getElement(xpathPendingRequestConnectButton, "Connect button is not visible").click();
            // Wait for animation
            Thread.sleep(1000);
        }
    }

    public boolean isConnectButtonDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathPendingRequestConnectButton);
    }

    public boolean isConnectToNameExist(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrPendingRequesterByName.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
