package com.wearezeta.auto.osx.pages.welcome;

import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.Message;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.InputMethodEnum;
import com.wearezeta.auto.osx.common.LoginBehaviourEnum;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ChangePasswordPage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.common.NoInternetConnectionPage;

public class LoginPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.LoginPage.idLoginPage)
	private WebElement window;

	@FindBy(how = How.NAME, using = OSXLocators.LoginPage.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.CSS, using = OSXLocators.LoginPage.relativePathEmailField)
	private WebElement emailField;

	@FindBy(how = How.ID, using = OSXLocators.LoginPage.idPasswordField)
	private WebElement passwordField;

	@FindBy(how = How.XPATH, using = OSXLocators.LoginPage.xpathWrongCredentialsMessage)
	private WebElement wrongCredentialsMessage;

	@FindBy(how = How.XPATH, using = OSXLocators.LoginPage.xpathForgotPasswordButton)
	private WebElement forgotPasswordButton;

	private Future<Message> passwordResetMessage;

	public LoginPage(ZetaOSXDriver driver, WebDriverWait wait) throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.LoginPage.idPasswordField), 10);
	}

	public ContactListPage signIn() throws Exception {
		return (ContactListPage) signIn(LoginBehaviourEnum.SUCCESSFUL);
	}

	public OSXPage signIn(LoginBehaviourEnum expectedBehaviour)
			throws Exception {
		signInButton.click();
		switch (expectedBehaviour) {
		case SUCCESSFUL:
			return new ContactListPage(this.getDriver(), this.getWait());
		case ERROR:
			return this;
		case NO_INTERNET:
			return new NoInternetConnectionPage(this.getDriver(),
					this.getWait());
		default:
			throw new Exception(String.format(
					"Unsupported expected sign in behaviour - %s",
					expectedBehaviour));
		}
	}

	public void typeEmail(String email) {
		emailField.sendKeys(email);
	}

	public void typePassword(String password) throws Exception {
		typePassword(password, InputMethodEnum.SEND_KEYS);
	}

	public void typePassword(String password, InputMethodEnum method)
			throws Exception {
		switch (method) {
		case SEND_KEYS:
			passwordField.sendKeys(password);
			break;
		case APPLE_SCRIPT:
			passwordField.clear();
			String script = String
					.format(OSXCommonUtils
							.readTextFileFromResources(OSXConstants.Scripts.INPUT_PASSWORD_LOGIN_PAGE_SCRIPT),
							password);
			driver.executeScript(script);

			break;
		default:
			throw new Exception(String.format("Unsupported input method - %s",
					method));
		}
	}

	public boolean waitForLogin() throws Exception {
		DriverUtils.turnOffImplicitWait(driver);
		boolean noSignIn = DriverUtils.waitUntilElementDissapear(driver,
				By.name(OSXLocators.LoginPage.nameSignInButton));
		DriverUtils.setDefaultImplicitWait(driver);
		return noSignIn;
	}

	public Boolean isLoginFinished(String contact) {
		String xpath = String.format(
				OSXLocators.xpathFormatContactEntryWithName, contact);
		WebElement el = null;
		try {
			el = driver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			el = null;
		}

		return el != null;
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
		String script = "tell application \"Wire\" to activate\n"
				+ "tell application \"System Events\"\n"
				+ "tell process \"Wire\"\n"
				+ "set value of attribute \"AXFocused\" of text field 1 of window 1 to true\n"
				+ "keystroke \"" + password + "\"\n" + "end tell\n"
				+ "end tell";
		driver.executeScript(script);
	}

	public ChangePasswordPage openResetPasswordPage() throws Exception {
		String passwordResetLink = BackendAPIWrappers
				.getPasswordResetLink(this.passwordResetMessage);
		String script = String
				.format(OSXCommonUtils
						.readTextFileFromResources(OSXConstants.Scripts.OPEN_SAFARI_WITH_URL_SCRIPT),
						passwordResetLink);
		driver.executeScript(script);
		return new ChangePasswordPage(this.getDriver(), this.getWait());
	}

	public ChangePasswordPage openStagingForgotPasswordPage() throws Exception {
		String script = String
				.format(OSXCommonUtils
						.readTextFileFromResources(OSXConstants.Scripts.OPEN_SAFARI_WITH_URL_SCRIPT),
						OSXConstants.BrowserActions.STAGING_CHANGE_PASSWORD_URL);
		driver.executeScript(script);
		return new ChangePasswordPage(this.getDriver(), this.getWait());
	}

	public boolean isForgotPasswordPageAppears() throws Exception {
		String script = String
				.format(OSXCommonUtils
						.readTextFileFromResources(OSXConstants.Scripts.PASSWORD_PAGE_VISIBLE_SCRIPT));

		@SuppressWarnings("unchecked")
		Map<String, String> value = (Map<String, String>) driver
				.executeScript(script);
		boolean result = Boolean.parseBoolean(value.get("result"));
		return result;
	}

	public void forgotPassword() {
		forgotPasswordButton.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

	public Future<Message> getPasswordResetMessage() {
		return passwordResetMessage;
	}

	public void setPasswordResetMessage(Future<Message> passwordResetMessage) {
		this.passwordResetMessage = passwordResetMessage;
	}
}
