package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DummyElement;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {
    private static final By nameSwitchToEmailLogin = MobileBy.AccessibilityId("I HAVE AN ACCOUNT");

    private static final By nameSwitchToPhoneLogin = MobileBy.AccessibilityId("LOG IN BY PHONE");

    private static final By nameLoginButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By nameLoginField = MobileBy.AccessibilityId("EmailField");

    private static final By namePasswordField = MobileBy.AccessibilityId("PasswordField");

    private static final By nameWrongCredentialsNotification = MobileBy.AccessibilityId("Please verify your details and try again.");

    private static final By nameForgotPassword = MobileBy.AccessibilityId("FORGOT PASSWORD?");

    private static final By namePhoneLoginButton = MobileBy.AccessibilityId("RegistrationRightButton");

    private static final By nameMaybeLater = MobileBy.AccessibilityId("MAYBE LATER");

    private static final By nameCountryPickerButton = MobileBy.AccessibilityId("CountryPickerButton");

    private static final By xpathSetEmailPasswordSuggestionLabel = By.xpath(
            "//UIAStaticText[contains(@name, 'Add email address and password')]");

    public static final By nameResentIn10min = MobileBy.AccessibilityId(
            "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.");

    private static final By nameInvalidPhoneNumber = MobileBy.AccessibilityId("Please enter a valid phone number");

    private static final By nameSomethingWentWrong = MobileBy.AccessibilityId("Something went wrong, please try again");

    private static final By nameInvalidEmail = MobileBy.AccessibilityId("Please enter a valid email address");

    private static final By nameAlreadyRegisteredNumber = MobileBy.AccessibilityId(
            "The phone number you provided has already been registered. Please try again.");

    private static final By nameAlreadyRegisteredEmail =
            MobileBy.AccessibilityId("The email address you provided has already been registered. Please try again.");

    private static final By nameNotNowButton = MobileBy.AccessibilityId("NOT NOW");

    public static final String nameSelfButton = "SelfButton";

    public String message;

    public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), MobileBy.AccessibilityId(nameStrMainWindow));
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
        final WebElement maybeLaterBtn = getElement(nameMaybeLater, "MAYBE LATER link is not visible",
                LOGIN_TIMEOUT_SECONDS);
        maybeLaterBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameMaybeLater, 3)) {
            maybeLaterBtn.click();
        }
    }

    public Boolean isSelfAvatarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), MobileBy.AccessibilityId(nameSelfButton), LOGIN_TIMEOUT_SECONDS);
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
