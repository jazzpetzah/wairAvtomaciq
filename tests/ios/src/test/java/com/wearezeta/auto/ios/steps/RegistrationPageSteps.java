package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.usrmgmt.*;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.RegistrationPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class RegistrationPageSteps {
    private RegistrationPage getRegistrationPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(RegistrationPage.class);
    }

    private ClientUser userToRegister = null;

    private Future<String> activationMessage = null;

    /**
     * Input fake phone number for given user
     *
     * @param name User name alias
     * @throws Exception
     */
    @When("^I enter phone number for (.*)$")
    public void IEnterPhoneNumber(String name) throws Exception {
        this.userToRegister = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(name);
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
     * @param digitsCount count of digits in the phone number
     * @throws Exception
     * @step. ^I enter (\\d+) digits phone number
     */
    @When("^I enter (\\d+) digits phone number$")
    public void IEnterXDigitsPhoneNumber(int digitsCount) throws Exception {
        getRegistrationPage().inputPhoneNumber(new PhoneNumber(digitsCount));
    }

    /**
     * Enter a phone number and then verify that no commit button is shown
     *
     * @param digitsCount count of digits in the phone number
     * @throws Exception
     * @step. ^I enter (\d+) digits phone number and expect no commit button$
     */
    @Then("^I enter (\\d+) digits phone number and expect no commit button$")
    public void IEnterDigitsAndExpectNoCommit(int digitsCount) throws Exception {
        getRegistrationPage().inputPhoneNumberAndExpectNoCommit(new PhoneNumber(digitsCount));
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
        this.userToRegister = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(name);
        getRegistrationPage().setName(this.userToRegister.getName());
    }

    @When("^I input (custom )?name (.*) and commit it$")
    public void IInputNameAndCommit(String isCuston, String name) throws Exception {
        if (isCuston == null) {
            IEnterName(name);
        } else {
            getRegistrationPage().setName(name);
        }
        getRegistrationPage().commitName();
    }

    @When("^I enter email (.*)$")
    public void IEnterEmail(String email) throws Exception {
        this.userToRegister = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByEmailOrEmailAlias(email);
        getRegistrationPage().setEmail(this.userToRegister.getEmail());
    }

    @When("^I enter password (.*)$")
    public void IEnterPassword(String password) throws Exception {
        this.userToRegister = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByPasswordAlias(password);
        getRegistrationPage().setPassword(this.userToRegister.getPassword());
    }

    @When("^I tap Create Account button on Registration page$")
    public void ITapCreateAccountButton() throws Exception {
        getRegistrationPage().tapCreateAccountButton();
    }

    @Then("^I see confirmation page$")
    public void ISeeConfirmationPage() throws Exception {
        Assert.assertTrue("Confirmation message is not shown or not correct",
                getRegistrationPage().isConfirmationShown());
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
        if (IOSTestContextHolder.getInstance().getTestContext().getUsersManager().isSelfUserSet()) {
            userToRegister = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .getSelfUserOrThrowError();
        }
        activationMessage = BackendAPIWrappers.initMessageListener(userToRegister, additionalHeaders);
    }

    /**
     * Start monitoring thread for activation email for the particular mailbox
     *
     * @param mbox mailbox email address/an alias
     * @param passwd mailbox password/an alias
     * @throws Exception
     * @step. ^I start activation email monitoring on mailbox (.*) with password (.*)
     */
    @When("^I start activation email monitoring on mailbox (.*) with password (.*)")
    public void IStartActivationEmailMonitoringOnMbox(String mbox, String passwd) throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        mbox = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(mbox, ClientUsersManager.FindBy.EMAIL_ALIAS);
        passwd = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(passwd, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        activationMessage = BackendAPIWrappers.initMessageListener(mbox, passwd, additionalHeaders);
    }

    /**
     * Activate email address using activation keys as soon as the corresponding message is received.
     * This steps expects mailbox monitoring to be already running
     *
     * @param address the expected email address for a user
     * @param user    user name/alias
     * @throws Exception
     * @step. ^I verify email address (.*) for (.*)
     */
    @Then("^I verify email address (.*) for (.*)")
    public void IVerifyEmail(String address, String user) throws Exception {
        if (this.activationMessage == null) {
            throw new IllegalStateException("Activation email monitoring is expected to be running");
        }
        BackendAPIWrappers.activateRegisteredUserByEmail(this.activationMessage);
        address = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(address, ClientUsersManager.FindBy.EMAIL_ALIAS);
        final ClientUser dstUser = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(user);
        dstUser.setEmail(address);
        this.activationMessage = null;
    }

    /**
     * Wait until email change monitoring overlay disappears in the UI for the Self user
     *
     * @throws Exception
     * @step. ^I wait until the UI detects successful email activation$
     */
    @And("^I wait until the UI detects successful email activation$")
    public void IWaitForEmailActivation() throws Exception {
        getRegistrationPage().waitForRegistrationToFinish();
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
     * Taps the corresponding button on sign up
     *
     * @throws Exception
     * @step. ^I tap (Choose Own Picture|Choose Photo|Keep This One|Take Photo) button$
     */
    @When("^I tap (Choose Own Picture|Choose Photo|Keep This One|Take Photo) button$")
    public void ISelectRegPhotoFlowButton(String name) throws Exception {
        switch (name) {
            case "Choose Own Picture":
                getRegistrationPage().tapChooseOwnPicButton();
                break;
            case "Choose Photo":
                getRegistrationPage().tapChoosePhotoButton();
                break;
            case "Keep This One":
                getRegistrationPage().tapKeepThisOneButton();
                break;
            case "Take Photo":
                getRegistrationPage().tapTakePhotoButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    /**
     * Verify visibility of "No code to show up" label
     *
     * @param shouldNotBeVisible equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see NO CODE TO SHOW UP label$
     */
    @When("^I (do not )?see NO CODE TO SHOW UP label$")
    public void VerifyNoCodeShowingUpLabelVisibility(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("No code to show up label is not visible", getRegistrationPage()
                    .noCodeShowingUpLabelIsDisplayed());
        } else {
            Assert.assertTrue("No code to show up label is visible", getRegistrationPage()
                    .noCodeShowingUpLabelIsNotDisplayed());
        }
    }

    /**
     * Verify visibility of "RESEND" button
     *
     * @param shouldNotBeVisible equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see RESEND button$
     */
    @When("^I (do not )?see RESEND button$")
    public void VerifyResendButtonVisibility(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("RESEND button is not visible", getRegistrationPage()
                    .resendButtonIsVisible());
        } else {
            Assert.assertTrue("RESEND button is visible", getRegistrationPage()
                    .resendButtonIsNotVisible());
        }
    }
}
