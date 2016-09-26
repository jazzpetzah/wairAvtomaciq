package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class LoginPageSteps {

	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());

        private final TestContext context;
        
        
    public LoginPageSteps() {
        this.context = new TestContext();
    }

    public LoginPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Enters user email and password into corresponding fields on sign in
	 * screen then taps "Sign In" button
	 * 
	 * @step. ^I Sign in using login (.*) and password (.*)$
	 * 
	 * @param login
	 *            user login string
	 * @param password
	 *            user password string
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("^I Sign in( temporary)? using login (.*) and password (.*)$")
	public void ISignInUsingLoginAndPassword(String temporary, String login, String password)
			throws Exception {
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
		log.debug("Starting to Sign in using login " + login + " and password "
				+ password);
		this.IEnterEmail(login);
		this.IEnterPassword(password);
		if(temporary != null) {
			this.IPressSignInButton();
		} else {
			this.ICheckOptionToRememberMe(null);
			this.IPressSignInButton();
		}
	}

	/**
	 * Presses Sign In button on the corresponding page
	 *
	 * @step. ^I press Sign In button$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^I press Sign In button$")
	public void IPressSignInButton() throws Exception {
		context.getPagesCollection().getPage(LoginPage.class).clickSignInButton();
	}

	/**
	 * Checks if Sign In button on the corresponding page is disabled
	 *
	 * @step. ^Sign In button is disabled$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^Sign In button is disabled$")
	public void SignInButtonIsDisabled() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(LoginPage.class)
				.isSignInButtonDisabled());
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
				context.getPagesCollection().getPage(LoginPage.class).waitForLogin());
	}

	/**
	 * Checks if a error message is shown on the sign in page
	 *
	 * @step. ^the sign in error message reads (.*)
	 * @param message
	 *            expected error message
	 * @throws Exception
	 */
	@Then("^the sign in error message reads (.*)")
	public void TheSignInErrorMessageReads(String message) throws Exception {
		assertThat("sign in error message",
				context.getPagesCollection().getPage(LoginPage.class)
						.getErrorMessage(), equalTo(message));
	}

	/**
	 * Checks if a orange line is shown around the email field on the sign in
	 * form
	 *
	 * @step. ^the email field on the sign in form is marked as error$
	 * @throws Exception
	 */
	@Then("^the email field on the sign in form is marked as error$")
	public void ARedDotIsShownOnTheEmailField() throws Exception {
		assertThat("Email field not marked",
				context.getPagesCollection().getPage(LoginPage.class)
						.isEmailFieldMarkedAsError());
	}

	/**
	 * Checks if a orange line is shown around the password field on the sign in
	 * form
	 *
	 * @step. ^the password field on the sign in form is marked as error$
	 * @throws Exception
	 */
	@Then("^the password field on the sign in form is marked as error$")
	public void ARedDotIsShownOnThePasswordField() throws Exception {
		assertThat("Password field not marked",
				context.getPagesCollection().getPage(LoginPage.class)
						.isPasswordFieldMarkedAsError());
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter email \"([^\"]*)\"$
	 * 
	 * @param email
	 *            user email string
	 */
	@When("^I enter email \"([^\"]*)\"$")
	public void IEnterEmail(String email) throws Exception {
		try {
			email = context.getUserManager().findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		context.getPagesCollection().getPage(LoginPage.class).inputEmail(email);
	}
    
    /**
     * Types phone number string into the corresponding email input field on sign in page
     *
     * @throws java.lang.Exception
     * @step. ^I enter phone number \"([^\"]*)\"$
     *
     * @param phoneNumber user phoneNumber string
     */
    @When("^I enter phone number \"([^\"]*)\"$")
    public void IEnterPhoneNumber(String phoneNumber) throws Exception {
        phoneNumber = context.getUserManager().replaceAliasesOccurences(phoneNumber,
                ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
        context.getPagesCollection().getPage(LoginPage.class).inputEmail(phoneNumber);
    }

	/**
	 * Types password string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter password \"([^\"]*)\"$
	 * 
	 * @param password
	 *            password string
	 */
	@When("^I enter password \"([^\"]*)\"$")
	public void IEnterPassword(String password) throws Exception {
		try {
			password = context.getUserManager().findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		context.getPagesCollection().getPage(LoginPage.class).inputPassword(password);
	}

	@When("I (un)?check option to remember me")
	public void ICheckOptionToRememberMe(String uncheck) throws Exception {
		if (uncheck == null) {
			context.getPagesCollection().getPage(LoginPage.class).checkRememberMe();
		} else {
			context.getPagesCollection().getPage(LoginPage.class).uncheckRememberMe();
		}
	}

	/**
	 * Verifies whether Sign In page is the current page
	 * 
	 * @step. ^I see Sign In page$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if current page is not Sign In page
	 */
	@Given("^I see Sign In page$")
	public void ISeeSignInPage() throws Exception {
		Assert.assertTrue(context.getPagesCollection().getPage(LoginPage.class)
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
		context.getPagesCollection().getPage(LoginPage.class)
				.clickChangePasswordButton(context.getPagesCollection());
	}

	/**
	 * Verifies whether the expected login error is visible on the page
	 * 
	 * @step. ^I see login error \"(.*)\"$
	 * 
	 * @param expectedError
	 *            the text of error
	 * @throws Exception
	 */
	@Then("^I see login error \"(.*)\"$")
	public void ISeeLoginError(String expectedError) throws Exception {
		final String loginErrorText = context.getPagesCollection().getPage(
				LoginPage.class).getErrorMessage();
		Assert.assertTrue(
				String.format(
						"The actual login error '%s' is not equal to the expected one: '%s'",
						loginErrorText, expectedError), loginErrorText
						.equals(expectedError));
	}

	@When("^I switch to phone number sign in page$")
	public void i_switch_to_phone_number_sign_in_page() throws Throwable {
		context.getPagesCollection().getPage(LoginPage.class)
				.switchToPhoneNumberLoginPage();
	}
        
    @Given("^I open (.*) session expired login page$")
    public void i_open_session_expired_login_page(String lang) throws Throwable {
        String langKey;
        switch (lang) {
            case "english":
                langKey = "en";
                break;
            case "german":
                langKey = "de";
                break;
            default:
                throw new IllegalArgumentException("Please specify a language for the session expired login page");
        }
        context.getPagesCollection().getPage(LoginPage.class)
                .visitSessionExpiredPage(langKey);
    }

    @Then("^I verify session expired message is visible$")
    public void i_verify_session_expired_message_is_visible() throws Throwable {
        Assert.assertTrue("session expired message is not visible", context.getPagesCollection().getPage(LoginPage.class)
                .isSessionExpiredErrorMessageVisible());
    }

    @Then("^I verify session expired message is equal to (.*)$")
    public void i_verify_session_expired_message_is_x(String sessionExpiredMessage) throws Throwable {
        Assert.assertEquals("session expired message does not match expected value", sessionExpiredMessage,
                context.getPagesCollection().getPage(LoginPage.class)
                .getSessionExpiredErrorMessage());
    }

	@Given("^I open (.*) login page as if I was redirected from get.wire.com$")
	public void IOpenLoginPageRedirected(String lang) throws Throwable {
		String langKey;
		switch (lang) {
			case "english":
				langKey = "en";
				break;
			case "german":
				langKey = "de";
				break;
			default:
				throw new IllegalArgumentException("Please specify a language for the login page");
		}
		context.getPagesCollection().getPage(LoginPage.class).visitRedirectedPage(langKey);
	}

	@Then("^I verify text about Wire is visible$")
	public void IVerifyTextAboutWireIsvisible() throws Throwable {
		Assert.assertTrue("description message is not visible", context.getPagesCollection().getPage(LoginPage.class)
				.isDescriptionMessageVisible());
	}

	@Then("^I see intro about Wire saying (.*)$")
	public void ISeeIntroAboutWireSayingX(String expectedIntro) throws Throwable {
		Assert.assertEquals("description message does not match expected value", expectedIntro,
				context.getPagesCollection().getPage(LoginPage.class)
						.getDescriptionMessage());
	}
        
        /**
	 * Switch to Registration page
	 * 
	 * @step. ^I switch to registration page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to registration page$")
	public void ISwitchToRegistrationPage() throws Exception {
		context.getPagesCollection().getPage(LoginPage.class)
				.switchToRegistrationPage();
	}
}
