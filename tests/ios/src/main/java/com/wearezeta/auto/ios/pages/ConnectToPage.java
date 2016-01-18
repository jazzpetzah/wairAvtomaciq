package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ConnectToPage extends IOSPage {

    private static final By xpathConnectOtherUserButton =
            By.xpath("//UIAButton[@name='CONNECT' or @name='OtherUserMetaControllerLeftButton']");

    public ConnectToPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isConnectToUserDialogVisible() throws Exception {
        return isConnectButtonVisible();
    }

    public boolean isConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathConnectOtherUserButton);
    }

    public void sendInvitation() throws Exception {
        final Optional<WebElement> connectOtherUserButton = getElementIfDisplayed(xpathConnectOtherUserButton);
        if (connectOtherUserButton.isPresent()) {
            connectOtherUserButton.get().click();
        } else if (isKeyboardVisible()) {
            clickKeyboardEnterButton();
            getElement(xpathConnectOtherUserButton).click();
        }
    }
}
