package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PendingRequestsPage extends IOSPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestIgnoreButton)
	private WebElement ignoreRequestButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePendingRequestConnectButton)
	private WebElement connectRequestButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequesterName)
	private WebElement requesterName;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequestMessage)
	private WebElement pendingMessage;
	
	private String url;
	private String path;
	
	private String autoHelloMessage = "Hello";

	public PendingRequestsPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public ContactListPage clickIgnoreButton() throws Throwable{
		ContactListPage page = null;
		ignoreRequestButton.click();
		page = new ContactListPage(url, path);
		return page;
	}
	
	public ContactListPage clickConnectButton() throws Throwable{
		ContactListPage page = null;
		connectRequestButton.click();
		page = new ContactListPage(url, path);
		return page;
	}
	
	public boolean isConnectButtonDisplayed(){
		return DriverUtils.isElementDisplayed(connectRequestButton);
	}
	
	public String getRequesterName(){
		return requesterName.getText();
	}
	
	public String getRequestMessage(){
		return pendingMessage.getText();
	}
	
	public boolean isAutoMessageCorrect(){
		return getRequestMessage().equals(autoHelloMessage);
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
				break;
			}
			case LEFT:
			{
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

}
