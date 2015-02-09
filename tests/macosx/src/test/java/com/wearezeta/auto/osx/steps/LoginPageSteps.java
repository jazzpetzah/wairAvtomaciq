package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
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

	@Given("I Sign in using login (.*) and password (.*)")
	public void GivenISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			try {
				//search for email by name aliases in case name is specified
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

	@When("I start Sign In")
	public void WhenIStartSignIn() {
		CommonOSXSteps.senderPages.getLoginPage().startSignIn();
	}

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

	@When("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getLoginPage().setLogin(login);
	}

	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) {
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getLoginPage().setPassword(password);
	}

	@Given("I see Sign In screen")
	public void GivenISeeSignInScreen() {
		Assert.assertNotNull(CommonOSXSteps.senderPages.getLoginPage()
				.isVisible());
	}

	@Given("I am signed out from ZClient")
	public void GivenIAmSignedOutFromZClient() throws Exception {
		CommonOSXSteps.senderPages.getLoginPage().logoutIfNotSignInPage();
		 CommonOSXSteps.resetBackendSettingsIfOverwritten();
	}

	@Then("I have returned to Sign In screen")
	public void ThenISeeSignInScreen() {
		Assert.assertTrue("Failed to logout", CommonOSXSteps.senderPages
				.getContactListPage().waitForSignOut());
		Assert.assertTrue(CommonOSXSteps.senderPages.getContactListPage()
				.isSignOutFinished());
	}

	@When("I start registration")
	public void IStartRegistration() throws Exception {
		RegistrationPage registration = CommonOSXSteps.senderPages
				.getLoginPage().startRegistration();
		CommonOSXSteps.senderPages.setRegistrationPage(registration);
	}
	
	@When("I input password (.*) using script")
	public void IInputPasswordUsingScript(String password) {
		CommonOSXSteps.senderPages.getLoginPage().setPasswordUsingScript(password);
	}

	@Then("I see wrong credentials message")
	public void ISeeWrongCredentialsMessage() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getLoginPage().isWrongCredentialsMessageDisplayed());
	}
	
	@Then("^I do not see wrong credentials message$")
	public void IDoNotSeeWrongCredentialsMessage() {
		Assert.assertFalse(CommonOSXSteps.senderPages.getLoginPage().isWrongCredentialsMessageDisplayed());
	}
	
	@Then("^I see internet connectivity error message")
	public void ISeeInternetConnectivityErrorMessage() {
		LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
		Assert.assertTrue(loginPage.isNoInternetMessageAppears());
		loginPage.closeNoInternetDialog();
	}
}
