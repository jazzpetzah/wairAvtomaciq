package com.wearezeta.auto.android.steps.details_overlay.common;


import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.details_overlay.common.DeviceDetailsPage;
import com.wearezeta.auto.android.pages.details_overlay.common.DeviceListPage;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;

public class DeviceListPageSteps {
    private final Map<Integer, ElementState> savedDeviceShieldStates = new HashMap<>();

    private DeviceListPage getDeviceListPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(DeviceListPage.class);
    }

    private DeviceDetailsPage getDeviceDetailPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(DeviceDetailsPage.class);
    }

    /**
     * Composite steps, which could directly verify a device from device list.
     *
     * @param deviceIndex the index of the device that you want to verify
     * @throws Exception
     * @step. ^I verify (\d+)(?:st|nd|rd|th)? device on Device list page$
     */
    @Then("^I verify (\\d+)(?:st|nd|rd|th)? device on Device list page$")
    public void IVerifyDeviceX(int deviceIndex) throws Exception {
        getDeviceListPage().tapOnDevice(deviceIndex);
        Assert.assertTrue("Not verified switch is not visible", getDeviceDetailPage().verifyDevice());
        AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage().navigateBack();
    }

    /**
     * Checks the number of devices in single participant devices tab
     *
     * @param expectedNumDevices Expected number of devices
     * @throws Exception
     * @step. ^(\\d+) devices? (?:are|is) shown in single participant devices
     * tab$
     */
    @When("^I see (\\d+) devices? (?:are|is) shown on Device list page$")
    public void ICheckNumberOfDevicesInSingleParticipantDevicesTab(int expectedNumDevices) throws Exception {
        int numDevices = getDeviceListPage().getParticipantDevices().size();
        Assert.assertTrue("expected size", expectedNumDevices == numDevices);
    }

    /**
     * Takes screenshot of device shield current state for the further comparison
     *
     * @param deviceNum Device number
     * @throws Exception
     * @step. ^I remember state of (\\d+)(?:st|nd|rd|th)? device on Device list page$
     */
    @When("^I remember state of (\\d+)(?:st|nd|rd|th)? device on Device list page$")
    public void IRememberDeviceShieldState(int deviceNum) throws Exception {
        savedDeviceShieldStates.put(deviceNum,
                new ElementState(
                        () -> getDeviceListPage().getDeviceShieldScreenshot(deviceNum)
                ).remember()
        );
    }

    private static final double SHIELD_STATE_OVERLAP_MAX_SCORE = 0.8d;

    /**
     * Checks to see if device shield state is changed. Make sure,
     * that the screenshot of previous state is already taken for this
     * device
     *
     * @param deviceNum Device number
     * @throws Exception
     * @step. ^I see state of (\\d+)(?:st|nd|rd|th)? device is changed on Device list page$
     */
    @Then("^I see state of (\\d+)(?:st|nd|rd|th)? device is changed on Device list page$")
    public void ICheckDeviceShieldStateIsChanged(int deviceNum) throws Exception {
        if (!savedDeviceShieldStates.containsKey(deviceNum)) {
            throw new IllegalStateException(String.format(
                    "Please call the corresponding step to take the screenshot of shield state for device '%s' first",
                    deviceNum));
        }
        Assert.assertTrue(
                String.format("Shield state for device '%s' seems not changed", deviceNum),
                savedDeviceShieldStates.get(deviceNum).isChanged(Timedelta.fromSeconds(10),
                        SHIELD_STATE_OVERLAP_MAX_SCORE));
    }

    /**
     * Checks the ids of all devices displayed in single participant devices tab
     *
     * @param user The user with devices to check for
     * @throws Exception
     * @step. ^I verify all device ids of user (.*) are shown on Device list page$
     */
    @Then("^I verify all device ids of user (.*) are shown on Device list page$")
    public void IVerifyAllDeviceIdsOfUserXAreShown(String user) throws Exception {
        List<String> expectedDeviceIds = AndroidTestContextHolder.getInstance().getTestContext().getDevicesManager()
                .getDeviceIds(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .findUserByNameOrNameAlias(user));
        List<String> actualDeviceIds = getDeviceListPage().getParticipantDevices();
        Assert.assertThat("List does not contain all device ids", actualDeviceIds, is(expectedDeviceIds));
    }
}
