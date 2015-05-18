package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class PasswordChangePage extends WebPage {
	@FindBy(id = ExternalLocators.PasswordChangePage.idPasswordInput)
	private WebElement passwordField;

	@FindBy(xpath = ExternalLocators.PasswordChangePage.xpathSubmitButton)
	private WebElement changePasswordButton;

	public PasswordChangePage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setNewPassword(String newPassword) {
		passwordField.clear();
		passwordField.sendKeys(newPassword);
	}

	public PasswordChangeSuccessfullPage clickChangePasswordButton()
			throws Exception {
		changePasswordButton.click();
		return new PasswordChangeSuccessfullPage(this.getLazyDriver());
	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
	}

	public void waitUntilVisible(int timeoutSeconds) throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(ExternalLocators.PasswordChangePage.idPasswordInput)) : "Password Change page has not been displayed within "
				+ timeoutSeconds + " seconds";
	}
}
