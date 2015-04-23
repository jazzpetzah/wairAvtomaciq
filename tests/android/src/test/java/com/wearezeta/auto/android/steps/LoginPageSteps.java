package com.wearezeta.auto.android.steps;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks to see if the sign in screen appears
	 * 
	 * -unnecessary the next step would fail if this page wasn't visible?
	 * 
	 * @step. ^I see sign in screen$
	 */
	@Given("^I see sign in screen$")
	public void GiveniSeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

	/**
	 * Inputs the login details for the given user and then clicks the sign in
	 * button Also sets the contactList page in the pagesObject
	 * 
	 * Should perhaps be broken into smaller steps?
	 * 
	 * @see #WhenIHaveEnteredPassword(String)
	 * @see #WhenIHaveEnteredLogin(String)
	 * @see #WhenIPressLogInButton()
	 * 
	 * @step. ^I Sign in using login (.*) and password (.*)$
	 * 
	 * @param login
	 * @param password
	 * @throws Exception
	 */
	@Given("^I Sign in using login (.*) and password (.*)$")
	public void GivenISignIn(String login, String password) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		PagesCollection.loginPage.SignIn();
		PagesCollection.loginPage.setLogin(login);
		PagesCollection.loginPage.setPassword(password);
		PagesCollection.contactListPage = (ContactListPage) (PagesCollection.loginPage
				.LogIn());
		Assert.assertTrue("Login in progress",
				PagesCollection.loginPage.waitForLoginScreenDisappear());
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Presses the sign in button on the welcome page. Note, this is not the
	 * button used for signing in once details have been placed in.
	 * 
	 * @step. ^I press Sign in button$
	 * 
	 * @throws IOException
	 */
	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws IOException {
		PagesCollection.loginPage.SignIn();
	}

	/**
	 * Presses the "forgot password" button
	 * 
	 * @step. ^I press FORGOT PASSWORD button$
	 * 
	 * @throws Exception
	 */
	@When("I press FORGOT PASSWORD button")
	public void WhenIPressForgotPasswordButton() throws Exception {
		PagesCollection.commonAndroidPage = PagesCollection.loginPage
				.forgotPassword();
	}

	/**
	 * Presses the Log in Button underneath the email and password fields
	 * 
	 * @step. ^I press FORGOT PASSWORD button$
	 * 
	 * @throws Exception
	 */
	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.loginPage.LogIn();
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Presses the Join button to begin the registration process
	 * 
	 * @step. ^I press Join button$
	 */
	@When("I press Join button")
	public void WhenIPressJoinButton() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	/**
	 * Types an email address into the email login field
	 * 
	 * @step. ^I have entered login (.*)$
	 * 
	 * @param login
	 * @throws Exception
	 */
	@When("^I have entered login (.*)$")
	public void WhenIHaveEnteredLogin(String login) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setLogin(login);
	}

	/**
	 * Enters a password into the password login field
	 * 
	 * @step. ^I have entered password (.*)$
	 * 
	 * @param password
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
	 * -unused
	 * 
	 * @step. ^I see sign in and join buttons$
	 */
	@When("^I see sign in and join buttons$")
	public void WhenISeeSignInAndJoinButton() {
		Assert.assertTrue("We don't see sign in buttons",
				PagesCollection.loginPage.isWelcomeButtonsExist());
	}

	/**
	 * Checks to see that the welcome screen is visible
	 * 
	 * @step. ^I see sign in and join buttons$
	 */
	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
		Assert.assertTrue("We don't see welcome buttons",
				PagesCollection.loginPage.isWelcomeButtonsExist());
	}

	/**
	 * Checks to see the login error message appears for false credentials
	 * 
	 * @step. ^I see sign in and join buttons$
	 * 
	 * @throws Exception
	 */
	@Then("^Login error message appears$")
	public void LoginErrorMessage() throws Exception {
		PagesCollection.loginPage.waitForLogin();
		Assert.assertTrue("Error message not shown",
				PagesCollection.loginPage.isLoginError());
	}

	/**
	 * Checks to see that the login error message contains the correct text
	 * 
	 * -unnecessary should perhaps be combined with LoginErrorMessage()
	 * 
	 * @step. ^Contains wrong name or password text$
	 * 
	 * @throws Exception
	 */
	@Then("^Contains wrong name or password text$")
	public void LoginErrorMessageText() throws Exception {
		PagesCollection.loginPage.waitForLogin();
		Assert.assertTrue("Text in error message is not as expected",
				PagesCollection.loginPage.isLoginErrorTextOk());
	}

}
