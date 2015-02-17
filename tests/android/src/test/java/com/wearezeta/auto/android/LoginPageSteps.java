package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("I see sign in screen")
	public void GiveniSeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

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

	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws IOException {
		PagesCollection.loginPage.SignIn();
	}
	
	@When("I press FORGOT PASSWORD button")
	public void WhenIPressForgotPasswordButton() throws Exception {
		PagesCollection.settingsPage = PagesCollection.loginPage.forgotPassword();
	}

	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.loginPage.LogIn();
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
	}

	@When("I press Join button")
	public void WhenIPressJoinButton() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setLogin(login);
	}

	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password)
			throws Exception {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.loginPage.setPassword(password);
	}

	@When("^I see sign in and join buttons$")
	public void WhenISeeSignInAndJoinButton() {
		Assert.assertTrue("We don't see sign in buttons",
				PagesCollection.loginPage.isWelcomeButtonsExist());
	}

	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
		Assert.assertTrue("We don't see welcome buttons",
				PagesCollection.loginPage.isWelcomeButtonsExist());
	}

	@Then("^Login error message appears$")
	public void LoginErrorMessage() throws Exception {
		PagesCollection.loginPage.waitForLogin();
		Assert.assertTrue("Error message not shown",
				PagesCollection.loginPage.isLoginError());
	}

	@Then("^Contains wrong name or password text$")
	public void LoginErrorMessageText() throws Exception {
		PagesCollection.loginPage.waitForLogin();
		Assert.assertTrue("Text in error message is not as expected",
				PagesCollection.loginPage.isLoginErrorTextOk());
	}

}
