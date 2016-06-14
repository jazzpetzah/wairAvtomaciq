package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.function.Function;

public class RegistrationPage extends IOSPage {

    private static final By xpathYourName = By.xpath(xpathStrMainWindow + "/UIATextField[@value='YOUR FULL NAME']");

    private static final By xpathNameField = By.xpath(xpathStrMainWindow + "/UIATextField[1]");

    private static final By nameYourEmail = MobileBy.AccessibilityId("EmailField");

    private static final By nameYourPassword = MobileBy.AccessibilityId("PasswordField");

    private static final By xpathCreateAccountButton = By
            .xpath("//UIASecureTextField[contains(@name, 'PasswordField')]/UIAButton");

    private static final Function<String, String> xpathStrConfirmationByMessage = msg -> String.format(
            "//UIAStaticText[contains(@name, 'We sent an email to %s')]", msg);

    private static final By xpathEmailVerifPrompt =
            By.xpath("//UIAStaticText[contains(@name, 'We sent an email to ')]");

    private static final By namePhoneNumberField = MobileBy.AccessibilityId("PhoneNumberField");

    private static final By xpathActivationCode = By.xpath(xpathStrMainWindow + "//UIATextField");

    private static final By nameCountryPickerButton = MobileBy.AccessibilityId("CountryPickerButton");

    public static final By xpathCountryList = By.xpath(xpathStrMainWindow + "/UIATableView[1]");

    private static final By nameConfirmButton = MobileBy.AccessibilityId("RegistrationConfirmButton");

    private static final By nameAgreeButton = MobileBy.AccessibilityId("I AGREE");

    private static final By nameTakePhotoButton = MobileBy.AccessibilityId("Take photo");

    private static final By xpathVerificationPage = By
            .xpath("//UIAStaticText[contains(@name, 'Enter the verification code we sent to')]");

    private static final By nameResendCodeButton = MobileBy.AccessibilityId("RESEND");

    private static final By nameInvalidCode = MobileBy.AccessibilityId("Please enter a valid code");

    private static final By nameChooseOwnPictureButton = MobileBy.AccessibilityId("ChooseOwnPictureButton");

    private static final By nameChoosePhotoButton = MobileBy.AccessibilityId("Choose Photo");

    private static final By nameKeepThisOneButton = MobileBy.AccessibilityId("KeepDefaultPictureButton");

    private static final By xpathNoCodeShowingUpLabel = By.
            xpath("//UIAStaticText[contains(@name, 'NO CODE SHOWING UP?')]");

    private String name;
    private String email;
    private String password;

    public RegistrationPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickAgreeButton() throws Exception {
        clickElementWithRetryIfStillDisplayed(nameAgreeButton);
    }

    public boolean isCountryPickerButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCountryPickerButton, 2);
    }

    public boolean isCountryPickerButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCountryPickerButton, 2);
    }

    private static final String WIRE_COUNTRY_NAME = "Wirestan";

    private void selectWirestan() throws Exception {
        final WebElement countryPickerBtn = getElement(nameCountryPickerButton);
        countryPickerBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCountryPickerButton, 5)) {
            countryPickerBtn.click();
        }
        ((IOSElement) getElement(xpathCountryList)).scrollTo(WIRE_COUNTRY_NAME).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public void inputPhoneNumber(PhoneNumber number) throws Exception {
        selectWirestan();
        final WebElement phoneNumberField = getElement(namePhoneNumberField);
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(number.withoutPrefix());
        getElement(nameConfirmButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConfirmButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public boolean isVerificationCodePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathVerificationPage);
    }

    public void inputActivationCode(PhoneNumber forNumber) throws Exception {
        final WebElement activationCodeInput = getElement(xpathActivationCode, "Activation code input is not visible");
        final String code = BackendAPIWrappers.getActivationCodeByPhoneNumber(forNumber);
        activationCodeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", 2).click();
    }

    public void inputLoginCode(PhoneNumber forNumber) throws Exception {
        final WebElement activationCodeInput = getElement(xpathActivationCode, "Login code input is not visible");
        final String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(forNumber);
        activationCodeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", 2).click();
    }

    public void inputActivationCode(String code) throws Exception {
        final WebElement activationCodeInput = getElement(xpathActivationCode, "Activation code input is not visible");
        activationCodeInput.sendKeys(code);
        getElement(nameConfirmButton, "Confirm button is not visible", 2).click();
    }

    private static final Random rand = new Random();

    public void inputRandomActivationCode() throws Exception {
        inputActivationCode(Integer.toString(100000 + rand.nextInt(900000)));
    }

    public void clickResendCodeButton() throws Exception {
        getElement(nameResendCodeButton, "Resend code button is not visible").click();
    }

    public boolean isConfirmationShown() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void inputName() throws Exception {
        getElement(nameConfirmButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConfirmButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public void clickCreateAccountButton() throws Exception {
        getElement(xpathCreateAccountButton, "Create Account button is not visible").click();
    }

    public void typeEmail() throws Exception {
        try {
            ((IOSElement) getElement(nameYourEmail)).setValue(getEmail());
        } catch (Exception e) {
            getElement(nameYourEmail).sendKeys(getEmail());
        }
    }

    public void typeUsername() throws Exception {
        getElement(xpathYourName, "Name input is not visible").sendKeys(getName());
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
        try {
            ((IOSElement) getElement(nameYourPassword)).setValue(getPassword());
        } catch (Exception e) {
            getElement(nameYourPassword).sendKeys(getPassword());
        }
    }

    public void setPassword(String password) throws Exception {
        this.password = password;
        typePassword();
    }

    public boolean isEmailVerificationPromptVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathEmailVerifPrompt);
    }

    public boolean isInvalidCodeAlertShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameInvalidCode);
    }

    public void clickChooseOwnPicButton() throws Exception {
        clickElementWithRetryIfNextElementAppears(nameChooseOwnPictureButton, nameChoosePhotoButton);
    }

    public void clickChoosePhotoButton() throws Exception {
        getElement(nameChoosePhotoButton, "Choose photo button is not visible").click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameChoosePhotoButton)) {
            throw new IllegalStateException("Confirm button is still visible");
        }
    }

    public void waitRegistrationToFinish() throws Exception {
        final By locator = By.xpath(xpathStrConfirmationByMessage.apply(getEmail()));
        if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator, 40)) {
            throw new IllegalStateException("Verification page is still visible after the timeout");
        }
        instantiatePage(FirstTimeOverlay.class).acceptIfVisible(2);
    }

    public void clickKeepThisOneButton() throws Exception {
        getElement(nameKeepThisOneButton).click();
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(nameTakePhotoButton).click();
    }

    /**
     * Send extra space keys (workaround for simulator bug)
     *
     * @throws Exception
     */
    public void tapNameInputField() throws Exception {
        getElement(xpathNameField).sendKeys("\n\n");
    }

    public boolean noCodeShowingUpLabelIsDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathNoCodeShowingUpLabel);
    }

    public boolean noCodeShowingUpLabelIsNotDisplayed() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathNoCodeShowingUpLabel);
    }

    public boolean resendButtonIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameResendCodeButton);
    }

    public boolean resendButtonIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameResendCodeButton);
    }

    public void inputPhoneNumberAndExpectNoCommit(PhoneNumber phoneNumber) throws Exception {
        selectWirestan();
        final WebElement phoneNumberField = getElement(namePhoneNumberField);
        DriverUtils.tapInTheCenterOfTheElement(getDriver(), phoneNumberField);
        Thread.sleep(2000);
        phoneNumberField.sendKeys(phoneNumber.withoutPrefix());
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameConfirmButton, 3)) {
            throw new IllegalStateException("Confirm button is visible, but should be hidden");
        }
    }
}
