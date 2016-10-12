package com.wearezeta.auto.web.steps;

import java.util.List;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.DevicesPage;
import org.junit.Assert;

import com.wearezeta.auto.web.pages.SettingsPage;
import com.wearezeta.auto.web.pages.SettingsPage.SoundAlertsLevel;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SettingsPageSteps {

	private String currentDeviceId = null;
        
        private final TestContext context;
        
    public SettingsPageSteps() {
        this.context = new TestContext();
    }

    public SettingsPageSteps(TestContext context) {
        this.context = context;
    }

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
		Assert.assertTrue(context.getPagesCollection().getPage(SettingsPage.class)
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
		context.getPagesCollection().getPage(SettingsPage.class).setSoundAlertsLevel(
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
		final String currentValue = context.getPagesCollection()
				.getPage(SettingsPage.class).getSoundAlertsLevel().toString();
		Assert.assertTrue(
				String.format(
						"Current sound alerts setting ('%s') is not equal to the expected one: '%s'",
						currentValue, expectedValue), expectedValue
						.equalsIgnoreCase(currentValue));
	}

	/**
	 * Remember the device id of the current device
	 * 
	 * @step. I remember the device id of the current device
	 *
	 * @throws Exception
	 */
	@When("^I remember the device id of the current device$")
	public void IRememberCurrentDeviceId() throws Exception {
		currentDeviceId = context.getPagesCollection().getPage(DevicesPage.class).getCurrentDeviceId();
	}

	/**
	 * Verify that the device id of the current device is still the same
	 *
	 * @step. I verify that the device id of the current device is (not) the
	 *        same
	 *
	 * @param not
	 *            If not is this set to null
	 * @throws Exception
	 */
	@When("^I verify that the device id of the current device is( not)? the same$")
	public void IVerifyCurrentDeviceId(String not) throws Exception {
		if (currentDeviceId == null) {
			throw new RuntimeException(
					"currentDeviceId was not remembered, please use the according step first");
		} else {
			if (not == null) {
				assertThat(context.getPagesCollection().getPage(DevicesPage.class)
						.getCurrentDeviceId(), equalTo(currentDeviceId));
			} else {
				assertThat(context.getPagesCollection().getPage(DevicesPage.class)
						.getCurrentDeviceId(), not(equalTo(currentDeviceId)));
			}
		}
	}

	/**
	 * Wait for devices to show up
	 *
	 * @throws Exception
     */
	@When("^I wait for devices$")
	public void IWaitForDevices() throws Exception {
		context.getPagesCollection().getPage(SettingsPage.class).waitForDevices();
	}

	/**
	 * Verify if you see a device in the device list (or not)
	 * 
	 * @step. I( do not)? see a device named (.*) in the devices section
	 *
	 * @param donot
	 *            If not this is set to null
	 * @param device
	 *            model and label of the device
	 * @throws Exception
	 */
	@When("^I( do not)? see a device named (.*) in the devices section$")
	public void ISeeACertainDeviceInDevicesSection(String donot, String device)
			throws Exception {
		List<String> labels = context.getPagesCollection().getPage(SettingsPage.class)
				.getDeviceLabels();
		if (donot == null) {
			assertThat(labels, hasItem((device+context.getTestname().hashCode()).toUpperCase()));
		} else {
			assertThat(labels, not(hasItem((device+context.getTestname().hashCode()).toUpperCase())));
		}
	}

	/**
	 * Click on the device
	 *
	 * @step. I click on the device (.*) in the devices section
	 *
	 * @param device
	 *            model and label of the device
	 * @throws Exception
	 */
	@When("^I click on the device (.*) in the devices section$")
	public void IClickOnDevice(String device) throws Exception {
		context.getPagesCollection().getPage(SettingsPage.class).clickDevice(device+context.getTestname().hashCode());
	}

	@When("^I click back button on self settings Device Detail section$")
	public void IClickBackButton() throws Exception {
		context.getPagesCollection().getPage(SettingsPage.class).clickBackButton();
	}

	@When("^I verify device on self settings Device Detail section$")
	public void IVerifyDevice() throws Exception {
		context.getPagesCollection().getPage(SettingsPage.class).verifyDevice();
	}

	@Then("^I( do not)? see device (.*) of user (.*) is verified in device section$")
	public void ISeeVerifiedDevice(String donot, String deviceName, String userAlias) throws Exception {
		ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
		String id = context.getDeviceManager().getDeviceId(user, deviceName + context.getTestname().hashCode());
                id = WebCommonUtils.removeDeviceIdPadding(id);
		context.getPagesCollection().getPage(SettingsPage.class).waitForDevices();
		List<String> devices = context.getPagesCollection().getPage(SettingsPage.class).getVerifiedDeviceIds();
		if (donot != null) {
			assertThat("Device id is in verified devices", !devices.contains(id.toUpperCase()));
		} else {
			assertThat("Device id is NOT in verified devices", devices, hasItem(id.toUpperCase()));
		}
	}

	/**
	 * Verify to see X devices in the devices section
	 *
	 * @step. I see X devices in the devices section
	 *
	 * @param size
	 *            amount of devices (current device not included)
	 * @throws Exception
	 */
	@Then("^I see (\\d+) devices in the devices section$")
	public void ISeeXDevices(int size) throws Exception {
		assertThat(context.getPagesCollection().getPage(SettingsPage.class)
				.getDeviceLabels(), hasSize(size));
	}

    /**
     * Click on import contacts from Gmail via Setting
     *
     * @throws Exception
     */
    @When("^I click button to import contacts from Gmail$")
    public void IClickImportButton() throws Exception {
        context.getPagesCollection().getPage(SettingsPage.class).clickImportButton();
    }
}
