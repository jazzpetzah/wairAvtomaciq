package com.wearezeta.auto.android_tablet.steps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.WireMessage;
import org.junit.Assert;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android_tablet.pages.registration.TabletRegisterConfirmationPage;
import com.wearezeta.auto.android_tablet.pages.registration.TabletRegistrationFormPage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegistrationSteps {
    private TabletRegistrationFormPage getRegistrationFormPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletRegistrationFormPage.class);
    }

    private TabletRegisterConfirmationPage getRegisterConfirmationPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletRegisterConfirmationPage.class);
    }

    private ClientUser userToRegister;

    {
        try {
            userToRegister = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(ClientUsersManager.NAME_ALIAS_TEMPLATE.apply(1));
        } catch (NoSuchUserException e) {
            Throwables.propagate(e);
        }
    }

    private Future<String> registrationMessage;

    /**
     * Enter the user name into the corresponding field on the registration form
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I enter (?:the |\\s*)registration name \"(.*)\"$
     */
    @When("^I enter (?:the |\\s*)registration name \"(.*)\"$")
    public void IEnterRegistrationName(String name) throws Exception {
        try {
            name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            this.userToRegister.setName(name);
        }
        getRegistrationFormPage().enterName(name);
    }

    /**
     * Enter the user email into the corresponding field on the registration
     * form
     *
     * @param email email address/alias
     * @throws Exception
     * @step. ^I enter (?:the |\\s*)registration email \"(.*)\"$
     */
    @When("^I enter (?:the |\\s*)registration email \"(.*)\"$")
    public void IEnterRegistrationEmail(String email) throws Exception {
        try {
            email = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            this.userToRegister.setEmail(email);
        }
        getRegistrationFormPage().enterEmail(email);
    }

    /**
     * Enter the user password into the corresponding field on the registration
     * form
     *
     * @param password the password/alias
     * @throws Exception
     * @step. ^I enter (?:the |\\s*)registration password \"(.*)\"$
     */
    @When("^I enter (?:the |\\s*)registration password \"(.*)\"$")
    public void IEnterRegistrationPassword(String password) throws Exception {
        try {
            password = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            this.userToRegister.setPassword(password);
        }
        getRegistrationFormPage().enterPassword(password);
    }

    /**
     * Starts the internal email listener to get the registration email
     *
     * @throws Exception
     * @step. ^I start listening for registration email$
     */
    @When("^I start listening for registration email$")
    public void IStartListeningForRegistrationEmail() throws Exception {
        final Map<String, String> additionalHeaders = new HashMap<>();
        additionalHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, ActivationMessage.MESSAGE_PURPOSE);
        if (AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().isSelfUserSet()) {
            registrationMessage = BackendAPIWrappers.initMessageListener(
                    AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .getSelfUserOrThrowError(), additionalHeaders);
        } else {
            registrationMessage = BackendAPIWrappers.initMessageListener(userToRegister, additionalHeaders);
        }
    }

    /**
     * Tap the corresponding button on the registration form to send the
     * registration data
     *
     * @throws Exception
     * @step. ^I submit (?:the |\\s*)registration data$
     */
    @When("^I submit (?:the |\\s*)registration data$")
    public void ISubmitRegistrationData() throws Exception {
        getRegistrationFormPage().tapSubmitButton();
    }

    /**
     * Wait until registration email with confirmation link is delivered and
     * then activates the user using values from this email message
     *
     * @throws Exception
     * @step. ^I verify my registration via email$
     */
    @Then("^I verify my registration via email$")
    public void IVerifyMyRegistrationData() throws Exception {
        BackendAPIWrappers.activateRegisteredUserByEmail(registrationMessage);
        if (!AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().isSelfUserSet()) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().setSelfUser(userToRegister);
        }
    }

    /**
     * Verify whether the registration confirmation page is visible or not
     *
     * @param shouldNotSee equals to null if "do not" part does not exist in the step
     * @throws Exception
     * @step. ^I (do not )?see (?:the |\\s*)[Cc]onfirmation page$"
     */
    @Then("^I (do not )?see (?:the |\\s*)[Cc]onfirmation page$")
    public void ISeeConfirmationPage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    "The confirmation is not visible after the timeout",
                    getRegisterConfirmationPage().waitUntilVisible());
        } else {
            Assert.assertTrue(
                    "The confirmation is still visible after the timeout",
                    getRegisterConfirmationPage().waitUntilInvisible());
        }
    }

    /**
     * Verify whether the particular email address is visible on the
     * confirmation page
     *
     * @throws Exception
     * @step. ^I see the entered email on (?:the |\\s*)[Cc]onfirmation page$
     */
    @Then("^I see the entered email on (?:the |\\s*)[Cc]onfirmation page$")
    public void ISeeEmail() throws Exception {
        final String expectedEmail = userToRegister.getEmail();
        Assert.assertTrue(
                String.format(
                        "The expected email address '%s' is not visible on the confirmation page",
                        expectedEmail), getRegisterConfirmationPage()
                        .waitUntilEmailIsVisible(expectedEmail));
    }
}
