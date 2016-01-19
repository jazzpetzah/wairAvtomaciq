package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage {
    public static final String nameOtherUserMetaControllerRightButtoniPadPopover = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameOtherUserMetaControllerRightButtoniPadPopover)
    private WebElement removeFromGroupChat;

    public static final String xpathOtherUserNameField =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAStaticText[@name='%s']";
    @FindBy(xpath = xpathOtherUserNameField)
    private WebElement nameFieldPopover;

    public static final String xpathOtherUserEmailField =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATextView[contains(@name, 'WIRE.COM')]";
    @FindBy(xpath = xpathOtherUserEmailField)
    private WebElement emailFieldPopover;

    public static final String xpathOtherUserConnectLabel = "//UIAPopover/UIAStaticText[@name='CONNECT']";
    @FindBy(xpath = xpathOtherUserConnectLabel)
    private WebElement connectLabel;

    public static final String xpathOtherUserConnectButton =
            "//UIAStaticText[@name='CONNECT']/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']";
    @FindBy(xpath = xpathOtherUserConnectButton)
    private WebElement connectButton;

    public static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameOtherUserProfilePageCloseButton)
    private WebElement goBackButton;

    public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void removeFromConversationOniPad() throws Exception {
        removeFromGroupChat.click();
    }

    public String getNameFieldValueOniPadPopover(String user) throws Exception {
        WebElement name = getDriver().findElement(
                By.xpath(String.format(xpathOtherUserNameField, user)));
        return name.getAttribute("name");
    }

    public String getEmailFieldValueOniPadPopover() throws Exception {
        try {
            DriverUtils.waitUntilLocatorAppears(getDriver(), xpathOtherPersonalInfoPageEmailField);
            return emailFieldPopover.getAttribute("value");
        } catch (NoSuchElementException ex) {
            return "";
        }
    }

    public boolean isConnectLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathOtherUserConnectLabel));
    }

    public boolean isConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathOtherUserConnectButton));
    }

    public void clickConnectButton() {
        connectButton.click();
    }

    public void clickGoBackButton() throws Exception {
        getElement(By.name(nameOtherUserProfilePageCloseButton)).click();
    }

    public void exitOtherUserGroupChatPopover() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(nameOtherUserConversationMenu), 50, 50);
    }

}
