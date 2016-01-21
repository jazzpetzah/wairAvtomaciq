package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.ios.pages.IOSPage;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.ios.pages.RegistrationPage;
import com.wearezeta.auto.ios.pages.LoginPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private RegistrationPage getRegistrationPage() throws Exception {
        return pagesCollection.getPage(RegistrationPage.class);
    }

    private LoginPage getLoginPage() throws Exception {
        return pagesCollection.getPage(LoginPage.class);
    }

    private ClientUser userToRegister = null;

    private Future<String> activationMessage;

    BufferedImage templateImage;

    @When("^I press Picture button$")
    public void WhenIPressPictureButton() throws Exception {
        getRegistrationPage().selectPicture();
    }

    @When("^I See selected picture$")
    public void ISeeSelectedPicture() throws Exception {
        templateImage = getRegistrationPage().takeScreenshot().orElseThrow(
                AssertionError::new);
        Assert.assertTrue(getRegistrationPage().isPictureSelected());
    }

    @When("^I confirm selection$")
    public void IConfirmSelection() throws Exception {
        getRegistrationPage().confirmPicture();
    }

    /**
     * Input fake phone number for given user
     *
     * @param name User name alias
     * @throws Exception
     */
    @When("^I enter phone number for user (.*)$")
    public void IEnterPhoneNumber(String name) throws Exception {
        if (this.userToRegister == null) {
            this.userToRegister = new ClientUser();
        }
        this.userToRegister.setName(name);
        this.userToRegister.clearNameAliases();
        this.userToRegister.addNameAlias(name);

        this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        getRegistrationPage().selectWirestan();
        getRegistrationPage().inputPhoneNumber(
                this.userToRegister.getPhoneNumber().toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
    }

    /**
     * Input phone number of allready registered user
     *
     * @param name username
     * @throws Exception
     * @step. ^I input phone number of already registered user (.*)$
     */
    @When("^I input phone number of already registered user (.*)$")
    public void IInputPhoneNumberOfRegisteredUser(String name) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
        getRegistrationPage().selectWirestan();
        getRegistrationPage().inputPhoneNumber(
                user.getPhoneNumber().toString().replace(PhoneNumber.WIRE_COUNTRY_PREFIX, ""));
    }

    /**
     * Input in sign in by phone number page a random phone number
     *
     * @throws Exception
     * @step. ^I enter random phone number$
     */
    @When("^I enter random phone number$")
    public void IEnterRandomPhoneNumber() throws Exception {
        getRegistrationPage().inputPhoneNumber(
                CommonUtils.generateRandomXdigits(7));
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
        getRegistrationPage().selectWirestan();
        assert code.equals(PhoneNumber.WIRE_COUNTRY_PREFIX) :
                "Only Wire-compatible phone numbers are supported";
        getRegistrationPage().inputPhoneNumber(number);
    }

    /**
     * Input in phone number field page an invalid phone number
     *
     * @throws Exception
     * @step. ^I enter invalid phone number$
     */
    @When("^I enter invalid phone number$")
    public void IEnterInvalidPhoneNumber() throws Exception {
        getRegistrationPage().inputPhoneNumber(
                CommonUtils.generateRandomXdigits(11));
    }

    /**
     * Input in phone number field page a random X digits
     *
     * @throws Exception
     * @step. ^I enter (.*) digits phone number
     */
    @When("^I enter (.*) digits phone number$")
    public void IEnterXDigitesPhoneNumber(int x) throws Exception {
        getRegistrationPage().inputPhoneNumber(
                CommonUtils.generateRandomXdigits(x));
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
        String code = BackendAPIWrappers
                .getActivationCodeByPhoneNumber(this.userToRegister
                        .getPhoneNumber());
        getRegistrationPage().inputActivationCode(code);
    }

    @When("^I enter name (.*)$")
    public void IEnterName(String name) throws Exception {
        try {
            this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            this.userToRegister.setName(name);
            this.userToRegister.clearNameAliases();
            this.userToRegister.addNameAlias(name);
        }
        getRegistrationPage().setName(this.userToRegister.getName());
    }

    @When("^I input name (.*) and hit Enter$")
    public void IInputNameAndHitEnter(String name) throws Exception {
        IEnterName(name);
        getRegistrationPage().inputName();
    }

    /**
     * Fill in name field username with leading and trailing spaces
     *
     * @param name username
     * @throws Exception
     * @step. ^I fill in name (.*) with leading and trailing spaces and hit
     * Enter$
     */
    @When("^I fill in name (.*) with leading and trailing spaces and hit Enter$")
    public void IInputNameWithSpacesAndHitEnter(String name) throws Exception {
        getRegistrationPage().setName("  " + name + "  ");
        getRegistrationPage().inputName();
    }

    /**
     * Fill in name field username with leading and trailing spaces on iPad
     *
     * @param name username
     * @throws Exception
     * @step. ^I fill in name (.*) with leading and trailing spaces on iPad
     */
    @When("^I fill in name (.*) with leading and trailing spaces on iPad$")
    public void IInputNameWithSpacesOnIpad(String name) throws Exception {
        try {
            this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            this.userToRegister.setName(name);
            this.userToRegister.clearNameAliases();
            this.userToRegister.addNameAlias(name);
        }
        getRegistrationPage().setName("  " + userToRegister.getName() + "  ");
    }

    @When("^I enter email (.*)$")
    public void IEnterEmail(String email) throws Exception {
        boolean flag = false;
        try {
            String realEmail = usrMgr.findUserByEmailOrEmailAlias(email)
                    .getEmail();
            this.userToRegister.setEmail(realEmail);
        } catch (NoSuchUserException e) {
            if (this.userToRegister == null) {
                this.userToRegister = new ClientUser();
            }
            flag = true;
        }

        if (flag) {
            getRegistrationPage().setEmail(email + "\n");
        } else {
            getRegistrationPage().setEmail(
                    this.userToRegister.getEmail() + "\n");
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
        Assert.assertTrue(getRegistrationPage().isConfirmationShown());
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
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
        this.activationMessage = IMAPSMailbox.getInstance().getMessage(
                expectedHeaders, BackendAPIWrappers.ACTIVATION_TIMEOUT);
    }

    @Then("^I verify registration address$")
    public void IVerifyRegistrationAddress() throws Exception {
        BackendAPIWrappers
                .activateRegisteredUserByEmail(this.activationMessage);
        userToRegister.setUserState(UserState.Created);
        getLoginPage().waitForLoginToFinish();
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
        Assert.assertTrue(getRegistrationPage().isEmailVerifPromptVisible());
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

}
