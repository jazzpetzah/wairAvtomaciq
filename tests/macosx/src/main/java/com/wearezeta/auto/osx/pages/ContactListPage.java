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
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class ContactListPage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement viewPager;

	@FindBy(how = How.ID, using = OSXLocators.idContactEntry)
	private List<WebElement> contactsTextFields;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameSignOutMenuItem)
	private WebElement signOutMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;

	public ContactListPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}
	
	public Boolean isVisible() {
		return viewPager != null;
	}
	
	public void SignOut() {
		signOutMenuItem.click();
		driver.navigate().to("ZClient");
	}
	
	public boolean isContactWithNameExists(String name) {
		boolean isFound = false;
		if (contactsTextFields.isEmpty()) {
			contactsTextFields = driver.findElements(By.id(OSXLocators.idContactEntry));
		}
		for (WebElement cl: contactsTextFields) {
			if (cl.getText().equals(name)) {
				isFound = true;
				break;
			}
		}
		return isFound;
	}
	
	public boolean openConversation(String conversationName) {
		for (WebElement contact: this.contactsTextFields) {
			if (contact.getText().equals(conversationName)) {
				contact.click();
				return true;
			}
		}
		return false;
	}
	
	public boolean waitForSignOut() {
		return DriverUtils.waitUntilElementDissapear(driver, By.id(OSXLocators.idContactEntry));
	}
	
	public Boolean isSignOutFinished() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 WebElement el = wait.until(new Function<WebDriver, WebElement>() {
			 
			 public WebElement apply(WebDriver driver) {
				 return driver.findElement(By.name(OSXLocators.nameSignInButton));
			 }
		 });
		return el != null;
	}
	
	@Override
	public void Close() throws IOException {
		try {
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
