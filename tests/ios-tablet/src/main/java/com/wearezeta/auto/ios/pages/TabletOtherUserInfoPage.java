package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage {
    private static final By nameOtherUserMetaControllerRightButtonIPadPopover =
            MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final Function<String, String> xpathStrOtherUserNameField = name ->
            String.format("%s//*[@name='%s']",
                    TabletGroupConversationDetailPopoverPage.xpathStrPopover, name);

    private static final Function<String, String> xpathStrOtherUserEmailField = email ->
            String.format("%s//*[@name='%s']",
                    TabletGroupConversationDetailPopoverPage.xpathStrPopover, email.toUpperCase());

    private static final By xpathOtherUserConnectLabel = By.xpath(
            TabletGroupConversationDetailPopoverPage.xpathStrPopover + "//UIAStaticText[@name='CONNECT']");

    private static final By xpathOtherUserConnectButton = By.xpath(
            "//UIAStaticText[@name='CONNECT']/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']");

    private static final By nameOtherUserProfilePageCloseButton =
            MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void removeFromConversationOniPad() throws Exception {
        clickElementWithRetryIfStillDisplayed(nameOtherUserMetaControllerRightButtonIPadPopover);
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

    public boolean isNameVisible(String user) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserNameField.apply(user));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherUserEmailField.apply(email));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
