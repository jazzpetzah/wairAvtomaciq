package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserOnPendingProfilePage extends IOSPage {

    private static final By xpathOtherProfileCancelRequestButton =
            By.xpath("//XCUIElementTypeButton[@label='CANCEL REQUEST']");

    private static final By xpathCancelRequestYesButton = By.xpath(
            "//XCUIElementTypeStaticText[@name='Cancel Request?']/following::XCUIElementTypeButton[@name='YES']");

    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameOtherProfilePageCloseButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By xpathConnectConfirmButton = By.xpath("(//XCUIElementTypeButton[@name='CONNECT'])[last()]");

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isClosePageButtonVisible() throws Exception {
        return isLocatorDisplayed(nameOtherProfilePageCloseButton);
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return isLocatorDisplayed(xpathOtherProfileCancelRequestButton);
    }

    public void tapCancelRequestButton() throws Exception {
        getElement(xpathOtherProfileCancelRequestButton).click();
    }

    public void confirmCancelRequest() throws Exception {
        getElement(xpathCancelRequestYesButton).click();
    }

    public void tapStartConversationButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        return isLocatorDisplayed(MobileBy.AccessibilityId(name), 10);
    }

    public boolean isRemoveFromGroupConversationVisible() throws Exception {
        return isLocatorDisplayed(nameRightActionButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public void confirmConnect() throws Exception {
        getElement(xpathConnectConfirmButton).click();
    }

    public void tapCloseButton() throws Exception {
        getElement(nameOtherProfilePageCloseButton).click();
    }
}
