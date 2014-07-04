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
	
	private String conversationName=null;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathLeaveConversation)
	private WebElement leaveChat;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLeaveConversationButton)
	private WebElement leaveChatButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationNameTextField)
	private WebElement conversationNameTextField;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathExitGroupInfoPageButton)
	private WebElement exitGroupInfoPageButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathNumberOfParticipantsText)
	private WebElement numberOfParticipantsText;
	
	public GroupChatInfoPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public void changeConversationNameToRandom(){
		conversationName = CommonUtils.generateGUID().substring(0,15);
		conversationNameTextField.sendKeys(conversationName+"\n");
	}

	public boolean verifyCorrectConversationName(String contact1, String contact2){
		if(conversationName==null){
			if(contact1.equals(CommonUtils.CONTACT_1)){
				contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
			}
			if(contact2.equals(CommonUtils.CONTACT_2)||contact2.equals("aqaPictureContact")){
				contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
			}
			conversationName = contact1+", "+contact2;
		}
		if(!conversationNameTextField.getText().equals(conversationName)){
			conversationName = contact2+", "+contact1;
		}
		return conversationNameTextField.getText().equals(conversationName);
	}

	public boolean verifyNumberOfParticipants(int correctNumber){
		int givenNumberOfParticipants = Integer.parseInt(numberOfParticipantsText.getText().replaceAll("\\D+",""));
		return givenNumberOfParticipants == correctNumber;
	}
	
	public boolean verifyParticipantAvatars(int participants){
		List<WebElement> participantAvatars = getCurrentParticipants(participants);
		for(WebElement avatar : participantAvatars){
			if(avatar.getAttribute("name")=="AQAPICTURECONTACT"){
				//compare picture to cat
			}
			if(avatar.getAttribute("name")==CommonUtils.CONTACT_1){
				//compare to image with contact 1's first letter inside
			}
			if(avatar.getAttribute("name")==CommonUtils.YOUR_USER_1){
				//compare to own user image/first character in username
			}

		}
		return true;
	}
	
	public List<WebElement> getCurrentParticipants(int participants){
		List<WebElement> currentParticipants= new ArrayList<WebElement>();
		int participantNumber=1;
		String xpathOfCurrentParticipant = String.format(IOSLocators.xpathParticipantAvatar, participantNumber);
		WebElement currentParticipant = driver.findElementByXPath(xpathOfCurrentParticipant);
		for(int i=1;i<participants;i++){
		currentParticipants.add(currentParticipant);
		participantNumber = participantNumber+1;
		xpathOfCurrentParticipant = String.format(IOSLocators.xpathParticipantAvatar, participantNumber);
		currentParticipant = driver.findElementByXPath(xpathOfCurrentParticipant);
		}
		return currentParticipants;
		}
	
	public void exitGroupInfoPage(){
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
	
	
	
	public String getConversationName(){
		return conversationName;
	}

	public void setConversationName(String newName){
		conversationName = newName;
	}
}

	
	
	