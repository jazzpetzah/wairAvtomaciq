package com.wearezeta.auto.osx.steps.webapp;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.Then;

public class PreferencesPageSteps {

	/**
	 * Verifies whether preferences dialog is visible
	 * 
	 * @step. ^I see [Pp]references dialog$
	 * 
	 * @throws AssertionError
	 *             if preferences dialog is not currently visible
	 */
	@Then("^I see [Pp]references dialog$")
	public void ISeeSetingsDialog() throws Exception {
		Assert.assertTrue(WebappPagesCollection.getInstance()
				.getPage(SettingsPage.class).isVisible());
	}

}
