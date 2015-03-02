package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class LoginPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = WebAppLocators.LoginPage.idLoginPage)
	private WebElement page;

	@FindBy(how = How.ID, using = WebAppLocators.LoginPage.idEmailInput)
	private WebElement emailInput;

	@FindBy(how = How.ID, using = WebAppLocators.LoginPage.idPasswordInput)
	private WebElement passwordInput;

	@FindBy(how = How.ID, using = WebAppLocators.LoginPage.idLoginButton)
	private WebElement loginButton;

	public LoginPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() {
		WebElement page = null;
		try {
			page = driver.findElement(By
					.id(WebAppLocators.LoginPage.idLoginPage));
		} catch (NoSuchElementException e) {
			page = null;
		}
		return page != null;
	}

	public ContactListPage confirmSignIn() throws Exception {

		loginButton.click();

		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public void inputEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void inputPassword(String password) {
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	private boolean waitForLoginButtonDisappearance()
			throws Exception {
		// workarounds for IE driver bugs:
		// 1. when findElements() returns one RemoteWebElement instead of list
		// of elements and throws WebDriverException
		// 2. NPE when findElements() call
		boolean noSignIn = false;
		try {
			noSignIn = DriverUtils.waitUntilElementDissapear(driver,
					By.id(WebAppLocators.LoginPage.idLoginButton), 40);
		} catch (WebDriverException e) {
			if (WebAppExecutionContext.browserName
					.equals(WebAppConstants.Browser.INTERNET_EXPLORER)) {
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
}
