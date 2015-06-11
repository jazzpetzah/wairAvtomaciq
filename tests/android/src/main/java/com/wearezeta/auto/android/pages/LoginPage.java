package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends AndroidPage {

	private static final String idLoginProgressViewContainer = "fl__sign_in__progress_view__container";

	private static final Function<String, String> xpathLoginMessageByText = text -> String
			.format("//*[@id='message' and @value='%s']", text);

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	private static final String idSignUpButton = "ttv__welcome__create_account";
	@FindBy(id = idSignUpButton)
	protected WebElement signUpButton;

	private static final String idForgotPass = "ttv_signin_forgot_password";
	@FindBy(id = idForgotPass)
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
				By.id(idSignUpButton), 40);
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();
		assert DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(idLoginProgressViewContainer));
		return new RegistrationPage(this.getLazyDriver());
	}

	public void verifyErrorMessageText(String expectedMsg) throws Exception {
		final By locator = By.xpath(xpathLoginMessageByText.apply(expectedMsg));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Error message '%s' is not visible on the screen",
						expectedMsg);
	}
}
