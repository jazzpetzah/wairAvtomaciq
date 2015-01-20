package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class OtherUserPersonalInfoPage extends IOSPage{
	
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
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageNameField )
	private WebElement nameField;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherPersonalInfoPageEmailField )
	private WebElement emailField;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement startDialogButton;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}
	
	public void catchContinueAlert() {
		try {
			WebElement el = driver.findElementByName(IOSLocators.nameContinueButton);
			el.click();
		}
		catch (NoSuchElementException ex) {
			//do nothing
		}
	}
	
	public IOSPage leavePageToGroupInfoPage() throws IOException{
		exitOtherPersonalInfoPageButton.click();
		return new GroupChatInfoPage(url, path);
	}
	
	public PeoplePickerPage addContactToChat() throws IOException {
		addButton.click();
		catchContinueAlert();
		return new PeoplePickerPage(url, path);
	}
	
	public boolean isOtherUserProfileEmailVisible(String name) {
		
		WebElement otherUserEmail = driver.findElementByXPath(String.format(IOSLocators.xpathOtherUserName, name.toUpperCase()));
		return otherUserEmail.isDisplayed();
	}
	
	public boolean isOtherUserProfileNameVisible(String name) {
		WebElement otherUserName = driver.findElementByName(name);
		return  otherUserName.isEnabled();
	}
	
	public void continueToAddUser(){
		continueButton.click();
	}
	
	public void removeFromConversation() {
		
		DriverUtils.mobileTapByCoordinates(driver, removeFromChat);
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
		return emailField.getAttribute("value");
	}
	
	public DialogPage clickOnStartDialogButton() throws Throwable{
		DialogPage page = null;
		driver.tap(1, driver.findElementByName(IOSLocators.nameAddContactToChatButton), 1);
		page = new DialogPage(url, path);
		return page;
	}
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction){
		case DOWN:
		{
			page = new ContactListPage(url, path);
			break;
		}
		case UP:
		{
			return this;
		}
		case LEFT:
		{
			page = new DialogPage(url, path);
			break;
		}
		case RIGHT:
		{
			break;
		}
	}	
		return page;
	}

}
