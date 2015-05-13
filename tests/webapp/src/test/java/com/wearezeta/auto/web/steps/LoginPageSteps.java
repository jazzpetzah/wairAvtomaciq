package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class LoginPageSteps {

	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final int MAX_LOGIN_RETRIES = 3;

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
	@Given("^I Sign in using login (.*) and password (.*)$")
	public void ISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			try {
				// search for email by name aliases in case name is specified
				login = usrMgr.findUserByNameOrNameAlias(login).getEmail();
			} catch (NoSuchUserException ex) {
			}
		}

		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		log.debug("Starting to Sign in using login " + login + " and password "
				+ password);

		// FIXME: Try to reenter login data if signing in fails to
		// workaround Amazon page load issues
		int ntry = 0;
		while (ntry < MAX_LOGIN_RETRIES) {
			PagesCollection.loginPage = PagesCollection.registrationPage
					.switchToLoginPage();
			this.IEnterEmail(login);
			this.IEnterPassword(password);
			try {
				this.IPressSignInButton();
				break;
			} catch (AssertionError e) {
				log.error(String.format(
						"Failed to sign in. Retrying (%s of %s)...", ntry + 1,
						MAX_LOGIN_RETRIES));
				if (ntry + 1 >= MAX_LOGIN_RETRIES) {
					throw e;
				}
			}
			ntry++;
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
		PagesCollection.contactListPage = PagesCollection.loginPage
				.clickSignInButton();
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
				PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Checks if a error message is shown on the sign in page
	 *
	 * @step. ^the sign in error message reads (.*)
	 * @param message
	 *            expected error message
	 * @throws Throwable
	 */
	@Then("^the sign in error message reads (.*)")
	public void TheSignInErrorMessageReads(String message) throws Throwable {
		assertThat("sign in error message",
				PagesCollection.loginPage.getErrorMessage(), equalTo(message));
	}

	/**
	 * Checks if a red dot is shown inside the email field on the sign in form
	 *
	 * @step. ^a red dot is shown inside the email field on the sign in form$
	 */
	@Then("^a red dot is shown inside the email field on the sign in form$")
	public void ARedDotIsShownOnTheEmailField() {
		assertThat("Red dot on email field",
				PagesCollection.loginPage.isRedDotOnEmailField());
	}

	/**
	 * Checks if a red dot is shown inside the password field on the sign in
	 * form
	 *
	 * @step. ^a red dot is shown inside the password field on the sign in form$
	 */
	@Then("^a red dot is shown inside the password field on the sign in form$")
	public void ARedDotIsShownOnThePasswordField() {
		assertThat("Red dot on password field",
				PagesCollection.loginPage.isRedDotOnEmailField());
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter email (\\S+)$
	 * 
	 * @param email
	 *            user email string
	 */
	@When("^I enter email (\\S+)$")
	public void IEnterEmail(String email) {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.inputEmail(email);
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
	public void IEnterPassword(String password) {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.inputPassword(password);
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
		Assert.assertTrue(PagesCollection.loginPage.isVisible());
	}

	/**
	 * Switch to [Rr]egistration page
	 * 
	 * @step. ^I switch to [Rr]egistration page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to [Rr]egistration page$")
	public void ISwitchToRegistrationPage() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage
				.switchToRegistrationPage();
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
		PagesCollection.passwordChangeRequestPage = PagesCollection.loginPage
				.clickChangePasswordButton();
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
		final String loginErrorText = PagesCollection.loginPage
				.getErrorMessage();
		Assert.assertTrue(
				String.format(
						"The actual login error '%s' is not equal to the expected one: '%s'",
						loginErrorText, expectedError), loginErrorText
						.equals(expectedError));
	}
}
