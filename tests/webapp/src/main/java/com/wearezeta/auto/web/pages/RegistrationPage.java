package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	public RegistrationPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	private void removeReadonlyAttr(String cssLocator) {
		driver.executeScript(String.format(
				"$(document).find(\"%s\").removeAttr('readonly');", cssLocator));
	}

	public void enterName(String name) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssNameFiled);
		nameField.clear();
		nameField.sendKeys(name);
	}

	public void enterEmail(String email) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssEmailFiled);
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void enterPassword(String password) {
		removeReadonlyAttr(WebAppLocators.RegistrationPage.cssPasswordFiled);
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void submitRegistration() throws Exception {
		assert DriverUtils.waitUntilElementClickable(driver, createAccount) : "'Create Account' button is not clickable after timeout";
		createAccount.click();
	}

	public boolean isVerificationEmailCorrect(String email) {
		return verificationEmail.getText().equalsIgnoreCase(email);
	}
}
