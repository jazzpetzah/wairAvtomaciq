package com.wearezeta.auto.osx.pages;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.Message;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class LoginPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.LoginPage.idLoginPage)
	private WebElement window;

	@FindBy(how = How.NAME, using = OSXLocators.LoginPage.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.CSS, using = OSXLocators.LoginPage.relativePathLoginField)
	private WebElement loginField;

	@FindBy(how = How.ID, using = OSXLocators.LoginPage.idPasswordField)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = OSXLocators.idSendProblemReportButton)
	private WebElement sendProblemReportButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathWrongCredentialsMessage)
	private WebElement wrongCredentialsMessage;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathNoInternetConnectionMessage)
	private WebElement noInternetConnectionMessage;

	@FindBy(how = How.ID, using = OSXLocators.idCloseNoInternetDialogButton)
	private WebElement closeNoInternetDialog;

	@FindBy(how = How.XPATH, using = OSXLocators.LoginPage.xpathForgotPasswordButton)
	private WebElement forgotPasswordButton;

	private Future<Message> passwordResetMessage;

	public LoginPage(ZetaOSXDriver driver, WebDriverWait wait) throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.LoginPage.idPasswordField));
	}

	public ContactListPage signIn() throws Exception {
		Thread.sleep(1000);
		signInButton.click();
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public void setLogin(String login) {
		loginField.sendKeys(login);
	}

	public void setPassword(String password) throws Exception {
		DriverUtils.turnOffImplicitWait(driver);
		try {
			passwordField.sendKeys(password);
		} catch (NoSuchElementException e) {
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
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

	public void sendProblemReportIfFound() throws Exception {
		long startDate = new Date().getTime();
		boolean isReport = false;
		for (int i = 0; i < 10; i++) {
			List<WebElement> windows = driver.findElements(By
					.xpath("//AXWindow"));
			if (windows.size() > 0) {
				for (WebElement win : windows) {
					if (win.getAttribute("AXIdentifier").equals(
							OSXLocators.idSendProblemReportWindow)) {
						isReport = true;
					}
				}
				if (!isReport) {
					log.debug("No need to close report. Correct window opened.");
					return;
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
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

	public boolean isNoInternetMessageAppears() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.xpathNoInternetConnectionMessage), 60);
	}

	public void closeNoInternetDialog() {
		closeNoInternetDialog.click();
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
