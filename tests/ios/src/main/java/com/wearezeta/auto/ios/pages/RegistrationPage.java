package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class RegistrationPage extends IOSPage {
    private static final String nameRegistrationCameraButton = "CameraButton";
    @FindBy(name = nameRegistrationCameraButton)
    private WebElement cameraButton;

    private static final String nameCameraShootButton = "CameraShootButton";
    @FindBy(name = nameCameraShootButton)
    private WebElement cameraShootButton;

    private static final String xpathPhotoButton = xpathStrMainWindow + "/UIAButton[5]";
    @FindBy(xpath = xpathPhotoButton)
    private WebElement photoButton;

    private static final String nameSwitchCameraButton = "CameraSwitchButton";
    @FindBy(name = nameSwitchCameraButton)
    private WebElement switchCameraButton;

    private static final String nameCameraFlashButton = "CameraFlashButton";
    @FindBy(name = nameCameraFlashButton)
    private WebElement cameraFlashButton;

    private static final String xpathAlbum = xpathStrMainWindow + "/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]";
    @FindBy(xpath = xpathAlbum)
    private WebElement photoAlbum;

    private static final String classNamePhotos = "UIACollectionCell";
    @FindBy(className = classNamePhotos)
    private List<WebElement> photos;

    private static final String nameConfirmImageButton = "OK";
    @FindBy(name = nameConfirmImageButton)
    private WebElement confirmImageButton;

    private static final String nameCancelImageButton = "CANCEL";
    @FindBy(name = nameCancelImageButton)
    private WebElement cancelImageButton;

    private static final String xpathYourName = xpathStrMainWindow + "/UIATextField[@value='YOUR FULL NAME']";
    @FindBy(xpath = xpathYourName)
    private WebElement yourName;

    public static final String xpathYourFilledName =
            "//UIATextField[preceding-sibling::UIAStaticText[@value='What should we call you?' or @value='Create an account']]";
    @FindBy(xpath = xpathYourFilledName)
    private WebElement yourFilledName;

    private static final String nameYourEmail = "EmailField";
    @FindBy(name = nameYourEmail)
    private WebElement yourEmail;

    private static final String nameYourPassword = "PasswordField";
    @FindBy(name = nameYourPassword)
    private WebElement yourPassword;

    public static final String xpathRevealPasswordButton =
            "//UIAApplication[1]/UIAWindow[1]/UIASecureTextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathRevealPasswordButton)
    private WebElement revealPasswordButton;

    private static final String xpathHidePasswordButton =
            "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathHidePasswordButton)
    private WebElement hidePasswordButton;

    private static final String xpathCreateAccountButton =
            "//UIASecureTextField[contains(@name, 'PasswordField')]/UIAButton";
    @FindBy(xpath = xpathCreateAccountButton)
    private WebElement createAccountButton;

    private static final Function<String, String> xpathConfirmationByMessage = msg ->
            String.format("//UIAStaticText[contains(@name, 'We sent an email to %s.')]", msg);

    private static final String idProvideValidEmailMessage = "PLEASE ENTER A VALID EMAIL ADDRESS";
    @FindBy(id = idProvideValidEmailMessage)
    private WebElement provideValidEmailMessage;

    private static final String nameBackToWelcomeButton = "BackToWelcomeButton";
    @FindBy(name = nameBackToWelcomeButton)
    private WebElement backToWelcomeButton;

    private static final String nameForwardWelcomeButton = "ForwardWelcomeButton";
    @FindBy(name = nameForwardWelcomeButton)
    private WebElement forwardWelcomeButton;

    private static final String nameKeyboardNextButton = "Next";
    @FindBy(name = nameKeyboardNextButton)
    private WebElement keyboardNextButton;

    private static final String xpathTakePhotoSmile = "//UIAApplication[1]/UIAWindow[1]/UIAImage[1]";
    @FindBy(xpath = xpathTakePhotoSmile)
    private WebElement takePhotoSmile;

    public static final String nameTakePhotoHintLabel = "CHOOSE A PICTURE  AND PICK A COLOR";
    @FindBy(name = nameTakePhotoHintLabel)
    private WebElement takePhotoHintLabel;

    public static final String nameVignetteOverlay = "••••";
    @FindBy(name = nameVignetteOverlay)
    private WebElement vignetteLayer;

    private static final String nameErrorPageButton = "BACK";
    @FindBy(name = nameErrorPageButton)
    private WebElement errorPageButton;

    private static final String xpathCloseColorModeButton = xpathStrMainWindow + "/UIAButton[4]";
    @FindBy(xpath = xpathCloseColorModeButton)
    private WebElement closeColorModeButton;

    private static final String xpathReSendButton = xpathStrMainWindow + "/UIATextView[1]";
    @FindBy(xpath = xpathReSendButton)
    private WebElement reSendButton;

    private static final String xpathEmailVerifPrompt = xpathStrMainWindow +
            "/UIAStaticText[contains(@name, 'We sent an email to ')]";
    @FindBy(xpath = xpathEmailVerifPrompt)
    private WebElement emailVerifPrompt;

    private static final String xpathPhoneNumber = xpathStrMainWindow + "/UIATextField[1]";
    @FindBy(xpath = xpathPhoneNumber)
    private WebElement phoneNumber;

    private static final String namePhoneNumberField = "PhoneNumberField";
    @FindBy(name = namePhoneNumberField)
    private WebElement phoneNumberField;

    private static final String xpathActivationCode = xpathStrMainWindow + "/UIATextField[1]";
    @FindBy(xpath = xpathActivationCode)
    private WebElement activationCode;

    private static final String xpathCountry = xpathStrMainWindow + "/UIAButton[1]";
    @FindBy(xpath = xpathCountry)
    private WebElement selectCountry;

    private static final String nameCountryPickerButton = "CountryPickerButton";
    @FindBy(name = nameCountryPickerButton)
    private WebElement countryPickerButton;

    private static final By xpathCountryList = By.xpath(xpathStrMainWindow + "/UIATableView[1]");

    private static final String xpathConfirmPhoneNumber = xpathStrMainWindow + "/UIATextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathConfirmPhoneNumber)
    private WebElement confirmInput;

    private static final String nameAgreeButton = "I AGREE";
    @FindBy(name = nameAgreeButton)
    private WebElement agreeButton;

    private static final String nameTermOfUsePage = "By continuing you agree to the Wire Terms of Use.";
    @FindBy(name = nameTermOfUsePage)
    private WebElement termOfUsePage;

    private static final String nameSelectPictureButton = "SET A PICTURE";
    @FindBy(name = nameSelectPictureButton)
    private WebElement selectPictureButton;

    private static final String xpathVerificationPage =
            "//UIAStaticText[contains(@name, 'Enter the verification code we sent to')]";
    @FindBy(xpath = xpathVerificationPage)
    private WebElement verificationPage;

    private static final String nameResendCodeButton = "RESEND";
    @FindBy(name = nameResendCodeButton)
    private WebElement resendCodeButton;

    private static final String nameInvalidCode = "Please enter a valid code";
    @FindBy(name = nameInvalidCode)
    private WebElement invalidCodeAlert;

    private static final String nameChooseOwnPictureButton = "ChooseOwnPictureButton";
    @FindBy(name = nameChooseOwnPictureButton)
    private WebElement chooseOwnPictureButton;

    private static final String nameChoosePhotoButton = "Choose Photo";
    @FindBy(name = nameChoosePhotoButton)
    private WebElement choosePhotoButton;

    private static final String nameRegistrationEmailInput = "RegistrationEmailField";
    @FindBy(name = nameRegistrationEmailInput)
    private WebElement registrationEmailInput;

    private String name;
    private String email;
    private String password;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickAgreeButton() throws Exception {
        getElement(By.name(nameAgreeButton), "Agree button is not visible").click();
    }

    public boolean isCountryPickerButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameCountryPickerButton), 2);
    }

    private static final String WIRE_COUNTRY_NAME = "Wirestan";

    public void selectWirestan() throws Exception{
        countryPickerButton.click();
        ((IOSElement) getElement(xpathCountryList)).
                scrollTo(WIRE_COUNTRY_NAME).click();
    }

    public void inputPhoneNumber(String number) throws Exception {
        phoneNumberField.sendKeys(number);
        confirmInput.click();
    }

    public boolean isVerificationCodePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathVerificationPage));
    }

    public void inputActivationCode(String code) throws Exception {
        getElement(By.xpath(xpathActivationCode), "Activation code input is not visible").sendKeys(code);
        confirmInput.click();
    }

    public void inputRandomActivationCode() throws Exception {
        inputActivationCode(CommonUtils.generateRandomXdigits(6));
    }

    public void clickResendCodeButton() throws Exception {
        getElement(By.name(nameResendCodeButton), "Resend code button is not visible").click();
    }

    public void selectPicture() throws Exception {
        getElement(By.name(nameSelectPictureButton), "Select Picture button is not visible").click();
        getElement(By.xpath(xpathPhotoButton), "Take Photo button is not visible").click();
    }

    public boolean isConfirmationShown() throws Exception {
        final By locator = By.xpath(xpathConfirmationByMessage.apply(getEmail()));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void confirmPicture() {
        confirmImageButton.click();
    }

    public void hideKeyboard() throws Exception {
        this.getDriver().hideKeyboard();
    }

    public void inputName() {
        confirmInput.click();
    }

    public void clickCreateAccountButton() throws Exception {
        getElement(By.xpath(xpathCreateAccountButton), "Create Account button is not visible").click();
    }

    public void typeEmail() {
        yourEmail.sendKeys(getEmail());
    }

    public void typeUsername() throws Exception {
        getElement(By.xpath(xpathYourName), "Name input is not visible").sendKeys(getName());
    }

    public boolean isPictureSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameConfirmImageButton));
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

    public void setEmail(String email) {
        this.email = email;
        typeEmail();
        this.email = email.replace('\n', ' ').trim();
    }

    public String getPassword() {
        return password;
    }

    private void typePassword() {
        yourPassword.sendKeys(getPassword());
    }

    public void setPassword(String password) {
        this.password = password;
        typePassword();

    }

    public boolean isEmailVerifPromptVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathEmailVerifPrompt));
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameInvalidCode));
    }

    public void clickChooseOwnPicButton() {
        chooseOwnPictureButton.click();
    }

    public void clickChoosePhotoButton() throws Exception {
        getElement(By.name(nameChoosePhotoButton), "Choose photo button is not visible").click();
    }
}
