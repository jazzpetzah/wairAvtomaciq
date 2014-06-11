package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class PeoplePickerPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement peoplePickerSearch;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerClearButton)
	private WebElement peoplePickerClearBtn;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> resultList;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classConnectToLabel)
	private WebElement connectToLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
	private WebElement sendButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConnectInput)
	private WebElement connectTextInput;
	
	
	private String url;
	private String path;
	private String inviteMessage = "Hello!";
	

	public PeoplePickerPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		url = URL;
		this.path = path;

	}
	
	public void TapOnPeoplePickerSearch(){
		peoplePickerSearch.click();
	}
	
	public void TapOnPeoplePickerClearBtn(){
		peoplePickerClearBtn.click();
	}
	
	private void FillTextInPeoplePickerSearch(String text){
		peoplePickerSearch.sendKeys(text);
	}
	
	private WebElement PickUser(String name){

		FillTextInPeoplePickerSearch(name);
		return driver.findElement(By.name(name));
	}
	
	public void PickerUserAndTap(String name){
		PickUser(name).click();
	}
	
	public IOSPage dismissPeoplePicker() throws IOException{
		IOSPage page = null;
		peoplePickerClearBtn.click();
		page = new ContactListPage(url, path);
		return page;
		
	}
	
	public void FillTextInConnectDialog(String message){
		connectTextInput.sendKeys(message);
	}
	
	public void ClickSendButton(){
		sendButton.click();
	}
	
	public IOSPage SendInvitation() throws IOException{
		IOSPage page = null;
		FillTextInConnectDialog(inviteMessage);
		ClickSendButton();
		page = new ContactListPage(url, path);
		return page;
	}
	
	public void CloseConnectDialog(){
		closeConnectDialoButon.click();
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
				break;
			}
			case LEFT:
			{
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
