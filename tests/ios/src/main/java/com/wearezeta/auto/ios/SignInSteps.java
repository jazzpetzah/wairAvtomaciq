package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.*;
import cucumber.api.java.en.*;

public class SignInSteps {
	
	
	 
	 @Before
	 public void setUp() throws Exception {
		String path = CommonUtils.getAppPathFromConfig(SignInSteps.class);
		if ( PagesCollection.loginPage == null)
		{
			PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(SignInSteps.class), path);
		}
	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 IOSPage.clearPagesCollection();
	 }
	 
	 @Given ("I see sign in screen")
	 public void GiveniSeeSignInScreen() {

		 Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	 }
	 
	 @When("I press Sign in button")
	 public void WhenIPressSignInButton() {
		 
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
