package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingOutgoingConnectionPage extends IOSPage {

    private static final By xpathConnectOtherUserButton =
            By.xpath("//XCUIElementTypeButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']");

    public PendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isConnectButtonVisible() throws Exception {
        return isElementDisplayed(xpathConnectOtherUserButton);
    }

    public boolean isConnectButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathConnectOtherUserButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(xpathConnectOtherUserButton).click();
        // Wait for animation
        Thread.sleep(2000);
    }
}
