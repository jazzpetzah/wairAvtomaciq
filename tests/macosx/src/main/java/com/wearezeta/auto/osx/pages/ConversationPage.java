package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.osx.locators.OSXLocators;

public class ConversationPage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement viewPager;

//	@FindBy(how = How.ID, using = OSXLocators.xpathNewMessageTextArea)
	private WebElement newMessageTextArea = findNewMessageTextArea();
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathMessageEntry)
	private List<WebElement> messageEntries;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameSayHelloMenuItem)
	private WebElement sayHelloMenuItem;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameSignOutMenuItem)
	private WebElement signOutMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;

	public ConversationPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public void SignOut() {
		signOutMenuItem.click();
	}
	
	private WebElement findNewMessageTextArea() {
		List<WebElement> rows = driver.findElements(By.xpath(OSXLocators.xpathNewMessageTextArea)); 
        for (WebElement row: rows) { 
            if (row.getText().equals("")) { 
                return row;
            } 
        } 
        return null;
	}
	
	public void knock() {
		sayHelloMenuItem.click();
	}
	
	public boolean isMessageExist(String message) {
//		messageEntries = driver.findElements(By.xpath(OSXLocators.xpathMessageEntry));
		for (WebElement entry: messageEntries) {
			if (entry.equals(message)) {
				return true;
			}
		}
		return false;
	}
	
	public void writeNewMessage(String message) {
		if (newMessageTextArea == null) {
			findNewMessageTextArea();
		}
		newMessageTextArea.sendKeys(message);
	}
	
	public void sendNewMessage() {
		newMessageTextArea.submit();
	}

	public int calcMessageEntries(String message) {
		int result = 0;
//		messageEntries = driver.findElements(By.xpath(OSXLocators.xpathMessageEntry));
		for (WebElement entry: messageEntries) {
			if (entry.getText() != null && entry.getText().equals(message)) {
				result++;
			}
		}
		return result;
	}
	
	public boolean isMessageSent(String message) {
		boolean isSend = false;
		for (WebElement entry: messageEntries) {
			if (entry.getAttribute("AXValue").equals(message)) {
				isSend = true;
			}
		}
		return isSend;
	}
	@Override
	public void Close() throws IOException {
		try {
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
