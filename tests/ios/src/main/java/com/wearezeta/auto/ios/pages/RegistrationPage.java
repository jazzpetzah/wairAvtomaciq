package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.ios.IOSElement;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;

public class RegistrationPage extends IOSPage {

    @FindBy(how = How.NAME, using = IOSLocators.nameRegistrationCameraButton)
    private WebElement cameraButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameCameraShootButton)
    private WebElement cameraShootButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathPhotoButton)
    private WebElement photoButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameSwitchCameraButton)
    private WebElement switchCameraButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameCameraFlashButton)
    private WebElement cameraFlashButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathAlbum)
    private WebElement photoAlbum;

    @FindBy(how = How.CLASS_NAME, using = IOSLocators.classNamePhotos)
    private List<WebElement> photos;

    @FindBy(how = How.NAME, using = IOSLocators.nameConfirmImageButton)
    private WebElement confirmImageButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameCancelImageButton)
    private WebElement cancelImageButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathYourName)
    private WebElement yourName;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathYourFilledName)
    private WebElement yourFilledName;

    @FindBy(how = How.NAME, using = IOSLocators.nameYourEmail)
    private WebElement yourEmail;

    @FindBy(how = How.NAME, using = IOSLocators.nameYourPassword)
    private WebElement yourPassword;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathRevealPasswordButton)
    private WebElement revealPasswordButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathHidePasswordButton)
    private WebElement hidePasswordButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathCreateAccountButton)
    private WebElement createAccountButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathConfirmationMessage)
    private WebElement confirmationText;

    @FindBy(how = How.ID, using = IOSLocators.idProvideValidEmailMessage)
    private WebElement provideValidEmailMessage;

    @FindBy(how = How.NAME, using = IOSLocators.nameBackToWelcomeButton)
    private WebElement backToWelcomeButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameForwardWelcomeButton)
    private WebElement forwardWelcomeButton;

    @FindBy(how = How.NAME, using = IOSLocators.nameKeyboardNextButton)
    private WebElement keyboardNextButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathTakePhotoSmile)
    private WebElement takePhotoSmile;

    @FindBy(how = How.NAME, using = IOSLocators.nameTakePhotoHintLabel)
    private WebElement takePhotoHintLabel;

    @FindBy(how = How.NAME, using = IOSLocators.nameVignetteOverlay)
    private WebElement vignetteLayer;

    @FindBy(how = How.NAME, using = IOSLocators.nameErrorPageButton)
    private WebElement errorPageButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathCloseColorModeButton)
    private WebElement closeColorModeButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathReSendButton)
    private WebElement reSendButton;

    @FindBy(how = How.XPATH, using = IOSLocators.xpathEmailVerifPrompt)
    private WebElement emailVerifPrompt;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathPhoneNumber)
    private WebElement phoneNumber;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.namePhoneNumberField)
    private WebElement phoneNumberField;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathActivationCode)
    private WebElement activationCode;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathCountry)
    private WebElement selectCountry;

    @FindBy(how = How.NAME, using = IOSLocators.LoginPage.nameCountryPickerButton)
    private WebElement countryPickerButton;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathCountryList)
    private WebElement countryList;

    @FindBy(how = How.XPATH, using = IOSLocators.RegistrationPage.xpathConfirmPhoneNumber)
    private WebElement confirmInput;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameAgreeButton)
    private WebElement agreeButton;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameTermOfUsePage)
    private WebElement termOfUsePage;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameSelectPictureButton)
    private WebElement selectPictureButton;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.xpathVerificationPage)
    private WebElement verificationPage;

    @FindBy(how = How.NAME, using = IOSLocators.RegistrationPage.nameResendCodeButton)
    private WebElement resendCodeButton;

    @FindBy(how = How.NAME, using = IOSLocators.Alerts.nameInvalidCode)
    private WebElement invalidCodeAlert;

    @FindBy(name = IOSLocators.RegistrationPage.nameChooseOwnPictureButton)
    private WebElement chooseOwnPictureButton;

    @FindBy(name = IOSLocators.RegistrationPage.nameChoosePhotoButton)
    private WebElement choosePhotoButton;

    @FindBy(name = IOSLocators.RegistrationPage.nameRegistrationEmailInput)
    private WebElement registrationEmailInput;

    private String name;
    private String email;
    private String password;

    private String defaultPassFieldValue = "Password";

    @SuppressWarnings("unused")
    private String confirmMessage = "We sent an email to %s. Check your Inbox and follow the link to verify your address. You won’t be able to use Wire until you do.\n\nDidn’t get the message?\n\nRe-send";

    private String[] listOfEmails;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public void clickAgreeButton() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(IOSLocators.RegistrationPage.nameAgreeButton));
        agreeButton.click();
    }

    private static final String WIRE_COUNTRY_NAME = "Wirestan";

    public void selectWirestan() throws Exception {
        countryPickerButton.click();
        ((IOSElement) getDriver().findElementByXPath(IOSLocators.RegistrationPage.xpathCountryList)).
                scrollTo(WIRE_COUNTRY_NAME);
        getDriver().findElementByName(PhoneNumber.WIRE_COUNTRY_PREFIX).click();
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
        boolean flag = DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.xpath(IOSLocators.RegistrationPage.xpathVerificationPage));
        return flag;
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
                By.name(IOSLocators.nameTakePhotoHintLabel));
    }

    public CameraRollPage selectPicture() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.name(IOSLocators.RegistrationPage.nameSelectPictureButton));
        selectPictureButton.click();
        DriverUtils.waitUntilElementClickable(getDriver(), photoButton);
        photoButton.click();
        return new CameraRollPage(this.getLazyDriver());
    }

    public boolean isConfirmationShown() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(String
                .format(IOSLocators.xpathConfirmationMessage, getEmail())));
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
        DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(IOSLocators.xpathCreateAccountButton));
        createAccountButton.click();
    }

    public void typeEmail() {
        yourEmail.sendKeys(getEmail());
    }

    public boolean typeAllInvalidEmails() {

        for (int i = 0; i < listOfEmails.length; i++) {
            yourEmail.sendKeys(listOfEmails[i] + "\n");
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

    public Boolean isBackButtonVisible() {

        return (ExpectedConditions.visibilityOf(backToWelcomeButton) != null);
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
