package com.wearezeta.auto.ios;

import java.io.File;

import org.junit.Assert;

import com.wearezeta.auto.ios.SignInSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.LoginPage;

import cucumber.api.java.*;
import cucumber.api.java.en.*;

public class SignInSteps {
	
	 private LoginPage page;
	 
	 @Before
	 public void setUp() throws Exception {
		 
		String path = CommonUtils.getAppPathFromConfig(SignInSteps.class);
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
	 public void WhenIPressSignInButton() {
		 
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
