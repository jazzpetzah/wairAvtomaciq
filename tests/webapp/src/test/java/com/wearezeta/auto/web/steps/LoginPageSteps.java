package com.wearezeta.auto.web.steps;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class LoginPageSteps {

	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Enters user email and password into corresponding fields on sign in screen
	 * then taps "Sign In" button
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

		//workaround for IE and Safari
		if (PagesCollection.loginPage != null) {
			PagesCollection.authorizationPage.clickSignInButton();
		}
		
		this.IEnterEmail(login);
		this.IEnterPassword(password);
		this.IPressSignInButton();
	}

	/**
	 * Presses Sign In button on the corresponding page and verifies whether an
	 * account is signed in properly
	 * 
	 * @step. ^I press Sign In button$
	 * 
	 * @throws IOException
	 */
	@When("^I press Sign In button$")
	public void IPressSignInButton() throws Exception {
		WebPage page = PagesCollection.loginPage.confirmSignIn();

		Assert.assertNotNull(
				"Login page or ContactList page expected. Page couldn't be null",
				page);

		Assert.assertTrue(PagesCollection.loginPage.waitForLogin());

		if (page instanceof ContactListPage) {
			PagesCollection.contactListPage = (ContactListPage) page;
		}
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
	 * 
	 * @throws AssertionError
	 *             if current page is not Sign In page
	 */
	@Given("^I see Sign In page$")
	public void ISeeSignInPage() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}
}
