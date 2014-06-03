package com.wearezeta.auto.android;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;

public class SignInSteps{

	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {

		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
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
	 
	 @Then ("Contact list appears with my name (.*)")
	 public void ThenContactListAppears(String value) {
		 
		 Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
		 
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));
		 
	 }

}
