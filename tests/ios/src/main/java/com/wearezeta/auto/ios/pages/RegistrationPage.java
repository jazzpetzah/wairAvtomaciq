package com.wearezeta.auto.ios.pages;

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

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;

public class RegistrationPage extends IOSPage {
    public static final String nameRegistrationCameraButton = "CameraButton";
    @FindBy(name = nameRegistrationCameraButton)
    private WebElement cameraButton;

    public static final String nameCameraShootButton = "CameraShootButton";
    @FindBy(name = nameCameraShootButton)
    private WebElement cameraShootButton;

    public static final String xpathPhotoButton = xpathMainWindow + "/UIAButton[5]";
    @FindBy(xpath = xpathPhotoButton)
    private WebElement photoButton;

    public static final String nameSwitchCameraButton = "CameraSwitchButton";
    @FindBy(name = nameSwitchCameraButton)
    private WebElement switchCameraButton;

    public static final String nameCameraFlashButton = "CameraFlashButton";
    @FindBy(name = nameCameraFlashButton)
    private WebElement cameraFlashButton;

    public static final String xpathAlbum = xpathMainWindow + "/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]";
    @FindBy(xpath = xpathAlbum)
    private WebElement photoAlbum;

    public static final String classNamePhotos = "UIACollectionCell";
    @FindBy(className = classNamePhotos)
    private List<WebElement> photos;

    public static final String nameConfirmImageButton = "OK";
    @FindBy(name = nameConfirmImageButton)
    private WebElement confirmImageButton;

    public static final String nameCancelImageButton = "CANCEL";
    @FindBy(name = nameCancelImageButton)
    private WebElement cancelImageButton;

    public static final String xpathYourName = xpathMainWindow + "/UIATextField[@value='YOUR FULL NAME']";
    @FindBy(xpath = xpathYourName)
    private WebElement yourName;

    public static final String xpathYourFilledName =
            "//UIATextField[preceding-sibling::UIAStaticText[@value='What should we call you?' or @value='Create an account']]";
    @FindBy(xpath = xpathYourFilledName)
    private WebElement yourFilledName;

    public static final String nameYourEmail = "EmailField";
    @FindBy(name = nameYourEmail)
    private WebElement yourEmail;

    public static final String nameYourPassword = "PasswordField";
    @FindBy(name = nameYourPassword)
    private WebElement yourPassword;

    public static final String xpathRevealPasswordButton =
            "//UIAApplication[1]/UIAWindow[1]/UIASecureTextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathRevealPasswordButton)
    private WebElement revealPasswordButton;

    public static final String xpathHidePasswordButton =
            "//UIAApplication[1]/UIAWindow[1]/UIATextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathHidePasswordButton)
    private WebElement hidePasswordButton;

    public static final String xpathCreateAccountButton =
            "//UIASecureTextField[contains(@name, 'PasswordField')]/UIAButton";
    @FindBy(xpath = xpathCreateAccountButton)
    private WebElement createAccountButton;

    public static final String xpathConfirmationMessage =
            "//UIAStaticText[contains(@name, 'We sent an email to %s.')]";
    @FindBy(xpath = xpathConfirmationMessage)
    private WebElement confirmationText;

    public static final String idProvideValidEmailMessage = "PLEASE ENTER A VALID EMAIL ADDRESS";
    @FindBy(id = idProvideValidEmailMessage)
    private WebElement provideValidEmailMessage;

    public static final String nameBackToWelcomeButton = "BackToWelcomeButton";
    @FindBy(name = nameBackToWelcomeButton)
    private WebElement backToWelcomeButton;

    public static final String nameForwardWelcomeButton = "ForwardWelcomeButton";
    @FindBy(name = nameForwardWelcomeButton)
    private WebElement forwardWelcomeButton;

    public static final String nameKeyboardNextButton = "Next";
    @FindBy(name = nameKeyboardNextButton)
    private WebElement keyboardNextButton;

    public static final String xpathTakePhotoSmile = "//UIAApplication[1]/UIAWindow[1]/UIAImage[1]";
    @FindBy(xpath = xpathTakePhotoSmile)
    private WebElement takePhotoSmile;

    public static final String nameTakePhotoHintLabel = "CHOOSE A PICTURE  AND PICK A COLOR";
    @FindBy(name = nameTakePhotoHintLabel)
    private WebElement takePhotoHintLabel;

    public static final String nameVignetteOverlay = "••••";
    @FindBy(name = nameVignetteOverlay)
    private WebElement vignetteLayer;

    public static final String nameErrorPageButton = "BACK";
    @FindBy(name = nameErrorPageButton)
    private WebElement errorPageButton;

    public static final String xpathCloseColorModeButton = xpathMainWindow + "/UIAButton[4]";
    @FindBy(xpath = xpathCloseColorModeButton)
    private WebElement closeColorModeButton;

    public static final String xpathReSendButton = xpathMainWindow + "/UIATextView[1]";
    @FindBy(xpath = xpathReSendButton)
    private WebElement reSendButton;

    public static final String xpathEmailVerifPrompt = xpathMainWindow +
            "/UIAStaticText[contains(@name, 'We sent an email to ')]";
    @FindBy(xpath = xpathEmailVerifPrompt)
    private WebElement emailVerifPrompt;

    public static final String xpathPhoneNumber = xpathMainWindow + "/UIATextField[1]";
    @FindBy(xpath = xpathPhoneNumber)
    private WebElement phoneNumber;

    public static final String namePhoneNumberField = "PhoneNumberField";
    @FindBy(name = namePhoneNumberField)
    private WebElement phoneNumberField;

    public static final String xpathActivationCode = xpathMainWindow + "/UIATextField[1]";
    @FindBy(xpath = xpathActivationCode)
    private WebElement activationCode;

    public static final String xpathCountry = xpathMainWindow + "/UIAButton[1]";
    @FindBy(xpath = xpathCountry)
    private WebElement selectCountry;

    public static final String nameCountryPickerButton = "CountryPickerButton";
    @FindBy(name = nameCountryPickerButton)
    private WebElement countryPickerButton;

    public static final String xpathCountryList = xpathMainWindow + "/UIATableView[1]";
    @FindBy(xpath = xpathCountryList)
    private WebElement countryList;

    public static final String xpathConfirmPhoneNumber = xpathMainWindow + "/UIATextField[1]/UIAButton[1]";
    @FindBy(xpath = xpathConfirmPhoneNumber)
    private WebElement confirmInput;

    public static final String nameAgreeButton = "I AGREE";
    @FindBy(name = nameAgreeButton)
    private WebElement agreeButton;

    public static final String nameTermOfUsePage = "By continuing you agree to the Wire Terms of Use.";
    @FindBy(name = nameTermOfUsePage)
    private WebElement termOfUsePage;

    public static final String nameSelectPictureButton = "SET A PICTURE";
    @FindBy(name = nameSelectPictureButton)
    private WebElement selectPictureButton;

    public static final String xpathVerificationPage =
            "//UIAStaticText[contains(@name, 'Enter the verification code we sent to')]";
    @FindBy(xpath = xpathVerificationPage)
    private WebElement verificationPage;

    public static final String nameResendCodeButton = "RESEND";
    @FindBy(name = nameResendCodeButton)
    private WebElement resendCodeButton;

    public static final String nameInvalidCode = "Please enter a valid code";
    @FindBy(name = nameInvalidCode)
    private WebElement invalidCodeAlert;

    public static final String nameChooseOwnPictureButton = "ChooseOwnPictureButton";
    @FindBy(name = nameChooseOwnPictureButton)
    private WebElement chooseOwnPictureButton;

    public static final String nameChoosePhotoButton = "Choose Photo";
    @FindBy(name = nameChoosePhotoButton)
    private WebElement choosePhotoButton;

    public static final String nameRegistrationEmailInput = "RegistrationEmailField";
    @FindBy(name = nameRegistrationEmailInput)
    private WebElement registrationEmailInput;

    private String name;
    private String email;
    private String password;

    private String defaultPassFieldValue = "Password";

    private String[] listOfEmails;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickAgreeButton() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameAgreeButton));
        agreeButton.click();
    }

    private static final String WIRE_COUNTRY_NAME = "Wirestan";

    public void selectWirestan() throws Exception {
        countryPickerButton.click();
        ((IOSElement) getDriver().findElementByXPath(xpathCountryList)).
                scrollTo(WIRE_COUNTRY_NAME).click();
    }

    public void inputPhoneNumber(String number) throws Exception {
        getWait().until(ExpectedConditions.elementToBeClickable(phoneNumber));
        try {
            phoneNumberField.sendKeys(number);
        } catch (WebDriverException ex) {
            phoneNumberField.clear();
            phoneNumberField.sendKeys(number);
        }
        confirmInput.click();
    }

    public boolean isVerificationCodePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathVerificationPage));
    }

    public void inputActivationCode(String code) throws Exception {
        getWait()
                .until(ExpectedConditions.elementToBeClickable(activationCode));
        activationCode.sendKeys(code);
        confirmInput.click();
    }

    public void inputRandomActivationCode() throws Exception {
        inputActivationCode(CommonUtils.generateRandomXdigits(6));
    }

    public void clickResendCodeButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), resendCodeButton);
        resendCodeButton.click();
    }

    public boolean isTakePhotoSmileDisplayed() {
        return takePhotoSmile.isEnabled();
    }

    public boolean isTakeOrSelectPhotoLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(nameTakePhotoHintLabel));
    }

    public void selectPicture() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(nameSelectPictureButton));
        selectPictureButton.click();
        DriverUtils.waitUntilElementClickable(getDriver(), photoButton);
        photoButton.click();
    }

    public boolean isConfirmationShown() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(String
                .format(xpathConfirmationMessage, getEmail())));
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

    public void clearEmailInput() throws Exception {
        registrationEmailInput.clear();
    }

    public void inputPassword() {
        yourPassword.sendKeys("\n");
    }

    public void clickCreateAccountButton() throws Exception {
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.xpath(xpathCreateAccountButton));
        createAccountButton.click();
    }

    public void typeEmail() {
        yourEmail.sendKeys(getEmail());
    }

    public boolean typeAllInvalidEmails() {
        for (String email : listOfEmails) {
            yourEmail.sendKeys(email + "\n");
            if (!provideValidEmailMessage.isDisplayed()) {
                return false;
            }
            yourEmail.clear();
        }
        return true;
    }

    public boolean isCreateAccountEnabled() {
        return createAccountButton.isEnabled();
    }

    // this test skips photo verification
    public void verifyUserInputIsPresent(String name, String email)
            throws Exception {
        new LoginPage(this.getLazyDriver()).clickJoinButton();
        forwardWelcomeButton.click(); // skip photo
        Assert.assertEquals("Name is not same as previously entered.", name,
                yourName.getText());
        forwardWelcomeButton.click();
        Assert.assertEquals("Email is not same as previously entered.", email,
                yourEmail.getText());
        forwardWelcomeButton.click();
        Assert.assertEquals("Preciously entered email shouln't be shown",
                defaultPassFieldValue, yourPassword.getText());
    }

    public void typeUsername() throws Exception {
        this.getWait().until(ExpectedConditions.elementToBeClickable(yourName));
        try {
            yourName.sendKeys(getName());
        } catch (WebDriverException ex) {
            yourName.clear();
            yourName.sendKeys(getName());
        }
    }

    public String getUsernameFieldValue() {
        return yourFilledName.getText();
    }

    public String getEmailFieldValue() {
        return yourEmail.getText();
    }

    public boolean isPictureSelected() {
        return confirmImageButton.isDisplayed();
    }

    public boolean confirmErrorPage() {
        return errorPageButton.isDisplayed();
    }

    public void backToEmailPageFromErrorPage() {
        backToWelcomeButton.click();
        backToWelcomeButton.click();
    }

    public void navigateToWelcomePage() {
        while (backToWelcomeButton.isDisplayed()) {
            backToWelcomeButton.click();
        }
    }

    public boolean isNextButtonPresented() {
        return forwardWelcomeButton.isDisplayed();
    }

    public void tapBackButton() {
        backToWelcomeButton.click();
    }

    public Boolean isBackButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameBackToWelcomeButton));
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

    public void setListOfEmails(String[] list) {
        this.listOfEmails = list;
    }

    public void reSendEmail() throws Exception {
        Point p = reSendButton.getLocation();
        Dimension k = reSendButton.getSize();
        this.getDriver().tap(1, (p.x) + (k.width / 2), (p.y) + (k.height - 5),
                1);
    }

    public void waitUntilEmailsCountReachesExpectedValue(int expectedMsgsCount,
                                                         String recipient, int timeoutSeconds) throws Exception {
        IMAPSMailbox mailbox = IMAPSMailbox.getInstance();
        mailbox.waitUntilMessagesCountReaches(recipient, expectedMsgsCount,
                timeoutSeconds);
    }

    public boolean isEmailVerifPromptVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                emailVerifPrompt);
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        DriverUtils.waitUntilAlertAppears(getDriver());
        return DriverUtils.isElementPresentAndDisplayed(getDriver(),
                invalidCodeAlert);
    }

    public void clickChooseOwnPicButton() {
        chooseOwnPictureButton.click();
    }

    public void clickChoosePhotoButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), choosePhotoButton);
        choosePhotoButton.click();
    }
}
