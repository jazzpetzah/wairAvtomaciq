package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletManageDevicesOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ManageDevicesOverlaySteps {
    private TabletManageDevicesOverlay getManageDevicesOverlay() throws Exception {
        return  AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletManageDevicesOverlay.class);
    }

    /**
     * Confirms the Manage Devices overlay is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I( do not)? see Manage Devices overlay$"
     */
    @Then("^I( do not)? see Manage Devices overlay$")
    public void ThenISeeAboutPage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Manage Devices overlay is not visible after timeout",
                    getManageDevicesOverlay().isVisible());
        } else {
            Assert.assertTrue("Manage Devices overlay is still visible after timeout",
                    getManageDevicesOverlay().isInvisible());
        }
    }

    /**
     * Tap Manage Devices button
     * <p/>
     * #step. ^I tap Manage Devices button on Manage Devices overlay$
     *
     * @throws Exception
     */
    @When("^I tap Manage Devices button on Manage Devices overlay$")
    public void ITapManageDevicesButton() throws Exception {
        getManageDevicesOverlay().tapManageDevicesButton();
    }
}