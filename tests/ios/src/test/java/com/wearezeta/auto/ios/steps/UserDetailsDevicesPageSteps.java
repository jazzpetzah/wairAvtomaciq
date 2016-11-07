package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.ios.pages.details_overlay.common.UserDetailsDevicesPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class UserDetailsDevicesPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UserDetailsDevicesPage getUserDetailsDevicesPage() throws Exception {
        return pagesCollection.getPage(UserDetailsDevicesPage.class);
    }

    /**
     * Tap the corresponding link on user details page. Since we cannot detect the exact link position
     * we just assume this link is located at the bottom left corner of the container text block.
     *
     * @param expectedLink the full text of the link to be clicked
     * @throws Exception
     * @step. ^I tap "(.*)" link on Devices tab of Single user profile page$
     */
    @When("^I tap \"(.*)\" link on Devices tab$")
    public void ITapLink(String expectedLink) throws Exception {
        getUserDetailsDevicesPage().tapLink(expectedLink);
    }

    /**
     * Verify all device with correct IDs are presented on participant devices tab
     *
     * @param name username
     * @throws Exception
     * @step. ^I see user (.*) devices? IDs? (?:is|are) presented on Devices tab of Single user profile page$
     */
    @Then("^I see user (.*) devices? IDs? (?:is|are) present on Devices tab$")
    public void ISeeUserDevicesIDPresentOnDetailsPage(String name) throws Exception {
        List<String> deviceIDs = CommonSteps.getInstance().GetDevicesIDsForUser(name);
        for (String id : deviceIDs) {
            Assert.assertTrue(String.format("Device ID '%s' is not visible", id),
                    getUserDetailsDevicesPage().isUserDeviceIdVisible(id));
        }
    }

    /**
     * Open the details page of corresponding device on conversation details page
     *
     * @param deviceIndex the device index. Starts from 1
     * @throws Exception
     * @step. ^I open details page of device number (\d+) (on Devices tab|in settings)$
     */
    @When("^I open details page of device number (\\d+) on Devices tab$")
    public void IOpenDeviceDetails(int deviceIndex) throws Exception {
        getUserDetailsDevicesPage().openDeviceDetailsPage(deviceIndex);

    }

    /**
     * Checks the number of devices in participant devices tab
     *
     * @param expectedNumDevices Expected number of devices
     * @throws Exception
     * @step. ^I see (\d+) items? (?:is|are) shown on Devices tab$
     * tab$
     */
    @When("^I see (\\d+) items? (?:is|are) shown on Devices tab$")
    public void ISeeDevicesShownInDevicesTab(int expectedNumDevices) throws Exception {
        Assert.assertTrue(
                String.format("The expected number of devices: %s is not equals to actual count", expectedNumDevices),
                getUserDetailsDevicesPage().isParticipantDevicesCountEqualTo(expectedNumDevices)
        );
    }
}
