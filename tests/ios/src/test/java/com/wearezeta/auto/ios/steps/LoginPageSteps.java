package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import com.wearezeta.auto.ios.tools.FastLoginContainer;
import org.junit.Assert;

import java.io.IOException;
import java.util.Random;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.RegistrationPage;

import cucumber.api.java.en.*;

/**
 * Contains steps to work with Login/Welcome page
 */
public class LoginPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private LoginPage getLoginPage() throws Exception {
        return pagesCollection.getPage(LoginPage.class);
    }

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollection.getPage(RegistrationPage.class);
    }

    private FirstTimeOverlay getFirstTimeOverlayPage() throws Exception {
        return pagesCollection.getPage(FirstTimeOverlay.class);
    }

    /**
     * Verifies whether sign in screen is the current screen
     *
     * @throws Exception
     * @step. I see sign in screen
     */
    @Given("I see sign in screen")
    public void ISeeSignInScreen() throws Exception {
        Assert.assertTrue("Login page is not visible", getLoginPage().isVisible());
    }

    /**
     * Enter user email and password into corresponding fields on sign in screen
     * then taps "Sign In" button
     *
     * @throws AssertionError if login operation was unsuccessful
     * @step. ^I sign in using my email$
     */
    @Given("^I sign in using my email$")
    public void GivenISignInUsingEmail() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        emailLoginSequence(self.getEmail(), self.getPassword());
    }

    private void emailLoginSequence(String login, String password) throws Exception {
        getLoginPage().switchToLogin();
        // TODO: skip the whole login flow when using fast log in option
        if (!FastLoginContainer.getInstance().isEnabled()) {
            try {
                login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
            } catch (NoSuchUserException e) {
                // Ignore silently
            }
            try {
                password = usrMgr.findUserByPasswordAlias(password).getPassword();
            } catch (NoSuchUserException e) {
                // Ignore silently
            }

            // if (getLoginPage().isEmailInputFieldInvisible()) {
            //     getLoginPage().switchToEmailLogin();
            // }
            getLoginPage().setLogin(login);
            getLoginPage().setPassword(password);
            getLoginPage().clickLoginButton();
        }
        getLoginPage().waitForLoginToFinish();
        getLoginPage().acceptAlertIfVisible(5);
        getFirstTimeOverlayPage().acceptIfVisible(2);
        getLoginPage().acceptAlertIfVisible(5);
        getLoginPage().dismissSettingsWarningIfVisible(5);
    }

    private void phoneLoginSequence(final PhoneNumber number) throws Exception {
        getLoginPage().switchToLogin();

//        if (getRegistrationPage().isCountryPickerButtonInvisible()) {
        getLoginPage().switchToPhoneLogin();
//        }
        getRegistrationPage().inputPhoneNumber(number);
        getRegistrationPage().inputActivationCode(number);
        getLoginPage().waitForLoginToFinish();
        getFirstTimeOverlayPage().acceptIfVisible(2);
        getLoginPage().dismissSettingsWarningIfVisible(5);
    }

    /**
     * Enters the phone number and verification code at self profile page
     *
     * @throws Throwable
     * @step. ^I enter phone number and verification code$
     */
    @When("^I enter phone number and verification code$")
    public void IEnterPhoneNumberAndVerificationCode() throws Throwable {
        ClientUser self = usrMgr.getSelfUserOrThrowError();
        getRegistrationPage().inputPhoneNumber(self.getPhoneNumber());
        getRegistrationPage().inputActivationCode(self.getPhoneNumber());
    }

    /**
     * Enter verification code for specified user
     *
     * @param name name of user
     * @throws Exception
     * @step. ^I enter verification code for user (.*)$
     */
    @When("^I enter verification code for user (.*)$")
    public void IEnterVerificationCodeForUser(String name) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
        getRegistrationPage().inputActivationCode(user.getPhoneNumber());
    }

    /**
     * Inputs not valid verification code
     *
     * @throws Exception
     * @step. ^I enter random verification code$
     */
    @When("^I enter random verification code$")
    public void IEnterRandomVerificationCode() throws Exception {
        getRegistrationPage().inputRandomActivationCode();
    }

    /**
     * Inputs random activation code
     *
     * @throws Exception
     * @step. ^I input random activation code$
     */
    @When("^I input random activation code$")
    public void IEnterRandomActivationCode() throws Exception {
        getRegistrationPage().inputRandomActivationCode();
    }

    /**
     * Click on RESEND button to send new verification code
     *
     * @throws Exception
     * @step. ^I tap RESEND code button$"
     */
    @When("^I tap RESEND code button$")
    public void ITapResendCodeButton() throws Exception {
        getRegistrationPage().clickResendCodeButton();
    }

    /**
     * Verify if PHONE SIGN IN button is visible
     *
     * @throws Exception
     * @step. ^I see PHONE SIGN IN button$
     */
    @When("^I see PHONE SIGN IN button$")
    public void ISeePhoneSignInButton() throws Exception {
        Assert.assertTrue("PHONE SIGN IN button is not visible", getLoginPage()
                .isPhoneSignInButtonVisible());
    }

    /**
     * Tap PHONE tab caption on log in screen
     *
     * @throws Exception
     * @step. ^I switch to Phone Log In tab$
     */
    @When("^I switch to Phone Log In tab$")
    public void ITapPhoneButton() throws Exception {
        getLoginPage().switchToPhoneLogin();
    }

    /**
     * Verify verification code page shown
     *
     * @throws Exception
     * @step. ^I see verification code page$
     */
    @When("^I see verification code page$")
    public void ISeeVerificationCodePage() throws Exception {
        Assert.assertTrue(getRegistrationPage().isVerificationCodePageVisible());
    }

    /**
     * Verify set email/password suggesstion page is shown
     *
     * @throws Exception
     * @step. ^I see set email/password suggesstion page$
     */
    @When("^I see set email/password suggesstion page$")
    public void ISeeSetEmailPassSuggestionPage() throws Exception {
        Assert.assertTrue(getLoginPage().isSetEmailPasswordSuggestionVisible());
    }

    private static final int BY_PHONE_NUMBER_LOGIN_PROBABILITY = 25;
    private static final Random rand = new Random();

    /**
     * Sign in with email/password (20%) or phone number (80%)
     *
     * @throws AssertionError if login operation was unsuccessful
     * @step. ^I sign in using my email or phone number$
     */
    @Given("^I sign in using my email or phone number$")
    public void GivenISignInUsingEmailOrPhone() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
//        if (rand.nextInt(100) < BY_PHONE_NUMBER_LOGIN_PROBABILITY) {
//            phoneLoginSequence(self.getPhoneNumber());
//        } else {
        emailLoginSequence(self.getEmail(), self.getPassword());
//        }
    }

    /**
     * Switch to Log In tab
     *
     * @throws Exception
     * @step. ^I switch to Log In tab$
     */
    @When("^I switch to Log In tab$")
    public void ISwitchToLogInTab() throws Exception {
        getLoginPage().switchToLogin();
    }

    /**
     * Taps Login button on the corresponding screen
     *
     * @throws IOException
     * @step. I press Login button
     */
    @When("I press Login button")
    public void WhenIPressSignInButtonAgain() throws Exception {
        getLoginPage().clickLoginButton();
        getLoginPage().waitForLoginToFinish();
    }

    /**
     * Taps Login button on the corresponding screen
     *
     * @throws Exception
     * @step. I attempt to press Login button
     */
    @When("I attempt to press Login button")
    public void IAttemptToPressLoginButton() throws Exception {
        getLoginPage().clickLoginButton();
    }

    /**
     * Types login string into the corresponding input field on sign in page
     *
     * @param login login string (usually it is user email)
     * @throws IOException
     * @step. I have entered login (.*)
     */
    @When("I have entered login (.*)")
    public void WhenIHaveEnteredLogin(String login) throws Exception {
        try {
            login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getLoginPage().setLogin(login);
    }

    /**
     * Types password string into the corresponding input field on sign in page
     *
     * @param password password string
     * @throws IOException
     * @step. I have entered password (.*)
     */
    @When("I have entered password (.*)")
    public void WhenIHaveEnteredPassword(String password) throws Exception {
        try {
            password = usrMgr.findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getLoginPage().setPassword(password);
    }

    /**
     * Tap and holds the "finger" for a while on email input field
     *
     * @throws Exception
     * @step. I tap and hold on Email input
     */
    @When("I tap and hold on Email input")
    public void ITapHoldEmailInput() throws Exception {
        getLoginPage().tapHoldEmailInput();
    }

    /**
     * Taps "Select All" item in popup menu of an input field
     *
     * @throws Exception
     * @step. I click on popup SelectAll item
     */
    @When("I click on popup SelectAll item")
    public void IClickPopupSelectAll() throws Exception {
        getLoginPage().tapPopupSelectAllButton();
    }

    /**
     * Taps "Copy" item in popup menu of an input field
     *
     * @throws Exception
     * @step. I click on popup Copy item
     */
    @When("I click on popup Copy item")
    public void IClickPopupCopy() throws Exception {
        getLoginPage().tapPopupCopyButton();
    }

    /**
     * Taps "Paste" item in popup menu of an input field
     *
     * @throws Exception
     * @step. ^I click on popup Paste item$
     */
    @When("^I click on popup Paste item$")
    public void IClickPopupPaste() throws Exception {
        getLoginPage().tapPopupPasteButton();
    }

    /**
     * Enters given text into email input field and taps password field
     *
     * @param wrongMail text to enter into email input field
     * @step. ^I enter wrong email (.*)
     */
    @When("^I enter wrong email (.*)")
    public void IEnterWrongEmail(String wrongMail) throws Exception {
        getLoginPage().setLogin(wrongMail);
        getLoginPage().tapPasswordField();
    }

    /**
     * Enters a text into password input field
     *
     * @step. ^I enter wrong password (.*)
     */
    @When("^I enter wrong password (.*)")
    public void IEnterWrongPassword(String wrongPassword) throws Exception {
        getLoginPage().setPassword(wrongPassword);
    }

    /**
     * Verifies whether the notification about wrong credentials exists on the
     * current screen
     *
     * @throws Exception
     * @step. ^I see wrong credentials notification$
     */
    @Then("^I see wrong credentials notification$")
    public void ISeeWrongCredentialsNotification() throws Exception {
        Assert.assertTrue("I don't see wrong credentials notification",
                getLoginPage().wrongCredentialsNotificationIsShown());
    }

    /**
     * Verifies whether the notification Resend avialble in 10 min is shown
     *
     * @throws Exception
     * @step. ^I see Resend will be possible after 10 min alert$
     */
    @Then("^I see Resend will be possible after 10 min alert$")
    public void ISeeResendIn10minAlert() throws Exception {
        Assert.assertTrue("I don't see Resend in 10 min alert", getLoginPage()
                .isResendIn10minAlertVisible());
    }

    /**
     * Verifies whether the notification invalid email is shown
     *
     * @throws Exception
     * @step. ^I see invalid email alert$
     */
    @Then("^I see invalid email alert$")
    public void ISeeInvalidEmailAlert() throws Exception {
        Assert.assertTrue("I don't see invalid email alert",
                getLoginPage().isInvalidEmailAlertShown());
    }

    /**
     * Verifies whether the notification already registered email shown
     *
     * @throws Exception
     * @step. ^I see already registered email alert$
     */
    @Then("^I see already registered email alert$")
    public void ISeeAlreadyRegisteredEmailAlert() throws Exception {
        Assert.assertTrue("I don't see already registered email alert",
                getLoginPage().isAlreadyRegisteredEmailAlertShown());
    }

    /**
     * Verifies whether the notification something went wrong is shown
     *
     * @throws Exception
     * @step. ^I see something went wrong alert$
     */
    @Then("^I see something went wrong alert$")
    public void ISeeSomethingWentWrongAlert() throws Exception {
        Assert.assertTrue("I don't see already registered email alert",
                getLoginPage().isSomethingWentWrongAlertShown());
    }

    /**
     * Clicks on the Forgot/Change password button on the Sign In screen
     *
     * @throws Exception
     * @step. ^I click on Change Password button on SignIn$
     */
    @When("^I click on Change Password button on SignIn$")
    public void IClickOnChangePasswordButtonOnSignIn() throws Exception {
        getLoginPage().tapForgotPasswordButton();
    }

    /**
     * @throws Throwable
     */
    @When("^I click Not Now to not add phone number$")
    public void IClickNotNowToNotAddPhoneNumber() throws Throwable {
        getLoginPage().clickPhoneNotNow();
        getLoginPage().waitForLoginToFinish();
    }

}
