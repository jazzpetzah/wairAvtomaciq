package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class LoginPage extends IOSPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSignInButton)
	private WebElement signInButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginButton)
	private WebElement confirmSignInButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameSignUpButton)
	private WebElement signUpButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLoginField)
	private WebElement loginField;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePasswordField)
	private WebElement passwordField;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameAlert)
	private WebElement alert;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAlertOK)
	private WebElement alertOk;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classUIATextView)
	private List<WebElement> userName;
	
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
	
	public IOSPage signIn() throws IOException{
		
		IOSPage page = null;
		
		try {
			signInButton.click();
			page = this;
		}
		catch(NoSuchElementException ex) {
			confirmSignInButton.click();
			page = new ContactListPage(url, path);
		}
		return page;
	}
	
	public void clickJoinButton()
	{
		signUpButton.click();
	}
	
	public RegistrationPage join() throws IOException{
		
		signUpButton.click();

		return new RegistrationPage(url, path);
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
	
	public Boolean isLoginFinished(String contact) throws IOException {
		WebElement el = null;
		catchLoginAllert();
		el = driver.findElement(By.name(contact));
		return el != null;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// no need to swipe
		return null;
	}
	
	public Boolean isLoginButtonVisible() {
		
		return (ExpectedConditions.visibilityOf(signInButton) != null);
	}

	public void catchLoginAllert() {
		try {
			DriverUtils.waitUntilElementAppears(driver, By.className(IOSLocators.classNameAlert));
			if(alert != null) {
				alertOk.click();
			}
		}
		catch(Exception ex) {
		}
		
	}

}
