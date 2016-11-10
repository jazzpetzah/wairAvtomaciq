package com.wearezeta.auto.win.steps.webapp;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class LoginPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class.getName());
    private final WrapperTestContext context;

    public LoginPageSteps() {
        this.context = new WrapperTestContext();
    }

    public LoginPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @Given("^I Sign in using login (.*) and password (.*)$")
    public void ISignInUsingLoginAndPassword(String login, String password) throws Exception {
        try {
            login = context.getUserManager().findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            try {
                // search for email by name aliases in case name is specified
                login = context.getUserManager().findUserByNameOrNameAlias(login).getEmail();
            } catch (NoSuchUserException ex) {
            }
        }

        try {
            password = context.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        LOG.debug("Starting to Sign in using login " + login + " and password " + password);
        this.IEnterEmail(login);
        this.IEnterPassword(password);
        this.IPressSignInButton();
    }

    @When("^I press Sign In button$")
    public void IPressSignInButton() throws Exception {
        context.getWebappPagesCollection().getPage(LoginPage.class).clickSignInButton();
    }

    @Then("^I am signed in properly$")
    public void IAmSignedInProperly() throws Exception {
        Assert.assertTrue("Sign In button/login progress spinner are still visible", context.getWebappPagesCollection().getPage(
                LoginPage.class).waitForLogin());
    }

    @Then("^the sign in error message reads (.*)")
    public void TheSignInErrorMessageReads(String message) throws Exception {
        assertThat("sign in error message", context.getWebappPagesCollection().getPage(LoginPage.class).getErrorMessage(),
                equalTo(message));
    }

    @Then("^a red dot is shown inside the email field on the sign in form$")
    public void ARedDotIsShownOnTheEmailField() throws Exception {
        assertThat("Red dot on email field", context.getWebappPagesCollection().getPage(LoginPage.class).
                isEmailFieldMarkedAsError());
    }

    @Then("^a red dot is shown inside the password field on the sign in form$")
    public void ARedDotIsShownOnThePasswordField() throws Exception {
        assertThat("Red dot on password field", context.getWebappPagesCollection().getPage(LoginPage.class).
                isPasswordFieldMarkedAsError());
    }

    @When("^I enter email (\\S+)$")
    public void IEnterEmail(String email) throws Exception {
        try {
            email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getWebappPagesCollection().getPage(LoginPage.class).inputEmail(email);
    }

    @When("^I enter password \"([^\"]*)\"$")
    public void IEnterPassword(String password) throws Exception {
        try {
            password = context.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getWebappPagesCollection().getPage(LoginPage.class).inputPassword(password);
    }

    @Given("^I see Sign In page$")
    public void ISeeSignInPage() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(LoginPage.class).isSignInFormVisible());
    }

    @When("^I click Change Password button$")
    public void IClickChangePassword() throws Exception {
        context.getWebappPagesCollection().getPage(LoginPage.class).
                clickChangePasswordButton(context.getWebappPagesCollection());
    }

    @Then("^I see login error \"(.*)\"$")
    public void ISeeLoginError(String expectedError) throws Exception {
        final String loginErrorText = context.getWebappPagesCollection().getPage(LoginPage.class).getErrorMessage();
        Assert.assertTrue(String.format("The actual login error '%s' is not equal to the expected one: '%s'", loginErrorText,
                expectedError), loginErrorText.equals(expectedError));
    }

    @When("^I switch to phone number sign in page$")
    public void i_switch_to_phone_number_sign_in_page() throws Throwable {
        context.getWebappPagesCollection().getPage(LoginPage.class).switchToPhoneNumberLoginPage();
    }
}
