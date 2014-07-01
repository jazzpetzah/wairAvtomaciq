package com.wearezeta.auto.ios;

import org.junit.Assert;

import java.io.IOException;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @Given("^Users are generated who I am not connected to$")
	 public void GivenNonConnectedUsers()
	 {
		 ClientUser unconnectedUser = new ClientUser("unconnectedTest:"+CommonUtils.generateGUID().toString()+"@wearezeta.com", "password123","user:"+CommonUtils.generateGUID().toString(), UsersState.Created);
		 
		 
		 BackEndREST.registerNewUser("example@example.com", "userName", "password");
		 CreateZetaUser.activateRegisteredUser("example@example.com",10, "userName","password");
	 }
	 
	 @Given("^I Sign in using login (.*) and password (.*)$")
	 public void GivenISignIn(String login, String password) throws IOException  {		
		 
		 if (login.equals(CommonUtils.YOUR_USER_1)) {
			 login = CommonUtils.yourUsers.get(0).getEmail();
		 }
		 password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		 PagesCollection.loginPage =(LoginPage)(PagesCollection.loginPage.signIn());
		 PagesCollection.loginPage.setLogin(login);
		 PagesCollection.loginPage.setPassword(password);
		 PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.signIn());
		 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	}
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {
		 
		 PagesCollection.loginPage.signIn();
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
