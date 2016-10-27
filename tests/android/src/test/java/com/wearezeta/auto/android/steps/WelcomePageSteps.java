package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.registration.AddNamePage;
import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.PhoneNumberVerificationPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class WelcomePageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private WelcomePage getWelcomePage() throws Exception {
        return pagesCollection.getPage(WelcomePage.class);
    }

    private AreaCodePage getAreaCodePage() throws Exception {
        return pagesCollection.getPage(AreaCodePage.class);
    }

    private PhoneNumberVerificationPage getVerificationPage() throws Exception {
        return pagesCollection.getPage(PhoneNumberVerificationPage.class);
    }

    private AddNamePage getAddNamePage() throws Exception {
        return pagesCollection.getPage(AddNamePage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final String ERROR_CODE_ALERT_HEADER = "Invalid code";
    private static final String ERROR_CODE_ALERT_MESSAGE = "Please enter a valid code.";

    private ClientUser userToRegister = null;

    /**
     * Verify whether Welcome screen is visible
     *
     * @throws Exception
     * @step. ^I see [Ww]elcome screen$
     */
    @Given("^I see [Ww]elcome screen$")
    public void ISeeWelcomeScreen() throws Exception {
        Assert.assertTrue("Welcome page is not shown", getWelcomePage().waitForInitialScreen());
    }

    /**
     * Tap the "I have an account" button on the welcome page to switch to
     * sign in using email address
     *
     * @throws Exception
     * @step. ^I switch to email sign in screen$
     */
    @When("^I switch to email sign in screen$")
    public void ISwitchToEmailSignIn() throws Exception {
        getWelcomePage().tapSignInTab();
    }

    /**
     * @param name user name/alias
     * @throws Exception
     * @step. ^I input a new phone number (.*)$
     */
    @When("^I input a new phone number for user (.*)$")
    public void IInputANewPhoneNumber(String name) throws Exception {
        getWelcomePage().tapAreaCodeSelector();
        getAreaCodePage().selectAreaCode(PhoneNumber.WIRE_COUNTRY_PREFIX);

        this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
        getWelcomePage().inputPhoneNumber(this.userToRegister.getPhoneNumber());
        getWelcomePage().tapConfirm();
    }

    /**
     * Get the verification code from the backend, type it into the
     * corresponding input field and confirm the input
     *
     * @throws Exception
     * @step. ^I input the verification code$
     */
    @When("^I input the verification code$")
    public void IInputTheVerificationCode() throws Exception {
        final PhoneNumber phoneNumber = this.userToRegister.getPhoneNumber();
        final String verificationCode = BackendAPIWrappers.getActivationCodeByPhoneNumber(phoneNumber);
        getVerificationPage().inputVerificationCode(verificationCode);
        getVerificationPage().tapConfirm();
    }

    /**
     * Enter the user name on the registration form
     *
     * @throws Exception
     * @step. ^I input my name$
     */
    @When("^I input my name$")
    public void IInputMyName() throws Exception {
        final String name = this.userToRegister.getName();
        getAddNamePage().inputName(name);
        getAddNamePage().tapConfirm();
    }

    /**
     * Inputs a random not correct code into the field
     *
     * @throws Exception
     * @step. ^I input random activation code$
     */
    @When("^I input random activation code$")
    public void IInputRandomActivationCode() throws Exception {
        final String randomVerificationCode = CommonUtils.generateRandomNumericString(6);
        getVerificationPage().inputVerificationCode(randomVerificationCode);
        getVerificationPage().tapConfirm();
    }

    /**
     * Verifies the correct invalid code error alert
     *
     * @throws Exception
     * @step. ^I see invalid code alert$
     */
    @Then("^I see invalid code alert$")
    public void ISeeInvalidCodeAlert() throws Exception {
        Assert.assertEquals(ERROR_CODE_ALERT_HEADER, getVerificationPage().getErrorAlertHeader());
        Assert.assertEquals(ERROR_CODE_ALERT_MESSAGE, getVerificationPage().getErrorAlertMessage());
    }

}
