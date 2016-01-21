package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

public class RegistrationPage extends IOSPage {
    private static final By xpathPhotoButton = By.xpath(xpathStrMainWindow + "/UIAButton[5]");

    private static final By nameConfirmImageButton = By.name("OK");

    private static final By xpathYourName =
            By.xpath(xpathStrMainWindow + "/UIATextField[@value='YOUR FULL NAME']");

    private static final By nameYourEmail = By.name("EmailField");

    private static final By nameYourPassword = By.name("PasswordField");

    private static final By xpathCreateAccountButton = By.xpath(
            "//UIASecureTextField[contains(@name, 'PasswordField')]/UIAButton");

    private static final Function<String, String> xpathStrConfirmationByMessage = msg ->
            String.format("//UIAStaticText[contains(@name, 'We sent an email to %s.')]", msg);

    private static final By xpathEmailVerifPrompt = By.xpath(xpathStrMainWindow +
            "/UIAStaticText[contains(@name, 'We sent an email to ')]");

    private static final By namePhoneNumberField = By.name("PhoneNumberField");

    private static final By xpathActivationCode = By.xpath(xpathStrMainWindow + "/UIATextField[1]");

    private static final By nameCountryPickerButton = By.name("CountryPickerButton");

    public static final By xpathCountryList = By.xpath(xpathStrMainWindow + "/UIATableView[1]");

    private static final By nameConfirmButton = By.name("RegistrationConfirmButton");

    private static final By nameAgreeButton = By.name("I AGREE");

    private static final By nameSelectPictureButton = By.name("SET A PICTURE");

    private static final By xpathVerificationPage = By.xpath(
            "//UIAStaticText[contains(@name, 'Enter the verification code we sent to')]");

    private static final By nameResendCodeButton = By.name("RESEND");

    private static final By nameInvalidCode = By.name("Please enter a valid code");

    private static final By nameChooseOwnPictureButton = By.name("ChooseOwnPictureButton");

    private static final By nameChoosePhotoButton = By.name("Choose Photo");

    private String name;
    private String email;
    private String password;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickAgreeButton() throws Exception {
        getElement(nameAgreeButton, "Agree button is not visible").click();
    }

    public boolean isCountryPickerButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCountryPickerButton, 2);
    }

    private static final String WIRE_COUNTRY_NAME = "Wirestan";

    public void selectWirestan() throws Exception {
        getElement(nameCountryPickerButton).click();
        ((IOSElement) getElement(xpathCountryList)).scrollTo(WIRE_COUNTRY_NAME).click();
    }

    public void inputPhoneNumber(String number) throws Exception {
        getElement(namePhoneNumberField).sendKeys(number);
        getElement(nameConfirmButton).click();
    }

    public boolean isVerificationCodePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathVerificationPage);
    }

    public void inputActivationCode(PhoneNumber forNumber) throws Exception {
        final WebElement activationCodeInput = getElement(xpathActivationCode, "Activation code input is not visible");
        final String code = BackendAPIWrappers.getActivationCodeByPhoneNumber(forNumber);
        activationCodeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", 2);
    }

    public void inputRandomActivationCode() throws Exception {
        inputActivationCode(new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX));
    }

    public void clickResendCodeButton() throws Exception {
        getElement(nameResendCodeButton, "Resend code button is not visible").click();
    }

    public void selectPicture() throws Exception {
        getElement(nameSelectPictureButton, "Select Picture button is not visible").click();
        getElement(xpathPhotoButton, "Take Photo button is not visible").click();
    }

    public boolean isConfirmationShown() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void confirmPicture() throws Exception {
        getElement(nameConfirmImageButton).click();
    }

    public void inputName() throws Exception {
        getElement(nameConfirmButton).click();
    }

    public void clickCreateAccountButton() throws Exception {
        getElement(xpathCreateAccountButton, "Create Account button is not visible").click();
    }

    public void typeEmail() throws Exception {
        getElement(nameYourEmail).sendKeys(getEmail());
    }

    public void typeUsername() throws Exception {
        getElement(xpathYourName, "Name input is not visible").sendKeys(getName());
    }

    public boolean isPictureSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConfirmImageButton);
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
        getElement(nameYourPassword).sendKeys(getPassword());
    }

    public void setPassword(String password) throws Exception {
        this.password = password;
        typePassword();
    }

    public boolean isEmailVerifPromptVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathEmailVerifPrompt);
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidCode);
    }

    public void clickChooseOwnPicButton() throws Exception {
        getElement(nameChooseOwnPictureButton).click();
    }

    public void clickChoosePhotoButton() throws Exception {
        getElement(nameChoosePhotoButton, "Choose photo button is not visible").click();
    }
}
