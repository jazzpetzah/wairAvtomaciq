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

public class LoginPage extends IOSPage {
	
	@FindBy(how = How.NAME, using = "ZClientMainWindow")
	private WebElement viewPager;
	
	@FindBy(how = How.NAME, using = "SignIn")
	private WebElement signInButton;
	
	@FindBy(how = How.NAME, using = "ConfirmSignIn")
	private WebElement confirmSignInButton;
	
	@FindBy(how = How.NAME, using = "SignInEmail")
	private WebElement loginField;
	
	@FindBy(how = How.NAME, using = "SignInPassword")
	private WebElement passwordField;
	
	@FindBy(how = How.CLASS_NAME, using = "UIAAlert")
	private WebElement alert;
	
	@FindBy(how = How.NAME, using = "OK")
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
		 Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 
		 Boolean bool = wait.until(new Function<WebDriver, Boolean>() {
			 
			 public Boolean apply(WebDriver driver) {
			       return (driver.findElement(By.name("SignInEmail")) != null);
			     }
		 });
		 
		 return bool;
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
