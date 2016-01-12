package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class OtherUserOnPendingProfilePage extends IOSPage {

	@FindBy(how = How.XPATH, using = IOSLocators.nameOtherUserProfilePageCloseButton)
	private WebElement closePageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherProfilePageCancelRequestLabel)
	private WebElement pendingLabel;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherProfilePageStartConversationButton)
	private WebElement startConversationButton;

	@FindBy(how = How.XPATH, using = IOSLocators.OtherUserProfilePage.xpathOtherProfileCancelRequestButton)
	private WebElement cancelRequestButton;

	@FindBy(how = How.XPATH, using = IOSLocators.OtherUserProfilePage.xpathCancelRequestYesButton)
	private WebElement cancelRequestConfirmationYesButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameExitOtherUserPersonalInfoPageButton)
	private WebElement backButtonToGroupPopover;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameRemoveFromConversation)
	private WebElement removePendingPersonFromChat;

	public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isClosePageButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameOtherProfilePageCloseButton));
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
						getDriver(),
						By.name(IOSLocators.OtherUserProfilePage.nameCancelRequestConfirmationLabel),
						3);
	}

	public void clickConfirmCancelRequestButton() {
		cancelRequestConfirmationYesButton.click();
	}

	public boolean isCancelRequestLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathOtherProfilePageCancelRequestLabel));
	}

	public void clickStartConversationButton() {
		startConversationButton.click();
	}

	public boolean isUserNameDisplayed(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(name), 10);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRemoveFromGroupConversationVisible() {
		return removePendingPersonFromChat.isDisplayed();
	}
}
