package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.UsersManager;
import com.wearezeta.auto.user_management.UsersManager.UserAliasType;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {
	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class
			.getSimpleName());
	private final UsersManager usrMgr = UsersManager.getInstance();

	@Given("I Sign in using login (.*) and password (.*)")
	public void GivenISignInUsingLoginAndPassword(String login, String password)
			throws IOException {
		ClientUser dstUser = usrMgr.findUserByNameAlias(login);
		login = dstUser.getEmail();
		password = dstUser.getPassword();

		log.debug("Starting to Sign in using login " + login + " and password "
				+ password);

		try {
			LoginPage loginPage = CommonOSXSteps.senderPages.getLoginPage();
			loginPage.startSignIn();

			loginPage.setLogin(login);
			loginPage.setPassword(password);

			loginPage.confirmSignIn();

			Assert.assertTrue("Failed to login", loginPage.waitForLogin());
		} catch (NoSuchElementException e) {
		}

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
	public void WhenIPressSignInButton() throws IOException {
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
		login = usrMgr.findUserByNameAlias(login).getEmail();
		CommonOSXSteps.senderPages.getLoginPage().setLogin(login);
	}

	@When("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) {
		password = usrMgr.findUserByAlias(password, UserAliasType.PASSWORD)
				.getPassword();
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
	}

	@Then("I have returned to Sign In screen")
	public void ThenISeeSignInScreen() {
		Assert.assertTrue("Failed to logout", CommonOSXSteps.senderPages
				.getContactListPage().waitForSignOut());
		Assert.assertTrue(CommonOSXSteps.senderPages.getContactListPage()
				.isSignOutFinished());
	}

	@When("I start registration")
	public void IStartRegistration() throws MalformedURLException {
		RegistrationPage registration = CommonOSXSteps.senderPages
				.getLoginPage().startRegistration();
		CommonOSXSteps.senderPages.setRegistrationPage(registration);
	}
}
