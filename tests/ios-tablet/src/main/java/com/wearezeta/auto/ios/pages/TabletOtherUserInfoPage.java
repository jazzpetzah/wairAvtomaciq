package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage {
    private static final By nameOtherUserMetaControllerRightButtonIPadPopover =
            MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final Function<String, String> xpathStrOtherUserEmailField = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());

    private static final By xpathOtherUserConnectButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    // idx starts from 1
    private static final Function<Integer, String> xpathStrDeviceByIndex = idx ->
            String.format("//XCUIElementTypeButton[@name='DEVICES']/following::" +
                    "XCUIElementTypeTable[1]/XCUIElementTypeCell[%d]", idx);

    private static final By nameOtherUserProfilePageCloseButton =
            MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void removeFromConversationOniPad() throws Exception {
        tapElementWithRetryIfStillDisplayed(nameOtherUserMetaControllerRightButtonIPadPopover);
    }

    public boolean isConnectButtonVisible() throws Exception {
        return isLocatorDisplayed(xpathOtherUserConnectButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(xpathOtherUserConnectButton).click();
    }

    public void tapGoBackButton() throws Exception {
        getElement(nameOtherUserProfilePageCloseButton).click();
    }

    public void exitOtherUserGroupChatPopover() throws Exception {
        this.tapAtTheCenterOfElement((FBElement) getElement(fbNameOtherUserConversationMenu));
    }

    public boolean isNameVisible(String user) throws Exception {
        final By locator = MobileBy.AccessibilityId(user);
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserEmailField.apply(email));
        return isLocatorDisplayed(locator);
    }

    @Override
    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
    }
}
