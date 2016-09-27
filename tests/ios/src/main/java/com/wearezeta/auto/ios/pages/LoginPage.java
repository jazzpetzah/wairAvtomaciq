package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class LoginPage extends IOSPage {

    private static final By nameSwitchToLoginButton = MobileBy.AccessibilityId("LOG IN");

    private static final By nameSwitchToEmailLogin = MobileBy.AccessibilityId("EMAIL");

    private static final By nameSwitchToPhoneLogin = MobileBy.AccessibilityId("PHONE");

    private static final By nameLoginButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By fbNameLoginField = FBBy.AccessibilityId("EmailField");

    private static final By fbNamePasswordField = FBBy.AccessibilityId("PasswordField");

    private static final By nameWrongCredentialsNotification = MobileBy.AccessibilityId("Please verify your details and try again.");

    private static final By nameForgotPassword = MobileBy.AccessibilityId("FORGOT PASSWORD?");

    private static final By nameMaybeLater = MobileBy.AccessibilityId("MAYBE LATER");

    private static final By xpathSetEmailPasswordSuggestionLabel = By.xpath(
            "//XCUIElementTypeStaticText[contains(@name, 'Add your email and password.')]");


    public static final By nameResentIn10min = MobileBy.AccessibilityId(
            "We already sent you a code via SMS. Tap Resend after 10 minutes to get a new one.");

    private static final By nameSomethingWentWrong = MobileBy.AccessibilityId("Something went wrong, please try again");

    private static final By nameInvalidEmail = MobileBy.AccessibilityId("Please enter a valid email address");

    private static final By nameAlreadyRegisteredEmail =
            MobileBy.AccessibilityId("The email address you provided has already been registered. Please try again.");

    private static final By nameNotNowButton = MobileBy.AccessibilityId("NOT NOW");

    public String message;

    public LoginPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return isElementDisplayed(MobileBy.AccessibilityId(nameStrMainWindow));
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
        if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(), nameSwitchToLoginButton, LOGIN_TIMEOUT_SECONDS)) {
            throw new IllegalStateException("Login button is still visible after the timeout");
        }
    }

    public void tapLoginButton() throws Exception {
        getElement(nameLoginButton).click();
    }

    public void setLogin(String login) throws Exception {
        final FBElement el = ((FBElement) getElement(fbNameLoginField));
        el.click();
        el.clear();
        el.sendKeys(login);
    }

    public void setPassword(String password) throws Exception {
        final FBElement el = ((FBElement) getElement(fbNamePasswordField));
        el.click();
        el.clear();
        el.sendKeys(password);
    }

    public static final int LOGIN_TIMEOUT_SECONDS = 30;

    public void dismissSettingsWarning() throws Exception {
        final WebElement maybeLaterBtn = getElement(nameMaybeLater, "MAYBE LATER link is not visible",
                LOGIN_TIMEOUT_SECONDS);
        maybeLaterBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameMaybeLater, 3)) {
            maybeLaterBtn.click();
        }
    }

    public Boolean isContactsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), ConversationsListPage.nameContactsButton,
                LOGIN_TIMEOUT_SECONDS);
    }

    public void tapHoldEmailInput() throws Exception {
        final FBElement loginField = (FBElement) getElement(fbNameLoginField);
        message = loginField.getText();
        loginField.longTap();
    }

    public void tapPasswordField() throws Exception {
        getElement(fbNamePasswordField).click();
    }

    public boolean wrongCredentialsNotificationIsShown() throws Exception {
        return isElementDisplayed(nameWrongCredentialsNotification);
    }

    public void tapForgotPasswordButton() throws Exception {
        getElement(nameForgotPassword).click();
    }

    public boolean isSetEmailPasswordSuggestionVisible() throws Exception {
        return isElementDisplayed(xpathSetEmailPasswordSuggestionLabel);
    }

    public boolean isResendIn10minAlertVisible() throws Exception {
        return waitUntilAlertDisplayed() && isElementDisplayed(nameResentIn10min);
    }

    public boolean isInvalidEmailAlertShown() throws Exception {
        return waitUntilAlertDisplayed() && isElementDisplayed(nameInvalidEmail);
    }

    public boolean isAlreadyRegisteredEmailAlertShown() throws Exception {
        return waitUntilAlertDisplayed() && isElementDisplayed(nameAlreadyRegisteredEmail);
    }

    public void clickPhoneNotNow() throws Exception {
        getElement(nameNotNowButton).click();
    }

    public boolean isSomethingWentWrongAlertShown() throws Exception {
        return waitUntilAlertDisplayed() && isElementDisplayed(nameSomethingWentWrong);
    }

    public void dismissSettingsWarningIfVisible(int timeoutSeconds) throws Exception {
        getElementIfDisplayed(nameMaybeLater, timeoutSeconds).orElseGet(DummyElement::new).click();
    }

    public void switchToLogin() throws Exception {
        final WebElement switchToLoginButton = getElement(nameSwitchToLoginButton);
        // Wait for a while until the button is 100% accessible
        Thread.sleep(1000);
        switchToLoginButton.click();
    }

    public void inputLoginCode(PhoneNumber forNumber) throws Exception {
        final WebElement codeInput = getElement(RegistrationPage.xpathVerificationCodeInput,
                "Login code input is not visible");
        final String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(forNumber);
        codeInput.sendKeys(code);
        getElement(RegistrationPage.nameConfirmButton, "Confirm button is not visible", 2).click();
    }
}
