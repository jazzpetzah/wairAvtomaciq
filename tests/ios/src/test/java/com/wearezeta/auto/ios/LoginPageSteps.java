package com.wearezeta.auto.ios;

import org.junit.Assert;

import java.io.IOException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @Given("^I Sign in using login (.*) and password (.*)$")
	 public void GivenISignIn(String login, String password) throws IOException, InterruptedException  {		
		 switch (login) {
		 case CommonUtils.YOUR_USER_1:
			 login = CommonUtils.yourUsers.get(0).getEmail();
			 break;
		 case CommonUtils.YOUR_USER_2:
			 login = CommonUtils.yourUsers.get(1).getEmail();
			 break;
		 case CommonUtils.YOUR_UNCONNECTED_USER:
			 login = CommonUtils.yourUsers.get(2).getEmail();
			 break;
		 case CommonUtils.CONTACT_1:
			 login = CommonUtils.contacts.get(0).getEmail();
			 break;
		 case CommonUtils.CONTACT_2:
			 login = CommonUtils.contacts.get(1).getEmail();
			 break;
		 case CommonUtils.CONTACT_3:
			 login = CommonUtils.contacts.get(2).getEmail();
			 break;
		 }
		 
		 password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		 PagesCollection.loginPage = (LoginPage)(PagesCollection.loginPage.signIn());
		 PagesCollection.loginPage.setLogin(login);
		 PagesCollection.loginPage.setPassword(password);
		 PagesCollection.contactListPage = (ContactListPage)(PagesCollection.loginPage.login());
		 
		 //Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	}
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {
		 
		 PagesCollection.loginPage.signIn();
	 }
	 
	 @When("I press Login button")
	 public void WhenIPressSignInButtonAgain() throws IOException {
		 
		 PagesCollection.loginPage.login();
	 }
	 
	 @When("I press Join button")
	 public void WhenIPressJoinButton() throws IOException {
		 
		 PagesCollection.registrationPage = PagesCollection.loginPage.join();
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String value) {
		 
		 if (value.equals(CommonUtils.YOUR_USER_1)) {
			 value = CommonUtils.yourUsers.get(0).getEmail();
		 }
		 PagesCollection.loginPage.setLogin(value);
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String value) {
		 
		 value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		 PagesCollection.loginPage.setPassword(value);
	 }	
	  
	 @Then("^I see login in screen$")
	 public void ThenISeeLogInScreen() {
		 Assert.assertTrue("I don't see login screen", PagesCollection.loginPage.isLoginButtonVisible());
	 }
	 
	 

}
