package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

public class PasswordChangeSuccessfullPage extends WebPage {
	private static final String CONFIRMATION_TEXT = "Password changed";

	public PasswordChangeSuccessfullPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void waitUntilVisible(int timeoutSeconds) throws Exception {
		try {
			assert DriverUtils.waitUntilLocatorIsDisplayed(
					getDriver(),
					By.xpath("//*[contains(text(),'" + CONFIRMATION_TEXT
							+ "')]"), timeoutSeconds) : "Password change confirmation page has not been shown after "
					+ timeoutSeconds + " seconds";
		} catch (Exception e) {
			System.out.println(getDriver().getPageSource());
			throw e;
		}
	}
}
