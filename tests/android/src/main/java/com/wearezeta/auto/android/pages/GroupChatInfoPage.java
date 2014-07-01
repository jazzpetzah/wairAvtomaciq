package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends AndroidPage {

	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> optionsButtons;
	
	@FindBy(how = How.ID, using = AndroidLocators.idLeaveConversationConfirmationMenu)
	private WebElement leaveConversationConfirmationMenu;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private List<WebElement> confirmButtons;
	
	@FindBy(how = How.ID, using = AndroidLocators.idGroupChatUserName)
	private List<WebElement> groupChatUsersNames;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private WebElement groupChatName;
	
	private String url;
	private String path;
	
	public GroupChatInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			break;
		}
		case LEFT:
		{
			break;
		}
		case RIGHT:
		{
			page = new GroupChatPage(url,path);
			break;
		}
		}	
		return page;
	}

	public void pressLeaveConversationButton() {
		for(WebElement button : optionsButtons)
		 {
			 if (button.getText().equals(LEAVE_CONVERSATION_BUTTON))
			 {
				 button.click();
				 break;
			 }
		 }
		
	}

	public void pressConfirmButton() {
		wait.until(ExpectedConditions.visibilityOf(leaveConversationConfirmationMenu));
		for(WebElement button : confirmButtons)
		{
			if (button.getText().equals(LEAVE_BUTTON))
			 {
				 button.click();
				 break;
			 }
		}
	}

	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	public OtherUserPersonalInfoPage selectContactByName(String contactName) throws IOException {
		
		for(WebElement element : groupChatUsersNames)
		 {
			 try{
				 if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
					 element.click();
					 break;
				 }
			 }
			 catch(Exception ex){
				 	continue;
			 }
		 }
		
		return new OtherUserPersonalInfoPage(url, path);
	}

}
