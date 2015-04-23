package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SettingsPageSteps {

	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Throwable {
		Assert.assertTrue(PagesCollection.settingsPage.isSettingsPageVisible());
	}

	/**
	 * Check that change password item is present in settings menu
	 * 
	 * @step. ^I see change password item$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see change password item$")
	public void ISeeSettingsChangePassword() throws Throwable {
		Assert.assertTrue(PagesCollection.settingsPage.isChangePasswordVisible());
	}
	
	/**
	 * Tap on change password item in settings menu
	 * 
	 * @step. ^I click on CHANGE PASSWORD$
	 * 
	 * @throws Throwable
	 */
	@When("^I click on CHANGE PASSWORD$")
	public void IClickSettingsChangePassword() throws Throwable {
		PagesCollection.commonAndroidPage = PagesCollection.settingsPage.clickChangePassword();
	}
}
