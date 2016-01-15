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

    @FindBy(name = nameOtherUserConversationMenu)
    private WebElement otherUserConversationMenuRightButton;

    public TabletOtherUserInfoPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
        // TODO Auto-generated constructor stub
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
        String result = "";
        try {
            DriverUtils.waitUntilLocatorAppears(getDriver(),
                    By.xpath(xpathOtherPersonalInfoPageEmailField));
            result = emailFieldPopover.getAttribute("value");
        } catch (NoSuchElementException ex) {

        }
        return result;
    }

    public boolean isConnectLabelVisible() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathOtherUserConnectLabel), 5);
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), connectLabel);
    }

    public boolean isConnectButtonVisible() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathOtherUserConnectButton), 5);
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), connectButton);
    }

    public void clickConnectButton() {
        connectButton.click();
    }

    public void clickGoBackButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), goBackButton);
        goBackButton.click();
    }

    public void exitOtherUserGroupChatPopover() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), otherUserConversationMenuRightButton,
                50, 50);
    }

}
