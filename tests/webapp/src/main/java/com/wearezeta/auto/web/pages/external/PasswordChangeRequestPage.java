package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class PasswordChangeRequestPage extends WebPage {
	// FIXME: Works for staging backend only
	private static final String STAGING_URL = WebAppConstants.STAGING_SITE_ROOT
			+ "/forgot/";

	@FindBy(id = ExternalLocators.PasswordChangeRequestPage.idEmailInput)
	private WebElement emailField;

	@FindBy(css = ExternalLocators.PasswordChangeRequestPage.cssSubmitButton)
	private WebElement changePasswordButton;

	public PasswordChangeRequestPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver, STAGING_URL);
	}

	public void setEmail(String email) {
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void clickChangePasswordButton() throws Exception {
		DriverUtils
				.waitUntilElementClickable(getDriver(), changePasswordButton);
		changePasswordButton.click();
	}

	public boolean isEmailFieldVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(ExternalLocators.PasswordChangeRequestPage.idEmailInput));
	}
}
