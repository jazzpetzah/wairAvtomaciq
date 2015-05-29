package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends AndroidPage {

	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(id = AndroidLocators.LoginPage.idIHaveAccountButton)
	private WebElement iHaveAccountButton;

	@FindBy(id = AndroidLocators.LoginPage.idSignUpButton)
	protected WebElement signUpButton;

	@FindBy(id = AndroidLocators.LoginPage.idForgotPass)
	private WebElement forgotPasswordButton;

	@FindBy(id = AndroidLocators.LoginPage.idLoginButton)
	protected WebElement confirmSignInButton;

	@FindBy(id = AndroidLocators.ContactListPage.idSelfUserAvatar)
	protected WebElement selfUserAvatar;

	@FindBy(id = AndroidLocators.LoginPage.idLoginInput)
	private WebElement loginInput;

	@FindBy(id = AndroidLocators.LoginPage.idPasswordInput)
	private WebElement passwordInput;

	@FindBy(id = AndroidLocators.LoginPage.idWelcomeSlogan)
	private List<WebElement> welcomeSloganContainer;

	@FindBy(id = AndroidLocators.ContactListPage.idContactListNames)
	private WebElement contactListNames;

	@FindBy(id = AndroidLocators.CommonLocators.xpathDismissUpdateButton)
	private WebElement dismissUpdateButton;

	public LoginPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public LoginPage switchToEmailSignIn() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.LoginPage.idIHaveAccountButton))) {
			iHaveAccountButton.click();
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginInput));
		return this;
	}

	public CommonAndroidPage forgotPassword() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(forgotPasswordButton));
		forgotPasswordButton.click();
		Thread.sleep(2000);
		if (DriverUtils.isElementPresentAndDisplayed(forgotPasswordButton)) {
			DriverUtils.androidMultiTap(this.getDriver(), forgotPasswordButton,
					1, 500);
		}
		return new CommonAndroidPage(this.getLazyDriver());
	}

	public ContactListPage LogIn() throws Exception {
		confirmSignInButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

	public void setLogin(String login) throws Exception {
		try {
			loginInput.sendKeys(login);
		} catch (Exception e) {
			log.debug(this.getDriver().getPageSource());
			throw e;
		}
	}

	public void setPassword(String password) throws Exception {
		passwordInput.click();
		passwordInput.sendKeys(password);
	}

	public boolean waitForLoginScreenDisappear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginButton), 40);
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idSignUpButton), 40);
	}

	private static final int SIGN_IN_TIMEOUT_SECONDS = 60;

	public void verifyLoginFinished() throws Exception {
		final By clearBtnLocator = By
				.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn);
		final By selfAvatarLocator = By
				.id(AndroidLocators.ContactListPage.idSelfUserAvatar);
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
					clearBtnLocator, 1)) {
				pickerClearBtn.click();
			}
			if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
					selfAvatarLocator, 1)) {
				return;
			}
		} while (System.currentTimeMillis() - millisecondsStarted <= SIGN_IN_TIMEOUT_SECONDS * 1000);
		assert false : String.format(
				"Sign in is still in progress after %s seconds",
				SIGN_IN_TIMEOUT_SECONDS);
	}

	public boolean waitForInitialScreen() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idWelcomeSlogan));
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		// no need to swipe
		return null;
	}

	public RegistrationPage join() throws Exception {
		signUpButton.click();
		assert DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.LoginPage.idLoginProgressViewContainer));
		return new RegistrationPage(this.getLazyDriver());
	}

	public boolean isDismissUpdateVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(AndroidLocators.CommonLocators.xpathDismissUpdateButton));
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
