package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class OtherUserPersonalInfoPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserEmailField)
	private WebElement otherUserEmail;

	@FindBy(how = How.NAME, using = IOSLocators.nameRemoveFromConversation)
	private WebElement removeFromChat;

	@FindBy(how = How.NAME, using = IOSLocators.nameComfirmRemoveButton)
	private WebElement confirmRemove;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement addButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserAddContactToChatButton)
	private WebElement addOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameContinueButton)
	private WebElement continueButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitOtherUserPersonalInfoPageButton)
	private WebElement exitOtherPersonalInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogInfoPage.xpathArchiveButton)
	private WebElement archiveButton;

	@FindBy(how = How.XPATH, using = IOSLocators.ConversationActionMenu.xpathDeleteConversationButton)
	private WebElement deleteButton;

	@FindBy(how = How.XPATH, using = IOSLocators.ConversationActionMenu.xpathConfirmDeleteButton)
	private WebElement confirmDeleteButton;

	@FindBy(how = How.NAME, using = IOSLocators.ConversationActionMenu.nameAlsoLeaveCheckerButton)
	private WebElement alsoLeaveButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageNameField)
	private List<WebElement> nameField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageEmailField)
	private WebElement emailField;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement startDialogButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOtherUserConversationMenu)
	private WebElement otherUserConversationMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationMenu)
	private WebElement conversationMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSilenceConversationButton)
	private WebElement silenceMenuButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSilenceConversationButton)
	private WebElement menuSilenceButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameUnsilenceConversationButton)
	private WebElement notifyMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameBlockMenuButton)
	private WebElement blockMenuButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCancelButton)
	private WebElement cancelButton;

	@FindBy(how = How.XPATH, using = IOSLocators.ConversationActionMenu.xpathActionMenu)
	private WebElement actionMenu;

	public OtherUserPersonalInfoPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void catchContinueAlert() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.nameContinueButton), 5)) {
			WebElement el = this.getDriver().findElementByName(
					IOSLocators.nameContinueButton);
			DriverUtils.waitUntilElementClickable(getDriver(), el);
			el.click();
		}
	}

	public void openEllipsisMenu() throws Exception {
		openConversationMenu();
	}

	public void clickArchiveMenuButton() {
		archiveButton.click();
	}

	public void clickDeleteMenuButton() {
		deleteButton.click();
	}

	public void clickConfirmDeleteButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), confirmDeleteButton);
		confirmDeleteButton.click();
	}

	public void clickAlsoLeaveButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), alsoLeaveButton);
		alsoLeaveButton.click();
	}

	public IOSPage leavePageToGroupInfoPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new GroupChatInfoPage(this.getLazyDriver());
	}

	public void clickCloseUserProfileButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				exitOtherPersonalInfoPageButton);
		exitOtherPersonalInfoPageButton.click();
	}

	public PeoplePickerPage addContactToChat() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.nameAddContactToChatButton), 2)) {
			addButton.click();
		} else {
			addOtherUserButton.click();
		}
		catchContinueAlert();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isOtherUserProfileNameVisible(String name) throws Exception {
		WebElement otherUserName = getDriver().findElementByName(name);
		return otherUserName.isEnabled();
	}

	public void removeFromConversation() throws Exception {
		DriverUtils.tapByCoordinates(this.getDriver(), removeFromChat);
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmRemove.isDisplayed();
	}

	public void confirmRemove() {
		confirmRemove.click();
	}

	public String getNameFieldValue(String user) throws Exception {
		WebElement name = getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.xpathOtherPersonalInfoPageNameField, user)));
		return name.getAttribute("name");
	}

	public String getEmailFieldValue() throws Exception {
		String result = "";
		try {
			DriverUtils.waitUntilLocatorAppears(getDriver(),
					By.xpath(IOSLocators.xpathOtherPersonalInfoPageEmailField));
			result = emailField.getAttribute("value");
		} catch (NoSuchElementException ex) {

		}
		return result;
	}

	public void clickOnStartDialogButton() throws Throwable {
		this.getDriver().tap(1,
				this.getDriver().findElementByName(
						IOSLocators.nameOtherUserAddContactToChatButton), 1);
	}

	public void openConversationMenu() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.nameConversationMenu), 2)) {
			conversationMenuButton.click();
		} else {
			otherUserConversationMenuButton.click();
		}
		Thread.sleep(2000);
	}

	public void clickSilenceMenuButton() throws InterruptedException {
		menuSilenceButton.click();
	}

	public void clickNotifyMenuButton() throws InterruptedException {
		notifyMenuButton.click();
		Thread.sleep(2000);
	}

	public void clickBlockMenuButton() {
		blockMenuButton.click();
	}

	public void clickCancelButton() {
		cancelButton.click();
	}

	public boolean isActionMenuVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(IOSLocators.ConversationActionMenu.xpathActionMenu));
	}

}
