package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class LoginPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class.getSimpleName());
	
	@FindBy(how = How.ID, using = OSXLocators.idLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameRegisterButton)
	private WebElement registerButton;

	@FindBy(how = How.NAME, using = OSXLocators.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.CLASS_NAME, using = OSXLocators.classNameLoginField)
	private WebElement loginField;

	@FindBy(how = How.ID, using = OSXLocators.idPasswordField)
	private WebElement passwordField;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathFailedApplicationDialogSend)
	private WebElement sendProblemReportButton;
	
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
		WebElement page = null;
		try {
			page = driver.findElement(By.id(OSXLocators.idLoginPage));
		} catch (NoSuchElementException e) {
			page = null;
		}
		return page != null;
	}
	
	public OSXPage SignIn() throws IOException {
		OSXPage page = null;
		boolean isLoginForm = false;
		try {
			loginField.getText();
			isLoginForm = true;
		} catch (NoSuchElementException e) {
			isLoginForm = false;
		} catch (NoSuchWindowException e) {
			isLoginForm = false;
		}
		signInButton.click();
		if (isLoginForm) {
			page = new ContactListPage(url, path);
		} else {
			page = this;
		}
		return page;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		DriverUtils.setImplicitWaitValue(driver, 1);
		try {
			loginField.sendKeys(login);
		} catch (NoSuchElementException e) {
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		DriverUtils.turnOffImplicitWait(driver);
		try {
			passwordField.sendKeys(password);
		} catch (NoSuchElementException e) {
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public boolean waitForLogin() {
		DriverUtils.turnOffImplicitWait(driver);
		boolean noSignIn = DriverUtils.waitUntilElementDissapear(driver, By.name(OSXLocators.nameSignInButton));
		DriverUtils.setDefaultImplicitWait(driver);
		return noSignIn;
	}
	
	public Boolean isLoginFinished(String contact) {
		String xpath = String.format(OSXLocators.xpathFormatContactEntryWithName, contact);
		WebElement el = null;
		try {
			el = driver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			el = null;
		}
		
		return el != null;
	}
	
	public RegistrationPage startRegistration() throws MalformedURLException {
		registerButton.click();
		RegistrationPage page = new RegistrationPage(url, path);
		return page;
	}
	
	public void logoutIfNotSignInPage() throws IOException {
		DriverUtils.setImplicitWaitValue(driver, 1);
		try {
			driver.findElement(By.id(OSXLocators.idLoginPage));
		} catch (NoSuchElementException e) {
			log.info("Logging out because previous user is signed in.");
			MainMenuPage menu = new MainMenuPage(url, path);
			menu.quitZClient();
			try {
				OSXCommonUtils.removeAllZClientSettingsFromDefaults();
			} catch (Exception ex) {
				log.error("Can't clear ZClient settings in OSX.\n" + ex.getMessage());
			}
			driver.navigate().to(path);
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public void sendProblemReportIfFound() {
		DriverUtils.setImplicitWaitValue(driver, 1);
		boolean isProblemReported = false;
		try {
			sendProblemReportButton.click();
			isProblemReported = true;
		} catch (NoSuchElementException e) {
		} finally {
			if (isProblemReported) {
				log.debug("ZClient were crashed on previous run.");
			}
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public RemoteWebDriver getDriver() {
		return driver;
	}
}
