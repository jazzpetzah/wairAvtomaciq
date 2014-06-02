package com.wearezeta.auto.android;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;


public class SignOutSteps {
	
	private LoginPage loginPage;
	private ContactListPage contactListPage;
	private InstructionsPage instructionsPage;
	private PersonalInfoPaga personalInfoPaga;
	private String path;

	 @Before
	 public void setUp() throws Exception {
		 
	        File app = new File(CommonUtils.getAppPathFromConfig(SignOutSteps.class));
	        path = app.getAbsolutePath();
	        loginPage = new LoginPage(CommonUtils.getUrlFromConfig(SignOutSteps.class), path);

	 }
	 
	 @After
	 public void tearDown() throws Exception {

		 loginPage.Close();
	 }
	 
	@Given("^I Sign in using login (.*) and password (.*)$")
	public void GivenISignIn(String login, String password) throws IOException  {
		
		 Assert.assertNotNull(loginPage.isVisible());
		 loginPage =(LoginPage)(loginPage.SignIn());
		 loginPage.setLogin(login);
		 loginPage.setPassword(password);
		 contactListPage =(ContactListPage)(loginPage.SignIn());
		 Assert.assertTrue("Login finished", loginPage.waitForLogin());
	}

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		 Assert.assertTrue(loginPage.isLoginFinished(name));
	}

	@When("^I tap on name (.*)$")
	public void WhenITapOnName(String name) throws IOException  {
		instructionsPage = (InstructionsPage) contactListPage.tapOnName(name);
	}

	@When("^I swipe to personal info screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		personalInfoPaga = (PersonalInfoPaga)(instructionsPage.swipeLeft(500));
		personalInfoPaga.waitForEmailFieldVisible();
	}

	@When("^I pull up for options$")
	public void WhenIPullUpForOptions() throws IOException {
		personalInfoPaga.swipeUp(1000);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		personalInfoPaga.tapOptionsButtonByText(buttonName);
	}

	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
	    Assert.assertTrue("We see welcome buttons", loginPage.isWelcomeButtonsExist());
	}

}
