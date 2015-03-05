package com.wearezeta.auto.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class RegistrationPage extends WebPage {

	@FindBy(how = How.XPATH, using = WebAppLocators.RegistrationPage.xpathNameFiled)
	private WebElement nameField;
	
	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idEmailFiled)
	private WebElement emailField;
	
	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idPasswordFiled)
	private WebElement passwordField;
	
	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idCreateAccountButton)
	private WebElement createAccount;
	
	@FindBy(how = How.ID, using = WebAppLocators.RegistrationPage.idVerificationEmail)
	private WebElement verificationEmail;
	
	public RegistrationPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}

	public void enterName(String name) {
		nameField.sendKeys(name);
	}
	
	public void enterEmail(String email) {
		emailField.sendKeys(email);
	}
	
	public void enterPassword(String password) {
		passwordField.sendKeys(password);
	}
	
	public void submitRegistration() {
		createAccount.click();
	}
	
	public boolean isVerificationEmailCorrect(String email) {
		
		return verificationEmail.getText().equalsIgnoreCase(email);
	}
}
