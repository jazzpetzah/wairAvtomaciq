package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ConnectToPage extends IOSPage {
	
	private static final int MAX_MESSAGE_CHARACTERS = 140;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
	private WebElement sendConnectButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConnectOtherUserButton)
	private WebElement connectOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameIgnoreOtherUserButton)
	private WebElement ignoreOtherUserButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectionInputField)
	private WebElement sendConnectionInput;
	
	private String url;
	private String path;
	private String inviteMessage = CommonSteps.CONNECTION_MESSAGE;

	public ConnectToPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isConnectToUserDialogVisible(){
		return sendConnectionInput.isDisplayed();
	}
	
	public String getConnectToUserLabelValue(){
		return sendConnectionInput.getText();
	}
	
	public void fillTextInConnectDialog() {
		sendConnectionInput.sendKeys(inviteMessage);
	}

	public void enterCharactersIntoDialog(int numberOfCharacters) {
		final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890!@#$%^&*()";
		StringBuilder result = new StringBuilder();
		while (numberOfCharacters > 0) {
			Random rand = new Random();
			result.append(characters.charAt(rand.nextInt(characters.length())));
			numberOfCharacters--;
		}
		sendConnectionInput.sendKeys(result.toString());
	}

	public boolean isMaxCharactersInMessage(){
		int messageCharCount = sendConnectionInput.getText().length();
		if(messageCharCount>MAX_MESSAGE_CHARACTERS||messageCharCount<MAX_MESSAGE_CHARACTERS){
			return false;
		}else{
			return true;
		}
	}
	
	public void deleteTextInConnectDialog() {
		sendConnectionInput.clear();
		//additional steps required because clear() does not disable the connect button
		sendConnectionInput.sendKeys("a");
		clickKeyboardDeleteButton();
	}
	
	public PeoplePickerPage clickSendButton() throws Throwable {
		sendConnectButton.click();
		return new PeoplePickerPage(url, path);	
	}
	
	public ContactListPage sendInvitation(String name) throws Exception{
		ContactListPage page = null;
		fillTextInConnectDialog();
		sendConnectButton.click();
		page = new ContactListPage(url, path);
		return page;
	}
	
	public PeoplePickerPage closeConnectDialog() throws Exception{
		closeConnectDialoButon.click();
		return new PeoplePickerPage(url, path);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean waitForConnectDialog(){
		return DriverUtils.waitUntilElementAppears(driver, By.className(IOSLocators.clasNameConnectDialogLabel));
	}
	
	public boolean isConnectButtonVisible(){
		return connectOtherUserButton.isDisplayed();
	}
	
	public PeoplePickerPage sendInvitation() throws Exception {
		connectOtherUserButton.click();
		return new PeoplePickerPage(url, path);	
	}
	
	public void acceptInvitation() {
		connectOtherUserButton.click();
	}
	
	public boolean isSendButtonVisible(){
		boolean flag=false;
		flag = DriverUtils.isElementDisplayed(sendConnectButton);
		return flag;
	}
	
	public boolean isSendConnectionInputVisible(){
		return DriverUtils.isElementDisplayed(sendConnectionInput);
	}

}
