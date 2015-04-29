package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class LandingPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(LandingPage.class
			.getSimpleName());

	public LandingPage(ZetaWebAppDriver driver, WebDriverWait wait, String url)
			throws Exception {
		super(driver, wait, url);
	}

	private static final int MAX_TRIES = 3;

	public LoginPage switchToLoginPage() throws Exception {
		WebCommonUtils.forceLogoutFromWebapp(getDriver(), true);
		final By signInBtnlocator = By
				.xpath(WebAppLocators.LoginPage.xpathSignInButton);
		final By switchtoSignInBtnlocator = By
				.xpath(WebAppLocators.LandingPage.xpathSwitchToSignInButton);
		int ntry = 0;
		// FIXME: temporary workaround for white page instead of landing issue
		while (ntry < MAX_TRIES) {
			try {
				if (DriverUtils.isElementDisplayed(this.getDriver(),
						switchtoSignInBtnlocator)) {
					driver.findElement(switchtoSignInBtnlocator).click();
				}
				if (DriverUtils.isElementDisplayed(this.getDriver(),
						signInBtnlocator)) {
					break;
				} else {
					log.debug(String
							.format("Trying to refresh currupted login page (retry %s of %s)...",
									ntry + 1, MAX_TRIES));
					driver.navigate().to(driver.getCurrentUrl());
				}
			} catch (Exception e) {
				driver.navigate().to(driver.getCurrentUrl());
			}
			ntry++;
		}
		assert DriverUtils.isElementDisplayed(this.getDriver(),
				signInBtnlocator) : "Sign in page is not visible";

		return new LoginPage(this.getDriver(), this.getWait());
	}

	@Override
	public void navigateTo() {
		super.navigateTo();
		WebCommonUtils.forceLogoutFromWebapp(getDriver(), true);

		// FIXME: I'm not sure whether white page instead of sign in is
		// Amazon issue or webapp issue,
		// but since this happens randomly in different browsers, then I can
		// assume this issue has something to do to the hosting and/or
		// Selenium driver
		int ntry = 0;
		while (ntry < MAX_TRIES) {
			try {
				if (!(DriverUtils
						.isElementDisplayed(
								this.getDriver(),
								By.xpath(WebAppLocators.LandingPage.xpathSwitchToRegisterButton)) && DriverUtils
						.isElementDisplayed(
								this.getDriver(),
								By.xpath(WebAppLocators.LandingPage.xpathSwitchToSignInButton)))) {
					log.error(String
							.format("Landing page has failed to load. Trying to refresh (%s of %s)...",
									ntry + 1, MAX_TRIES));
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

	public RegistrationPage switchToRegistrationPage() throws Exception {
		WebCommonUtils.forceLogoutFromWebapp(getDriver(), true);
		final By locator = By
				.xpath(WebAppLocators.LandingPage.xpathSwitchToRegisterButton);
		if (DriverUtils.waitUntilElementAppears(this.getDriver(), locator, 2)) {
			for (WebElement btn : driver.findElements(locator)) {
				if (btn.isDisplayed()) {
					btn.click();
					break;
				}
			}
		}
		assert DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.RegistrationPage.xpathRootForm)) : "Registration page is not visible";

		return new RegistrationPage(this.getDriver(), this.getWait());
	}
}
