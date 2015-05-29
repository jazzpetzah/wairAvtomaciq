package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

	/**
	 * Checks to see that the settings page is visible
	 * 
	 * @step. ^I see settings page$
	 * 
	 * @throws Throwable
	 */
	@When("^I input a new phone number$")
	public void IInputANewPhoneNumber() throws Throwable {
		
	}
	
	/**
	 * Press the "I have an account" button on the welcome page. to switch to
	 * sign in using email address
	 * 
	 * @step. ^I switch to email sign in screen$
	 * @throws Exception
	 */
	@When("^I switch to email sign in screen$")
	public void ISwitchToEmailSignIn() throws Exception {
		PagesCollection.welcomePage.clickIHaveAnAccount();
	}
}
