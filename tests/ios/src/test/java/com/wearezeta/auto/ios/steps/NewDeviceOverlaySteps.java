package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.NewDeviceOverlay;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class NewDeviceOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private NewDeviceOverlay getNewDeviceOverlay() throws Exception {
        return pagesCollection.getPage(NewDeviceOverlay.class);
    }

    /**
     * Wait for a while and accept First Time Usage overlay if it is viisble
     *
     * @throws Exception
     * @param expectedLabel the expected label. The label may contain user name aliases
     * @step. ^I see the label "(.*)" on New Device overlay$
     */
    @Then("^I see the label \"(.*)\" on New Device overlay$")
    public void ISeeLabel(String expectedLabel) throws Exception {
        expectedLabel = usrMgr.replaceAliasesOccurences(expectedLabel, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("New Device overlay does not contain the label '%s'", expectedLabel),
                getNewDeviceOverlay().isConatniningLabel(expectedLabel));
    }

    /**
     * Tap SEND ANYWAY button on New Deice overlay
     *
     * @step. ^I tap SEND ANYWAY button on New Device overlay$
     *
     * @throws Exception
     */
    @And("^I tap SEND ANYWAY button on New Device overlay$")
    public void ITapSendAnywayButton() throws Exception {
        getNewDeviceOverlay().tapSendAnywayButton();
    }

    /**
     * Tap SHOW DEVICE button on New Deice overlay
     *
     * @step. ^I tap SHOW DEVICE button on New Device overlay$
     *
     * @throws Exception
     */
    @And("^I tap SHOW DEVICE button on New Device overlay$")
    public void ITapShowDeviceButton() throws Exception {
        getNewDeviceOverlay().tapShowDeviceButton();
    }
}