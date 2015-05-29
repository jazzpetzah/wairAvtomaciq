package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import registration.AddPhoneNumberPage;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EmailSignInSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
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
		(new WelcomePageSteps()).ISwitchToEmailSignIn();
		PagesCollection.emailSignInPage.setLogin(login);
		PagesCollection.emailSignInPage.setPassword(password);
		
		AndroidPage returnedPage = PagesCollection.emailSignInPage
			.logIn();
		
		// We want to skip the "AddPhoneNumber page if it is presented to us
		ContactListPage contactListPage;
		if (returnedPage instanceof AddPhoneNumberPage) {
			contactListPage = ((AddPhoneNumberPage) returnedPage).notNowButtonClick();
		} else {
			contactListPage = (ContactListPage) returnedPage;
		}
		
		PagesCollection.contactListPage = contactListPage;
		
		Assert.assertTrue("Login in progress",
				PagesCollection.loginPage.waitForLoginScreenDisappear());
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
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
		PagesCollection.emailSignInPage.setLogin(login);
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
		PagesCollection.emailSignInPage.setPassword(password);
	}
	
	/**
	 * Presses the Log in Button underneath the email and password fields
	 * 
	 * @step. ^I press Log in button$
	 * 
	 * @throws Exception
	 */
	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.emailSignInPage.logIn();
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
	}
	
	/**
	 * Checks to see that the login error message contains the correct text
	 * After providing a false email address or password
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
