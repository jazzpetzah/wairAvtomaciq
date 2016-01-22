package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.driver.DriverUtils;
import org.junit.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.RegistrationPage;

import cucumber.api.java.en.*;
import org.openqa.selenium.By;

/**
 * Contains steps to work with Login/Welcome page
 */
public class LoginPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection
            .getInstance();

    private LoginPage getLoginPage() throws Exception {
        return pagesCollecton.getPage(LoginPage.class);
    }

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollecton.getPage(RegistrationPage.class);
    }

    private Future<String> activationMessage;
    private static final String stagingURLForgot = "https://staging-website.zinfra.io/forgot/";

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

        if (getLoginPage().isEmailInputFieldInvisible()) {
            getLoginPage().switchToEmailLogin();
        }
        getLoginPage().setLogin(login);
        getLoginPage().setPassword(password);
        getLoginPage().clickLoginButton();
        getLoginPage().waitForLoginToFinish();
    }

    private void phoneLoginSequence(final PhoneNumber number) throws Exception {
        if (!getRegistrationPage().isCountryPickerButtonVisible()) {
            getLoginPage().switchToPhoneLogin();
        }

        getRegistrationPage().selectWirestan();
        getRegistrationPage().inputPhoneNumber(
                number.toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
        getRegistrationPage().inputActivationCode(number);
        getLoginPage().waitForLoginToFinish();
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
        self.setPhoneNumber(new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX));
        getRegistrationPage().selectWirestan();
        getRegistrationPage().inputPhoneNumber(
                self.getPhoneNumber().toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
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
     * Tap on PHONE SIGN IN button is visible
     *
     * @throws Exception
     * @step. I tap on PHONE SIGN IN button
     */
    @When("I tap on PHONE SIGN IN button")
    public void ITapPhoneSignInButton() throws Exception {
        getLoginPage().switchToPhoneLogin();
    }

    /**
     * Verify country picker button presented
     *
     * @throws Exception
     * @step. ^I see country picker button on Sign in screen$
     */
    @When("^I see country picker button on Sign in screen$")
    public void ISeeCountryPickerButton() throws Exception {
        Assert.assertTrue("Country picker button is not visible",
                getLoginPage().isCountryPickerButtonVisible());
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

    /**
     * Sign in with email/password (20%) or phone number (80%)
     *
     * @throws AssertionError if login operation was unsuccessful
     * @step. ^I sign in using my email or phone number$
     */
    @Given("^I sign in using my email or phone number$")
    public void GivenISignInUsingEmailOrPhone() throws Exception {
        // FIXME: iOS wants to reregister if login by phone
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
//        if (CommonUtils.trueInPercents(BY_PHONE_NUMBER_LOGIN_PROBABILITY)) {
//        phoneLoginSequence(self.getPhoneNumber());
//        } else {
        emailLoginSequence(self.getEmail(), self.getPassword());
//        }
    }

    /**
     * Tap I HAVE AN ACCOUNT button
     *
     * @throws Exception
     * @step. ^I tap I HAVE AN ACCOUNT button$
     */
    @When("^I tap I HAVE AN ACCOUNT button$")
    public void ITapHaveAnAccount() throws Exception {
        getLoginPage().switchToEmailLogin();
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
        getLoginPage().clickPopupSelectAllButton();
    }

    /**
     * Taps "Copy" item in popup menu of an input field
     *
     * @throws Exception
     * @step. I click on popup Copy item
     */
    @When("I click on popup Copy item")
    public void IClickPopupCopy() throws Exception {
        getLoginPage().clickPopupCopyButton();
    }

    /**
     * Taps "Paste" item in popup menu of an input field
     *
     * @throws Exception
     * @step. I click on popup Paste item
     */
    @When("I click on popup Paste item")
    public void IClickPopupPaste() throws Exception {
        getLoginPage().clickPopupPasteButton();
    }

    /**
     * Taps "Terms of Service" link on Welcome screen
     *
     * @step. ^I press Terms of Service link$
     */
    @When("^I press Terms of Service link$")
    public void IPressTermsOfServiceLink() throws Throwable {
        getLoginPage().openTermsLink();
    }

    /**
     * Verifies whether the current page is "Terms and Conditions" page
     *
     * @throws AssertionError if the current page is not "Terms and Conditions" page
     * @step. ^I see the terms info page$
     */
    @Then("^I see the terms info page$")
    public void ISeeTheTermsInfoPage() throws Throwable {
        Assert.assertTrue("I don't see terms of service page", getLoginPage()
                .isTermsPrivacyCloseButtonVisible());
        // TODO:verify correct content as far as copywrite is in
    }

    /**
     * Closes "Terms and Conditions" page to return back to Welcome page
     *
     * @throws AssertionError if the current page is not "Welcome" page
     * @step. ^I return to welcome page$
     */
    @When("^I return to welcome page$")
    public void IReturnToWelcomePage() throws Throwable {
        getLoginPage().closeTermsPrivacyController();
        Assert.assertTrue("I don't see login screen", getLoginPage()
                .isLoginButtonVisible());
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
     * @step. ^I see Resend will be possible after 10 min aleart$
     */
    @Then("^I see Resend will be possible after 10 min aleart$")
    public void ISeeResendIn10minAlert() throws Exception {
        Assert.assertTrue("I don't see Resend in 10 min alert", getLoginPage()
                .isResendIn10minAlertVisible());
    }

    /**
     * Verifies whether the notification invalid phone number shown
     *
     * @throws Exception
     * @step. ^I see invalid phone number alert$
     */
    @Then("^I see invalid phone number alert$")
    public void ISeeInvalidPhoneNumberAlert() throws Exception {
        Assert.assertTrue("I don't see invalid phone number alert",
                getLoginPage().isInvalidPhoneNumberAlertShown());
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
     * Verifies whether the notification registered phone number shown
     *
     * @throws Exception
     * @step. ^I see already registered phone number alert$
     */
    @Then("^I see already registered phone number alert$")
    public void ISeeRegisteredNumberAlert() throws Exception {
        Assert.assertTrue("I don't see registered phone number alert",
                getLoginPage().isRegisteredNumberAlertShown());
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
     * Clicks on the Forgot/Change password button on the Sign In screen
     *
     * @throws Exception
     * @step. ^I click on Change Password button on SignIn$
     */
    @When("^I click on Change Password button on SignIn$")
    public void IClickOnChangePasswordButtonOnSignIn() throws Exception {
        getLoginPage().tapChangePasswordButton();

    }

    @When("^I change URL to staging$")
    public void IChangeURLToStaging() throws Exception {
        getLoginPage().changeURLInBrowser(stagingURLForgot);
    }

    /**
     * Types the mail into the field, where change password link should be send
     * to
     *
     * @param email
     * @throws Exception
     * @step. ^I type in email (.*) to change password$
     */
    @When("^I type in email (.*) to change password$")
    public void ITypeInEmailToChangePassword(String email) throws Exception {
        email = usrMgr.replaceAliasesOccurences(email, FindBy.EMAIL_ALIAS);
        getLoginPage().tapEmailFieldToChangePassword(email);

        // activate the user, to get access to the mails
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Delivered-To", email);
        expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME,
                PasswordResetMessage.MESSAGE_PURPOSE);
        this.activationMessage = IMAPSMailbox.getInstance().getMessage(
                expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
    }

    /**
     * Presses the change password button in the safari webview
     *
     * @throws Exception
     * @step. ^I press Change Password button in browser$
     */
    @When("^I press Change Password button in browser$")
    public void IPressChangePasswordButtonInBrowser() throws Exception {
        getLoginPage().tapChangePasswordButtonInWebView();
    }

    /**
     * Copies the link in the email and types it into the safari search field
     *
     * @throws Exception
     * @step. ^I copy link from email and past it into Safari
     */
    @When("^I copy link from email and past it into Safari$")
    public void ICopyLinkFromEmailAndPastItIntoSafari() throws Exception {
        String link = BackendAPIWrappers.getPasswordResetLink(this.activationMessage);
        getLoginPage().changeURLInBrowser(link);
    }

    /**
     * Types the new password into the password field
     *
     * @param newPassword that gets set as new password by typing it into the field
     * @throws Exception
     * @step. ^I type in new password (.*)$
     */
    @When("^I type in new password (.*)$")
    public void ITypeInNewPassword(String newPassword) throws Exception {
        usrMgr.getSelfUserOrThrowError().setPassword(newPassword);
        getLoginPage().tapPasswordFieldToChangePassword(newPassword);
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
