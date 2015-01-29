package com.wearezeta.auto.web.pages;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class LoginPage extends WebPage {

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

	private String url;
	private String path;

	public LoginPage(String url, String path) throws IOException {
		super(url, path, true);

		this.url = url;
		this.path = path;
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

	public ContactListPage confirmSignIn() throws IOException {
		
		loginButton.click();

		return new ContactListPage(url, path);
	}

	public void inputEmail(String email) {
		emailInput.clear();
		emailInput.sendKeys(email);
	}

	public void inputPassword(String password) {
		passwordInput.clear();
		passwordInput.sendKeys(password);
	}

	public boolean waitForLogin() {
		boolean noSignIn = DriverUtils.waitUntilElementDissapear(driver,
				By.id(WebAppLocators.LoginPage.idLoginButton));;
		return noSignIn;
	}
}
