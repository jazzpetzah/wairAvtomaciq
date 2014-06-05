package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class LoginPage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathLoginField)
	private WebElement loginField;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPasswordField)
	private WebElement passwordField;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;
	
	private String login;
	
	private String password;
	
	private String url;
	private String path;
	
	public LoginPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public OSXPage SignIn() throws IOException {
		OSXPage page = null;
		signInButton.click();
		page = new ContactListPage(url, path);
		return page;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		driver.getPageSource();
		loginField.sendKeys(login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		passwordField.sendKeys(password);
	}
	
	public boolean waitForLogin() {
		 
		 return DriverUtils.waitUntilElementDissapear(driver, By.name(OSXLocators.nameSignInButton));
	}
	
	public Boolean isLoginFinished(String contact) {
		WebElement el = null;
        List<WebElement> cells = driver.findElements(By.id(OSXLocators.idContactEntry)); 
        for (WebElement cell: cells) { 
            try { 
                if (cell.getText().equals(contact)) { 
                    el = cell;
                    break; 
                } 
            } catch (NoSuchElementException e) { el = null; } 
        } 
		
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
