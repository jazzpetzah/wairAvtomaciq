package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserOnPendingProfilePage extends IOSPage {

	public static final String nameOtherUserProfilePageCloseButton = "OtherUserProfileCloseButton";
	@FindBy(xpath = nameOtherUserProfilePageCloseButton)
	private WebElement closePageButton;

    public static final String xpathOtherProfilePageCancelRequestLabel = "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]";
    @FindBy(xpath = xpathOtherProfilePageCancelRequestLabel)
	private WebElement pendingLabel;

    public static final String nameOtherProfilePageStartConversationButton = "OtherUserMetaControllerLeftButton";
    @FindBy(name = nameOtherProfilePageStartConversationButton)
	private WebElement startConversationButton;

    public static final String xpathOtherProfileCancelRequestButton =
            "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']";
    @FindBy(xpath = xpathOtherProfileCancelRequestButton)
	private WebElement cancelRequestButton;

    public static final String xpathCancelRequestYesButton =
            "//UIAStaticText[@name='Cancel Request?']/following-sibling::UIAButton[@name='YES']";
    @FindBy(xpath = xpathCancelRequestYesButton)
	private WebElement cancelRequestConfirmationYesButton;

    public static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameExitOtherUserPersonalInfoPageButton)
	private WebElement backButtonToGroupPopover;

    public static final String nameRemoveFromConversation = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameRemoveFromConversation)
	private WebElement removePendingPersonFromChat;

    public static final String nameOtherProfilePageCloseButton = "OtherUserProfileCloseButton";

    public static final String nameCancelRequestConfirmationLabel = "Cancel Request?";

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isClosePageButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(nameOtherProfilePageCloseButton));
	}

	public boolean isCancelRequestButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				cancelRequestButton);
	}

	public void clickCancelRequestButton() {
		cancelRequestButton.click();
	}

	public boolean isCancelRequestConfirmationVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						getDriver(), By.name(nameCancelRequestConfirmationLabel), 3);
	}

	public void clickConfirmCancelRequestButton() {
		cancelRequestConfirmationYesButton.click();
	}

	public boolean isCancelRequestLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpathOtherProfilePageCancelRequestLabel));
	}

	public void clickStartConversationButton() {
		startConversationButton.click();
	}

	public boolean isUserNameDisplayed(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(name), 10);
	}

	public boolean isRemoveFromGroupConversationVisible() {
		return removePendingPersonFromChat.isDisplayed();
	}
}
