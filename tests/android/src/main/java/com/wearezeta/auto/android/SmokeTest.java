package com.wearezeta.auto.android;

import java.io.File;

import org.junit.Assert;
import com.wearezeta.auto.android.pages.LoginPage;

import cucumber.api.java.*;
import cucumber.api.java.en.*;

public class SmokeTest {
	
	 private LoginPage page;
	 
	 @Before
	 public void setUp() throws Exception {
	        File app = new File("C:\\zclient-release-2007.apk");
	        String path = app.getAbsolutePath();

	        page = new LoginPage("http://127.0.0.1:4723/wd/hub", path);
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
		 
		 Assert.assertTrue("Progress bar dissapeared", page.waitForLogin());
		 
		 Assert.assertTrue(page.isLoginFinished(value));
		 
	 }

}
