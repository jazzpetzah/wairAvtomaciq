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

    private static final Function<String, String> xpathStrOtherUserNameField = name ->
            String.format("(//*[@name='%s'])[last()]", name);

    private static final Function<String, String> xpathStrOtherUserEmailField = email ->
            String.format("(//*[@name='%s'])[last()]", email.toUpperCase());

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
        return isElementDisplayed(xpathOtherUserConnectButton);
    }

    public void clickConnectButton() throws Exception {
        getElement(xpathOtherUserConnectButton).click();
    }

    public void clickGoBackButton() throws Exception {
        getElement(nameOtherUserProfilePageCloseButton).click();
    }

    public void exitOtherUserGroupChatPopover() throws Exception {
        this.tapAtTheCenterOfElement((FBElement) getElement(fbNameOtherUserConversationMenu));
    }

    public boolean isNameVisible(String user) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserNameField.apply(user));
        return isElementDisplayed(locator);
    }

    public boolean isEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserEmailField.apply(email));
        return isElementDisplayed(locator);
    }

    @Override
    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
    }
}
