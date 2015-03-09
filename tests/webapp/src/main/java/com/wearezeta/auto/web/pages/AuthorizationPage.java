package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.SeleniumException;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class AuthorizationPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(AuthorizationPage.class
			.getSimpleName());

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
		final int maxTries = 3;
		int ntry = 0;
		do {
			DriverUtils.waitUntilElementVisible(driver, signInButton);
			DriverUtils.waitUntilElementClickable(driver, signInButton);
			try {
				signInButton.click();
				break;
			} catch (SeleniumException e) {
				ntry++;
				if (ntry < maxTries) {
					log.debug(String
							.format("Failed to click Sign In button. Trying to retry (%d of %d)...",
									ntry + 1, maxTries));
					e.printStackTrace();
					Thread.sleep(1000);
				} else {
					throw e;
				}
			}
		} while (ntry < maxTries);

		return new LoginPage(this.getDriver(), this.getWait());
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, createAccount, 5);
	}

}
