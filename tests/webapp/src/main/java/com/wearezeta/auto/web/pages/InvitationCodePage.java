package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class InvitationCodePage extends WebPage {

	private static final Logger log = ZetaLogger
			.getLog(InvitationCodePage.class.getSimpleName());

	@FindBy(how = How.ID, using = WebAppLocators.InvitationCodePage.idCodeInput)
	private WebElement codeInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.InvitationCodePage.xpathProceedButton)
	private WebElement proceedButton;

	public InvitationCodePage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait, url);
	}

	private static final int MAX_LOAD_RETRIES = 3;

	@Override
	public void navigateTo() {
		super.navigateTo();
		if (!WebAppExecutionContext.ProfileManagement
				.isSupportedInCurrentBrowser()) {
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
		}

		// FIXME: I'm not sure whether white page instead of sign in is
		// Amazon issue or webapp issue,
		// but since this happens randomly in different browsers, then I can
		// assume this issue has something to do to the hosting and/or
		// Selenium driver
		int ntry = 0;
		while (ntry < MAX_LOAD_RETRIES) {
			try {
				if (!(DriverUtils.isElementDisplayed(this.getDriver(),
						By.id(WebAppLocators.InvitationCodePage.idCodeInput))
						|| DriverUtils
								.isElementDisplayed(
										this.getDriver(),
										By.xpath(WebAppLocators.LoginPage.xpathSwitchToRegisterButtons)) || DriverUtils
							.isElementDisplayed(
									this.getDriver(),
									By.xpath(WebAppLocators.RegistrationPage.xpathSwitchToSignInButton)))) {
					log.error(String
							.format("Initial page has failed to load. Trying to refresh (%s of %s)...",
									ntry, MAX_LOAD_RETRIES));
					driver.navigate().to(driver.getCurrentUrl());
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ntry++;
		}
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(driver,
				By.id(WebAppLocators.InvitationCodePage.idCodeInput));
	}

	public void inputCode(String code) {
		codeInput.sendKeys(code);
	}

	public LoginPage proceed() throws Exception {
		proceedButton.click();

		return new LoginPage(getDriver(), getWait(), getUrl());
	}
}
