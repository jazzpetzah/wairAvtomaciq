package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import io.appium.java_client.ios.IOSElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {
    final String[] scriptString = new String[]{
            "tell application \"System Events\"",
            "tell application \"iOS Simulator\" to activate",
            "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}",
            "end tell"};

    private static final String nameMainWindow = "ZClientMainWindow";
    @FindBy(name = nameMainWindow)
    private WebElement viewPager;

    private static final String nameSignInButton = "I HAVE AN ACCOUNT";
    @FindBy(name = nameSignInButton)
    private WebElement signInButton;

    private static final String nameLoginButton = "RegistrationConfirmButton";
    @FindBy(name = nameLoginButton)
    private WebElement confirmSignInButton;

    private static final String nameRegisterButton = "SignUp";
    @FindBy(name = nameRegisterButton)
    private WebElement registerButton;

    private static final String nameLoginField = "EmailField";
    @FindBy(name = nameLoginField)
    private WebElement loginField;

    private static final String namePasswordField = "PasswordField";
    @FindBy(name = namePasswordField)
    private WebElement passwordField;

    private static final String classUIATextView = "UIATextView";
    @FindBy(className = classUIATextView)
    private List<WebElement> userName;

    private static final String nameTermsPrivacyLinks = "TermsPrivacyTextView";
    @FindBy(name = nameTermsPrivacyLinks)
    private WebElement termsButton;
    @FindBy(name = nameTermsPrivacyLinks)
    private WebElement privacyButton;

    private static final String nameTermsPrivacyCloseButton = "WebViewCloseButton";
    @FindBy(name = nameTermsPrivacyCloseButton)
    private WebElement termsPrivacyCloseButton;

    private static final String nameErrorMailNotification = "PLEASE PROVIDE A VALID EMAIL ADDRESS";
    @FindBy(name = nameErrorMailNotification)
    private WebElement errorMailNotification;

    private static final String nameWrongCredentialsNotification = "Please verify your details and try again.";
    @FindBy(name = nameWrongCredentialsNotification)
    private WebElement wrongCredentialsNotification;

    private static final String nameIgnoreUpdateButton = "Ignore";
    @FindBy(name = nameIgnoreUpdateButton)
    private WebElement ignoreUpdateButton;

    private static final String nameTermsOfServiceButton = "LegalCheckmarkButton";
    @FindBy(name = nameTermsOfServiceButton)
    private WebElement termsOfServiceButton;

    private static final String nameProfileName = "ProfileSelfNameField";
    @FindBy(name = nameProfileName)
    private WebElement selfProfileName;

    private static final String nameShareButton = "SHARE CONTACTS";
    @FindBy(name = nameShareButton)
    private WebElement shareButton;

    private static final String nameForgotPasswordButton = "FORGOT PASSWORD?";
    @FindBy(name = nameForgotPasswordButton)
    private WebElement changePasswordButtonSignIn;

    private static final String xpathSafariChangePasswordEmailField = "//UIAApplication[@name='Safari']//UIATextField[@value='Email']";
    @FindBy(xpath = xpathSafariChangePasswordEmailField)
    private WebElement safariChangePWEmailField;

    private static final String xpathSafariURLButton = "//UIAButton[@name='URL']";
    @FindBy(xpath = xpathSafariURLButton)
    private WebElement safariURLButton;

    private static final String xpathSafariGoButton = "//UIAButton[@name='Go']";
    @FindBy(xpath = xpathSafariGoButton)
    private WebElement safariGoButton;

    private static final String xpathSafariEnterNewPasswordField = "//UIASecureTextField[@value='Enter new password']";
    @FindBy(xpath = xpathSafariEnterNewPasswordField)
    private WebElement safariEnterNewPasswordField;

    private static final String xpathChangePasswordPageChangePasswordButton = "//UIAButton[@name='RESET PASSWORD']";
    @FindBy(xpath = xpathChangePasswordPageChangePasswordButton)
    private WebElement changePasswordPageChangePasswordButton;

    private static final String namePhoneLoginButton = "RegistrationRightButton";
    @FindBy(name = namePhoneLoginButton)
    private WebElement phoneLoginButton;

    private static final String nameEmailLoginButton = "EMAIL SIGN IN";
    @FindBy(name = nameEmailLoginButton)
    private WebElement emailLoginButton;

    private static final String nameBackButton = "BackToWelcomeButton";
    @FindBy(name = nameBackButton)
    private WebElement backButton;

    private static final String nameMaybeLater = "MAYBE LATER";
    @FindBy(name = nameMaybeLater)
    private WebElement maybeLater;

    private static final String nameCountryPickerButton = "CountryPickerButton";
    @FindBy(name = nameCountryPickerButton)
    private WebElement countryPickerButtton;

    private static final String xpathSetEmailPasswordSuggetionLabel =
            "//UIAStaticText[contains(@name, 'Add email address and password')]";
    @FindBy(xpath = xpathSetEmailPasswordSuggetionLabel)
    private WebElement setEmailPasswordSuggetionLabel;

    public static final String nameResentIn10min =
            "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.";
    @FindBy(name = nameResentIn10min)
    private WebElement resendIn10minAlert;

    private static final String nameInvalidPhoneNumber = "Please enter a valid phone number";
    @FindBy(name = nameInvalidPhoneNumber)
    private WebElement invalidPhoneNumberAlert;

    private static final String nameInvalidEmail = "Please enter a valid email address";
    @FindBy(name = nameInvalidEmail)
    private WebElement invalidEmailAlert;

    private static final String nameAlreadyRegisteredNumber =
            "The phone number you provided has already been registered. Please try again.";
    @FindBy(name = nameAlreadyRegisteredNumber)
    private WebElement alreadyRegisteredNumberAlert;

    private static final String nameAlreadyRegisteredEmail = "The email address you provided has already been registered. Please try again.";
    @FindBy(name = nameAlreadyRegisteredEmail)
    private WebElement alreadyRegisteredEmailAlert;

    private static final String nameNotNowButton = "NOT NOW";
    @FindBy(name = nameNotNowButton)
    private WebElement notNowPhoneButton;

    private static final String nameGotItButton = "GOT IT";
    @FindBy(name = nameGotItButton)
    private WebElement gotItButton;

    public String message;

    public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public Boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameMainWindow));
    }

    public void signIn() throws IOException {
        signInButton.click();
    }

    public void switchToEmailLogin() throws Exception {
        if (!backButton.getText().equals("REGISTRATION")) {
            DriverUtils.tapByCoordinates(getDriver(), backButton);
        }
        if (!DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.name(IOSLocators.LoginPage.nameEmailLoginButton))) {
            signIn();
        } else {
            DriverUtils.tapByCoordinates(getDriver(), emailLoginButton);
        }
    }

    public boolean isPhoneSignInButtonVisible() throws Exception {
        Assert.assertTrue(
                "iPhone Sign in button is not presented in elements tree",
                DriverUtils.waitUntilLocatorAppears(getDriver(),
                        By.name(IOSLocators.LoginPage.namePhoneLoginButton)));
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
                By.name(IOSLocators.nameLoginButton), 40)) {
            throw new AssertionError(
                    "Login button is still visible after the timeout");
        }
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameGotItButton), 2)) {
            gotItButton.click();
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

    public void setLogin(String login) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), loginField);
        ((IOSElement) getDriver().findElementByName(IOSLocators.nameLoginField)).
                setValue(login);
    }

    public void setPassword(String password) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), passwordField);
        ((IOSElement) getDriver().findElementByName(IOSLocators.namePasswordField)).
                setValue(password);
    }

    public boolean waitForLogin() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.name(IOSLocators.nameLoginField));
    }

    public void dismisSettingsWaring() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.name(IOSLocators.LoginPage.nameMaybeLater))) {
            DriverUtils.waitUntilElementClickable(getDriver(), maybeLater);
            for (int i = 0; i < 3; i++) {
                try {
                    maybeLater.click();
                } catch (WebDriverException ex) {
                    // ignore silently
                }
                if (mayBeLaterIsNotShown()) {
                    break;
                }
            }
        }
    }

    private boolean mayBeLaterIsNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.xpath(IOSLocators.LoginPage.nameMaybeLater), 3);
    }

    public Boolean isLoginFinished() throws Exception {
        dismisSettingsWaring();
        return DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.name(IOSLocators.ContactListPage.nameSelfButton), 60);
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

    public boolean isTermsPrivacyColseButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameTermsPrivacyCloseButton));
    }

    public void tapPasswordField() {
        passwordField.click();
    }

    public Boolean errorMailNotificationIsShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(IOSLocators.nameErrorMailNotification));
    }

    public boolean errorMailNotificationIsNotShown() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(nameErrorMailNotification));
    }

    public boolean wrongCredentialsNotificationIsShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(IOSLocators.nameWrongCredentialsNotification));
    }

      public PersonalInfoPage tapChangePasswordButton() throws Exception {
        changePasswordButtonSignIn.click();
        return new PersonalInfoPage(this.getLazyDriver());
    }

    public void tapEmailFieldToChangePassword(String email) throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                safariChangePWEmailField);
        DriverUtils.tapByCoordinates(getDriver(),
                safariChangePWEmailField);
        this.inputStringFromKeyboard(email);
    }

    public void tapChangePasswordButtonInWebView() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(),
                changePasswordPageChangePasswordButton);
    }

    public void changeURLInBrowser(String URL) throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), safariURLButton);
        this.inputStringFromKeyboard(URL);

        DriverUtils.tapByCoordinates(getDriver(), safariGoButton);
    }

    public void tapPasswordFieldToChangePassword(String newPassword)
            throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(),
                safariEnterNewPasswordField, 5);
        DriverUtils.tapByCoordinates(getDriver(),
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
