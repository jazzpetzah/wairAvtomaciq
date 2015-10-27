package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;

public class LoginPage extends WebPage {
	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger.getLog(LoginPage.class
			.getName());

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathCreateAccountButton)
	private WebElement createAccountButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssPhoneSignInButton)
	private WebElement phoneSignInButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathChangePasswordButton)
	private WebElement changePasswordButton;

	@FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssEmailInput)
	private WebElement emailInput;

	@FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssPasswordInput)
	private WebElement passwordInput;

	@FindBy(how = How.CSS, using = WebAppLocators.LoginPage.cssLoginErrorText)
	private WebElement loginErrorText;

	@FindBy(css = WebAppLocators.LoginPage.errorMarkedEmailField)
	private WebElement redDotOnEmailField;

	@FindBy(css = WebAppLocators.LoginPage.errorMarkedPasswordField)
	private WebElement redDotOnPasswordField;

	public LoginPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public LoginPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver, url);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.LoginPage.xpathSignInButton));
	}

	public boolean isSignInButtonDisabled() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WebAppLocators.LoginPage.xpathSignInButton));
		return !signInButton.isEnabled();
	}

	public void inputEmail(String email) {
                emailInput.click();
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void inputPassword(String password) throws Exception {
                passwordInput.click();
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	private boolean waitForLoginButtonDisappearance() throws Exception {
		// workarounds for IE driver bugs:
		// 1. when findElements() returns one RemoteWebElement instead of list
		// of elements and throws WebDriverException
		// 2. NPE when findElements() call
		boolean noSignIn = false;
		try {
			noSignIn = DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.xpath(WebAppLocators.LoginPage.xpathSignInButton), 60);
		} catch (WebDriverException e) {
			if (WebAppExecutionContext.getBrowser() == Browser.InternetExplorer) {
				noSignIn = true;
			} else {
				throw e;
			}
		}
		return noSignIn;
	}

	public boolean waitForLogin() throws Exception {
		boolean noSignIn = waitForLoginButtonDisappearance();
		boolean noSignInSpinner = DriverUtils.waitUntilLocatorDissapears(
				this.getDriver(),
				By.className(WebAppLocators.LoginPage.classNameSpinner), 40);
		return noSignIn && noSignInSpinner;
	}

	public ContactListPage clickSignInButton() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				signInButton);
		signInButton.click();

		return new ContactListPage(this.getLazyDriver());
	}

	public PasswordChangeRequestPage clickChangePasswordButton()
			throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changePasswordButton);

		// TODO: This is commented because the button always redirects to
		// production site and we usually need staging one
		// changePasswordButton.click();
		final PasswordChangeRequestPage changePasswordPage = new PasswordChangeRequestPage(
				this.getLazyDriver());
		changePasswordPage.navigateTo();
		return changePasswordPage;
	}

	public String getErrorMessage() throws InterruptedException, Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(WebAppLocators.LoginPage.cssLoginErrorText));
		return loginErrorText.getText();
	}

	public boolean isEmailFieldMarkedAsError() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(WebAppLocators.LoginPage.errorMarkedEmailField));
	}

	public boolean isPasswordFieldMarkedAsError() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.LoginPage.errorMarkedPasswordField));
	}

	public PhoneNumberLoginPage switchToPhoneNumberLoginPage() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), phoneSignInButton);
		phoneSignInButton.click();
		return new PhoneNumberLoginPage(this.getLazyDriver());
	}
}
