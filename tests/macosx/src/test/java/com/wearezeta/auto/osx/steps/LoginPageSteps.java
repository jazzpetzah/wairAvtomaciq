package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.RegistrationPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {
	private static final Logger log = ZetaLogger.getLog(LoginPageSteps.class.getSimpleName());
	
	@Given ("I Sign in using login (.*) and password (.*)")
	public void GivenISignInUsingLoginAndPassword(String login, String password) throws IOException {
		login = CommonUtils.retrieveRealUserContactPasswordValue(login);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(login.toLowerCase())) {
				login = user.getEmail();
				password = CommonUtils.retrieveRealUserContactPasswordValue(password);
				break;
			}
		}
		
		for (ClientUser user: CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(login.toLowerCase())) {
				login = user.getEmail();
				password = CommonUtils.retrieveRealUserContactPasswordValue(password);
				break;
			}
		}
		
		log.debug("Starting to Sign in using login " + login + " and password " + password);

		try {
			LoginPage loginPage = CommonSteps.senderPages.getLoginPage();
			loginPage.startSignIn();
			
			loginPage.setLogin(login);
			loginPage.setPassword(password);

			loginPage.confirmSignIn();
			
			Assert.assertTrue("Failed to login", loginPage.waitForLogin());
		} catch (NoSuchElementException e) { }
		
		CommonSteps.senderPages.setContactListPage(new ContactListPage(
				CommonUtils.getOsxAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils.getOsxApplicationPathFromConfig(ContactListPage.class)));
	 }
	
	@When("I start Sign In") 
	public void WhenIStartSignIn() {
		CommonSteps.senderPages.getLoginPage().startSignIn();
	}
	
	 @When("I press Sign In button")
	 public void WhenIPressSignInButton() throws IOException {
		 OSXPage page = CommonSteps.senderPages.getLoginPage().confirmSignIn();
		 Assert.assertNotNull("After sign in button click Login page or Contact List page should appear. Page couldn't be null", page);
		 if (page instanceof ContactListPage) {
			 CommonSteps.senderPages.setContactListPage((ContactListPage)page);
		 }
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String login) {
		 if (login.equals(CommonUtils.YOUR_USER_1)) {
			 login = CommonUtils.yourUsers.get(0).getEmail();
		 }
		 CommonSteps.senderPages.getLoginPage().setLogin(login);
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String password) {
		 password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		 CommonSteps.senderPages.getLoginPage().setPassword(password);
	 }
	 
	 @Given ("I see Sign In screen")
	 public void GivenISeeSignInScreen() {
		 Assert.assertNotNull(CommonSteps.senderPages.getLoginPage().isVisible());
	 }
	 
	 @Given ("I am signed out from ZClient")
	 public void GivenIAmSignedOutFromZClient() throws Exception {
		 CommonSteps.senderPages.getLoginPage().logoutIfNotSignInPage();
	 }
	 
	 @Then("I have returned to Sign In screen")
	 public void ThenISeeSignInScreen() {
		 Assert.assertTrue("Failed to logout", CommonSteps.senderPages.getContactListPage().waitForSignOut());
		 Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isSignOutFinished());
	 }
	 
	 @When("I start registration")
	 public void IStartRegistration() throws MalformedURLException {
		 RegistrationPage registration = CommonSteps.senderPages.getLoginPage().startRegistration();
		 CommonSteps.senderPages.setRegistrationPage(registration);
	 }
}
