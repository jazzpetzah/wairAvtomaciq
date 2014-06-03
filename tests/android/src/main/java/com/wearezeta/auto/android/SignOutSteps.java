package com.wearezeta.auto.android;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;


public class SignOutSteps{

	private String path;
	
	 @Before
	 public void setUp() throws Exception {
		 
	        File app = new File(CommonUtils.getAppPathFromConfig(TestRun.class));
	        path = app.getAbsolutePath();
	        if ( PagesCollection.loginPage == null)
	        	{
	        		PagesCollection.loginPage = new LoginPage(CommonUtils.getUrlFromConfig(TestRun.class), path);
	        	}
	        	
	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 PagesCollection.loginPage.Close();
		 AndroidPage.clearPagesCollection();
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

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		 Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}

	@When("^I tap on name (.*)$")
	public void WhenITapOnName(String name) throws IOException  {
		PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
	}

	@When("^I swipe to personal info screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		PagesCollection.instructionsPage = (InstructionsPage) PagesCollection.androidPage;
		PagesCollection.personalInfoPaga = (PersonalInfoPage)(PagesCollection.instructionsPage.swipeLeft(500));
		PagesCollection.personalInfoPaga.waitForEmailFieldVisible();
	}

	@When("^I pull up for options$")
	public void WhenIPullUpForOptions() throws IOException {
		PagesCollection.personalInfoPaga.swipeUp(1000);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPaga.tapOptionsButtonByText(buttonName);
	}

	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
	    Assert.assertTrue("We see welcome buttons", PagesCollection.loginPage.isWelcomeButtonsExist());
	}

}
