package com.wearezeta.auto.android;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.*;
import cucumber.api.java.en.*;

public class SignInSteps {
	
	 private LoginPage page;
	 
	 @Before
	 public void setUp() throws Exception {
		 
	        File app = new File(CommonUtils.getAppPathFromConfig(SignInSteps.class));
	        String path = app.getAbsolutePath();

	        page = new LoginPage(CommonUtils.getUrlFromConfig(SignInSteps.class), path);
	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 page.Close();
	 }
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {

		 Assert.assertNotNull(page.isVisible());
	 }
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() throws IOException {
		 
		 page.SignIn();
	 }
	 
	 @When ("I have entered login (.*)")
	 public void WhenIHaveEnteredLogin(String value) {
		 
		 page.setLogin(value);
	 }
	 
	 @When ("I have entered password (.*)")
	 public void WhenIHaveEnteredPassword(String value) {
		 
		 page.setPassword(value);
	 }
	 
	 @Then ("Contact list appears with my name (.*)")
	 public void ThenContactListAppears(String value) {
		 
		 Assert.assertTrue("Login finished", page.waitForLogin());
		 
		 Assert.assertTrue(page.isLoginFinished(value));
		 
	 }

}
