package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;
import com.wearezeta.auto.web.pages.SettingsPage.SoundAlertsLevel;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SettingsPageSteps {

	/**
	 * Verifies whether settings dialog is visible
	 * 
	 * @step. ^I see Settings dialog$
	 * 
	 * @throws AssertionError
	 *             if settings dialog is not currently visible
	 */
	@Then("^I see Settings dialog$")
	public void ISeeSetingsDialog() throws Exception {
		PagesCollection.settingsPage = (SettingsPage) PagesCollection.loginPage
				.instantiatePage(SettingsPage.class);
		Assert.assertTrue(PagesCollection.settingsPage.isVisible());
	}

	/**
	 * Set relevant setting for sound alerts
	 * 
	 * @step. I select Sound Alerts setting to be (None|Some|All)
	 * 
	 * @param newLevel
	 *            possible values None, Some, All
	 */
	@When("^I select Sound Alerts setting to be (None|Some|All)")
	public void ISelectSoundAlertsSetting(String newLevel) {
		PagesCollection.settingsPage.setSoundAlertsLevel(SoundAlertsLevel
				.fromString(newLevel));
	}

	/**
	 * Verify what sound alert setting is set
	 * 
	 * @step. I see Sound Alerts setting is set to (None|Some|All)
	 * 
	 * @param expectedValue
	 *            possible values None, Some, All
	 */
	@When("^I see Sound Alerts setting is set to (None|Some|All)")
	public void ISeeSoundAlertsSettingIs(String expectedValue) {
		final String currentValue = PagesCollection.settingsPage
				.getSoundAlertsLevel().toString();
		Assert.assertTrue(
				String.format(
						"Current sound alerts setting ('%s') is not equal to the expected one: '%s'",
						currentValue, expectedValue), expectedValue
						.equalsIgnoreCase(currentValue));
	}

	/**
	 * Click close button on Settings page
	 * 
	 * @step. I click close settings page button
	 */
	@When("I click close settings page button")
	public void IClickCloseSettingsPageButton() {
		PagesCollection.settingsPage.clickCloseButton();
	}

}
