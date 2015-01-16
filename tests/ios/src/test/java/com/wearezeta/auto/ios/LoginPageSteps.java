package com.wearezeta.auto.ios;

import org.junit.Assert;

import java.io.IOException;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verifies whether sign in screen is the current screen
	 * 
	 * @step. I see sign in screen
	 */
	@Given("I see sign in screen")
	public void GiveniSeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

	/**
	 * Enter user email and password into corresponding fields on sign in screen
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
	public void GivenISignIn(String login, String password) throws IOException,
			InterruptedException {
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
		PagesCollection.loginPage = (LoginPage) (PagesCollection.loginPage
				.signIn());
		PagesCollection.loginPage.setLogin(login);
		PagesCollection.loginPage.setPassword(password);
		PagesCollection.contactListPage = (ContactListPage) (PagesCollection.loginPage
				.login());

		Assert.assertNotNull("Login not passed",
				PagesCollection.contactListPage);
	}

	/**
	 * Taps Sign In button on the corresponding screen and verifies whether an
	 * account is signed in properly
	 * 
	 * @step. I press Sign in button
	 * 
	 * @throws IOException
	 */
	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws IOException {
		PagesCollection.loginPage.signIn();
	}

	/**
	 * Taps Login button on the corresponding screen
	 * 
	 * @step. I press Login button
	 * 
	 * @throws IOException
	 */
	@When("I press Login button")
	public void WhenIPressSignInButtonAgain() throws IOException {
		PagesCollection.contactListPage = (ContactListPage) (PagesCollection.loginPage
				.login());
	}

	/**
	 * Taps Login button on the corresponding screen
	 * 
	 * @step. I attemt to press Login button
	 */
	@When("I attemt to press Login button")
	public void IAttemptToPressLoginButton() {
		PagesCollection.loginPage.clickLoginButton();
	}

	/**
	 * Taps Join button on Welcome page
	 * 
	 * @step. I press Join button
	 * 
	 * @throws IOException
	 */
	@When("I press Join button")
	public void WhenIPressJoinButton() throws IOException {
		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	/**
	 * Types login string into the corresponding input field on sign in page
	 * 
	 * @step. I have entered login (.*)
	 * 
	 * @param login
	 *            login string (usually it is user email)
	 * @throws IOException
	 */
	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) throws IOException {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setLogin(login);
	}

	/**
	 * Types password string into the corresponding input field on sign in page
	 * 
	 * @step. I have entered password (.*)
	 * 
	 * @param password
	 *            password string
	 * @throws IOException
	 */
	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws IOException {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setPassword(password);
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. I fill in email input (.*)
	 * 
	 * @param text
	 *            a string to type
	 */
	@When("I fill in email input (.*)")
	public void IFillInEmailInput(String text) throws IOException {
		PagesCollection.loginPage.setLogin(text);
	}

	/**
	 * Verifies whether login screen is the current screen
	 * 
	 * @step. ^I see login in screen$
	 * 
	 * @throws AssertionError
	 *             if current screen is not login screen
	 */
	@Then("^I see login in screen$")
	public void ThenISeeLogInScreen() {
		Assert.assertTrue("I don't see login screen",
				PagesCollection.loginPage.isLoginButtonVisible());
	}

	/**
	 * Tap and holds the "finger" for a while on email input field
	 * 
	 * @step. I tap and hold on Email input
	 */
	@When("I tap and hold on Email input")
	public void ITapHoldEmailInput() {
		PagesCollection.loginPage.tapHoldEmailInput();
	}

	/**
	 * Taps "Select All" item in popup menu of an input field
	 * 
	 * @step. I click on popup SelectAll item
	 */
	@When("I click on popup SelectAll item")
	public void IClickPopupSelectAll() {
		PagesCollection.loginPage.clickPopupSelectAllButton();
	}

	/**
	 * Taps "Copy" item in popup menu of an input field
	 * 
	 * @step. I click on popup Copy item
	 */
	@When("I click on popup Copy item")
	public void IClickPopupCopy() {
		PagesCollection.loginPage.clickPopupCopyButton();
	}

	/**
	 * Copies the content of Email input field into clipboard
	 * 
	 * @step. I copy email input field content
	 */
	@When("I copy email input field content")
	public void ICopyEmailInputContent() {
		PagesCollection.loginPage.tapHoldEmailInput();
		PagesCollection.loginPage.clickPopupSelectAllButton();
		PagesCollection.loginPage.clickPopupCopyButton();
	}

	/**
	 * Taps "Paste" item in popup menu of an input field
	 * 
	 * @step. I click on popup Paste item
	 */
	@When("I click on popup Paste item")
	public void IClickPopupPaste() {
		PagesCollection.loginPage.clickPopupPasteButton();
	}

	/**
	 * Taps "Terms of Service" link on Welcome screen
	 * 
	 * @step. ^I press Terms of Service link$
	 */
	@When("^I press Terms of Service link$")
	public void IPressTermsOfServiceLink() throws Throwable {
		PagesCollection.loginPage.openTermsLink();
	}

	/**
	 * Verifies whether the current page is "Terms and Conditions" page
	 * 
	 * @step. ^I see the terms info page$
	 * @throws AssertionError
	 *             if the current page is not "Terms and Conditions" page
	 */
	@Then("^I see the terms info page$")
	public void ISeeTheTermsInfoPage() throws Throwable {
		Assert.assertTrue("I don't see terms of service page",
				PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	/**
	 * Closes "Terms and Conditions" page to return back to Welcome page
	 * 
	 * @step. ^I return to welcome page$
	 * @throws AssertionError
	 *             if the current page is not "Welcome" page
	 */
	@When("^I return to welcome page$")
	public void IReturnToWelcomePage() throws Throwable {
		PagesCollection.loginPage.closeTermsPrivacyController();
		Assert.assertTrue("I don't see login screen",
				PagesCollection.loginPage.isLoginButtonVisible());
	}

	/**
	 * Taps "Privacy Policy" link on Welcome page
	 * 
	 * @step. ^I press Privacy Policy link$
	 */
	@When("^I press Privacy Policy link$")
	public void IPressPrivacyPolicyLink() throws Throwable {
		PagesCollection.loginPage.openPrivacyLink();
	}

	/**
	 * Verifies whether the current page is "Privacy Info" page
	 * 
	 * @step. ^I see the privacy info page$
	 * 
	 * @throws AssertionError
	 *             if the current page differs from "Privacy Info" page
	 */
	@Then("^I see the privacy info page$")
	public void ISeeThePrivacyInfoPage() throws Throwable {
		Assert.assertTrue("I don't see privacy policy page",
				PagesCollection.loginPage.isTermsPrivacyColseButtonVisible());
		// TODO:verify correct content as far as copywrite is in
	}

	/**
	 * Enters given text into email input field and taps password field
	 * 
	 * @step. ^I enter wrong email (.*)
	 * @param wrongMail
	 *            text to enter into email input field
	 */
	@When("^I enter wrong email (.*)")
	public void IEnterWrongEmail(String wrongMail) throws IOException {
		PagesCollection.loginPage.setLogin(wrongMail);
		PagesCollection.loginPage.tapPasswordField();
	}

	/**
	 * Verifies whether error message about email field is visible
	 * 
	 * @step. ^I see error with email notification$
	 * @throws AssertionError
	 *             if error notification is not visible
	 */
	@Then("^I see error with email notification$")
	public void ISeeErrorWithEmailNotification() {
		Assert.assertTrue("I don't see error mail notification",
				PagesCollection.loginPage.errorMailNotificationIsShown());
	}

	/**
	 * Verifies whether error message about email field is NOT visible
	 * 
	 * @step. ^I see no error notification$
	 * @throws AssertionError
	 *             if error notification is visible
	 */
	@Then("^I see no error notification$")
	public void ISeeNoErrorNotification() {
		Assert.assertFalse("I see error mail notification",
				PagesCollection.loginPage.errorMailNotificationIsNotShown());
	}

	/**
	 * Enters a text into password input field
	 * 
	 * @step. ^I enter wrong password (.*)
	 */
	@When("^I enter wrong password (.*)")
	public void IEnterWrongPassword(String wrongPassword) throws IOException {
		PagesCollection.loginPage.setPassword(wrongPassword);
	}

	/**
	 * Verifies whether the notification about wrong credentials exists on the
	 * current screen
	 * 
	 * @step. ^I see wrong credentials notification$
	 */
	@Then("^I see wrong credentials notification$")
	public void ISeeWrongCredentialsNotification() {
		Assert.assertTrue("I don't see wrong credentials notification",
				PagesCollection.loginPage.wrongCredentialsNotificationIsShown());
	}

}
