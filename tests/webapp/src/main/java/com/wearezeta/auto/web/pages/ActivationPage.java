package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ActivationPage extends WebPage {

	@FindBy(css = WebAppLocators.ActivationPage.cssBtnOpenWebApp)
	private WebElement btnOpenWebapp;

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

	public ContactListPage openWebApp(int timeoutSeconds) throws Exception {
		if (DriverUtils.waitUntilElementClickable(getDriver(), btnOpenWebapp)) {
			btnOpenWebapp.click();
		} else {
			throw new RuntimeException(
					"It seems there was some failure while verifying registered account");
		}
		return new ContactListPage(getLazyDriver());
	}
}
