package com.wearezeta.auto.ios.pages;

import java.net.MalformedURLException;
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
import com.wearezeta.auto.common.IOSLocators;

public class LoginPage extends IOSPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSignInButton)
	private WebElement signInButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginButton)
	private WebElement confirmSignInButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginField)
	private WebElement loginField;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePasswodField)
	private WebElement passwordField;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameAlert)
	private WebElement alert;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAlertOK)
	private WebElement alertOk;
	
	private String login;
	
	private String password;
	
	public LoginPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
	}
	
	public Boolean isVisible() {
		
		return viewPager != null;
	}
	
	public void SignIn() {
		
		try {
			signInButton.click();
		}
		catch(NoSuchElementException ex) {
			confirmSignInButton.click();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {

		loginField.sendKeys(login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		 
		passwordField.sendKeys(password);
	}
	
	public boolean waitForLogin() {
		 
		 return DriverUtils.waitUntilElementDissapear(driver, By.name(IOSLocators.nameLoginField));
	}
	
	public Boolean isLoginFinished(String contact) {
		
		try {
			if(alert != null) {
				alertOk.click();
			}
		}
		catch(NoSuchElementException ex) {
			//Do nothing
		}
		WebElement el = driver.findElement(By.name(contact));
		
		return el != null;
	}

}
