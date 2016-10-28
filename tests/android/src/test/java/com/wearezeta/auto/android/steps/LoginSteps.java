package com.wearezeta.auto.android.steps;

import java.util.Random;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.registration.*;
import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class LoginSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final static int DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS = 60 * 2;

    private EmailSignInPage getEmailSignInPage() throws Exception {
        return pagesCollection.getPage(EmailSignInPage.class);
    }

    private WelcomePage getWelcomePage() throws Exception {
        return pagesCollection.getPage(WelcomePage.class);
    }

    private AreaCodePage getAreaCodePage() throws Exception {
        return pagesCollection.getPage(AreaCodePage.class);
    }

    private AddPhoneNumberPage getAddPhoneNumberPage() throws Exception {
        return pagesCollection.getPage(AddPhoneNumberPage.class);
    }

    private PhoneNumberVerificationPage getVerificationPage() throws Exception {
        return pagesCollection.getPage(PhoneNumberVerificationPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Inputs the login details for the self user and then taps the sign in
     * button.
     *
     * @throws Exception
     * @step. ^I sign in using my email$
     */
    @Given("^I sign in using my email$")
    public void ISignInUsingMyEmail() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        assert getWelcomePage().waitForInitialScreen() : "The initial screen was not shown";
        getWelcomePage().tapSignInTab();
        // FIXME: AN-4116
        try {
            getEmailSignInPage().setLogin(self.getEmail());
            getEmailSignInPage().setPassword(self.getPassword());
            getEmailSignInPage().logIn(true, DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS);
        } catch (Exception e) {
            Thread.sleep(2000);
            getEmailSignInPage().setLogin(self.getEmail());
            getEmailSignInPage().setPassword(self.getPassword());
            getEmailSignInPage().logIn(true, DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS);
        }
    }

    /**
     * Do sign in using phone number, there are 2 ways to fill up verification code
     * 1) Type verification code direct on Wire
     * 2) Use Web browser link
     *
     * @throws Exception
     * @step. ^I sign in using my phone number( with SMS verification)?$
     */
    @Given("^I sign in using my phone number( with SMS verification)?$")
    public void ISignInUsingMyPhoneNumber(String verifiedBySmsURL) throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        assert getWelcomePage().waitForInitialScreen() : "The initial screen was not shown";
        getWelcomePage().tapAreaCodeSelector();
        getAreaCodePage().selectAreaCode(self.getPhoneNumber().getPrefix());
        getWelcomePage().inputPhoneNumber(self.getPhoneNumber());
        getWelcomePage().tapConfirm();
        final String verificationCode = BackendAPIWrappers.getLoginCodeByPhoneNumber(self.getPhoneNumber());
        if (verifiedBySmsURL == null) {
            getVerificationPage().inputVerificationCode(verificationCode);
            getVerificationPage().tapConfirm();
        } else {
            AndroidCommonUtils.openWebsiteFromADB(String.format("http://wire.com/v/%s", verificationCode));
        }
        Assert.assertTrue("Phone number verification code input screen is still visible",
                getVerificationPage().waitUntilConfirmButtonDisappears());
    }

    private void ISignInUsingMyPhoneNumber() throws Exception {
        ISignInUsingMyPhoneNumber(null);
    }

    private static final int PHONE_NUMBER_LOGIN_THRESHOLD = 60;
    private static final Random random = new Random();

    /**
     * Enter self user credentials into the corresponding fields on sign in
     * screen and tap Sign In button. Sometimes this step uses phone number to
     * sign in and sometimes it uses email address
     *
     * @throws Exception
     * @step. ^I sign in using my email or phone number$
     */
    @Given("^I sign in using my email or phone number$")
    public void ISignInUsingMyEmailOrPhoneNumber() throws Exception {
        if (random.nextInt(100) < PHONE_NUMBER_LOGIN_THRESHOLD) {
            ISignInUsingMyPhoneNumber();
        } else {
            ISignInUsingMyEmail();
        }
    }

    /**
     * Types an email address into the email login field
     *
     * @param login email/alias
     * @throws Exception
     * @step. ^I have entered login (.*)$
     */
    @When("^I have entered login (.*)$")
    public void IHaveEnteredLogin(String login) throws Exception {
        try {
            login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getEmailSignInPage().setLogin(login);
    }

    /**
     * Enters a password into the password login field
     *
     * @param password password/alias
     * @throws Exception
     * @step. ^I have entered password (.*)$
     */
    @When("I have entered password (.*)")
    public void IHaveEnteredPassword(String password) throws Exception {
        try {
            password = usrMgr.findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getEmailSignInPage().setPassword(password);
    }

    /**
     * Tap the Log in Button underneath the email and password fields
     *
     * @throws Exception
     * @step. ^I tap Log in button$
     */
    @When("I tap Log in button")
    public void ITapLogInButton() throws Exception {
        getEmailSignInPage().logIn(false, DEFAULT_LOGIN_SCREEN_TIMEOUT_SECONDS);
    }

    /**
     * Accept an error message by tapping OK button
     *
     * @throws Exception
     * @step. ^I accept the error message$
     */
    @When("^I accept the error message$")
    public void IAcceptErrorMsg() throws Exception {
        getEmailSignInPage().acceptErrorMessage();
    }

    /**
     * Verify whether forcer email login page is visible
     *
     * @throws Exception
     * @step. ^I see (?:forced)? e?mail login page$
     */
    @Given("^I see (?:forced)? e?mail login page$")
    public void ISeeEmailScreen() throws Exception {
        Assert.assertTrue("Forced email login page is not shown", getEmailSignInPage()
                .waitForForcedEmailLoginScreen());
    }

    /**
     * Click NOT NOW button on the corresponding page
     *
     * @throws Exception
     * @step. ^I postpone Add Phone Number action$
     */
    @And("^I postpone Add Phone Number action$")
    public void IPostponeAddPhoneNumber() throws Exception {
        getAddPhoneNumberPage().tapNotNowButton();
    }
}
