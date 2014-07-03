package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameRemoveFromConversation)
	private WebElement removeFromChat;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameComfirmRemoveButton)
	private WebElement confirmRemove;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	private WebElement addButton;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;

	}
	
	public PeoplePickerPage addContactToChat() throws MalformedURLException {
		addButton.click();
		return new PeoplePickerPage(url, path);
	}
	
	public boolean isOtherUserProfileEmailVisible(String name) {
		
		WebElement otherUserEmail = driver.findElementByXPath(String.format(IOSLocators.xpathOtherUserName, name));
		return otherUserEmail.isDisplayed();
	}
	
	public void removeFromConversation() {
		
		removeFromChat.click();
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
