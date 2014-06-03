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
		PagesCollection.iOSPage = PagesCollection.contactListPage.tapOnName(name);
	}

	@When("^I swipe left to personal screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		PagesCollection.welcomePage = (WelcomePage)(PagesCollection.iOSPage);
		PagesCollection.personalInfoPage = (PersonalInfoPage)(PagesCollection.welcomePage.swipeLeft(500));
		PagesCollection.personalInfoPage.waitForEmailFieldVisible();
	}

	@When("^I swipe up to bring up options$")
	public void WhenIPullUpForOptions() throws IOException {
		PagesCollection.personalInfoPage.swipeUp(1000);
	}

	@When("^I press Sign out button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButtonByText(buttonName);
	}

	@Then("^I see login in screen$")
	public void ThenISeeLogInScreen() {
	    Assert.assertTrue("I see login screen", PagesCollection.loginPage.isWelcomeButtonsExist());
	}

}