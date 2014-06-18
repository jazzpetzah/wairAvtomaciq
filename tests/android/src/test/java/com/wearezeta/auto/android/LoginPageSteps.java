package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.*;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @Given("^I Sign in using login (.*) and password (.*)$")
	 public void GivenISignIn(String login,String password) throws IOException  {
		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		 PagesCollection.loginPage =(LoginPage)(PagesCollection.loginPage.SignIn());
		 if(login.contains(CommonUtils.YOUR_USER_1)){
			 PagesCollection.loginPage.setLogin(CommonUtils.yourUsers.get(0).getEmail());
		 }
		 else
		 {
			 PagesCollection.loginPage.setLogin(login);
		 }
		 if(password.contains(CommonUtils.YOUR_PASS)){
			 PagesCollection.loginPage.setPassword(CommonUtils.yourUsers.get(0).getPassword());
		 }
		 else{
			 PagesCollection.loginPage.setPassword(password);
		 }
		 PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.SignIn());
		 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	 }
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {	 
		 PagesCollection.loginPage.SignIn();
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String login) {
		 if(login.contains(CommonUtils.YOUR_USER_1)){
			 PagesCollection.loginPage.setLogin(CommonUtils.yourUsers.get(0).getEmail());
		 }
		 else{
			 PagesCollection.loginPage.setLogin(login);
		 }
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String password) {
		 if(password.contains(CommonUtils.YOUR_PASS)){
			 PagesCollection.loginPage.setPassword(CommonUtils.yourUsers.get(0).getPassword());
		 }
		 else{
			 PagesCollection.loginPage.setPassword(password);
		 }
	 }
	 
	 @Then("^I see welcome screen$")
		public void ThenISeeWelcomeScreen() {
		    Assert.assertTrue("We don't see welcome buttons", PagesCollection.loginPage.isWelcomeButtonsExist());
		}

}
