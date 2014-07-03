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
				System.out.println("contact 1 is now"+contact1);
			}
			if(contact2.equals(CommonUtils.CONTACT_2)){
				contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
				System.out.println("contact 2 is now"+contact2);
			}
			System.out.println("conversationName is null");
			conversationName = contact1+", "+contact2;
		}
		if(!conversationNameTextField.getText().equals(conversationName)){
			conversationName = contact2+", "+contact1;
		}
		System.out.println("conversationName is now "+conversationName);
		return conversationNameTextField.getText().equals(conversationName);
	}
	//may use later
	public List<String> getShownParticipantUsernames(){
		List<String> currentParticipantUsernames= new ArrayList<String>();
		int numberOfParticipants=1;
		String xpathOfCurrentParticipantUsername1 = String.format(IOSLocators.xpathParticipantName, numberOfParticipants,"1");
		String xpathOfCurrentParticipantUsername2 = String.format(IOSLocators.xpathParticipantName, numberOfParticipants,"2");
		WebElement currentParticipantUsername1 = driver.findElementByXPath(xpathOfCurrentParticipantUsername1);
		WebElement currentParticipantUsername2 = driver.findElementByXPath(xpathOfCurrentParticipantUsername2);
		System.out.println(currentParticipantUsername1.getAttribute("value"));
		System.out.println("and again 2nd username: "+currentParticipantUsername2.getAttribute("value"));
		
			while(currentParticipantUsername1!=null) {
				try{
					xpathOfCurrentParticipantUsername1 = String.format(IOSLocators.xpathParticipantName, numberOfParticipants,"1");
					xpathOfCurrentParticipantUsername2 = String.format(IOSLocators.xpathParticipantName, numberOfParticipants,"2");
					System.out.println("set new user names:"+numberOfParticipants);
					currentParticipantUsername1 = driver.findElementByXPath(xpathOfCurrentParticipantUsername1);
					currentParticipantUsername2 = driver.findElementByXPath(xpathOfCurrentParticipantUsername2);
					System.out.println("set new elements:"+numberOfParticipants);
						if(currentParticipantUsername2!=null){
							currentParticipantUsernames.add(currentParticipantUsername2.getText());
							System.out.println("and again 2nd username: "+currentParticipantUsername2.getAttribute("value"));
						}else{
					currentParticipantUsernames.add(currentParticipantUsername1.getText());
						}
					numberOfParticipants = numberOfParticipants+1;
				}catch (NoSuchElementException e){
					currentParticipantUsername1=null;
				}
			}
		return currentParticipantUsernames;
	}
	//may use later
	public List<WebElement> getCurrentParticipants(){
		List<WebElement> currentParticipants= new ArrayList<WebElement>();
		int numberOfParticipants=1;
		String xpathOfCurrentParticipant = String.format(IOSLocators.xpathParticipantAvatar, numberOfParticipants);
		WebElement currentParticipant = driver.findElementByXPath(xpathOfCurrentParticipant);
		while(currentParticipant!=null){
		currentParticipants.add(currentParticipant);
		numberOfParticipants = numberOfParticipants+1;
		xpathOfCurrentParticipant = String.format(IOSLocators.xpathParticipantAvatar, numberOfParticipants);
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

	
	
	