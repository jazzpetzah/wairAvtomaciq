package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

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
import com.wearezeta.auto.osx.locators.OSXLocators;

public class LoginPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class.getSimpleName());
	
	@FindBy(how = How.ID, using = OSXLocators.idLoginPage)
	private WebElement viewPager;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathAcceptTermsOfServiceCheckBox)
	private WebElement acceptTermOfServiceCheckBox;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameRegisterButton)
	private WebElement registerButton;

	@FindBy(how = How.NAME, using = OSXLocators.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.CSS, using = OSXLocators.relativePathLoginField)
	private WebElement loginField;

	@FindBy(how = How.ID, using = OSXLocators.idPasswordField)
	private WebElement passwordField;
	
	@FindBy(how = How.ID, using = OSXLocators.idSendProblemReportButton)
	private WebElement sendProblemReportButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathWrongCredentialsMessage)
	private WebElement wrongCredentialsMessage;
	
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
	
	public void startSignIn() {
		signInButton.click();
	}
	
	public ContactListPage confirmSignIn() throws IOException {
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		signInButton.click();
		return new ContactListPage(url, path);
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
		acceptTermOfServiceCheckBox.click();
		for (int i = 0; i < 3; i++) {
			if (registerButton.getAttribute("AXEnabled").equals("1")) break;
		}
		registerButton.click();
		RegistrationPage page = new RegistrationPage(url, path);
		return page;
	}
	
	public void logoutIfNotSignInPage() throws Exception {
		DriverUtils.setImplicitWaitValue(driver, 1);
		try {
			driver.findElement(By.id(OSXLocators.idLoginPage));
		} catch (NoSuchElementException e) {
			log.info("Logging out because previous user is signed in.");
			MainMenuPage menu = new MainMenuPage(url, path);
			menu.SignOut();
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public void sendProblemReportIfFound() {
		long startDate = new Date().getTime();
		boolean isReport = false;
		for (int i = 0; i < 10; i++) {
			List<WebElement> windows = driver.findElements(By.xpath("//AXWindow"));
			if (windows.size() > 0) {
				for (WebElement win: windows) {
					if (win.getAttribute("AXIdentifier").equals(OSXLocators.idSendProblemReportWindow)) {
						isReport = true;
					}
				}
				if (!isReport) {
					log.debug("No need to close report. Correct window opened.");
					return;
				}
			}
			try { Thread.sleep(500); } catch (InterruptedException e) { }
		}

		DriverUtils.setImplicitWaitValue(driver, 1);
		boolean isProblemReported = false;
		try {
			sendProblemReportButton.click();
			isProblemReported = true;
		} catch (NoSuchElementException e) {
		} catch (NoSuchWindowException e) {
		} finally {
			if (isProblemReported) {
				log.debug("ZClient were crashed on previous run.");
			}
			DriverUtils.setDefaultImplicitWait(driver);
		}
		long endDate = new Date().getTime();
		log.debug("Sending problem report took " + (endDate - startDate) + "ms");
	}
	
	public boolean isWrongCredentialsMessageDisplayed() {
		try {
			String text = wrongCredentialsMessage.getText();
			log.debug("Found element with text: " + text);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public void setPasswordUsingScript(String password) {
		String script = "tell application \"Wire\" to activate\n" +
				"tell application \"System Events\"\n" +
				"tell process \"Wire\"\n" +
				"set value of attribute \"AXFocused\" of text field 1 of window 1 to true\n" +
				"keystroke \"" + password + "\"\n" +
				"end tell\n" +
				"end tell";
		driver.executeScript(script);
	}
	
	public RemoteWebDriver getDriver() {
		return driver;
	}
}
