package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletOtherUserInfoPage extends OtherUserPersonalInfoPage {

	@FindBy(how = How.NAME, using = IOSTabletLocators.TabletOtherUserInfoPage.nameOtherUserMetaControllerRightButtoniPadPopover)
	private WebElement removeFromGroupChat;

	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserNameField)
	private WebElement nameFieldPopover;

	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserEmailField)
	private WebElement emailFieldPopover;

	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserConnectLabel)
	private WebElement connectLabel;

	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserConnectButton)
	private WebElement connectButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserProfilePageCloseButton)
	private WebElement goBackButton;

	@FindBy(name = IOSLocators.nameOtherUserConversationMenu)
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
				By.xpath(String.format(
						IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserNameField, user)));
		return name.getAttribute("name");
	}

	public String getEmailFieldValueOniPadPopover() throws Exception {
		String result = "";
		try {
			DriverUtils.waitUntilLocatorAppears(getDriver(),
					By.xpath(IOSLocators.xpathOtherPersonalInfoPageEmailField));
			result = emailFieldPopover.getAttribute("value");
		} catch (NoSuchElementException ex) {

		}
		return result;
	}

	public boolean isConnectLabelVisible() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserConnectLabel),
						5);
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				connectLabel);
	}

	public void clickConnectLabel() {
		connectLabel.click();
	}

	public boolean isConnectButtonVisible() throws Exception {
		DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(IOSTabletLocators.TabletOtherUserInfoPage.xpathOtherUserConnectButton),
						5);
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				connectButton);
	}

	public void clickConnectButton() {
		connectButton.click();
	}

	public void clickGoBackButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), goBackButton);
		goBackButton.click();
	}

	public void exitOtherUserGroupChatPopover() throws Exception {
		DriverUtils.mobileTapByCoordinates(getDriver(), otherUserConversationMenuRightButton,
				50, 50);
	}

}
