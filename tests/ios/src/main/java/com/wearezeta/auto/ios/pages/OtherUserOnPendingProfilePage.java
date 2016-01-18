package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserOnPendingProfilePage extends IOSPage {

    private static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";
    @FindBy(xpath = nameOtherUserProfilePageCloseButton)
    private WebElement closePageButton;

    private static final String xpathOtherProfilePageCancelRequestLabel = "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]";
    @FindBy(xpath = xpathOtherProfilePageCancelRequestLabel)
    private WebElement pendingLabel;

    private static final String nameOtherProfilePageStartConversationButton = "OtherUserMetaControllerLeftButton";
    @FindBy(name = nameOtherProfilePageStartConversationButton)
    private WebElement startConversationButton;

    private static final String xpathOtherProfileCancelRequestButton =
            "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']";
    @FindBy(xpath = xpathOtherProfileCancelRequestButton)
    private WebElement cancelRequestButton;

    private static final String xpathCancelRequestYesButton =
            "//UIAStaticText[@name='Cancel Request?']/following-sibling::UIAButton[@name='YES']";
    @FindBy(xpath = xpathCancelRequestYesButton)
    private WebElement cancelRequestConfirmationYesButton;

    private static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameExitOtherUserPersonalInfoPageButton)
    private WebElement backButtonToGroupPopover;

    private static final String nameRemoveFromConversation = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameRemoveFromConversation)
    private WebElement removePendingPersonFromChat;

    private static final String nameOtherProfilePageCloseButton = "OtherUserProfileCloseButton";

    private static final String nameCancelRequestConfirmationLabel = "Cancel Request?";

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isClosePageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.name(nameOtherProfilePageCloseButton));
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathOtherProfileCancelRequestButton));
    }

    public void clickCancelRequestButton() {
        cancelRequestButton.click();
    }

    public boolean isCancelRequestConfirmationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(
                getDriver(), By.name(nameCancelRequestConfirmationLabel), 3);
    }

    public void clickConfirmCancelRequestButton() {
        cancelRequestConfirmationYesButton.click();
    }

    public boolean isCancelRequestLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathOtherProfilePageCancelRequestLabel));
    }

    public void clickStartConversationButton() {
        startConversationButton.click();
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(name), 10);
    }

    public boolean isRemoveFromGroupConversationVisible() {
        return removePendingPersonFromChat.isDisplayed();
    }
}
