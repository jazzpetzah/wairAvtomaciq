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
		PagesCollection.personalInfoPage = PagesCollection.aboutPage.tapOnVersion();
	}
	
	/**
	 * Confirms the about page has been seen
	 * 
	 * @step. ^I see About page$
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I see About page$")
	public void ThenISeeAboutPage() throws Exception {
	   Assert.assertTrue(PagesCollection.aboutPage.aboutLogoIsVisible());
	}
}
