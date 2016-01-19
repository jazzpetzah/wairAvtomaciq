package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage {
    private static final By nameOtherUserMetaControllerRightButtoniPadPopover =
            By.name("OtherUserMetaControllerRightButton");

    private static final Function<String, String> xpathStrOtherUserNameField = name ->
            String.format("%s/UIAPopover[1]/UIAStaticText[@name='%s']", xpathStrMainWindow, name);

    private static final By xpathOtherUserEmailField = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIATextView[contains(@name, 'WIRE.COM')]");

    private static final By xpathOtherUserConnectLabel = By.xpath("//UIAPopover/UIAStaticText[@name='CONNECT']");

    private static final By xpathOtherUserConnectButton = By.xpath(
            "//UIAStaticText[@name='CONNECT']/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']");

    private static final By nameOtherUserProfilePageCloseButton = By.name("OtherUserProfileCloseButton");

    public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void removeFromConversationOniPad() throws Exception {
        getElement(nameOtherUserMetaControllerRightButtoniPadPopover).click();
    }

    public String getNameFieldValueOniPadPopover(String user) throws Exception {
        return getElement(By.xpath(xpathStrOtherUserNameField.apply(user))).getAttribute("name");
    }

    public String getEmailFieldValueOniPadPopover() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), xpathOtherPersonalInfoPageEmailField);
        return getElement(xpathOtherUserEmailField).getAttribute("value");
    }

    public boolean isConnectLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOtherUserConnectLabel);
    }

    public boolean isConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOtherUserConnectButton);
    }

    public void clickConnectButton() throws Exception {
        getElement(xpathOtherUserConnectButton).click();
    }

    public void clickGoBackButton() throws Exception {
        getElement(nameOtherUserProfilePageCloseButton).click();
    }

    public void exitOtherUserGroupChatPopover() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(nameOtherUserConversationMenu), 50, 50);
    }

}
