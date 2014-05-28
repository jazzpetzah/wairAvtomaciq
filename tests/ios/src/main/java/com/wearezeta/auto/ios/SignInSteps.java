package com.wearezeta.auto.ios;

import org.junit.Assert;
import com.wearezeta.auto.ios.pages.LoginPage;

import cucumber.api.java.*;
import cucumber.api.java.en.*;

public class SignInSteps {
	
	 private LoginPage page;
	 
	 @Before
	 public void setUp() throws Exception {
	        String path = "/Users/admin/Downloads/ZClient.app";

	        page = new LoginPage("http://192.168.159.128:4723/wd/hub", path);
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
	 
	 @When ("I have entered login \"(.*)\"")
	 public void WhenIHaveEnteredLogin(String value) {
		 
		 page.setLogin(value);
	 }
	 
	 @When ("I have entered password \"(.*)\"")
	 public void WhenIHaveEnteredPassword(String value) {
		 
		 page.setPassword(value);
	 }
	 
	 @Then ("Contact list appears with \"(.*)\" element")
	 public void ThenContactListAppears(String value) {
		 
		 Assert.assertTrue("Login finished", page.waitForLogin());
		 
		 Assert.assertTrue(page.isLoginFinished(value));
		 
	 }

}
