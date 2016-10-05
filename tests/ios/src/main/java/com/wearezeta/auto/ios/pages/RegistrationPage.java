package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.MobileBy;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.function.Function;

public class RegistrationPage extends IOSPage {
    private static final String WIRE_COUNTRY_NAME_PREFIX = "Wirestan";

    private static final String WIRE_COUNTRY_NAME = WIRE_COUNTRY_NAME_PREFIX + " ☀️";

    private static final By nameSearchField = MobileBy.AccessibilityId("Search");

    private static final By nameWireCountry = MobileBy.AccessibilityId(WIRE_COUNTRY_NAME);

    private static final By xpathYourName = By.xpath("//XCUIElementTypeTextField[@value='YOUR FULL NAME']");

    private static final By nameYourEmail = MobileBy.AccessibilityId("EmailField");

    private static final By nameYourPassword = MobileBy.AccessibilityId("PasswordField");

    private static final By xpathCreateAccountButton = By
            .xpath("//XCUIElementTypeSecureTextField[contains(@name, 'PasswordField')]/XCUIElementTypeButton");

    private static final Function<String, String> xpathStrConfirmationByMessage = msg -> String.format(
            "//XCUIElementTypeStaticText[contains(@name, 'We sent an email to %s')]", msg);

    private static final By xpathEmailVerifPrompt =
            By.xpath("//XCUIElementTypeStaticText[contains(@name, 'We sent an email to ')]");

    private static final By fbNamePhoneNumberField = FBBy.AccessibilityId("PhoneNumberField");

    public static final By xpathVerificationCodeInput = By.xpath("//XCUIElementTypeTextField");

    private static final By nameCountryPickerButton = MobileBy.AccessibilityId("CountryPickerButton");

    public static final By nameConfirmButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By nameAgreeButton = MobileBy.AccessibilityId("I AGREE");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("Take photo");

    private static final By xpathVerificationPage = By
            .xpath("//XCUIElementTypeStaticText[contains(@name, 'Enter the verification code we sent to')]");

    private static final By nameResendCodeButton = MobileBy.AccessibilityId("RESEND");

    private static final By nameInvalidCode = MobileBy.AccessibilityId("Please enter a valid code");

    private static final By nameChooseOwnPictureButton = MobileBy.AccessibilityId("ChooseOwnPictureButton");

    private static final By nameChoosePhotoButton = MobileBy.AccessibilityId("Choose Photo");

    private static final By nameKeepThisOneButton = MobileBy.AccessibilityId("KeepDefaultPictureButton");

    private static final By xpathNoCodeShowingUpLabel = By.
            xpath("//XCUIElementTypeStaticText[contains(@name, 'NO CODE SHOWING UP?')]");

    private static final Logger log = ZetaLogger.getLog(RegistrationPage.class.getSimpleName());


    private String name;
    private String email;
    private String password;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickAgreeButton() throws Exception {
        tapElementWithRetryIfStillDisplayed(nameAgreeButton);
    }

    private void selectWirestan() throws Exception {
        final WebElement countryPickerBtn = getElement(nameCountryPickerButton);
        countryPickerBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCountryPickerButton, 5)) {
            countryPickerBtn.click();
        }
        final WebElement searchInput = getElement(nameSearchField);
        searchInput.click();
        searchInput.sendKeys(WIRE_COUNTRY_NAME_PREFIX);
        getElement(nameWireCountry).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public void inputPhoneNumber(PhoneNumber number) throws Exception {
        selectWirestan();
        final FBElement phoneNumberField = (FBElement) getElement(fbNamePhoneNumberField);
        this.tapAtTheCenterOfElement(phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(number.withoutPrefix());
        getElement(nameConfirmButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConfirmButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public boolean isVerificationCodePageVisible() throws Exception {
        return isElementDisplayed(xpathVerificationPage);
    }

    public void inputActivationCode(PhoneNumber forNumber) throws Exception {
        final WebElement codeInput = getElement(xpathVerificationCodeInput,
                "Activation code input is not visible");
        final String code = BackendAPIWrappers.getActivationCodeByPhoneNumber(forNumber);
        codeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", 2).click();
    }

    private static final Random rand = new Random();

    public void inputRandomConfirmationCode() throws Exception {
        getElement(xpathVerificationCodeInput).sendKeys(Integer.toString(100000 + rand.nextInt(900000)));
        getElement(nameConfirmButton, "Confirm button is not visible", 2).click();
    }

    public void clickResendCodeButton() throws Exception {
        getElement(nameResendCodeButton, "Resend code button is not visible").click();
    }

    public boolean isConfirmationShown() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        return isElementDisplayed(locator);
    }

    public void commitName() throws Exception {
        getElement(nameConfirmButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConfirmButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public void tapCreateAccountButton() throws Exception {
        getElement(xpathCreateAccountButton, "Create Account button is not visible").click();
    }

    public void typeEmail() throws Exception {
        final WebElement emailInput = getElement(nameYourEmail);
        emailInput.click();
        emailInput.sendKeys(getEmail());
    }

    public void typeUsername() throws Exception {
        final WebElement yourNameInput = getElement(xpathYourName);
        yourNameInput.click();
        yourNameInput.sendKeys(getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        this.name = name;
        typeUsername();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        this.email = email;
        typeEmail();
        this.email = email.replace('\n', ' ').trim();
    }

    public String getPassword() {
        return password;
    }

    private void typePassword() throws Exception {
        final WebElement passwordInput = getElement(nameYourPassword);
        passwordInput.click();
        passwordInput.sendKeys(getPassword());
    }

    public void setPassword(String password) throws Exception {
        this.password = password;
        typePassword();
    }

    public boolean isEmailVerificationPromptVisible() throws Exception {
        return isElementDisplayed(xpathEmailVerifPrompt);
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        return isElementDisplayed(nameInvalidCode);
    }

    public void tapChooseOwnPicButton() throws Exception {
        tapElementWithRetryIfNextElementAppears(nameChooseOwnPictureButton, nameChoosePhotoButton);
    }

    public void tapChoosePhotoButton() throws Exception {
        getElement(nameChoosePhotoButton, "Choose photo button is not visible").click();
    }

    public void waitRegistrationToFinish() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator, 40)) {
            throw new IllegalStateException("Verification page is still visible after the timeout");
        }
    }

    private static final int SELF_PICTURE_LOAD_TIMEOUT_SECONDS = 30;

    public void tapKeepThisOneButton() throws Exception {
        getElement(nameKeepThisOneButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameKeepThisOneButton,
                SELF_PICTURE_LOAD_TIMEOUT_SECONDS)) {
            log.warn(String.format("The self picture has not been loaded within %s seconds timeout",
                    SELF_PICTURE_LOAD_TIMEOUT_SECONDS));
        }
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(nameTakePhotoButton).click();
    }

    public boolean noCodeShowingUpLabelIsDisplayed() throws Exception {
        return isElementDisplayed(xpathNoCodeShowingUpLabel);
    }

    public boolean noCodeShowingUpLabelIsNotDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathNoCodeShowingUpLabel);
    }

    public boolean resendButtonIsVisible() throws Exception {
        return isElementDisplayed(nameResendCodeButton);
    }

    public boolean resendButtonIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameResendCodeButton);
    }

    public void inputPhoneNumberAndExpectNoCommit(PhoneNumber phoneNumber) throws Exception {
        selectWirestan();
        final FBElement phoneNumberField = (FBElement) getElement(fbNamePhoneNumberField);
        this.tapAtTheCenterOfElement(phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(phoneNumber.withoutPrefix());
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConfirmButton, 3)) {
            throw new IllegalStateException("Confirm button is visible, but should be hidden");
        }
    }
}
