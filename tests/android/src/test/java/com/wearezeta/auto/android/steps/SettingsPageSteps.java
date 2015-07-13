package com.wearezeta.auto.android.steps;

import org.junit.Assert;
import org.openqa.selenium.By;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.SettingsPage;
import com.wearezeta.auto.common.driver.DriverUtils;

import cucumber.api.java.en.Then;

public class SettingsPageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private SettingsPage getSettingsPage() throws Exception {
		return (SettingsPage) pagesCollection.getPage(SettingsPage.class);
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
		Assert.assertTrue("Settings page is not visible", getSettingsPage()
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
		Assert.assertTrue(getSettingsPage().isChangePasswordVisible());
	}

	/**
	 * Navigates to the spotify login page in the settings
	 * 
	 * @step. ^I navigate to the spotify login page$
	 * 
	 * @throws Throwable
	 */
	@Then("^I navigate to the spotify login page$")
	public void IClickTheServicesButton() throws Throwable {
		getSettingsPage().clickServicesButton();
		getSettingsPage().clickConnectWithSpotifyButton();
	}

	/**
	 * Navigates the spotify web view and fills it out with the provided
	 * credentials
	 * 
	 * @step. ^I input (.*) and (.*) into the spotify login page$
	 * 
	 * @throws Throwable
	 */
	@Then("^I input (.*) and (.*) into the spotify login page$")
	public void IClickOnLogIntoSpotify(String username, String password)
		throws Throwable {
		SettingsPage settingsPage = getSettingsPage();

		settingsPage.openSpotifyLoginFields();
		settingsPage.enterSpotifyUsername(username);
		settingsPage.enterSpotifyPassword(password);
		settingsPage.navigateBackToSettingsScreen();
	}

	/**
	 * Checks to see that the "Connect to Spotify" button has changed to
	 * "Disconnect from Spotify"
	 * 
	 * @step. ^I see that I am connected to spotify$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see that I am connected to spotify$")
	public void ISeeIAmConnectedToSpotify() throws Throwable {
		Assert.assertTrue(getSettingsPage().doesSpotifyOptionSayDisconnect());
	}
}
