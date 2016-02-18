package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DummyElement;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {
    private static final By nameSwitchToEmailLogin = By.name("I HAVE AN ACCOUNT");

    private static final By nameSwitchToPhoneLogin = By.name("LOG IN BY PHONE");

    private static final By nameLoginButton = By.name("RegistrationConfirmButton");

    private static final By nameLoginField = By.name("EmailField");

    private static final By namePasswordField = By.name("PasswordField");

    private static final By nameWrongCredentialsNotification = By.name("Please verify your details and try again.");

    private static final By nameForgotPassword = By.name("FORGOT PASSWORD?");

    private static final By xpathSafariChangePasswordEmailField =
            By.xpath("//UIAApplication[@name='Safari']//UIATextField[@value='Email']");

    private static final By xpathSafariEnterNewPasswordField =
            By.xpath("//UIASecureTextField[@value='Enter new password']");

    private static final By namePhoneLoginButton = By.name("RegistrationRightButton");

    private static final By nameMaybeLater = By.name("MAYBE LATER");

    private static final By nameCountryPickerButton = By.name("CountryPickerButton");

    private static final By xpathSetEmailPasswordSuggestionLabel = By.xpath(
            "//UIAStaticText[contains(@name, 'Add email address and password')]");

    public static final By nameResentIn10min = By.name(
            "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.");

    private static final By nameInvalidPhoneNumber = By.name("Please enter a valid phone number");

    private static final By nameSomethingWentWrong = By.name("Something went wrong, please try again");

    private static final By nameInvalidEmail = By.name("Please enter a valid email address");

    private static final By nameAlreadyRegisteredNumber = By.name(
            "The phone number you provided has already been registered. Please try again.");

    private static final By nameAlreadyRegisteredEmail =
            By.name("The email address you provided has already been registered. Please try again.");

    private static final By nameNotNowButton = By.name("NOT NOW");

    public static final String nameSelfButton = "SelfButton";

    public String message;

    public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameStrMainWindow));
    }

    public boolean isPhoneSignInButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), namePhoneLoginButton);
    }

    public void switchToEmailLogin() throws Exception {
        final WebElement emailSwitchBtn = getElement(nameSwitchToEmailLogin);
        emailSwitchBtn.click();
        //Work around: to click again, when it didnt got clicked the first time
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameSwitchToEmailLogin)) {
            emailSwitchBtn.click();
        }
    }

    public void switchToPhoneLogin() throws Exception {
        getElement(nameSwitchToPhoneLogin).click();
    }

    public void waitForLoginToFinish() throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(), nameLoginButton, 40)) {
            throw new IllegalStateException("Login button is still visible after the timeout");
        }
    }

    public void clickLoginButton() throws Exception {
        getElement(nameLoginButton).click();
    }

    public boolean isEmailInputFieldInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLoginField);
    }

    public void setLogin(String login) throws Exception {
        ((IOSElement) getElement(nameLoginField)).setValue(login);
    }

    public void setPassword(String password) throws Exception {
        ((IOSElement) getElement(namePasswordField)).setValue(password);
    }

    private static final int LOGIN_TIMEOUT_SECONDS = 30;

    public void dismissSettingsWarning() throws Exception {
        getElement(nameMaybeLater,
                String.format("MAYBE LATER label is not visible after %s seconds timeout", LOGIN_TIMEOUT_SECONDS),
                LOGIN_TIMEOUT_SECONDS).click();
    }

    public Boolean isSelfAvatarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameSelfButton), LOGIN_TIMEOUT_SECONDS);
    }

    public void tapHoldEmailInput() throws Exception {
        final WebElement loginField = getElement(nameLoginField);
        message = loginField.getText();
        this.getDriver().tap(1, loginField, 1000);
    }

    public void tapPasswordField() throws Exception {
        getElement(namePasswordField).click();
    }

    public boolean wrongCredentialsNotificationIsShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameWrongCredentialsNotification);
    }

    public void tapForgotPasswordButton() throws Exception {
        getElement(nameForgotPassword).click();
    }

    public void commitEmail(String email) throws Exception {
        this.inputStringFromKeyboard(getElement(xpathSafariChangePasswordEmailField,
                "Email input field in Safari is not visible"), email, false, true);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathSafariChangePasswordEmailField)) {
            this.inputStringFromKeyboard(getElement(xpathSafariChangePasswordEmailField,
                    "Email input field in Safari is not visible"), email, false, true);
        }
    }

    public void changeURLInBrowser(String URL) throws Exception {
        this.inputStringFromKeyboard(getElement(xpathBrowserURLButton), URL, false, true);
    }

    public void commitNewPassword(String newPassword) throws Exception {
        this.inputStringFromKeyboard(getElement(xpathSafariEnterNewPasswordField,
                "Password input field in Safari is not visible"), newPassword, false, true);
    }

    public boolean isCountryPickerButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCountryPickerButton);
    }

    public boolean isSetEmailPasswordSuggestionVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSetEmailPasswordSuggestionLabel);
    }

    public boolean isResendIn10minAlertVisible() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameResentIn10min, 1);
    }

    public boolean isInvalidPhoneNumberAlertShown() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidPhoneNumber, 1);
    }

    public boolean isInvalidEmailAlertShown() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidEmail, 1);
    }

    public boolean isRegisteredNumberAlertShown() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameAlreadyRegisteredNumber, 1);
    }

    public boolean isAlreadyRegisteredEmailAlertShown() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameAlreadyRegisteredEmail, 1);
    }

    public void clickPhoneNotNow() throws Exception {
        getElement(nameNotNowButton).click();
    }

    public boolean isSomethingWentWrongAlertShown() throws Exception {
        return DriverUtils.waitUntilAlertAppears(getDriver()) &&
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSomethingWentWrong, 1);
    }

    public void dismissSettingsWarningIfVisible(int timeoutSeconds) throws Exception {
        getElementIfDisplayed(nameMaybeLater, timeoutSeconds).orElseGet(DummyElement::new).click();
    }
}
