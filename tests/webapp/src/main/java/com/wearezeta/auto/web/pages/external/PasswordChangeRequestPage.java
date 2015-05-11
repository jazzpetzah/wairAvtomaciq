package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

public class PasswordChangeRequestPage extends WebPage {
	private static final String STAGING_URL = "https://staging-website.zinfra.io/forgot/";

	@FindBy(id = "email")
	private WebElement emailField;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement changePasswordButton;

	public PasswordChangeRequestPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver, STAGING_URL);
	}

	public void setEmail(String email) {
		emailField.clear();
		emailField.sendKeys(email);
	}

	public PasswordChangeRequestSuccessfullPage clickChangePasswordButton()
			throws Exception {
		changePasswordButton.click();

		return new PasswordChangeRequestSuccessfullPage(this.getLazyDriver());
	}

	public void waitUntilVisible(int timeoutSeconds) throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id("email")) : "Password Change Request page has not been displayed within "
				+ timeoutSeconds + " seconds";
	}
}
