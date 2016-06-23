package com.wearezeta.auto.android.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.RegistrationPage;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import cucumber.api.java.en.*;

public class RegistrationSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollection.getPage(RegistrationPage.class);
    }

    private ClientUser userToRegister = null;

    public Future<String> activationMessage;

    /**
     * Presses the camera button to bring up the camera app
     *
     * @throws Exception
     * @step. ^I press Picture button$
     */
    @When("^I press Picture button$")
    public void WhenIPressPictureButton() throws Exception {
        getRegistrationPage().selectPicture();
    }

    /**
     * Submits all of the data given in the registration process by pressing the
     * "create account" button Also waits at the inbox of the user to check to
     * receive the verification email
     *
     * @throws Exception
     * @step. ^I submit registration data$
     */
    @When("^I submit registration data$")
    public void ISubmitRegistrationData() throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        activationMessage = BackendAPIWrappers.initMessageListener(userToRegister, additionalHeaders);
        getRegistrationPage().createAccount();
    }

    /**
     * Checks to see that the confirmation page exists once registration is
     * finished. This is the page that tells the user to check their emails
     *
     * @throws Exception
     * @step. ^I see confirmation page$
     */
    @Then("^I see confirmation page$")
    public void ISeeConfirmationPage() throws Exception {
        Assert.assertTrue(getRegistrationPage().isConfirmationVisible());
    }

    /**
     * Type a password into the corresponding field on the registration by email invite page
     *
     * @param pwd password or alias
     * @throws Exception
     * @step. ^I input password "(.*)"$
     */
    @When("^I input password \"(.*)\"$")
    public void IInputPassword(String pwd) throws Exception {
        final String password = usrMgr.replaceAliasesOccurences(pwd, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        getRegistrationPage().enterPassword(password);
    }

    /**
     * Tap the corresponding button which confirms the typed password on register by email invitation page
     *
     * @throws Exception
     * @step. ^I confirm password$
     */
    @And("^I confirm password$")
    public void IConfirmPassword() throws Exception {
        getRegistrationPage().tapContinueButton();
    }

    /**
     * Tap the corresponding button to select own picture  during registration instead of the automatic one chosen by
     * Unsplash
     *
     * @throws Exception
     * @step. ^I select to choose my own picture$
     */
    @And("^I select to (choose my own|keep the current) picture$")
    public void ITapOwnPictureButton(String action) throws Exception {
        switch (action.toLowerCase()) {
            case "choose my own":
                getRegistrationPage().tapOwnPictureButton();
                break;
            case "keep the current":
                getRegistrationPage().tapKeepThisPictureButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported action name %s", action));
        }
    }

    /**
     * Select picture picture on the corresponding confirmation alert after I click "choose my own" button
     * during registration
     *
     * @param src either Camera or Gallery
     * @throws Exception
     * @step. ^I select (Camera|Gallery) as picture source$
     */
    @And("^I select (Camera|Gallery) as picture source$")
    public void ISelectPictureSource(String src) throws Exception {
        getRegistrationPage().selectPictureSource(src);
    }

    /**
     * Wait for Unsplash screen to appear
     *
     * @throws Exception
     * @step. ^I wait until Unsplash screen is visible$
     */
    @And("^I wait until Unsplash screen is visible$")
    public void IWaitUnspashScreen() throws Exception {
        Assert.assertTrue("Unsplash screen is still not visible after timeout",
                getRegistrationPage().waitUntilUnsplashScreenIsVisible());
    }

    private Map<String, Future<String>> emailConfirmMessages = new HashMap<>();

    /**
     * Starts the internal email listener to get the confirmation email
     *
     * @param email    email address/alias
     * @param password password/alias
     * @throws Exception
     * @step. ^I start listening for (.*) confirmation email$
     */
    @When("^I start listening for confirmation email (.*) with mailbox password (.*)$")
    public void IStartListeningForConfirmEmail(String email, String password) throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        email = usrMgr.replaceAliasesOccurences(email, ClientUsersManager.FindBy.EMAIL_ALIAS);
        password = usrMgr.replaceAliasesOccurences(password, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        emailConfirmMessages.put(email,
                BackendAPIWrappers.initMessageListener(email, password, additionalHeaders));
    }

    /**
     * Wait until confirmation email with confirmation link is delivered and
     * then activates the user using values from this email message
     *
     * @param email email address/alias
     * @throws Exception
     * @step. ^I verify email (.*)
     */
    @Then("^I verify email (.*)")
    public void IVerifyMyRegistrationData(String email) throws Exception {
        email = usrMgr.replaceAliasesOccurences(email, ClientUsersManager.FindBy.EMAIL_ALIAS);
        if (emailConfirmMessages.containsKey(email)) {
            BackendAPIWrappers.activateRegisteredUserByEmail(emailConfirmMessages.get(email));
        } else {
            throw new IllegalStateException(String.format("Email listener for email '%s' has not been started", email));
        }
    }
}
