package com.wearezeta.auto.android.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.android.pages.registration.ProfilePicturePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import cucumber.api.java.en.*;

public class RegistrationSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollection.getPage(RegistrationPage.class);
    }

    private ProfilePicturePage getProfilePicturePage() throws Exception {
        return pagesCollection.getPage(ProfilePicturePage.class);
    }

    private ClientUser userToRegister = null;

    public Future<String> activationMessage;

    /**
     * Presses the camera button once to bring up the camera, and again to take
     * a picture
     *
     * @param shouldPressTwice this will tap camera button twice if exists
     * @throws Exception
     * @step. ^I press Camera button( twice|\s*)$
     */
    @When("^I press Camera button( twice|\\s*)$")
    public void WhenIPressCameraButton(String shouldPressTwice) throws Exception {
        getProfilePicturePage().clickCameraButton();
        if (shouldPressTwice != null && shouldPressTwice.contains("twice")) {
            Thread.sleep(2000);
            getProfilePicturePage().clickCameraButton();
        }
    }

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
     * Presses the confirm button to confirm the selected picture
     *
     * @throws Exception
     * @step. ^I confirm selection$
     */
    @When("^I confirm selection$")
    public void IConfirmSelection() throws Exception {
        getProfilePicturePage().confirmPicture();
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
    @And("^I select to choose my own picture$")
    public void ITapOwnPictureButton() throws Exception {
        getRegistrationPage().tapOwnPictureButton();
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

    private Future<String> emailConfirmMessage;

    /**
     * Starts the internal email listener to get the confirmation email
     *
     * @throws Exception
     * @step. ^I start listening for confirmation email$
     */
    @When("^I start listening for confirmation email$")
    public void IStartListeningForConfirmEmail() throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        emailConfirmMessage = BackendAPIWrappers.initMessageListener(usrMgr.getSelfUserOrThrowError(),
                additionalHeaders);
    }

    /**
     * Wait until confirmation email with confirmation link is delivered and
     * then activates the user using values from this email message
     *
     * @throws Exception
     * @step. ^I verify my email$
     */
    @Then("^I verify my email$")
    public void IVerifyMyRegistrationData() throws Exception {
        BackendAPIWrappers.activateRegisteredUserByEmail(emailConfirmMessage);
    }
}
