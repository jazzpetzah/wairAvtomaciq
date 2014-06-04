package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {
	
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {

		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @Given("^I Sign in using login (.*) and password (.*)$")
	 public void GivenISignIn(String login, String password) throws IOException  {
			
			 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
			 PagesCollection.loginPage =(LoginPage)(PagesCollection.loginPage.SignIn());
			 PagesCollection.loginPage.setLogin(login);
			 PagesCollection.loginPage.setPassword(password);
			 PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.SignIn());
			 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	 }
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {
		 
		 PagesCollection.loginPage.SignIn();
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String value) {
		 
		 PagesCollection.loginPage.setLogin(value);
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String value) {
		 
		 PagesCollection.loginPage.setPassword(value);
	 }
	 
	 @Then("^I see welcome screen$")
		public void ThenISeeWelcomeScreen() {
		    Assert.assertTrue("We see welcome buttons", PagesCollection.loginPage.isWelcomeButtonsExist());
		}

}
