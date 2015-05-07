package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LoginPage extends IOSPage {
	private static final Logger log = ZetaLogger.getLog(LoginPage.class
			.getSimpleName());

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

	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordEmailField)
	private WebElement changePWEmailField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordPageChangePasswordButton)
	private WebElement changePasswordPageChangePasswordButton;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIATextField)
	private List<WebElement> textFields;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIAButton)
	private List<WebElement> uiButtons;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIASecureTextField)
	private List<WebElement> secureTextFields;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangedPasswordConfirmationText)
	private WebElement changedPasswordPageConfirmation;

	private String login;

	private String password;

	public String message;

	public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public Boolean isVisible() {
		return viewPager != null;
	}

	public IOSPage signIn() throws IOException {

		signInButton.click();
		return this;
	}

	public void waitForLaterButton(int time) throws Exception {
		DriverUtils.waitUntilElementAppears(getDriver(),
				By.name(IOSLocators.nameShareButton), time);
	}

	public PeoplePickerPage clickLaterButton() throws Exception {
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameShareButton))) {
			shareButton.click();
			return new PeoplePickerPage(this.getLazyDriver());
		} else {
			return null;
		}
	}

	public boolean isSelfProfileVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameProfileName));
	}

	public IOSPage login() throws Exception {

		confirmSignInButton.click();

		if (DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.name(IOSLocators.xpathLoginButton), 40)) {
			return new ContactListPage(this.getLazyDriver());
		} else {
			return null;
		}
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
		String script = String.format(IOSLocators.scriptSignInEmailPath
				+ ".setValue(\"%s\")", login);
		try {
			this.getDriver().executeScript(script);
		} catch (WebDriverException ex) {
			log.debug("fucking appium! " + ex.getMessage());
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		String script = String.format(IOSLocators.scriptSignInPasswordPath
				+ ".setValue(\"%s\")", password);
		try {
			this.getDriver().executeScript(script);
		} catch (WebDriverException ex) {
			log.debug("fucking web appium! " + ex.getMessage());
		}
	}

	public boolean waitForLogin() throws Exception {
		return DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.name(IOSLocators.nameLoginField));
	}

	public Boolean isLoginFinished(String contact) throws Exception {
		try {
			this.getWait().until(
					ExpectedConditions.presenceOfElementLocated(By
							.name(contact)));
			this.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.name(contact)));
		} catch (WebDriverException ex) {
		}
		return DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.name(contact));
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
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameErrorMailNotification));
	}

	public Boolean errorMailNotificationIsNotShown() {
		return (ExpectedConditions.visibilityOf(errorMailNotification) == null);
	}

	public Boolean wrongCredentialsNotificationIsShown() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameWrongCredentialsNotification));
	}

	public void ignoreUpdate() throws Exception {
		DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.name(IOSLocators.nameIgnoreUpdateButton));
		ignoreUpdateButton.click();
	}

	public PersonalInfoPage tapChangePasswordButton() throws Exception {
		changePasswordButtonSignIn.click();
		return new PersonalInfoPage(this.getLazyDriver());
	}

	public void tapEmailFieldToChangePassword(String email) throws Exception {
		for (WebElement textField : textFields) {
			String valueOfField = textField.getAttribute("value");
			if (valueOfField.equals("Email")) {
				DriverUtils.mobileTapByCoordinates(getDriver(), textField);
				this.inputStringFromKeyboard(email);
			}
		}
	}

	public void tapChangePasswordButtonInWebView() throws Exception {
		for (WebElement uiButton : uiButtons) {
			String nameOfButton = uiButton.getAttribute("name");
			if (nameOfButton.equals("CHANGE PASSWORD")) {
				DriverUtils.mobileTapByCoordinates(getDriver(), uiButton);
			}
		}
	}

	public void changeURLInBrowser(String URL) throws Exception {
		for (WebElement uiButton : uiButtons) {
			String nameOfButton = uiButton.getAttribute("name");
			if (nameOfButton.equals("URL")) {
				DriverUtils.mobileTapByCoordinates(getDriver(), uiButton);
				this.inputStringFromKeyboard(URL);
			}
			for (WebElement uiButton2 : uiButtons) {
				String nameOfButton2 = uiButton2.getAttribute("name");
				if (nameOfButton2.equals("Go")) {
					DriverUtils.mobileTapByCoordinates(getDriver(), uiButton2);
				}
			}
		}
	}

	public void tapPasswordFieldToChangePassword(String newPassword)
			throws Exception {
		for (WebElement secureTextField : secureTextFields) {
			String valueOfField = secureTextField.getAttribute("value");
			if (valueOfField.equals("Password")) {
				DriverUtils
						.mobileTapByCoordinates(getDriver(), secureTextField);
				this.inputStringFromKeyboard(newPassword);
			}
		}
	}

	public boolean passwordConfiamtionIsVisible() {
		return changedPasswordPageConfirmation.isDisplayed();
	}

	public void pressSimulatorHomeButton() throws Exception {
		cmdVscript(scriptString);
		DriverUtils.resetApp(getDriver());
	}
}
