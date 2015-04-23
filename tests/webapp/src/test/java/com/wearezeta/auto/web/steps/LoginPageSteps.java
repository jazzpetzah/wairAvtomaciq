package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

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

		InvitationCodePageSteps invitationPageSteps = new InvitationCodePageSteps();
		invitationPageSteps.ISeeInvitationPage();
		invitationPageSteps.IEnterInvitationCode();

		// FIXME: Try to reenter login data if signing in fails to
		// workaround Amazon page load issues
		int ntry = 0;
		while (ntry < MAX_LOGIN_RETRIES) {
			PagesCollection.registrationPage.switchToLoginPage();
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
	 * Presses Sign In button on the corresponding page and verifies whether an
	 * account is signed in properly
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

		Assert.assertTrue("Sign In button/progress spimmer are still visible",
				PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter email (.*)$
	 * 
	 * @param email
	 *            user email string
	 */
	@When("^I enter email (.*)$")
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
	 * @step. ^I enter password (.*)$
	 * 
	 * @param password
	 *            password string
	 */
	@When("I enter password (.*)")
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
	 * Clicks the corresponding switcher button to make the registration page
	 * active
	 * 
	 * @step. ^I switch to registration page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to [Rr]egistration page$")
	public void ISwitchToRegistrationPage() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage
				.switchToRegistrationPage();
	}
}
