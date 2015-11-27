package com.wearezeta.auto.osx.steps.osx;

import org.junit.Assert;

import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.osx.pages.common.NoInternetConnectionPage;

import cucumber.api.java.en.Then;

public class NoInternetConnectionPageSteps {

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();

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
		NoInternetConnectionPage noInternetPage = osxPagesCollection
				.getPage(NoInternetConnectionPage.class);
		Assert.assertTrue(noInternetPage.isVisible());
		noInternetPage.closeDialog();
	}
}
