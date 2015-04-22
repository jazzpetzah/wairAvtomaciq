package com.wearezeta.auto.android;

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
	 * @throws Throwable
	 * 
	 */
	@When("^I tap on About page$")
	public void WhenITapOnAboutPage() throws Throwable {
		PagesCollection.personalInfoPage = PagesCollection.aboutPage.tapOnVersion();
	}
	
	/**
	 * Confirms the about page has been seen
	 * 
	 * @step. ^I see About page$
	 * 
	 * @throws Throwable
	 * 
	 */
	@Then("^I see About page$")
	public void ThenISeeAboutPage() throws Throwable {
	   Assert.assertTrue(PagesCollection.aboutPage.aboutLogoIsVisible());
	}
}
