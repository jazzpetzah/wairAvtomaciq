package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SettingsPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private SettingsPage getSettingsPage() throws Exception {
		return pagesCollection.getPage(SettingsPage.class);
	}

	/**
	 * Checks to see that the settings page is visible
	 * 
	 * @step. ^I see settings page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Exception {
		Assert.assertTrue("Settings page is not visible", getSettingsPage()
				.waitUntilVisible());
	}

	/**
	 * Tap the corresponding menu item
	 * 
	 * @step. ^I select \"(.*)\" settings menu item$
	 * 
	 * @param name
	 *            the name of the corresponding menu item
	 * @throws Exception
	 */
	@When("^I select \"(.*)\" settings menu item$")
	public void ISelectSettingsMenuItem(String name) throws Exception {
		getSettingsPage().selectMenuItem(name);
	}
	
	/**
	 * Click the corresponding button on sign out alert to confirm it
	 * 
	 * @step. ^I confirm sign out$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm sign out$")
	public void IConfirmSignOut() throws Exception {
		getSettingsPage().confirmSignOut();
	}
	
}
