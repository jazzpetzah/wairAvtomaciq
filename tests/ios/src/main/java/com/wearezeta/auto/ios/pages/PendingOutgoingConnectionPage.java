package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PendingOutgoingConnectionPage extends IOSPage {

    private static final By xpathConnectOtherUserButton =
            By.xpath("//XCUIElementTypeButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']");

    public PendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isConnectButtonVisible() throws Exception {
        return isDisplayed(xpathConnectOtherUserButton);
    }

    public boolean isConnectButtonInvisible() throws Exception {
        return isInvisible(xpathConnectOtherUserButton);
    }

    public void tapConnectButton() throws Exception {
        this.tapElementWithRetryIfStillDisplayed(xpathConnectOtherUserButton);
        // Wait for animation
        Thread.sleep(2000);
    }
}
