package com.wearezeta.auto.osx.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.common.InputMethodEnum;
import com.wearezeta.auto.osx.common.LoginBehaviourEnum;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.UserProfilePage;
import com.wearezeta.auto.osx.pages.common.NoInternetConnectionPage;
import com.wearezeta.auto.osx.pages.welcome.LoginPage;

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
	public void ISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		log.debug(String.format("Sign in using email %s and password %s",
				login, password));
		WelcomePageSteps welcomePageSteps = new WelcomePageSteps();
		welcomePageSteps.IStartSignIn();
		this.ITypeLogin(login);
		this.ITypePassword(password);
		this.IPressSignInButton();

		Assert.assertTrue(String.format(
				"Failed to sign in using email %s and password %s", login,
				password), PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Clicks on Sign In button and submits entered credentials
	 * 
	 * @step. ^I press [Ss]ign [Ii]n button$
	 * 
	 * @throws Exception
	 */
	@When("^I press [Ss]ign [Ii]n button$")
	public void IPressSignInButton() throws Exception {
		ISignInExpectingResult(LoginBehaviourEnum.SUCCESSFUL.getResult());
	}

	@When("^I [Ss]ign [Ii]n expecting (sucessful login|error|[Nn]o [Ii]nternet message)$")
	public void ISignInExpectingResult(String result) throws Exception {
		OSXPage page = null;
		for (LoginBehaviourEnum value : LoginBehaviourEnum.values()) {
			if (value.getResult().toLowerCase().equals(result.toLowerCase())) {
				page = PagesCollection.loginPage.signIn(value);
			}
		}
		if (page instanceof ContactListPage) {
			PagesCollection.contactListPage = (ContactListPage) page;
			PagesCollection.userProfilePage = new UserProfilePage(
					PagesCollection.contactListPage.getDriver(),
					PagesCollection.contactListPage.getWait());
		} else if (page instanceof NoInternetConnectionPage) {
			PagesCollection.noInternetPage = (NoInternetConnectionPage) page;
		} else if (page instanceof LoginPage) {
			PagesCollection.loginPage = (LoginPage) page;
		}
	}

	/**
	 * Enters login in corresponding field on Sign In page
	 * 
	 * @step. ^I type login (.*)$
	 * 
	 * @param login
	 *            user login string
	 */
	@When("^I type login (.*)$")
	public void ITypeLogin(String login) {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			try {
				// search for email by name aliases in case name is specified
				login = usrMgr.findUserByNameOrNameAlias(login).getEmail();
			} catch (NoSuchUserException ex) {
			}
		}

		PagesCollection.loginPage.typeEmail(login);
	}

	/**
	 * Enters password in corresponding field on Sign In page (this step doesn't
	 * support password with spaces in it, use full version: 'I type password
	 * (.*) by sending keys' for password with spaces testing)
	 * 
	 * @step. ^I type password (\\S*)$
	 * 
	 * @param password
	 *            user password string
	 *
	 * @throws Exception
	 */
	@When("^I type password (\\S*)$")
	public void ITypePassword(String password) throws Exception {
		ITypePasswordByMode(password, InputMethodEnum.SEND_KEYS.getMethod());
	}

	/**
	 * Enters password in corresponding field on Sign In page using specified
	 * input mode
	 * 
	 * @step. ^I type password (.*) by (sending keys|AppleScript)$
	 * 
	 * @param password
	 *            user password string
	 * @param mode
	 *            set value mode ('sending keys' - sends text using
	 *            WebElement.sendKeys() method | 'AppleScript')
	 *
	 * @throws Exception
	 */
	@When("^I type password (.*) by (sending keys|AppleScript)$")
	public void ITypePasswordByMode(String password, String mode)
			throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
		}
		for (InputMethodEnum method : InputMethodEnum.values()) {
			if (method.getMethod().toLowerCase().equals(mode.toLowerCase())) {
				PagesCollection.loginPage.typePassword(password, method);
			}
		}
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
		PagesCollection.loginPage.setPasswordResetMessage(IMAPSMailbox
				.getInstance().getMessage(expectedHeaders,
						BackendAPIWrappers.UI_ACTIVATION_TIMEOUT));
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
