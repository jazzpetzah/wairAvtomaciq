package com.wearezeta.auto.osx.pages;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.sikuli.script.App;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ConversationInfoPage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idAddPeopleButtonSingleChat)
	private WebElement addPeopleButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idConfirmationViewConfirmButton)
	private WebElement confirmationViewConfirmButton;

	@FindBy(how = How.ID, using = OSXLocators.idRemoveUserFromConversation)
	private WebElement removeUserFromConversationButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idLeaveConversationButton)
	private WebElement leaveConversationButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idConversationScrollArea)
	private WebElement conversationScrollArea;
	
	private String url;
	private String path;
	
	public ConversationInfoPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public void selectUser(String user) {
		String xpath = String.format(OSXLocators.xpathFormatPeoplePickerUserCell, user);
		WebElement el = driver.findElement(By.xpath(xpath));
		el.click();
	}
	
	public void selectUserIfNotSelected(String user) throws IOException {
		Screen s = new Screen();
		try {
    		App.focus(CommonUtils.getAppPathFromConfig(ConversationInfoPage.class));
			s.click(Env.getMouseLocation());
			s.click(Env.getMouseLocation());
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}
	                
	private void confirmIfRequested() {
		try {
			DriverUtils.setImplicitWaitValue(driver, 3);
			confirmationViewConfirmButton.click();
		} catch (NoSuchElementException e) { 
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public PeoplePickerPage openPeoplePicker() throws MalformedURLException {
		addPeopleButton.click();
		confirmIfRequested();
		return new PeoplePickerPage(url, path);
	}
	
	public void removeUser() {
		removeUserFromConversationButton.click();
		confirmIfRequested();
		conversationScrollArea.click();
	}
	
	public void leaveConversation() {
		leaveConversationButton.click();
		confirmIfRequested();
	}
}
