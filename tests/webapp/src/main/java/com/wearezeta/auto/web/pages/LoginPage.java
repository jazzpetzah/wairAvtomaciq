package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class LoginPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathCreateAccountButton)
	private WebElement createAccountButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathEmailInput)
	private WebElement emailInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.LoginPage.xpathPasswordInput)
	private WebElement passwordInput;

	public LoginPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(WebAppLocators.LoginPage.xpathLoginPage));
	}

	public void inputEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void inputPassword(String password) {
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
			noSignIn = DriverUtils.waitUntilElementDissapear(driver,
					By.xpath(WebAppLocators.LoginPage.xpathSignInButton), 60);
		} catch (WebDriverException e) {
			if (WebAppExecutionContext.getCurrentBrowser() == Browser.InternetExplorer) {
				noSignIn = true;
			} else {
				throw e;
			}
		}
		return noSignIn;
	}

	public boolean waitForLogin() throws Exception {
		boolean noSignIn = waitForLoginButtonDisappearance();
		boolean noSignInSpinner = DriverUtils.waitUntilElementDissapear(driver,
				By.className(WebAppLocators.LoginPage.classNameSpinner), 40);
		return noSignIn && noSignInSpinner;
	}

	public ContactListPage clickSignInButton() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, signInButton);
		signInButton.click();

		return new ContactListPage(this.getDriver(), this.getWait());
	}
}
