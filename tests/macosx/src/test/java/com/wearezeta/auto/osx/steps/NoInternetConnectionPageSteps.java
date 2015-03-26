package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.Then;

public class NoInternetConnectionPageSteps {

	/**
	 * Checks that No internet connection error appears when internet is blocked
	 * 
	 * @step. ^I see internet connectivity error message$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no message about internet connection error
	 */
	@Then("^I see internet connectivity error message$")
	public void ISeeInternetConnectivityErrorMessage() throws Exception {
		Assert.assertTrue(PagesCollection.noInternetPage.isVisible());
		PagesCollection.noInternetPage.closeDialog();
	}
}
