package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class ConnectToPage extends IOSPage {
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.clasNameConnectDialogLabel)
	private WebElement connectLabel;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConnectDialogInput)
	private WebElement connectInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSendConnectButton)
	private WebElement sendButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;
	
	private String url;
	private String path;
	private String inviteMessage = "Hello";

	public ConnectToPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isConnectToUserDialogVisible(){
		return connectLabel.isDisplayed();
	}
	
	public String getConnectToUserLabelValue(){
		return connectLabel.getText();
	}
	
	public void fillTextInConnectDialog(){
		connectInput.sendKeys(inviteMessage);
	}
	
	public ContactListPage clickSendButton() throws IOException{
		sendButton.click();
		return new ContactListPage(url, path);
	}
	
	public IOSPage sendInvitation(String name) throws IOException{
		IOSPage page = null;
		fillTextInConnectDialog();
		clickSendButton();
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

}
