package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class ZClientMenuSteps {

	/**
	 * Signs out from Wire
	 * 
	 * @step. I am signing out
	 * 
	 * @throws Exception
	 */
	@When("I am signing out")
	public void WhenIAmSigningOut() throws Exception {
		PagesCollection.mainMenuPage.SignOut();
		CommonOSXSteps.resetBackendSettingsIfOverwritten();
	}
}
