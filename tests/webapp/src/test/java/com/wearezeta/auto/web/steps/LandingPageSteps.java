package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;

public class LandingPageSteps {
	/**
	 * Switch to Sign In page
	 * 
	 * @step. ^I switch to [Ss]ign [Ii]n page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to [Ss]ign [Ii]n page$")
	public void ISwitchToLoginPage() throws Exception {
		PagesCollection.loginPage = PagesCollection.landingPage
				.switchToLoginPage();
	}

	/**
	 * Clicks the corresponding switcher button to make the registration page
	 * active
	 * 
	 * @step. ^I switch to registration page$
	 * 
	 * @throws Exception
	 */
	@Given("^I switch to [Rr]egistration page$")
	public void ISwitchToRegistrationPage() throws Exception {
		PagesCollection.registrationPage = PagesCollection.landingPage
				.switchToRegistrationPage();
	}
}
