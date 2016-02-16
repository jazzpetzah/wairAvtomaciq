package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class SettingsPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SettingsPage getSettingsPage() throws Exception {
        return pagesCollection.getPage(SettingsPage.class);
    }

    /**
     * Verify whether Settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see settings page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getSettingsPage().waitUntilVisible());
    }

    /**
     * Select the corresponding Settings menu item
     *
     * @param itemName
     * @throws Exception
     * @step. ^I select settings item (.*)
     */
    @When("^I select settings item (.*)")
    public void ISelectItem(String itemName) throws Exception {
        getSettingsPage().selectItem(itemName);
    }

    /**
     * Apply current settings and close the view
     *
     * @throws Exception
     * @step. ^I accept current settings$
     */
    @And("^I accept current settings$")
    public void IAcceptCurrentSettings() throws Exception {
        getSettingsPage().apply();
    }

    /**
     * Tap back button on settings page
     *
     * @throws Exception
     * @step. ^I switch to the previous settings page$
     */
    @And("^I switch to the previous settings page$")
    public void ISwitchToThePreviousSettingsPage() throws Exception {
        getSettingsPage().goBack();
    }

    /**
     * Verify that alert settings are set to default values
     *
     * @step. ^I verify sound alerts settings are set to default values$
     *
     * @throws Exception
     */
    @When("^I verify sound alerts settings are set to default values$")
    public void IVerifyAllIsDefaultValue() throws Exception {
        Assert.assertTrue("Sound alerts settings are NOT set to their default values", getSettingsPage()
            .isSoundAlertsSetToDefault());
    }

    /**
     * Verify whether the corresponding settings menu item is visible
     *
     * @step. ^I (dont )?see settings item (.*)$
     *
     * @param itemName the expected item name
     * @throws Exception
     */
    @Then("^I (do not )?see settings item (.*)$")
    public void ISeeSettingsItem(String shouldNot, String itemName) throws Exception {
        if (shouldNot == null) {
            Assert.assertTrue(String.format("Settings menu item '%s' is not visible", itemName),
                    getSettingsPage().isItemVisible(itemName));
        } else {
            Assert.assertTrue(String.format("Settings menu item %s is visible",itemName),
                    getSettingsPage().isItemInvisible(itemName));
        }
    }

    /**
     * Verify Device label (Verified|Not Verified)
     * 
     * @step. ^I see the label (Verified|Not Verified) is shown for the device (.*)$
     * 
     * @param label label of device
     * @param deviceName name of device
     * @throws Exception
     */
    @Then("^I see the label (Verified|Not Verified) is shown for the device (.*)$")
    public void ISeeForDeviceALabelB(String label, String deviceName) throws Exception {
        Assert.assertTrue(String.format("Label '%s' is not visible for device '%s'", label, deviceName), getSettingsPage()
            .verificationLabelVisibility(deviceName, label));

    }

    /**
     * Presses the Edit Button in Settings Manage devices
     *
     * @throws Exception
     * @step. ^I tap Edit button$
     */
    @When("^I tap Edit button$")
    public void ITapEditButton() throws Exception {
        getSettingsPage().pressEditButton();
    }

    /**
     * Presses the delete button for the particular device
     *
     * @param deviceName name of device that should be deleted
     * @throws Exception
     * @step. ^I tap Delete (.*) button from devices$
     */
    @When("^I tap Delete (.*) button from devices$")
    public void ITapDeleteButtonFromDevices(String deviceName) throws Exception {
        getSettingsPage().pressDeleteDeviceButton(deviceName);
        getSettingsPage().pressDeleteButton();
    }

    /**
     * Types in the password and presses OK to confirm the device deletion
     *
     * @param password of user
     * @throws Exception
     * @step. ^I confirm with my (.*) the deletion of the device$
     */
    @When("^I confirm with my (.*) the deletion of the device$")
    public void IConfirmWithMyPasswordTheDeletionOfTheDevice(String password) throws Exception {
        getSettingsPage().typePasswordToConfirmDeleteDevice(password);
        pagesCollection.getCommonPage().acceptAlert();
    }

    /**
     * Verifies that device is or is not in device settings list
     *
     * @param shouldNot equals to null if the device is in list
     * @param device    name of device in list
     * @throws Exception
     * @step. ^I (do not )?see device (.*) in devices list$
     */
    @Then("^I (do not )?see device (.*) in devices list$")
    public void ISeeDeviceInDevicesList(String shouldNot, String device) throws Exception {
        if (shouldNot == null) {
            Assert.assertTrue(String.format("The device %s is not visible in the device list",device),
                    getSettingsPage().isDeviceVisibleInList(device));
        } else {
            Assert.assertFalse(String.format("The device %s is still visible in the device list",device),
                    getSettingsPage().isDeviceVisibleInList(device));
        }
    }

    /**
     * Tap on current device
     *
     * @throws Exception
     * @step. ^I tap on current device$
     */
    @Then("^I tap on current device$")
    public void ITapOnCurrentDevice() throws Throwable {
        getSettingsPage().tapCurrentDevice();
    }

    /**
     * Taps the big delete button opened from swipe left on the device cell
     *
     * @throws Exception
     * @step. ^I tap Delete button opened from swipe left on device$
     */
    @When("^I tap Delete button opened from swipe left on device$")
    public void ITapDeleteButtonOpenedFromSwipeLeftOnDevice() throws Exception {
        getSettingsPage().pressDeleteButton();
    }

    /**
     * Swipes left on the device cell
     *
     * @param deviceIndex index of device cell
     * @throws Exception
     * @step. ^I swipe left on device number (\d+)$
     */
    @When("^I swipe left on device number (\\d+)$")
    public void ISwipeLeftOnDeviceNumber(int deviceIndex) throws Exception {
        getSettingsPage().swipeLeftOnDevice(deviceIndex);
    }
}
