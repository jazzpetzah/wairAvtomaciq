package com.wearezeta.auto.osx.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.pages.ChangePasswordPage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;

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

		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		loginPage.startSignIn();

		loginPage.setLogin(login);
		loginPage.setPassword(password);

		loginPage.confirmSignIn();

		Assert.assertTrue("Failed to login", loginPage.waitForLogin());

		CommonOSXSteps.senderPages
				.setContactListPage(new ContactListPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(ContactListPage.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(ContactListPage.class)));
	}

	/**
	 * Clicks on Sign In button on Welcome screen and opens Sign In screen
	 * 
	 * @step. I start Sign In
	 * 
	 * @throws Exception
	 */
	@When("I start Sign In")
	public void WhenIStartSignIn() {
		CommonOSXSteps.senderPages.getLoginPage().startSignIn();
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
		OSXPage page = CommonOSXSteps.senderPages.getLoginPage()
				.confirmSignIn();
		Assert.assertNotNull(
				"After sign in button click Login page or Contact List page should appear. Page couldn't be null",
				page);
		if (page instanceof ContactListPage) {
			CommonOSXSteps.senderPages
					.setContactListPage((ContactListPage) page);
		}
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
		CommonOSXSteps.senderPages.getLoginPage().setLogin(login);
	}

	/**
	 * Enters password in corresponding field on Sign In page
	 * 
	 * @step. I have entered password (.*)
	 * 
	 * @param password
	 *            user password string
	 */
	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getLoginPage().setPassword(password);
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
		Assert.assertNotNull(CommonOSXSteps.senderPages.getLoginPage()
				.isVisible());
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
		CommonOSXSteps.senderPages.getLoginPage().logoutIfNotSignInPage();
		CommonOSXSteps.resetBackendSettingsIfOverwritten();
	}

	/**
	 * When called after logout, checks that Sign In screen is opened
	 * 
	 * @step. I have returned to Sign In screen
	 */
	@Then("I have returned to Sign In screen")
	public void ThenISeeSignInScreen() {
		Assert.assertTrue("Failed to logout", CommonOSXSteps.senderPages
				.getContactListPage().waitForSignOut());
		Assert.assertTrue(CommonOSXSteps.senderPages.getContactListPage()
				.isSignOutFinished());
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
		RegistrationPage registration = CommonOSXSteps.senderPages
				.getLoginPage().startRegistration();
		CommonOSXSteps.senderPages.setRegistrationPage(registration);
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
		CommonOSXSteps.senderPages.getLoginPage().setPasswordUsingScript(
				password);
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
		Assert.assertTrue(CommonOSXSteps.senderPages.getLoginPage()
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
		Assert.assertFalse(CommonOSXSteps.senderPages.getLoginPage()
				.isWrongCredentialsMessageDisplayed());
	}

	/**
	 * Checks that No internet connection error appears when internet is blocked
	 * 
	 * @step. ^I see internet connectivity error message$
	 * 
	 * @throws AssertionError
	 *             if there is no message about internet connection error
	 */
	@Then("^I see internet connectivity error message$")
	public void ISeeInternetConnectivityErrorMessage() {
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		Assert.assertTrue(loginPage.isNoInternetMessageAppears());
		loginPage.closeNoInternetDialog();
	}

	/**
	 * Clicks on Change password button on Sign In screen
	 * 
	 * @step. ^I select to Reset Password$
	 */
	@When("^I select to Reset Password$")
	public void ISelectToResetPassword() {
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		loginPage.forgotPassword();
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
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		loginPage.isForgotPasswordPageAppears();
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
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		CommonOSXSteps.senderPages.setChangePasswordPage(loginPage
				.openStagingForgotPasswordPage());
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

		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();

		ChangePasswordPage changePasswordPage = CommonOSXSteps.senderPages
				.getChangePasswordPage();

		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", email);
		loginPage.setListener(IMAPSMailbox.createDefaultInstance()
				.startMboxListener(expectedHeaders));
		changePasswordPage.enterEmailForChangePasswordAndSubmit(email);
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
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		CommonOSXSteps.senderPages.setChangePasswordPage(loginPage
				.openResetPasswordPage());
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

		ChangePasswordPage changePasswordPage = CommonOSXSteps.senderPages
				.getChangePasswordPage();

		Assert.assertTrue(changePasswordPage.resetPasswordSetNew(password));
	}
}
