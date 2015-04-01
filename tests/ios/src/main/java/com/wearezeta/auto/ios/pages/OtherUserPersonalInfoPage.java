package com.wearezeta.auto.ios.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
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

	@FindBy(how = How.NAME, using = IOSLocators.nameContinueButton)
	private WebElement continueButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitOtherUserPersonalInfoPageButton)
	private WebElement exitOtherPersonalInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageNameField)
	private WebElement nameField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageEmailField)
	private WebElement emailField;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement startDialogButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConversationMenu)
	private WebElement conversationMenuButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSilenceConversationButton)
	private WebElement silenceMenuButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathSilenceConversationButton)
	private WebElement menuSilenceButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameUnsilenceConversationButton)
	private WebElement notifyMenuButton;

	public OtherUserPersonalInfoPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void catchContinueAlert() {
		try {
			WebElement el = driver
					.findElementByName(IOSLocators.nameContinueButton);
			el.click();
		} catch (NoSuchElementException ex) {
			// do nothing
		}
	}

	public IOSPage leavePageToGroupInfoPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new GroupChatInfoPage(this.getDriver(), this.getWait());
	}
	
	public DialogPage leavePageToDialogPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new DialogPage(this.getDriver(), this.getWait());
	}

	public PeoplePickerPage addContactToChat() throws Exception {
		addButton.click();
		catchContinueAlert();
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	public boolean isOtherUserProfileEmailVisible(String name) {

		WebElement otherUserEmail = driver.findElementByXPath(String.format(
				IOSLocators.xpathOtherUserName, name.toUpperCase()));
		return otherUserEmail.isDisplayed();
	}

	public boolean isOtherUserProfileNameVisible(String name) {
		WebElement otherUserName = driver.findElementByName(name);
		return otherUserName.isEnabled();
	}

	public void continueToAddUser() {
		continueButton.click();
	}

	public void removeFromConversation() {

		DriverUtils.mobileTapByCoordinates(this.getDriver(), removeFromChat);
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmRemove.isDisplayed();
	}

	public void confirmRemove() {
		confirmRemove.click();
	}

	public String getNameFieldValue() {
		return nameField.getAttribute("value");
	}

	public String getEmailFieldValue() {
		String result = "";
		try {
			result = emailField.getAttribute("value");
		} catch (NoSuchElementException ex) {
			
		}
		return result;
	}

	public DialogPage clickOnStartDialogButton() throws Throwable {
		DialogPage page = null;
		this.getDriver().tap(
				1,
				this.getDriver().findElementByName(
						IOSLocators.nameAddContactToChatButton), 1);
		page = new DialogPage(this.getDriver(), this.getWait());
		return page;
	}
	
	public void openConversationMenu() throws InterruptedException{
		conversationMenuButton.click();
		Thread.sleep(2000);
	}
	
	public void clickSilenceMenuButton() throws InterruptedException{
		menuSilenceButton.click();
	}
	
	public void clickNotifyMenuButton() throws InterruptedException{
		notifyMenuButton.click();
        Thread.sleep(2000);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new DialogPage(this.getDriver(), this.getWait());
			break;
		}
		case UP: {
			return this;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

}
