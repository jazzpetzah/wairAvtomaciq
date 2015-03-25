package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

	/**
	 * Checks that Welcome screen is visible
	 * 
	 * @step. I see Welcome screen
	 * 
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if Welcome screen did not appear
	 */
	@Given("^I see Welcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue(PagesCollection.welcomePage.isVisible());
	}

	/**
	 * Accepts terms of service and starts registration
	 * 
	 * @step. I start registration
	 * 
	 * @throws Exception
	 */
	@When("I start registration")
	public void IStartRegistration() throws Exception {
		PagesCollection.registrationPage = PagesCollection.welcomePage
				.startRegistration();
	}

	/**
	 * Clicks on Sign In button on Welcome screen and opens Sign In screen
	 * 
	 * @step. I start Sign In
	 * 
	 * @throws Exception
	 */
	@When("I start Sign In")
	public void WhenIStartSignIn() throws Exception {
		PagesCollection.loginPage = PagesCollection.welcomePage.startSignIn();
	}

}
