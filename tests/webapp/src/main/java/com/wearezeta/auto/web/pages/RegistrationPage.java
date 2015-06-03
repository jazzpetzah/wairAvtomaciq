package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class RegistrationPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(RegistrationPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssNameFiled)
	private WebElement nameField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssEmailFiled)
	private WebElement emailField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssPasswordFiled)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idCreateAccountButton)
	private WebElement createAccount;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssVerificationEmail)
	private WebElement verificationEmail;
	
	@FindBy(css = WebAppLocators.RegistrationPage.cssSwitchToSignInButton)
	private WebElement switchToSignInButton;

	@FindBy(css = ".auth-page .has-error .form-control #wire-create-email")
	private WebElement redDotOnEmailField;

	@FindBy(xpath = "//*[@data-uie-name='status-error']//div")
	private WebElement errorMessage;

	public RegistrationPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver, url);
	}

	public LoginPage switchToLoginPage() throws Exception {
		final By signInBtnlocator = By
				.xpath(WebAppLocators.LoginPage.xpathSignInButton);
		if (DriverUtils.waitUntilElementClickable(this.getDriver(),
				switchToSignInButton)) {
			switchToSignInButton.click();
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				signInBtnlocator) : "Sign in page is not visible";

		return new LoginPage(this.getLazyDriver(), this.getUrl());
	}

	private void removeReadonlyAttr(String cssLocator) throws Exception {
		this.getDriver().executeScript(
				String.format(
						"$(document).find(\"%s\").removeAttr('readonly');",
						cssLocator));
	}

	public void enterName(String name) throws Exception {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssNameFiled);
		nameField.clear();
		nameField.sendKeys(name);
	}

	public void enterEmail(String email) throws Exception {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssEmailFiled);
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void enterPassword(String password) throws Exception {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssPasswordFiled);
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void submitRegistration() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				createAccount) : "'Create Account' button is not clickable after timeout";
		createAccount.click();
	}

	public boolean isVerificationEmailCorrect(String email) {
		return verificationEmail.getText().equalsIgnoreCase(email);
	}

	public String getErrorMessage() {
		return errorMessage.getText();
	}

	public boolean isRedDotOnEmailField() {
		return DriverUtils.isElementPresentAndDisplayed(redDotOnEmailField);
	}
}
