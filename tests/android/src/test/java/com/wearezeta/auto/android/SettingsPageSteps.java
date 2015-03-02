package com.wearezeta.auto.android;

import org.junit.Assert;
import com.wearezeta.auto.android.pages.PagesCollection;
import cucumber.api.java.en.Then;

public class SettingsPageSteps {

	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Throwable {
		Assert.assertTrue(PagesCollection.settingsPage.isSettingsPageVisible());
	}

}
