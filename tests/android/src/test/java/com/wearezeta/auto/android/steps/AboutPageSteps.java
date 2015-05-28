package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AboutPageSteps {

	/**
	 * Taps on the about page
	 * 
	 * @step. ^I tap on About page$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I tap on About page$")
	public void WhenITapOnAboutPage() throws Exception {
		PagesCollection.personalInfoPage = PagesCollection.aboutPage
				.tapOnVersion();
	}

	/**
	 * Confirms the about page is visible or not
	 * 
	 * @step. ^I( do not)? see [Aa]bout page$"
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not present
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I( do not)? see [Aa]bout page$")
	public void ThenISeeAboutPage(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue(PagesCollection.aboutPage.isVisible());
		} else {
			Assert.assertTrue(PagesCollection.aboutPage.isInvisible());
		}
	}
}
