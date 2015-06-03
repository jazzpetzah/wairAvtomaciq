package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TabletWelcomePageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletWelcomePage getWelcomePage() throws Exception {
		return (TabletWelcomePage) pagesCollection.getPage(TabletWelcomePage.class);
	}

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue("Welcome page is not shown", getWelcomePage()
				.waitForInitialScreen());
	}

	/**
	 * Press the "I have an account" button on the welcome page to switch to
	 * sign in using email address
	 * 
	 * @step. ^I switch to [Ee]mail [Ss]ign [Ii]n screen$
	 * @throws Exception
	 */
	@When("^I switch to [Ee]mail [Ss]ign [Ii]n screen$")
	public void ISwitchToEmailSignIn() throws Exception {
		getWelcomePage().clickIHaveAnAccount();
	}
}
