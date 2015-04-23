package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AboutPageSteps {

	@When("^I tap on About page$")
	public void WhenITapOnAboutPage() throws Throwable {
		PagesCollection.personalInfoPage = PagesCollection.aboutPage.tapOnVersion();
	}
	
	@Then("^I see About page$")
	public void ThenISeeAboutPage() throws Throwable {
	   Assert.assertTrue(PagesCollection.aboutPage.aboutLogoIsVisible());
	}
}
