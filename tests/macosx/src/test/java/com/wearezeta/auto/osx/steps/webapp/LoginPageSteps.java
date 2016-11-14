package com.wearezeta.auto.osx.steps.webapp;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class LoginPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public LoginPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public LoginPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    /**
     * Enters user email and password into corresponding fields on sign in screen then taps "Sign In" button
     *
     * @step. ^I Sign in using login (.*) and password (.*)$
     *
     * @param login user login string
     * @param password user password string
     *
     * @throws AssertionError if login operation was unsuccessful
     */
    @Given("^I Sign in using login (.*) and password (.*)$")
    public void ISignInUsingLoginAndPassword(String login, String password)
            throws Exception {
        try {
            login = webContext.getUserManager().findUserByEmailOrEmailAlias(login).getEmail();
        } catch (NoSuchUserException e) {
            try {
                // search for email by name aliases in case name is specified
                login = webContext.getUserManager().findUserByNameOrNameAlias(login).getEmail();
            } catch (NoSuchUserException ex) {
            }
        }

        try {
            password = webContext.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        LOG.debug("Starting to Sign in using login " + login + " and password "
                + password);
        this.IEnterEmail(login);
        this.IEnterPassword(password);
        Thread.sleep(1000);
        this.IPressSignInButton();
        Thread.sleep(1000);
    }

    /**
     * Presses Sign In button on the corresponding page
     *
     * @step. ^I press Sign In button$
     *
     * @throws Exception if Selenium fails to wait until sign in action completes
     */
    @When("^I press Sign In button$")
    public void IPressSignInButton() throws Exception {
        Thread.sleep(1000);
        webContext.getPagesCollection().getPage(LoginPage.class).clickSignInButton();
    }

    /**
     * Verifies whether an account is signed in properly
     *
     * @step. ^I am signed in properly$
     *
     * @throws Exception
     */
    @Then("^I am signed in properly$")
    public void IAmSignedInProperly() throws Exception {
        Assert.assertTrue(
                "Sign In button/login progress spinner are still visible",
                webContext.getPagesCollection().getPage(LoginPage.class).waitForLogin());
    }

    /**
     * Checks if a error message is shown on the sign in page
     *
     * @step. ^the sign in error message reads (.*)
     * @param message expected error message
     * @throws Exception
     */
    @Then("^the sign in error message reads (.*)")
    public void TheSignInErrorMessageReads(String message) throws Exception {
        assertThat("sign in error message",
                webContext.getPagesCollection().getPage(LoginPage.class)
                .getErrorMessage(), equalTo(message));
    }

    /**
     * Checks if a red dot is shown inside the email field on the sign in form
     *
     * @step. ^a red dot is shown inside the email field on the sign in form$
     * @throws Exception
     */
    @Then("^a red dot is shown inside the email field on the sign in form$")
    public void ARedDotIsShownOnTheEmailField() throws Exception {
        assertThat("Red dot on email field",
                webContext.getPagesCollection().getPage(LoginPage.class)
                .isEmailFieldMarkedAsError());
    }

    /**
     * Checks if a red dot is shown inside the password field on the sign in form
     *
     * @step. ^a red dot is shown inside the password field on the sign in form$
     * @throws Exception
     */
    @Then("^a red dot is shown inside the password field on the sign in form$")
    public void ARedDotIsShownOnThePasswordField() throws Exception {
        assertThat("Red dot on password field",
                webContext.getPagesCollection().getPage(LoginPage.class)
                .isPasswordFieldMarkedAsError());
    }

    /**
     * Types email string into the corresponding input field on sign in page
     *
     * @step. ^I enter email (\\S+)$
     *
     * @param email user email string
     */
    @When("^I enter email (\\S+)$")
    public void IEnterEmail(String email) throws Exception {
        try {
            email = webContext.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputEmail(email);
    }

    /**
     * Types password string into the corresponding input field on sign in page
     *
     * @step. ^I enter password \"([^\"]*)\"$
     *
     * @param password password string
     */
    @When("^I enter password \"([^\"]*)\"$")
    public void IEnterPassword(String password) throws Exception {
        try {
            password = webContext.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webContext.getPagesCollection().getPage(LoginPage.class).inputPassword(password);
    }

    /**
     * Verifies whether Sign In page is the current page
     *
     * @step. ^I see Sign In page$
     * @throws Exception
     *
     * @throws AssertionError if current page is not Sign In page
     */
    @Given("^I see Sign In page$")
    public void ISeeSignInPage() throws Exception {
        Assert.assertTrue(webContext.getPagesCollection().getPage(LoginPage.class)
                .isSignInFormVisible());
    }

    /**
     * Click Change Password button on login page
     *
     * @step. ^I click Change Password button$
     *
     * @throws Exception
     */
    @When("^I click Change Password button$")
    public void IClickChangePassword() throws Exception {
        webContext.getPagesCollection().getPage(LoginPage.class)
                .clickChangePasswordButton(webContext.getPagesCollection());
    }

    /**
     * Verifies whether the expected login error is visible on the page
     *
     * @step. ^I see login error \"(.*)\"$
     *
     * @param expectedError the text of error
     * @throws Exception
     */
    @Then("^I see login error \"(.*)\"$")
    public void ISeeLoginError(String expectedError) throws Exception {
        final String loginErrorText = webContext.getPagesCollection().getPage(
                LoginPage.class).getErrorMessage();
        Assert.assertTrue(
                String.format(
                        "The actual login error '%s' is not equal to the expected one: '%s'",
                        loginErrorText, expectedError), loginErrorText
                .equals(expectedError));
    }

    @When("^I switch to phone number sign in page$")
    public void i_switch_to_phone_number_sign_in_page() throws Throwable {
        webContext.getPagesCollection().getPage(LoginPage.class)
                .switchToPhoneNumberLoginPage();
    }
}
