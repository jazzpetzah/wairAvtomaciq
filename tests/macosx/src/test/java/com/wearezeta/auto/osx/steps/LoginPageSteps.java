package com.wearezeta.auto.osx.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {

	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Enters user email and password into corresponding fields on sign in
	 * screen then taps "Sign In" button
	 * 
	 * @step. I Sign in using login (.*) and password (.*)
	 * 
	 * @param login
	 *            user login string
	 * @param password
	 *            user password string
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("I Sign in using login (.*) and password (.*)")
	public void GivenISignInUsingLoginAndPassword(String login, String password)
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

		PagesCollection.loginPage.startSignIn();

		PagesCollection.loginPage.setLogin(login);
		PagesCollection.loginPage.setPassword(password);

		PagesCollection.loginPage.confirmSignIn();

		Assert.assertTrue("Failed to login",
				PagesCollection.loginPage.waitForLogin());

		PagesCollection.contactListPage = new ContactListPage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
		PagesCollection.userProfilePage = new UserProfilePage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
	}

	/**
	 * Clicks on Sign In button on Welcome screen and opens Sign In screen
	 * 
	 * @step. I start Sign In
	 */
	@When("I start Sign In")
	public void WhenIStartSignIn() {
		PagesCollection.loginPage.startSignIn();
	}

	/**
	 * Clicks on Sign In button and submits entered credentials
	 * 
	 * @step. I press Sign In button
	 * 
	 * @throws Exception
	 */
	@When("I press Sign In button")
	public void WhenIPressSignInButton() throws Exception {
		OSXPage page = PagesCollection.loginPage.confirmSignIn();
		Assert.assertNotNull(
				"After sign in button click Login page or Contact List page should appear. Page couldn't be null",
				page);
		if (page instanceof ContactListPage) {
			PagesCollection.contactListPage = (ContactListPage) page;
		}
		PagesCollection.userProfilePage = new UserProfilePage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
	}

	/**
	 * Enters login in corresponding field on Sign In page
	 * 
	 * @step. I have entered login (.*)
	 * 
	 * @param login
	 *            user login string
	 */
	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setLogin(login);
	}

	/**
	 * Enters password in corresponding field on Sign In page
	 * 
	 * @step. I have entered password (.*)
	 * 
	 * @param password
	 *            user password string
	 * @throws Exception
	 */
	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setPassword(password);
	}

	/**
	 * Checks that Sign In screen is visible
	 * 
	 * @step. I see Sign In screen
	 * 
	 * @throws AssertionError
	 *             if Sign In screen did not appear
	 */
	@Given("I see Sign In screen")
	public void GivenISeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

	/**
	 * Checks that user is not signed in client and resets backend settings
	 * 
	 * @step. I am signed out from ZClient
	 * 
	 * @throws Exception
	 */
	@Given("I am signed out from ZClient")
	public void GivenIAmSignedOutFromZClient() throws Exception {
		PagesCollection.loginPage.logoutIfNotSignInPage();
		CommonOSXSteps.resetBackendSettingsIfOverwritten();
	}

	/**
	 * When called after logout, checks that Sign In screen is opened
	 * 
	 * @step. I have returned to Sign In screen
	 * @throws Exception
	 */
	@Then("I have returned to Sign In screen")
	public void ThenISeeSignInScreen() throws Exception {
		Assert.assertTrue("Failed to logout",
				PagesCollection.contactListPage.waitForSignOut());
		Assert.assertTrue(PagesCollection.contactListPage.isSignOutFinished());
	}

	/**
	 * Accepts terms of service and starts registration
	 * 
	 * @step. I start registration
	 * 
	 * @throws Exception
	 */
	@When("I start registration")
	public void IStartRegistration() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage
				.startRegistration();
	}

	/**
	 * Sets password on Sign In screen using Apple Script
	 * 
	 * @step. I input password (.*) using script
	 * 
	 * @param password
	 *            user password string
	 */
	@When("I input password (.*) using script")
	public void IInputPasswordUsingScript(String password) {
		PagesCollection.loginPage.setPasswordUsingScript(password);
	}

	/**
	 * Checks that corresponding error message appears when incorrect
	 * credentials are entered
	 * 
	 * @step. I see wrong credentials message
	 * 
	 * @throws AssertionError
	 *             if there is no message about wrong credentials on Sign In
	 *             screen
	 */
	@Then("I see wrong credentials message")
	public void ISeeWrongCredentialsMessage() {
		Assert.assertTrue(PagesCollection.loginPage
				.isWrongCredentialsMessageDisplayed());
	}

	/**
	 * Checks that corresponding error message did not appear when credentials
	 * are entered
	 * 
	 * @step. ^I do not see wrong credentials message$
	 * 
	 * @throws AssertionError
	 *             if there is message about wrong credentials on Sign In screen
	 */
	@Then("^I do not see wrong credentials message$")
	public void IDoNotSeeWrongCredentialsMessage() {
		Assert.assertFalse(PagesCollection.loginPage
				.isWrongCredentialsMessageDisplayed());
	}

	/**
	 * Checks that No internet connection error appears when internet is blocked
	 * 
	 * @step. ^I see internet connectivity error message$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no message about internet connection error
	 */
	@Then("^I see internet connectivity error message$")
	public void ISeeInternetConnectivityErrorMessage() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage
				.isNoInternetMessageAppears());
		PagesCollection.loginPage.closeNoInternetDialog();
	}

	/**
	 * Clicks on Change password button on Sign In screen
	 * 
	 * @step. ^I select to Reset Password$
	 */
	@When("^I select to Reset Password$")
	public void ISelectToResetPassword() {
		PagesCollection.loginPage.forgotPassword();
	}

	/**
	 * Checks that opened page is Forgot Password page
	 * 
	 * @step. ^I see Forgot Password page in browser$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Forgot Password page in browser$")
	public void ISeeChangePasswordPageInBrowser() throws Exception {
		PagesCollection.loginPage.isForgotPasswordPageAppears();
	}

	/**
	 * Opens Forgot Password page on staging website
	 * 
	 * @step. ^I go to Forgot Password page$
	 * 
	 * @throws Exception
	 */
	@When("^I go to Forgot Password page$")
	public void IGoToForgotPasswordPage() throws Exception {
		PagesCollection.changePasswordPage = PagesCollection.loginPage
				.openStagingForgotPasswordPage();
	}

	/**
	 * Enters email to receive Change Password link
	 * 
	 * @step. ^I enter user email (.*) to change password$
	 * 
	 * @param email
	 *            user email string
	 * 
	 * @throws Exception
	 */
	@When("^I enter user email (.*) to change password$")
	public void IEnterEmailToChangePassword(String email) throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", email);
		PagesCollection.loginPage.setListener(IMAPSMailbox
				.getInstance().startMboxListener(expectedHeaders));
		PagesCollection.changePasswordPage
				.enterEmailForChangePasswordAndSubmit(email);
	}

	/**
	 * Searches for email message with Change Password link and opens it in
	 * Safari
	 * 
	 * @step. ^I open change password link from email$
	 * 
	 * @throws Exception
	 */
	@When("^I open change password link from email$")
	public void IOpenChangePasswordLinkFromEmail() throws Exception {
		PagesCollection.changePasswordPage = PagesCollection.loginPage
				.openResetPasswordPage();
	}

	/**
	 * Submits new password on Change Password page in Safari
	 * 
	 * @step. ^I reset password to (.*)$
	 * 
	 * @param password
	 *            new user password string
	 * 
	 * @throws AssertionError
	 *             when correct message about password changing does not appear
	 *             in browser
	 */
	@When("^I reset password to (.*)$")
	public void IEnterNewPasswordOnChangePasswordPage(String password)
			throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		Assert.assertTrue(PagesCollection.changePasswordPage
				.resetPasswordSetNew(password));
	}
}
