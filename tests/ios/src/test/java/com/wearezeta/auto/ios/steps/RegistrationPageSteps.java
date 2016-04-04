package com.wearezeta.auto.ios.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.WireMessage;
import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.ios.pages.RegistrationPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollection.getPage(RegistrationPage.class);
    }

    private ClientUser userToRegister = null;

    private Future<String> activationMessage;

    /**
     * Input fake phone number for given user
     *
     * @param name User name alias
     * @throws Exception
     */
    @When("^I enter phone number for user (.*)$")
    public void IEnterPhoneNumber(String name) throws Exception {
        this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        getRegistrationPage().inputPhoneNumber(this.userToRegister.getPhoneNumber());
    }

    /**
     * Input in phone number field page phone number with code
     *
     * @param number phone number
     * @param code   country code
     * @throws Exception
     * @step. ^I input phone number (.*) with code (.*)$
     */
    @When("^I input phone number (.*) with code (.*)$")
    public void IInputPhoneNumber(String number, String code) throws Exception {
        assert code.equals(PhoneNumber.WIRE_COUNTRY_PREFIX) : "Only Wire-compatible phone numbers are supported";
        getRegistrationPage().inputPhoneNumber(new PhoneNumber(code, number));
    }

    /**
     * Input in phone number field page a random X digits
     *
     * @throws Exception
     * @step. ^I enter (.*) digits phone number
     */
    @When("^I enter (.*) digits phone number$")
    public void IEnterXDigitsPhoneNumber(int x) throws Exception {
        getRegistrationPage().inputPhoneNumber(new PhoneNumber(x));
    }

    /**
     * Click on I AGREE button to accept terms of service
     *
     * @throws Exception
     */
    @When("^I accept terms of service$")
    public void IAcceptTermsOfService() throws Exception {
        getRegistrationPage().clickAgreeButton();
    }

    /**
     * Input activation code generated for fake phone number
     *
     * @throws Exception
     */
    @When("^I enter activation code$")
    public void IEnterActivationCode() throws Exception {
        getRegistrationPage().inputActivationCode(this.userToRegister.getPhoneNumber());
    }

    @When("^I enter name (.*)$")
    public void IEnterName(String name) throws Exception {
        this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        getRegistrationPage().setName(this.userToRegister.getName());
    }

    @When("^I input name (.*) and hit Enter$")
    public void IInputNameAndHitEnter(String name) throws Exception {
        IEnterName(name);
        getRegistrationPage().inputName();
    }

    /**
     * Copy and paste non-English chars and send extra space keystroke (workaround for simulator bug)
     *
     * @throws Exception
     * @step. ^I input Non-English name (.*) and hit Enter
     */
    @When("^I input Non-English name (.*) and hit Enter$")
    public void IInputNonEnglishNameAndHitEnter(String name) throws Exception {
        getRegistrationPage().setName(name);
        getRegistrationPage().tapNameInputField();
        getRegistrationPage().inputName();
    }

    @When("^I enter email (.*)$")
    public void IEnterEmail(String email) throws Exception {
        boolean isRealEmail = false;
        try {
            String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
                    .getEmail();
            this.userToRegister.setEmail(realEmail);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            isRealEmail = true;
        }

        if (isRealEmail) {
            getRegistrationPage().setEmail(email);
        } else {
            getRegistrationPage().setEmail(
                    this.userToRegister.getEmail());
        }
    }

    @When("^I enter password (.*)$")
    public void IEnterPassword(String password) throws Exception {
        try {
            this.userToRegister.setPassword(usrMgr.findUserByPasswordAlias(
                    password).getPassword());
        } catch (NoSuchUserException e) {
            this.userToRegister.setPassword(password);
            this.userToRegister.addPasswordAlias(password);
        }
        getRegistrationPage().setPassword(this.userToRegister.getPassword());
    }

    @When("I click Create Account Button")
    public void IClickCreateAccountButton() throws Exception {
        getRegistrationPage().clickCreateAccountButton();
    }

    @Then("^I see confirmation page$")
    public void ISeeConfirmationPage() throws Exception {
        Assert.assertTrue("Confirmation message is not shown or not correct", getRegistrationPage().isConfirmationShown());
    }

    /**
     * Start monitoring thread for activation email. Please put this step BEFORE
     * you submit the registration form
     *
     * @throws Exception
     * @step. ^I start activation email monitoring$
     */
    @When("^I start activation email monitoring$")
    public void IStartActivationEmailMonitoring() throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        activationMessage = BackendAPIWrappers.initMessageListener(userToRegister, additionalHeaders);
    }

    @Then("^I verify registration address$")
    public void IVerifyRegistrationAddress() throws Exception {
        BackendAPIWrappers.activateRegisteredUserByEmail(this.activationMessage);
        userToRegister.setUserState(UserState.Created);
        getRegistrationPage().waitRegistrationToFinish();
    }

    /**
     * Verifies that the email verification reminder on the login page is
     * displayed
     *
     * @throws Exception
     * @step. I see email verification reminder
     */
    @Then("^I see email verification reminder$")
    public void ISeeEmailVerificationReminder() throws Exception {
        Assert.assertTrue(getRegistrationPage().isEmailVerificationPromptVisible());
    }

    /**
     * Verifies whether the notification invalid code is shown
     *
     * @throws Exception
     * @step. ^I see invalid code alert$
     */
    @Then("^I see invalid code alert$")
    public void ISeeInvalidEmailAlert() throws Exception {
        Assert.assertTrue("I don't see invalid code alert",
                getRegistrationPage().isInvalidCodeAlertShown());
    }

    /**
     * Presses the Choose own picture button on sign up
     *
     * @throws Exception
     * @step. ^I press Choose Own Picture button$
     */
    @When("^I press Choose Own Picture button$")
    public void IPressChooseOwnPictureButton() throws Exception {
        getRegistrationPage().clickChooseOwnPicButton();
    }

    /**
     * Presses on Alert Choose Photo button
     *
     * @throws Exception
     * @step. ^I press Choose Photo button$
     */
    @When("^I press Choose Photo button$")
    public void IPressChoosePhotoButton() throws Exception {
        getRegistrationPage().clickChoosePhotoButton();
    }

    /**
     * Tap Keep This One button on unsplash page
     *
     * @throws Exception
     * @step. ^I press Keep This One button$
     */
    @And("^I press Keep This One button$")
    public void IPressKeepThisOneButton() throws Exception {
        getRegistrationPage().clickKeepThisOneButton();
    }

    /**
     * Tap Take Photo button on unsplash page
     *
     * @throws Exception
     * @step. ^I press Take Photo button$
     */
    @And("^I press Take Photo button$")
    public void IPressTakePhotoButton() throws Exception {
        getRegistrationPage().tapTakePhotoButton();
    }

}
