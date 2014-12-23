package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ConnectToPage extends IOSPage {
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
	private WebElement sendConnectButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConnectOtherUserButton)
	private WebElement connectOtherUserButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameIgnoreOtherUserButton)
	private WebElement ignoreOtherUserButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectionInputField)
	private WebElement sendConecttionInput;
	
	private String url;
	private String path;
	private String inviteMessage = "Hello";

	public ConnectToPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isConnectToUserDialogVisible(){
		return sendConecttionInput.isDisplayed();
	}
	
	public String getConnectToUserLabelValue(){
		return sendConecttionInput.getText();
	}
	
	public void fillTextInConnectDialog() {
		sendConecttionInput.sendKeys(inviteMessage);
	}
	
	public PeoplePickerPage clickSendButton() throws Throwable {
		sendConnectButton.click();
		return new PeoplePickerPage(url, path);	
	}
	
	public ContactListPage sendInvitation(String name) throws IOException{
		ContactListPage page = null;
		fillTextInConnectDialog();
		sendConnectButton.click();
		page = new ContactListPage(url, path);
		return page;
	}
	
	public PeoplePickerPage closeConnectDialog() throws MalformedURLException{
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
	
	public PeoplePickerPage sendInvitation() throws MalformedURLException {
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
		return DriverUtils.isElementDisplayed(sendConecttionInput);
	}

}
