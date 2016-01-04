package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;
import com.wearezeta.auto.web.pages.SettingsPage.SoundAlertsLevel;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SettingsPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private String currentDeviceId = null;

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
		Assert.assertTrue(webappPagesCollection.getPage(SettingsPage.class)
				.isVisible());
	}

	/**
	 * Set relevant setting for sound alerts
	 * 
	 * @step. I select Sound Alerts setting to be (None|Some|All)
	 * 
	 * @param newLevel
	 *            possible values None, Some, All
	 * @throws Exception
	 */
	@When("^I select Sound Alerts setting to be (None|Some|All)")
	public void ISelectSoundAlertsSetting(String newLevel) throws Exception {
		webappPagesCollection.getPage(SettingsPage.class).setSoundAlertsLevel(
				SoundAlertsLevel.fromString(newLevel));
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
	public void ISeeSoundAlertsSettingIs(String expectedValue) throws Exception {
		final String currentValue = webappPagesCollection
				.getPage(SettingsPage.class).getSoundAlertsLevel().toString();
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
	public void IClickCloseSettingsPageButton() throws Exception {
		webappPagesCollection.getPage(SettingsPage.class).clickCloseButton();
	}

	/**
	 * Remember the device id of the current device
	 * 
	 * @throws Exception
	 */
	@When("I remember the device id of the current device")
	public void IRememberCurrentDeviceId() throws Exception {
		currentDeviceId = webappPagesCollection.getPage(SettingsPage.class)
				.getCurrentDeviceId();
	}

	/**
	 * Verify that the device id of the current device is still the same
	 * 
	 * @throws Exception
	 */
	@When("I verify that the device id of the current device is still the same")
	public void IVerifyCurrentDeviceId() throws Exception {
		if (currentDeviceId == null) {
			throw new RuntimeException(
					"currentDeviceId was not remembered, please use the according step first");
		} else {
			assertThat(webappPagesCollection.getPage(SettingsPage.class)
					.getCurrentDeviceId(), equalTo(currentDeviceId));
		}
	}

	/**
	 * Verify if you see a device in the device list (or not)
	 * 
	 * @param donot
	 *            If not this is set to null
	 * @param device
	 *            model and label of the device
	 * @throws Exception
	 */
	@When("I( do not)? see a device named (.*) in the devices section")
	public void ISeeACertainDeviceInDevicesSection(String donot, String device)
			throws Exception {
		List<String> labels = webappPagesCollection.getPage(SettingsPage.class)
				.getDeviceLabels();
		if (donot == null) {
			assertThat(labels, hasItem(device.toUpperCase()));
		} else {
			assertThat(labels, not(hasItem(device.toUpperCase())));
		}
	}

	/**
	 * Click on the device
	 * 
	 * @param device
	 *            model and label of the device
	 * @throws Exception
	 */
	@When("I click on the device (.*) in the devices section")
	public void IClickOnDevice(String device) throws Exception {
		webappPagesCollection.getPage(SettingsPage.class).clickDevice(device);
	}
}
