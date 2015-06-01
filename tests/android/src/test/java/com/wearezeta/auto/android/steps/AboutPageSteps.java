package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AboutPage;
import com.wearezeta.auto.android.pages.AndroidPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AboutPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private AboutPage getAboutPage() throws Exception {
		return (AboutPage) pagesCollection.getPage(AboutPage.class);
	}

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
		pagesCollection.setPage(getAboutPage().tapOnVersion());
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
			Assert.assertTrue("About page is not visible after timeout",
					getAboutPage().isVisible());
		} else {
			Assert.assertTrue("About page is still visible after timeout",
					getAboutPage().isInvisible());
		}
	}
}
