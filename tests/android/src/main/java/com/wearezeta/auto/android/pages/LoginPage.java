package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends AndroidPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(id = AndroidLocators.LoginPage.idSignUpButton)
	protected WebElement signUpButton;

	@FindBy(id = AndroidLocators.LoginPage.idForgotPass)
	private WebElement forgotPasswordButton;

	public LoginPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitForLoginScreenDisappear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(EmailSignInPage.idLoginButton), 40);
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idSignUpButton), 40);
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();
		assert DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginProgressViewContainer));
		return new RegistrationPage(this.getLazyDriver());
	}

	public void verifyErrorMessageText(String expectedMsg) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.LoginPage.xpathLoginMessageByText
						.apply(expectedMsg));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Error message '%s' is not visible on the screen",
						expectedMsg);
	}
}
