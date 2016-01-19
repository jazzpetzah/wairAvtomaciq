package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * This page represents the email sign in screen, which you can reach from the
 * welcome screen. From here, you can choose to go to the phone signin page, go
 * back to the welcome screen, or log in with an existing email and password
 *
 * @author deancook
 */
public class EmailSignInPage extends AndroidPage {
    private static final Function<String, String> xpathStrLoginMessageByText = text -> String
            .format("//*[@id='message' and @value='%s']", text);

    private static final By xpathAlertOKButton = By.xpath("//*[starts-with(@id, 'button') and @value='OK']");

    private static final By idLoginInput = By.id("get__sign_in__email");

    private static final By idPasswordInput = By.id("get__sign_in__password");

    public static final By idLoginButton = By.id("pcb__signin__email");

    public EmailSignInPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void setLogin(String login) throws Exception {
        final WebElement loginInput = getElement(idLoginInput);
        loginInput.click();
        loginInput.sendKeys(login);
    }

    public void setPassword(String password) throws Exception {
        final WebElement passwordInput = getElement(idPasswordInput);
        passwordInput.click();
        passwordInput.sendKeys(password);
    }

    /**
     * If the user already has a phone number attached to their account, this
     * page will go directly to the start UI, or else it will return a page in
     * which the user is asked to add a phone number
     *
     * @param enableLoginWorkaround set to try to accept any login alert automatically and retry
     *                              until timeout happens
     * @param timeoutSeconds        sign in timeout
     * @throws Exception
     */
    public void logIn(boolean enableLoginWorkaround, int timeoutSeconds) throws Exception {
        getElement(idLoginButton).click();
        waitForLogInToComplete(enableLoginWorkaround, timeoutSeconds);
    }

    public void waitForLogInToComplete(boolean enableLoginWorkaround, int timeoutSeconds) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            if (DriverUtils.waitUntilLocatorDissapears(this.getDriver(), idLoginButton, 5)) {
                // FIXME: Workaround for 403 error from the backend
                if (enableLoginWorkaround) {
                    if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathAlertOKButton, 1)) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (enableLoginWorkaround && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathAlertOKButton, 1)) {
                acceptErrorMessage();
                getElement(idLoginButton).click();
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        throw new IllegalStateException(String.format(
                "Login screen is still visible after %s seconds timeout", timeoutSeconds));

    }

    public void verifyErrorMessageText(String expectedMsg) throws Exception {
        getElement(By.xpath(xpathStrLoginMessageByText.apply(expectedMsg)),
                String.format("Error message '%s' is not visible on the screen", expectedMsg), 15);
    }

    public void acceptErrorMessage() throws Exception {
        getElement(xpathAlertOKButton).click();
    }
}
