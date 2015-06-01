package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue(PagesCollection.welcomePage.waitForInitialScreen());
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
		PagesCollection.emailSignInPage = PagesCollection.welcomePage
			.clickIHaveAnAccount();
	}
	
	/**
	 * Opens the area code chooser by selecting the area code button in front of the
	 * phone number, and then selects the given area code
	 * 
	 * @step. ^I set the area code to (.*)$
	 * 
	 * @param areaCode
	 * @throws Exception
	 */
	@When("^I set the area code to (.*)$")
	public void WhenISetTheAreaCodeTo(String areaCode) throws Exception {
		PagesCollection.areaCodePage = PagesCollection.welcomePage.clickAreaCodeSelector();
		PagesCollection.welcomePage = PagesCollection.areaCodePage.selectAreaCode(areaCode);
	}
	
	
}
