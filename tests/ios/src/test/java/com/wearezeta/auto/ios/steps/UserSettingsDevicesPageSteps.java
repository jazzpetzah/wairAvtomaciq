package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.common.UserSettingsDevicesOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class UserSettingsDevicesPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UserSettingsDevicesOverlay getUserSettingsDevicesPage() throws Exception {
        return pagesCollection.getPage(UserSettingsDevicesOverlay.class);
    }

    /**
     * Open the details page of corresponding device on conversation details page
     *
     * @param deviceIndex the device index. Starts from 1
     * @throws Exception
     * @step. ^I open details page of device number (\d+) on Settings page$
     */
    @When("^I open details page of device number (\\d+) on Settings page$")
    public void IOpenDeviceDetails(int deviceIndex) throws Exception {
        getUserSettingsDevicesPage().openDeviceDetailsPage(deviceIndex);
    }

    /**
     * Presses the delete button for the particular device
     *
     * @param deviceName name of device that should be deleted
     * @throws Exception
     * @step. ^I tap Delete (.*) button on Settings page$
     */
    @When("^I tap Delete (.*) button on Settings page$")
    public void ITapDeleteButtonFromDevices(String deviceName) throws Exception {
        getUserSettingsDevicesPage().tapDeleteDeviceButton(deviceName);
        getUserSettingsDevicesPage().tapDeleteButton();
    }

    /**
     * Types in the password and presses OK to confirm the device deletion
     *
     * @param password of user
     * @throws Exception
     * @step. ^I confirm with my (.*) the deletion of the device$
     */
    @When("^I confirm with my (.*) the deletion of the device on Settings page$")
    public void IConfirmWithMyPasswordTheDeletionOfTheDevice(String password) throws Exception {
        password = usrMgr.replaceAliasesOccurences(password, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        getUserSettingsDevicesPage().typePasswordToConfirmDeleteDevice(password);
        pagesCollection.getCommonPage().tapAlertButton("OK");
    }

    /**
     * Verifies that device is or is not in device settings list
     *
     * @param shouldNot equals to null if the device is in list
     * @param device    name of device in list
     * @throws Exception
     * @step. ^I (do not )?see device (.*) in devices list on Settings page$
     */
    @Then("^I (do not )?see device (.*) in devices list on Settings page$")
    public void ISeeDeviceInDevicesList(String shouldNot, String device) throws Exception {
        if (shouldNot == null) {
            Assert.assertTrue(String.format("The device %s is not visible in the device list", device),
                    getUserSettingsDevicesPage().isDeviceVisibleInList(device));
        } else {
            Assert.assertTrue(String.format("The device %s is still visible in the device list", device),
                    getUserSettingsDevicesPage().isDeviceInvisibleInList(device));
        }
    }

    /**
     * Tap on current device
     *
     * @throws Exception
     * @step. ^I tap my current device on Settings page$
     */
    @Then("^I tap my current device on Settings page$")
    public void ITapOnCurrentDevice() throws Exception {
        getUserSettingsDevicesPage().tapCurrentDevice();
    }

}
