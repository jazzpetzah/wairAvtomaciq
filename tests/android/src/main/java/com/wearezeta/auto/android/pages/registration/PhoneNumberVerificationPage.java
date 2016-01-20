package com.wearezeta.auto.android.pages.registration;

import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    private static final By idCodeOldInput = By.id("et__reg__phone");

    private static final By idCodeInput = By.id("et__reg__code");

    private static final By idConfirmButton = By.id("pcb__activate");

    private static final By idOkButton = By.id("button3");

    private static final By idEditPhoneButton = By.id("alertTitle");

    private static final By idErrorAlertMessage = By.id("message");

    public PhoneNumberVerificationPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void inputVerificationCode(String verificationCode) throws Exception {
        final Optional<WebElement> codeInput = getElementIfDisplayed(idCodeInput, 15);
        if (codeInput.isPresent()){
            codeInput.get().sendKeys(verificationCode);
        } else {
            final Optional<WebElement> oldCodeInput = getElementIfDisplayed(idCodeOldInput, 15);
            if (oldCodeInput.isPresent()) {
                oldCodeInput.get().sendKeys(verificationCode);
            } else{
                getElement(idCodeInput, "Verification code input has not been shown in time", 1);
            }
        }
    }

    public void clickConfirm() throws Exception {
        getElement(idConfirmButton).click();
    }

    public boolean waitUntilConfirmButtonDissapears() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idConfirmButton, 40);
    }

    public boolean isIncorrectCodeErrorNotAppears() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idOkButton);
    }

    public String getErrorAlertHeader() throws Exception {
        return getElement(idEditPhoneButton).getText();
    }

    public String getErrorAlertMessage() throws Exception {
        return getElement(idErrorAlertMessage).getText();
    }
}
