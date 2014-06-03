package com.wearezeta.auto.ios;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;


public class SignOutSteps {
	
	private LoginPage loginPage;
	private ContactListPage contactListPage;
	private WelcomePage welcomePage;
	private PersonalInfoPage personalInfoPage;
	private String path;

	 @Before
	 public void setUp() throws Exception {
		 
		String path = CommonUtils.getAppPathFromConfig(SignInSteps.class);
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
		welcomePage = (WelcomePage) contactListPage.tapOnName(name);
	}

	@When("^I swipe to personal info screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		personalInfoPage = (PersonalInfoPage)(welcomePage.swipeLeft(500));
		personalInfoPage.waitForEmailFieldVisible();
	}

	@When("^I pull up for options$")
	public void WhenIPullUpForOptions() throws IOException {
		personalInfoPage.swipeUp(1000);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		personalInfoPage.tapOptionsButtonByText(buttonName);
	}

	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
	    Assert.assertTrue("We see welcome buttons", loginPage.isWelcomeButtonsExist());
	}

}