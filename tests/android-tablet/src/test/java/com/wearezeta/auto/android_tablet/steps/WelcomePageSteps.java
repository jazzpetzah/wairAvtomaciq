package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WelcomePageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletWelcomePage getWelcomePage() throws Exception {
		return pagesCollection.getPage(TabletWelcomePage.class);
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
		getWelcomePage().tapSignInButton();
	}

	/**
	 * Tap the Register button on Welcome page
	 * 
	 * @step. ^I tap Register button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Register button$")
	public void ITapRegisterButton() throws Exception {
		getWelcomePage().tapRegisterButton();
	}

	/**
	 * Verify whether the particular link is visible on Welcome page
	 * 
	 * @step. ^I see \"(.*)\" link on [Ww]elcome screen$
	 * 
	 * @param linkText
	 *            the text of the link
	 * @throws Exception
	 */
	@Then("^I see \"(.*)\" link on [Ww]elcome screen$")
	public void ISeeLink(String linkText) throws Exception {
		Assert.assertTrue(String.format(
				"'%s' link is not visible on Welcome screen", linkText),
				getWelcomePage().waitUntilLinkVisible(linkText));
	}

	/**
	 * Tap the corresponding link on welcome page
	 * 
	 * @step. ^I tap \"(.*)\" link on [Ww]elcome screen$
	 * 
	 * @param linkText
	 *            the text of the link
	 * @throws Exception
	 */
	@When("^I tap \"(.*)\" link on [Ww]elcome screen$")
	public void ITapLink(String linkText) throws Exception {
		getWelcomePage().tapLink(linkText);
	}
}
