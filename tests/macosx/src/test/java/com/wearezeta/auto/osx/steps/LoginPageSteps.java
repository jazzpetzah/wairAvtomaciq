package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.OSXPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {
	@Given ("I Sign in using login (.*) and password (.*)")
	public void GivenISignInUsingLoginAndPassword(String login, String password) throws IOException {
		 if (login.equals(CommonUtils.YOUR_USER_1)) {
			 login = CommonUtils.yourUsers.get(0).getEmail();
		 }
		password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		try {
			LoginPage loginPage = CommonSteps.senderPages.getLoginPage();
			Assert.assertNotNull(loginPage.isVisible());
			loginPage.SignIn();
			loginPage.setLogin(login);
			loginPage.setPassword(password);
			loginPage.SignIn();
			Assert.assertTrue("Failed to login", loginPage.waitForLogin());
		} catch (NoSuchElementException e) { }
		
		CommonSteps.senderPages.setContactListPage(new ContactListPage(
				CommonUtils.getUrlFromConfig(ContactListPage.class),
				CommonUtils.getAppPathFromConfig(ContactListPage.class)));
	 }
	
	 @When("I press Sign In button")
	 public void WhenIPressSignInButton() throws IOException {

		 OSXPage page = CommonSteps.senderPages.getLoginPage().SignIn();
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
	 public void GivenIAmSignedOutFromZClient() throws MalformedURLException, IOException {
		 CommonSteps.senderPages.setContactListPage(new ContactListPage(
					CommonUtils.getUrlFromConfig(ContactListPage.class),
					CommonUtils.getAppPathFromConfig(ContactListPage.class)));

		int num = CommonSteps.senderPages.getContactListPage().numberOfContacts();
		if (num > 0) {
			CommonSteps.senderPages.getContactListPage().SignOut();
		}
	 }
	 
	 @Then("I have returned to Sign In screen")
	 public void ThenISeeSignInScreen() {
		 Assert.assertTrue("Failed to logout", CommonSteps.senderPages.getContactListPage().waitForSignOut());
		 Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isSignOutFinished());
	 }
}
