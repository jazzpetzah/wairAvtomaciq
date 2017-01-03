package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
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

    private static final By nameSearchField = MobileBy.AccessibilityId("Search");

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

    public static final By nameVerificationCodeInput = MobileBy.AccessibilityId("verificationField");

    private static final By nameCountryPickerButton = MobileBy.AccessibilityId("CountryPickerButton");

    public static final By nameConfirmButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By nameAgreeButton = MobileBy.AccessibilityId("I AGREE");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("Take photo");

    private static final By nameResendCodeButton = MobileBy.AccessibilityId("RESEND");

    private static final By nameInvalidCode = MobileBy.AccessibilityId("Please enter a valid code");

    private static final By nameChooseOwnPictureButton = MobileBy.AccessibilityId("ChooseOwnPictureButton");

    private static final By nameChoosePhotoButton = MobileBy.AccessibilityId("Choose Photo");

    private static final By nameKeepThisOneButton = MobileBy.AccessibilityId("KeepDefaultPictureButton");

    private static final By xpathNoCodeShowingUpLabel = By.
            xpath("//XCUIElementTypeStaticText[contains(@name, 'NO CODE SHOWING UP?')]");

    private static final By nameSearchResultsTable = MobileBy.AccessibilityId("Search results");

    private static final Function<String, String> xpathStrSearchResultByPrefix = prefix ->
            String.format("//XCUIElementTypeStaticText[starts-with(@name, '%s')]", prefix);

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
        if (!isLocatorInvisible(nameCountryPickerButton, Timedelta.fromSeconds(5))) {
            countryPickerBtn.click();
        }
        final WebElement searchInput = getElement(nameSearchField);
        searchInput.click();
        searchInput.sendKeys(WIRE_COUNTRY_NAME_PREFIX);
        // Wait for animation
        Thread.sleep(2000);
        final WebElement searchResults = getElement(nameSearchResultsTable);
        final By searchResultLocator = By.xpath(xpathStrSearchResultByPrefix.apply(WIRE_COUNTRY_NAME_PREFIX));
        searchResults.findElement(searchResultLocator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public void inputPhoneNumber(PhoneNumber number) throws Exception {
        selectWirestan();
        final FBElement phoneNumberField = (FBElement) getElement(fbNamePhoneNumberField);
        this.tapAtTheCenterOfElement(phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(number.withoutPrefix());
        getElement(nameConfirmButton).click();
        if (!isLocatorInvisible(nameConfirmButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public void inputActivationCode(PhoneNumber forNumber) throws Exception {
        final WebElement codeInput = getElement(nameVerificationCodeInput, "Activation code input is not visible");
        final String code = BackendAPIWrappers.getActivationCodeByPhoneNumber(forNumber);
        codeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", Timedelta.fromSeconds(2)).click();
    }

    private static final Random rand = new Random();

    public void inputRandomConfirmationCode() throws Exception {
        getElement(nameVerificationCodeInput).sendKeys(Integer.toString(100000 + rand.nextInt(900000)));
        getElement(nameConfirmButton, "Confirm button is not visible", Timedelta.fromSeconds(2)).click();
    }

    public void clickResendCodeButton() throws Exception {
        getElement(nameResendCodeButton, "Resend code button is not visible").click();
    }

    public boolean isConfirmationShown() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        return isLocatorDisplayed(locator);
    }

    public void commitName() throws Exception {
        getElement(nameConfirmButton).click();
        if (!isLocatorInvisible(nameConfirmButton)) {
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
        return isLocatorDisplayed(xpathEmailVerifPrompt);
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        return isLocatorDisplayed(nameInvalidCode);
    }

    public void tapChooseOwnPicButton() throws Exception {
        tapElementWithRetryIfNextElementAppears(nameChooseOwnPictureButton, nameChoosePhotoButton);
    }

    public void tapChoosePhotoButton() throws Exception {
        getElement(nameChoosePhotoButton, "Choose photo button is not visible").click();
    }

    public void waitRegistrationToFinish() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        if (!isLocatorInvisible(locator, Timedelta.fromSeconds(40))) {
            throw new IllegalStateException("Verification page is still visible after the timeout");
        }
    }

    private static final Timedelta SELF_PICTURE_LOAD_TIMEOUT = Timedelta.fromSeconds(30);

    public void tapKeepThisOneButton() throws Exception {
        getElement(nameKeepThisOneButton).click();
        if (!isLocatorInvisible(nameKeepThisOneButton, SELF_PICTURE_LOAD_TIMEOUT)) {
            log.warn(String.format("The self picture has not been loaded within %s timeout",
                    SELF_PICTURE_LOAD_TIMEOUT.toString()));
        }
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(nameTakePhotoButton).click();
    }

    public boolean noCodeShowingUpLabelIsDisplayed() throws Exception {
        return isLocatorDisplayed(xpathNoCodeShowingUpLabel);
    }

    public boolean noCodeShowingUpLabelIsNotDisplayed() throws Exception {
        return isLocatorInvisible(xpathNoCodeShowingUpLabel);
    }

    public boolean resendButtonIsVisible() throws Exception {
        return isLocatorDisplayed(nameResendCodeButton);
    }

    public boolean resendButtonIsNotVisible() throws Exception {
        return isLocatorInvisible(nameResendCodeButton);
    }

    public void inputPhoneNumberAndExpectNoCommit(PhoneNumber phoneNumber) throws Exception {
        selectWirestan();
        final FBElement phoneNumberField = (FBElement) getElement(fbNamePhoneNumberField);
        this.tapAtTheCenterOfElement(phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(phoneNumber.withoutPrefix());
        if (isLocatorDisplayed(nameConfirmButton, Timedelta.fromSeconds(3))) {
            throw new IllegalStateException("Confirm button is visible, but should be hidden");
        }
    }
}
