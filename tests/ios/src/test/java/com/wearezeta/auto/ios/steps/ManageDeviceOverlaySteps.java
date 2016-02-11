package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import com.wearezeta.auto.ios.pages.ManageDevicesOverlay;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class ManageDeviceOverlaySteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ManageDevicesOverlay getManageDevicesOverlay() throws Exception {
        return pagesCollection.getPage(ManageDevicesOverlay.class);
    }

    /**
     * Verify whether Manage Devices overlay is visible
     *
     * @param shouldNotSee equals to null if the overlay should be visible
     * @throws Exception
     * @step. I (do not )?see Manage Devices overlay$
     */
    @Then("^I (do not )?see Manage Devices overlay$")
    public void ISeeManageDevicesOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Manage Devices overlay is not visible", getManageDevicesOverlay().waitUntiVisible());
        } else {
            Assert.assertTrue("Manage Devices overlay is not visible", getManageDevicesOverlay().waitUntiInvisible());
        }
    }

    /**
     * Wait for a while and accept Manage Devices overlay if it is viisble
     *
     * @throws Exception
     * @step. ^I accept Manage Devices overlay if it is visible
     */
    @And("^I accept Manage Devices overlay if it is visible")
    public void IAcceptManageDevicesOverlayIfVisible() throws Exception {
        getManageDevicesOverlay().acceptIfVisible(5);
    }
}
