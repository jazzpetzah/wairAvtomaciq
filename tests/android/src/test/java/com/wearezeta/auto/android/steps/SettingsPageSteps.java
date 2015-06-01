package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.SettingsPage;

import cucumber.api.java.en.Then;

public class SettingsPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private SettingsPage getSettingsPage(boolean shouldCreateIfNotExists)
			throws Exception {
		if (shouldCreateIfNotExists) {
			return (SettingsPage) pagesCollection
					.getPageOrElseInstantiate(SettingsPage.class);
		} else {
			return (SettingsPage) pagesCollection.getPage(SettingsPage.class);
		}
	}

	@SuppressWarnings("unused")
	private SettingsPage getSettingsPage() throws Exception {
		return getSettingsPage(false);
	}

	/**
	 * Checks to see that the settings page is visible
	 * 
	 * @step. ^I see settings page$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Throwable {
		Assert.assertTrue("Settings page is not visible", getSettingsPage(true)
				.isSettingsPageVisible());
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
		Assert.assertTrue(getSettingsPage(true).isChangePasswordVisible());
	}
}
