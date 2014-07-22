package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends AndroidPage {

	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	
	@FindBy(how = How.ID, using = AndroidLocators.idLeaveConversationButton)
	private WebElement leaveConversationButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private WebElement confirmButton;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLinearLayout)
	private List<WebElement> linearLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameGridView)
	private WebElement groupChatUsersGrid;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private WebElement groupChatName;
	
	private String url;
	private String path;
	
	public GroupChatInfoPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception {
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
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(leaveConversationButton));
		leaveConversationButton.click();

	}

	public void pressConfirmButton() {
		refreshUITree();//TODO workaround
		confirmButton.click();
	}

	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	
	public OtherUserPersonalInfoPage selectContactByName(String contactName) throws Exception, InterruptedException {
		boolean flag = false;
		refreshUITree();
		
		for(WebElement user : linearLayout)
		{
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.classNameTextView));
			for(WebElement element : elements){
					if(element.getText() != null && element.getText().equals((contactName.toUpperCase()))){
						user.click();
						flag = true;
						break;
					}
			}
			if(flag){
				break;
			}
		}

		return new OtherUserPersonalInfoPage(url, path);
	}

	public GroupChatPage tabBackButton() throws Exception {
		driver.navigate().back();
		return new GroupChatPage(url, path);
	}

}
