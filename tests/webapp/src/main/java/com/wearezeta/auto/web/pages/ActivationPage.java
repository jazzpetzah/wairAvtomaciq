package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ActivationPage extends WebPage {
	public ActivationPage(ZetaWebAppDriver driver, WebDriverWait wait,
			String url) throws Exception {
		super(driver, wait, url);
	}

	private ContactListPage openWebApp(int timeoutSeconds) throws Exception {
		final By openWebAppBtnLocator = By
				.xpath(WebAppLocators.ActivationPage.xpathBtnOpenWebApp);
		if (DriverUtils.isElementDisplayed(driver, openWebAppBtnLocator,
				timeoutSeconds)) {
			driver.findElement(openWebAppBtnLocator).click();
		} else {
			throw new RuntimeException(
					"It seems there was some failure while verifying registered account");
		}
		return new ContactListPage(getDriver(), getWait());
	}

	public ContactListPage verifyActivation(int timeoutSeconds)
			throws Exception {
		if (WebAppExecutionContext.getCurrentPlatform().toLowerCase()
				.contains("win")) {
			return new ContactListPage(getDriver(), getWait());
		} else {
			return openWebApp(timeoutSeconds);
		}
	}
}
