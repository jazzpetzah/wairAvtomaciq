package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaDriver;

public class LoginPage extends IOSPage {
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
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
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classUIATextView)
	private List<WebElement> userName;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement termsButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement privacyButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyCloseButton)
	private WebElement termsPrivacyCloseButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameErrorMailNotification)
	private WebElement errorMailNotification;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameWrongCredentialsNotification)
	private WebElement wrongCredentialsNotification;
	
	private String login;
	
	private String password;
	
	private String url;
	
	private String path;
	
	public String message;
	
	public LoginPage(String URL, String path) throws MalformedURLException {
		
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public LoginPage(String URL, String path, boolean acceptAlerts) throws MalformedURLException {
		
		super(URL, path, acceptAlerts);
		this.url = URL;
		this.path = path;
	}
	
	public ZetaDriver getDriver() {
		return driver;
	}
	
	public Boolean isVisible() {
		return viewPager != null;
	}
	
	public IOSPage signIn() throws IOException{
		
		signInButton.click();
		return this;
	}
	
	public IOSPage login() throws IOException {
		
		confirmSignInButton.click();
		return new ContactListPage(url, path);
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
	
	public boolean waitForLogin() throws InterruptedException {
		 
		return DriverUtils.waitUntilElementDissapear(driver, By.name(IOSLocators.nameLoginField));
	}
	
	public Boolean isLoginFinished(String contact) throws IOException {
		
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(contact)));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(contact)));
		}
		catch (WebDriverException ex)
		{
			System.out.println(ex.getMessage());
		}
		WebElement el = null;
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
	
	public void tapHoldEmailInput(){
		message = driver.findElement(By.name(IOSLocators.nameLoginField)).getText();
		driver.tap(1, driver.findElement(By.name(IOSLocators.nameLoginField)), 1000);
	}
	
	public void openTermsLink(){
		Point p = termsButton.getLocation();
		Dimension k = termsButton.getSize();
		driver.tap(1, (p.x) + (k.width -70), (p.y) + (k.height -16), 1);
	}
	
	public void openPrivacyLink(){
		Point p = privacyButton.getLocation();
		Dimension k = privacyButton.getSize();
		driver.tap(1, (p.x) + (k.width/3), (p.y) + (k.height -8), 1);
	}
	
	public void closeTermsPrivacyController(){
		termsPrivacyCloseButton.click();
	}
	
	public Boolean isTermsPrivacyColseButtonVisible() {
		
		return (ExpectedConditions.visibilityOf(termsPrivacyCloseButton) != null);
	}
	
	public void tapPasswordField(){
		passwordField.click();
	}
	
	public Boolean errorMailNotificationIsShown(){
		return (ExpectedConditions.visibilityOf(errorMailNotification) != null);
	}
	
	public Boolean errorMailNotificationIsNotShown(){
		return (ExpectedConditions.visibilityOf(errorMailNotification) == null);
	}
	
	public Boolean wrongCredentialsNotificationIsShown(){
		return (ExpectedConditions.visibilityOf(wrongCredentialsNotification) != null);
	}

}
