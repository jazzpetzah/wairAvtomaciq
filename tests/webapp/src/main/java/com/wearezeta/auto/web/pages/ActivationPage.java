package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ActivationPage extends WebPage {
	public ActivationPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		this(lazyDriver, null);
	}

	public ActivationPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver, url);
	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
	}
	
	@Override
	public void navigateTo() throws Exception {
		super.navigateTo();
	}

	private ContactListPage openWebApp(int timeoutSeconds) throws Exception {
		final By openWebAppBtnLocator = By
				.xpath(WebAppLocators.ActivationPage.xpathBtnOpenWebApp);
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				openWebAppBtnLocator, timeoutSeconds)) {
			getDriver().findElement(openWebAppBtnLocator).click();
		} else {
			throw new RuntimeException(
					"It seems there was some failure while verifying registered account");
		}
		return new ContactListPage(getLazyDriver());
	}

	public ContactListPage verifyActivation(int timeoutSeconds)
			throws Exception {
		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			assert DriverUtils.waitUntilElementDissapear(this.getDriver(),
					By.xpath(WebAppLocators.ActivationPage.xpathBtnOpenWebApp),
					timeoutSeconds) : "Activation page is visible instead of the web app";
			return new ContactListPage(getLazyDriver());
		} else {
			return openWebApp(timeoutSeconds);
		}
	}
}
