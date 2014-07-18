package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends IOSPage{
	private String url;
	private String path;
	
	private String conversationName = null;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathLeaveConversation)
	private WebElement leaveChat;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversationButton)
	private WebElement leaveChatButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationNameTextField)
	private WebElement conversationNameTextField;

	@FindBy(how = How.NAME, using = IOSLocators.nameExitGroupInfoPageButton)
	private WebElement exitGroupInfoPageButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAvatarCollectionView)
	private WebElement avatarCollectionView;

	public GroupChatInfoPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public void changeConversationNameToRandom() {
		conversationName = CommonUtils.generateGUID().substring(0, 15);
		conversationNameTextField.sendKeys(conversationName + "\n");
	}

	public boolean verifyCorrectConversationName(String contact1,
			String contact2) {
		if (conversationNameTextField.getText().equals(conversationName)) {
			return true;
		} else {
			String currentConversationName = conversationNameTextField
					.getText();
			return currentConversationName.contains(contact1)
					&& currentConversationName.contains(contact2)
					&& currentConversationName.contains(", ");
		}
	}
	
	public int numberOfPeopleInConversation() {
		int result = -1;
		List<WebElement> elements = driver.findElements(By.xpath(IOSLocators.xpathNumberPeopleText)); //check if this works right, getting number of participants
		for (WebElement element: elements) {
			String value = element.getText();
			if (value.contains(IOSLocators.peopleCountTextSubstring)) {
				result = Integer.parseInt(value.substring(0, value.indexOf(IOSLocators.peopleCountTextSubstring)));
			}
		}
		return result;
	}
	
	public int numberOfParticipantsAvatars() {
		List<WebElement> elements = driver.findElements(By.xpath(IOSLocators.xpathParticipantAvatar));
		return elements.size();
	}

	// use later
	public List<WebElement> getCurrentParticipants() {
		return avatarCollectionView.findElements(By.name("element name"));
	}

	public void exitGroupInfoPage() {
		exitGroupInfoPageButton.click();
	}

	public void leaveConversation() {
		leaveChat.click();
	}
	
	public void confirmLeaveConversation() {
		leaveChatButton.click();
	}
	
	public OtherUserPersonalInfoPage selectContactByName(String name) throws IOException {
		driver.findElementByName(name.toUpperCase()).click();
		
		return new OtherUserPersonalInfoPage(url, path);
	}
	
	public boolean isLeaveConversationAlertVisible() {
		
		return DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameLeaveConversationAlert));
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
				page = new GroupChatInfoPage(url, path);
				break;
			}
			case LEFT:
			{
				page = new GroupChatInfoPage(url, path);
				break;
			}
			case RIGHT:
			{
				page = new ContactListPage(url, path);
				break;
			}
		}	
		return page;
	}

	public String getConversationName() {
		return conversationName;
	}

	public void setConversationName(String newName) {
		conversationName = newName;
	}
}
