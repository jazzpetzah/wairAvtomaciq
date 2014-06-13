package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
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

	@FindBy(how = How.ID, using = OSXLocators.idAddImageButton)
	private WebElement addImageButton;
	
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
		for (WebElement entry: messageEntries) {
			if (entry.equals(message)) {
				return true;
			}
		}
		return false;
	}
	
	public void writeNewMessage(String message) {
		int i = 0;
		while (newMessageTextArea == null) {
			System.out.println("here " + i);
			newMessageTextArea = findNewMessageTextArea();
			if (++i > 10) {
				break;
			}
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
		}
		newMessageTextArea.sendKeys(message);
	}
	
	public void sendNewMessage() {
		newMessageTextArea.submit();
	}

	public void openChooseImageDialog() {
		if (addImageButton == null) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(10, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
			addImageButton = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.name(OSXLocators.nameSignInButton));
				}
			});
		}
		addImageButton.click();
	}
	
	public int getNumberOfMessageEntries(String message) {
		String xpath = String.format(OSXLocators.xpathFormatSpecificMessageEntry, message);
		List<WebElement> messageEntries = driver.findElements(By.xpath(xpath));
		return messageEntries.size();
	}
	
	public int getNumberOfImageEntries() {
		List<WebElement> conversationImages =
				driver.findElements(By.xpath(OSXLocators.xpathConversationImageEntry));
		return conversationImages.size();
	}
	
	public boolean isMessageSent(String message) {
		boolean isSend = false;
		String xpath = String.format(OSXLocators.xpathFormatSpecificMessageEntry, message);
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
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
