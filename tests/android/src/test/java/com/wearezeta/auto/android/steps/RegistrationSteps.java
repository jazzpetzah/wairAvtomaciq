package com.wearezeta.auto.android.steps;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.android.pages.registration.ProfilePicturePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.UserState;

import cucumber.api.java.en.*;

public class RegistrationSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

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
     *
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
        activationMessage = BackendAPIWrappers
                .initMessageListener(userToRegister);
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
     * Uses the backend API to activate the registered user and then waits for
     * the confirmation page to disappear (somehow, locator is not clear...)
     *
     * @throws Throwable
     * @step. ^I verify registration address$
     */
    @Then("^I verify registration address$")
    public void IVerifyRegistrationAddress() throws Throwable {
        BackendAPIWrappers.activateRegisteredUserByEmail(activationMessage);
        this.userToRegister.setUserState(UserState.Created);
        getRegistrationPage().continueRegistration();
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
        final String password = usrMgr.replaceAliasesOccurences(pwd,
                ClientUsersManager.FindBy.PASSWORD_ALIAS);
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
}
