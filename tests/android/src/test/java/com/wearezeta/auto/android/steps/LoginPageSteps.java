package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks to see if the start screen appears
	 * 
	 * @step. ^I see start screen$
	 * @throws Exception
	 */
	@Given("^I see start screen$")
	public void GiveniSeeSignInScreen() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage.waitForInitialScreen());
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
		Assert.assertTrue("Welcome page is not visible",
				PagesCollection.loginPage.waitForInitialScreen());
		PagesCollection.loginPage.switchToEmailSignIn();
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
	 * Press the "I have an account" button on the welcome page. to switch to
	 * sign in using email address
	 * 
	 * @step. ^I switch to email sign in screen$
	 * @throws Exception
	 */
	@When("^I switch to email sign in screen$")
	public void ISwitchToEmailSignIn() throws Exception {
		PagesCollection.loginPage.switchToEmailSignIn();
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
	 * Checks to see that the welcome screen is visible
	 * 
	 * @step. ^I see sign in and join buttons$
	 * @throws Exception
	 */
	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue("We don't see welcome buttons",
				PagesCollection.loginPage.waitForInitialScreen());
	}

	/**
	 * Checks to see that the login error message contains the correct text
	 * 
	 * @step. ^I see error message \"(.*)\"$
	 * @param expectedMsg
	 *            the expected error message
	 * 
	 * @throws Exception
	 */
	@Then("^I see error message \"(.*)\"$")
	public void ISeeErrorMessage(String expectedMsg) throws Exception {
		PagesCollection.loginPage.waitForLogin();
		PagesCollection.loginPage.verifyErrorMessageText(expectedMsg);
	}

}
