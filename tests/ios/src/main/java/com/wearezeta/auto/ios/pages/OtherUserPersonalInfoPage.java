package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameProfileOtherUserNameField)
	private WebElement otherUserName;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameProfileOtherUserEmailField)
	private WebElement otherUserEmail;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameRemoveFromConversation)
	private WebElement removeFromChat;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameComfirmRemoveButton)
	private WebElement confirmRemove;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}
	
	public String getOtherUserProfileName(){
		return otherUserName.getText();
	}
	
	public String getOtherUserProfileEmail(){
		return otherUserEmail.getText();
	}
	
	public void removeFromConversation() {
		removeFromChat.click();
	}
	
	public boolean isOtherUserNameVisible(String name){
		return getOtherUserProfileName().equals(name);
	}
	
	public boolean isRemoveFromConversationAlertVisible() {
		return confirmRemove.isDisplayed();
	}
	
	public void confirmRemove() {
		confirmRemove.click();
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
		switch (direction){
		case DOWN:
		{
			page = new PeoplePickerPage(url, path);
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
