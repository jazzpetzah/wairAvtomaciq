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
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathTypeYourMessage)
	private WebElement typeYourMessage;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectCloseButton)
	private WebElement closeConnectDialoButon;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectionMessageInput)
	private WebElement messageInput;
	
	private String url;
	private String path;
	private String inviteMessage = "Hello";

	public ConnectToPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isConnectToUserDialogVisible(){
		return typeYourMessage.isDisplayed();
	}
	
	public String getConnectToUserLabelValue(){
		return typeYourMessage.getText();
	}
	
	public ContactListPage fillTextInConnectDialog() throws IOException {
		messageInput.click();
		messageInput.sendKeys(inviteMessage + "\\n");
		
		return new ContactListPage(url, path);
	}
	
	public ContactListPage sendInvitation(String name) throws IOException{
		ContactListPage page = null;
		fillTextInConnectDialog();
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
