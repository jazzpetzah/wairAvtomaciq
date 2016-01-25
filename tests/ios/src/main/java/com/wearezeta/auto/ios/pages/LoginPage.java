package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {
    private static final By nameSwitchToEmailLogin = By.name("I HAVE AN ACCOUNT");

    private static final By nameSwitchToPhoneLogin = By.name("LOG IN BY PHONE");

    private static final By nameLoginButton = By.name("RegistrationConfirmButton");

    private static final By nameLoginField = By.name("EmailField");

    private static final By namePasswordField = By.name("PasswordField");

    private static final By nameTermsPrivacyLinks = By.name("TermsPrivacyTextView");

    private static final By nameTermsPrivacyCloseButton = By.name("WebViewCloseButton");

    private static final By nameWrongCredentialsNotification = By.name("Please verify your details and try again.");

    private static final By nameForgotPassword = By.name("FORGOT PASSWORD?");

    private static final By xpathSafariChangePasswordEmailField =
            By.xpath("//UIAApplication[@name='Safari']//UIATextField[@value='Email']");

    private static final By xpathSafariURLButton = By.xpath("//UIAButton[@name='URL']");

    private static final By xpathSafariGoButton = By.xpath("//UIAButton[@name='Go']");

    private static final By xpathSafariEnterNewPasswordField =
            By.xpath("//UIASecureTextField[@value='Enter new password']");

    private static final By xpathChangePasswordPageChangePasswordButton =
            By.xpath("//UIAButton[@name='RESET PASSWORD']");

    private static final By namePhoneLoginButton = By.name("RegistrationRightButton");

    private static final By nameMaybeLater = By.name("MAYBE LATER");

    private static final By nameCountryPickerButton = By.name("CountryPickerButton");

    private static final By xpathSetEmailPasswordSuggetionLabel = By.xpath(
            "//UIAStaticText[contains(@name, 'Add email address and password')]");

    public static final By nameResentIn10min = By.name(
            "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.");

    private static final By nameInvalidPhoneNumber = By.name("Please enter a valid phone number");

    private static final By nameInvalidEmail = By.name("Please enter a valid email address");

    private static final By nameAlreadyRegisteredNumber = By.name(
            "The phone number you provided has already been registered. Please try again.");

    private static final By nameAlreadyRegisteredEmail =
            By.name("The email address you provided has already been registered. Please try again.");

    private static final By nameNotNowButton = By.name("NOT NOW");

    private static final By nameGotItButton = By.name("GOT IT");

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
        getElement(nameSwitchToEmailLogin).click();
    }

    public void switchToPhoneLogin() throws Exception {
        getElement(nameSwitchToPhoneLogin).click();
    }

    public void waitForLoginToFinish() throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(), nameLoginButton, 40)) {
            throw new IllegalStateException("Login button is still visible after the timeout");
        }
        final Optional<WebElement> gotItButton = getElementIfDisplayed(nameGotItButton, 2);
        if (gotItButton.isPresent()) {
            gotItButton.get().click();
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

    public void dismissSettingsWaring() throws Exception {
        final Optional<WebElement> maybeLater = getElementIfDisplayed(nameMaybeLater);
        if (maybeLater.isPresent()) {
            maybeLater.get().click();
        }
    }

    public Boolean isLoginFinished() throws Exception {
        dismissSettingsWaring();
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameSelfButton), 60);
    }

    public boolean isLoginButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSwitchToEmailLogin);
    }

    public void tapHoldEmailInput() throws Exception {
        final WebElement loginField = getElement(nameLoginField);
        message = loginField.getText();
        this.getDriver().tap(1, loginField, 1000);
    }

    public void openTermsLink() throws Exception {
        final WebElement termsButton = getElement(nameTermsPrivacyLinks);
        Point p = termsButton.getLocation();
        Dimension k = termsButton.getSize();
        this.getDriver().tap(1, (p.x) + (k.width - 70),
                (p.y) + (k.height - 16), 1);
    }

    public void closeTermsPrivacyController() throws Exception {
        getElement(nameTermsPrivacyCloseButton, "Close Terms button is not visible").click();
    }

    public boolean isTermsPrivacyCloseButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameTermsPrivacyCloseButton);
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

    public void tapEmailFieldToChangePassword(String email) throws Exception {
        final WebElement safariChangePWEmailField = getElement(xpathSafariChangePasswordEmailField,
                "Email input field in Safari is not visible");
        DriverUtils.tapByCoordinates(getDriver(), safariChangePWEmailField);
        this.inputStringFromKeyboard(email);
    }

    public void tapChangePasswordButtonInWebView() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(xpathChangePasswordPageChangePasswordButton));
    }

    public void changeURLInBrowser(String URL) throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(xpathSafariURLButton));
        this.inputStringFromKeyboard("a+b");
        this.inputStringFromKeyboard(URL);
        DriverUtils.tapByCoordinates(getDriver(), getElement(xpathSafariGoButton));
    }

    public void tapPasswordFieldToChangePassword(String newPassword) throws Exception {
        final WebElement safariEnterNewPasswordField = getElement(xpathSafariEnterNewPasswordField,
                "Password input field in Safari is not visible");
        DriverUtils.tapByCoordinates(getDriver(), safariEnterNewPasswordField);
        this.inputStringFromKeyboard(newPassword);
    }

    public boolean isCountryPickerButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCountryPickerButton);
    }

    public boolean isSetEmailPasswordSuggestionVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSetEmailPasswordSuggetionLabel);
    }

    public boolean isResendIn10minAlertVisible() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameResentIn10min, 1);
    }

    public boolean isInvalidPhoneNumberAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidPhoneNumber, 1);
    }

    public boolean isInvalidEmailAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidEmail, 1);
    }

    public boolean isRegisteredNumberAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameAlreadyRegisteredNumber, 1);
    }

    public boolean isAlreadyRegisteredEmailAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameAlreadyRegisteredEmail, 1);
    }

    public void clickPhoneNotNow() throws Exception {
        getElement(nameNotNowButton).click();
    }
}
