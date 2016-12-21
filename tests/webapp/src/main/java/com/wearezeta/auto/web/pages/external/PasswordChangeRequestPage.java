package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class PasswordChangeRequestPage extends WebPage {

    private static final Logger LOG = ZetaLogger.getLog(PasswordChangeRequestPage.class.getName());

	@FindBy(css = ExternalLocators.PasswordChangeRequestPage.cssEmailInput)
	private WebElement emailField;

	@FindBy(css = ExternalLocators.PasswordChangeRequestPage.cssSubmitButton)
	private WebElement changePasswordButton;

	public PasswordChangeRequestPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		final String website = CommonUtils.getAccountPagesFromConfig(DownloadPage.class) + "forgot/";
        LOG.info(website);
		super.setUrl(website);
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
				By.cssSelector(ExternalLocators.PasswordChangeRequestPage.cssEmailInput));
	}
}
