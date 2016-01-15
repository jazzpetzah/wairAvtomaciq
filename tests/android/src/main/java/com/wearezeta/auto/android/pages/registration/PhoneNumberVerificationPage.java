package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * The page that appears after inputing a phone number. It will ask for either
 * the activation code, or the login code, depending on whether the phone number
 * is new or has been used before.
 *
 * @author deancook
 */

public class PhoneNumberVerificationPage extends AndroidPage {

    public static final String idCodeOldInput = "et__reg__phone";
    @FindBy(id = idCodeOldInput)
    private WebElement codeOldInput;

    public static final String idCodeInput = "et__reg__code";
    @FindBy(id = idCodeInput)
    private WebElement codeInput;

    public static final String idConfirmButton = "pcb__activate";
    @FindBy(id = idConfirmButton)
    private WebElement confirmButton;

    public static final String idOkButton = "button3";
    @FindBy(id = idOkButton)
    private WebElement okButton;

    public static final String idEditPhoneButton = "ll__activation_button__back";
    @FindBy(id = idEditPhoneButton)
    private WebElement editPhoneButton;

    public static final String idErrorAlertHeader = "alertTitle";
    @FindBy(id = idErrorAlertHeader)
    private WebElement errorAlertHeader;

    public static final String idErrorAlertMessage = "message";
    @FindBy(id = idErrorAlertMessage)
    private WebElement errorAlertMessage;

    public PhoneNumberVerificationPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void inputVerificationCode(String verificationCode) throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCodeInput), 15)) {
            codeInput.sendKeys(verificationCode);
        } else if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCodeOldInput), 15)) {
            codeOldInput.sendKeys(verificationCode);
        } else {
            verifyLocatorPresence(By.id(idCodeInput), "Verification code input has not been shown in time", 15);
        }
    }

    public AddNamePage clickConfirm() throws Exception {
        confirmButton.click();
        return new AddNamePage(this.getLazyDriver());
    }

    public boolean waitUntilConfirmButtonDissapears() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idConfirmButton), 40);
    }

    public boolean isIncorrectCodeErrorNotAppears() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idOkButton));
    }

    public String getErrorAlertHeader() {
        return errorAlertHeader.getText();
    }

    public String getErrorAlertMessage() {
        return errorAlertMessage.getText();
    }
}
