package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {
	final String[] scriptString = new String[] {
			"tell application \"System Events\"",
			"tell application \"iOS Simulator\" to activate",
			"tell application \"System Events\" to keystroke \"h\" using {command down, shift down}",
			"end tell" };

	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement viewPager;

	@FindBy(how = How.NAME, using = IOSLocators.nameSignInButton)
	private WebElement signInButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathLoginButton)
	private WebElement confirmSignInButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameRegisterButton)
	private WebElement registerButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameLoginField)
	private WebElement loginField;

	@FindBy(how = How.NAME, using = IOSLocators.namePasswordField)
	private WebElement passwordField;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classUIATextView)
	private List<WebElement> userName;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement termsButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyLinks)
	private WebElement privacyButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsPrivacyCloseButton)
	private WebElement termsPrivacyCloseButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameErrorMailNotification)
	private WebElement errorMailNotification;

	@FindBy(how = How.NAME, using = IOSLocators.nameWrongCredentialsNotification)
	private WebElement wrongCredentialsNotification;

	@FindBy(how = How.NAME, using = IOSLocators.nameIgnoreUpdateButton)
	private WebElement ignoreUpdateButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsOfServiceButton)
	private WebElement termsOfServiceButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement selfProfileName;

	@FindBy(how = How.NAME, using = IOSLocators.nameShareButton)
	private WebElement shareButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameForgotPasswordButton)
	private WebElement changePasswordButtonSignIn;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSafariChangePasswordEmailField)
	private WebElement safariChangePWEmailField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSafariURLButton)
	private WebElement safariURLButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSafariGoButton)
	private WebElement safariGoButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSafariEnterNewPasswordField)
	private WebElement safariEnterNewPasswordField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordPageChangePasswordButton)
	private WebElement changePasswordPageChangePasswordButton;

	@FindBy(how = How.NAME, using = IOSLocators.LoginPage.namePhoneLoginButton)
	private WebElement phoneLoginButton;

	@FindBy(how = How.NAME, using = IOSLocators.LoginPage.nameEmailLoginButton)
	private WebElement emailLoginButton;

	@FindBy(how = How.NAME, using = IOSLocators.LoginPage.nameBackButton)
	private WebElement backButton;

	@FindBy(how = How.NAME, using = IOSLocators.LoginPage.nameMaybeLater)
	private WebElement maybeLater;

	@FindBy(how = How.NAME, using = IOSLocators.LoginPage.nameCountryPickerButton)
	private WebElement countryPickerButtton;

	@FindBy(how = How.XPATH, using = IOSLocators.LoginPage.xpathSetEmailPasswordSuggetionLabel)
	private WebElement setEmailPasswordSuggetionLabel;

	@FindBy(how = How.NAME, using = IOSLocators.Alerts.nameResentIn10min)
	private WebElement resendIn10minAlert;

	@FindBy(how = How.NAME, using = IOSLocators.Alerts.nameInvalidPhoneNumber)
	private WebElement invalidPhoneNumberAlert;
	
	@FindBy(how = How.NAME, using = IOSLocators.Alerts.nameInvalidEmail)
	private WebElement invalidEmailAlert;
	
	@FindBy(how = How.NAME, using = IOSLocators.Alerts.nameAlreadyRegisteredNumber)
	private WebElement alreadyRegisteredNumberAlert; 
	
	@FindBy(how = How.NAME, using = IOSLocators.Alerts.nameAlreadyRegisteredEmail)
	private WebElement alreadyRegisteredEmailAlert;
	
	@FindBy(how = How.NAME, using = IOSLocators.PeoplePickerPage.nameNotNowButton)
	private WebElement notNowPhoneButton;

	private String login;

	private String password;

	public String message;

	public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public Boolean isVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(), viewPager);
	}

	public void signIn() throws IOException {

		signInButton.click();
	}

	public void switchToEmailLogin() throws Exception {
		if (!backButton.getText().equals("REGISTRATION")) {
			DriverUtils.mobileTapByCoordinates(getDriver(), backButton);
		}
		if (!DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.LoginPage.nameEmailLoginButton))) {
			signIn();
		} else {
			DriverUtils.mobileTapByCoordinates(getDriver(), emailLoginButton);
		}
	}

	public boolean isPhoneSignInButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				phoneLoginButton);
	}

	public void clickPhoneLogin() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), phoneLoginButton);
		phoneLoginButton.click();
	}

	public void waitForLaterButton(int time) throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.nameShareButton), time);
	}

	public PeoplePickerPage clickLaterButton() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameShareButton))) {
			shareButton.click();
			return new PeoplePickerPage(this.getLazyDriver());
		} else {
			return null;
		}
	}

	public void waitForLoginToFinish() throws Exception {

		if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(IOSLocators.xpathLoginButton), 40)) {
			throw new AssertionError(
					"Login button is still visible after the timeout");
		}
	}

	public void login() throws Exception {

		confirmSignInButton.click();

		waitForLoginToFinish();
	}

	public void clickLoginButton() {
		confirmSignInButton.click();
	}

	public void clickJoinButton() {
		registerButton.click();
	}

	public RegistrationPage join() throws Exception {
		termsOfServiceButton.click();
		registerButton.click();

		return new RegistrationPage(this.getLazyDriver());
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), loginField);
		DriverUtils.sendTextToInputByScript(getDriver(),
				IOSLocators.scriptSignInEmailPath, login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), passwordField);
		DriverUtils.sendTextToInputByScript(getDriver(),
				IOSLocators.scriptSignInPasswordPath, password);
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.name(IOSLocators.nameLoginField));
	}

	public void dismisSettingsWaring() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.LoginPage.nameMaybeLater))) {
			try {
				maybeLater.click();
			} catch (WebDriverException ex) {
				maybeLater.click();
			}
		}
	}

	public Boolean isLoginFinished() throws Exception {
		dismisSettingsWaring();
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.ContactListPage.nameSelfButton));
		return DriverUtils.isElementPresentAndDisplayed(
				getDriver(),
				getDriver().findElement(
						By.name(IOSLocators.ContactListPage.nameSelfButton)));
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		// no need to swipe
		return null;
	}

	public Boolean isLoginButtonVisible() {

		return (ExpectedConditions.visibilityOf(signInButton) != null);
	}

	public void tapHoldEmailInput() throws Exception {
		message = getDriver().findElement(By.name(IOSLocators.nameLoginField))
				.getText();
		this.getDriver().tap(
				1,
				this.getDriver().findElement(
						By.name(IOSLocators.nameLoginField)), 1000);
	}

	public void openTermsLink() throws Exception {
		Point p = termsButton.getLocation();
		Dimension k = termsButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width - 70),
				(p.y) + (k.height - 16), 1);
	}

	public void openPrivacyLink() throws Exception {
		Point p = privacyButton.getLocation();
		Dimension k = privacyButton.getSize();
		this.getDriver().tap(1, (p.x) + (k.width / 3), (p.y) + (k.height - 8),
				1);
	}

	public void closeTermsPrivacyController() throws Exception {
		this.getWait().until(
				ExpectedConditions
						.elementToBeClickable(termsPrivacyCloseButton));
		termsPrivacyCloseButton.click();
	}

	public Boolean isTermsPrivacyColseButtonVisible() {

		return (ExpectedConditions.visibilityOf(termsPrivacyCloseButton) != null);
	}

	public void tapPasswordField() {
		passwordField.click();
	}

	public Boolean errorMailNotificationIsShown() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameErrorMailNotification));
	}

	public Boolean errorMailNotificationIsNotShown() {
		return (ExpectedConditions.visibilityOf(errorMailNotification) == null);
	}

	public Boolean wrongCredentialsNotificationIsShown() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameWrongCredentialsNotification));
	}

	public void ignoreUpdate() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameIgnoreUpdateButton));
		ignoreUpdateButton.click();
	}

	public PersonalInfoPage tapChangePasswordButton() throws Exception {
		changePasswordButtonSignIn.click();
		return new PersonalInfoPage(this.getLazyDriver());
	}

	public void tapEmailFieldToChangePassword(String email) throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				safariChangePWEmailField);
		DriverUtils.mobileTapByCoordinates(getDriver(),
				safariChangePWEmailField);
		this.inputStringFromKeyboard(email);
	}

	public void tapChangePasswordButtonInWebView() throws Exception {
		DriverUtils.mobileTapByCoordinates(getDriver(),
				changePasswordPageChangePasswordButton);
	}

	public void changeURLInBrowser(String URL) throws Exception {
		DriverUtils.mobileTapByCoordinates(getDriver(), safariURLButton);
		this.inputStringFromKeyboard(URL);

		DriverUtils.mobileTapByCoordinates(getDriver(), safariGoButton);
	}

	public void tapPasswordFieldToChangePassword(String newPassword)
			throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				safariEnterNewPasswordField, 5);
		DriverUtils.mobileTapByCoordinates(getDriver(),
				safariEnterNewPasswordField);
		this.inputStringFromKeyboard(newPassword);
	}

	public void pressSimulatorHomeButton() throws Exception {
		cmdVscript(scriptString);
		DriverUtils.resetApp(getDriver());
	}

	public boolean isCountryPickerButttonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				countryPickerButtton);
	}

	public boolean isSetEmailPasswordSuggestionVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				setEmailPasswordSuggetionLabel);
	}

	public boolean isResendIn10minAlertVisible() throws Exception {
		DriverUtils.waitUntilAlertAppears(getDriver());
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				resendIn10minAlert);
	}

	public boolean isInvalidPhoneNumberAlertShown() throws Exception {
		DriverUtils.waitUntilAlertAppears(getDriver());
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				invalidPhoneNumberAlert);
	}
	
	public boolean isInvalidEmailAlertShown() throws Exception {
		DriverUtils.waitUntilAlertAppears(getDriver());
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				invalidEmailAlert);
	}
	
	public boolean isRegisteredNumberAlertShown() throws Exception {
		DriverUtils.waitUntilAlertAppears(getDriver());
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				alreadyRegisteredNumberAlert);
	}
	
	
	public boolean isAlreadyRegisteredEmailAlertShown() throws Exception {
		DriverUtils.waitUntilAlertAppears(getDriver());
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				alreadyRegisteredEmailAlert);
	}

	public void clickPhoneNotNow() {
		notNowPhoneButton.click();
	}
}
