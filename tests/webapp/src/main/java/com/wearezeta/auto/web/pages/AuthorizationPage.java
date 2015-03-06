package com.wearezeta.auto.web.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class AuthorizationPage extends WebPage {

	@FindBy(how = How.XPATH, using = WebAppLocators.AuthorizationPage.xpathCreateAccountButton)
	private WebElement createAccount;

	@FindBy(how = How.XPATH, using = WebAppLocators.AuthorizationPage.xpathSignInButton)
	private WebElement signInButton;

	public AuthorizationPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		// TODO Auto-generated constructor stub
	}

	public RegistrationPage clickCreateAccountButton() throws Exception {
		createAccount.click();

		return new RegistrationPage(this.getDriver(), this.getWait());
	}

	public LoginPage clickSignInButton() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, signInButton);
		signInButton.click();

		return new LoginPage(this.getDriver(), this.getWait());
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, createAccount, 5);
	}

}
