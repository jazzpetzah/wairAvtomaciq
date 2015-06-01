package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.registration.WelcomePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class WelcomePageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private WelcomePage getWelcomePage(boolean shouldCreateIfNotExists)
			throws Exception {
		if (shouldCreateIfNotExists) {
			return (WelcomePage) pagesCollection
					.getPageOrElseInstantiate(WelcomePage.class);
		} else {
			return (WelcomePage) pagesCollection.getPage(WelcomePage.class);
		}
	}

	private WelcomePage getWelcomePage() throws Exception {
		return getWelcomePage(false);
	}

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue("Welcome page is not shown", getWelcomePage(true)
				.waitForInitialScreen());
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
		pagesCollection.setPage(getWelcomePage().clickIHaveAnAccount());
	}
}
