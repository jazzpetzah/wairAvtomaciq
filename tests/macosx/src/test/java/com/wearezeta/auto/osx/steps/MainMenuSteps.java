package com.wearezeta.auto.osx.steps;

import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class MainMenuSteps {

	/**
	 * Signs out from Wire
	 * 
	 * @step. ^I [Ss]ign [Oo]ut$
	 * 
	 * @throws Exception
	 */
	@When("^I [Ss]ign [Oo]ut$")
	public void ISignOut() throws Exception {
		PagesCollection.mainMenuPage.signOut();
		CommonOSXSteps.resetBackendSettingsIfOverwritten();
	}
}
