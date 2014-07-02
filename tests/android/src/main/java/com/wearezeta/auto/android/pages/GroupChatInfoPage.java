package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends AndroidPage {

	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	
	@FindBy(how = How.ID, using = AndroidLocators.idLeaveConversationButton)
	private WebElement leaveConversationButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private WebElement confirmButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idGroupChatUserGrid)
	private WebElement groupChatUsersGrid;
	
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

		leaveConversationButton.click();

	}

	public void pressConfirmButton() {
		confirmButton.click();
	}

	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	public OtherUserPersonalInfoPage selectContactByName(String contactName) throws IOException {
		boolean flag = false;
		List<WebElement> users = groupChatUsersGrid.findElements(By.className(AndroidLocators.classNameLinearLayout));
		for(WebElement user : users)
		{
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.classNameTextView));
			for(WebElement element : elements){
				try{
					if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
						user.click();
						flag = true;
						break;
					}
				}
				catch(Exception ex){
					continue;
				}
			}
			if(flag){
				break;
			}
		}

		return new OtherUserPersonalInfoPage(url, path);
	}

	public GroupChatPage tabBackButton() throws IOException {
		driver.navigate().back();
		return new GroupChatPage(url, path);
	}

}
