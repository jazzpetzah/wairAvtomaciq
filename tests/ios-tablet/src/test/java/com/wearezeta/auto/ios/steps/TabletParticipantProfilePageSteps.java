package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletParticipantProfilePage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletParticipantProfilePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletParticipantProfilePage getTabletParticipantProfilePage() throws Exception {
        return pagesCollection.getPage(TabletParticipantProfilePage.class);
    }

    /**
     * Verifies that singleuser mail and name is seen
     *
     * @param user that I check name and mail for
     * @throws Exception
     * @step. ^I see email and name of user (.*) on iPad popover$
     */
    @Then("^I see email and name of user (.*) on iPad popover$")
    public void ISeeEmailAndNameOfUserOniPadPopover(String user) throws Exception {
        user = usrMgr.findUserByNameOrNameAlias(user).getName();
        String email = usrMgr.findUserByNameOrNameAlias(user).getEmail();

        Assert.assertTrue(String.format("Participant name %s is not displayed on the popover", user),
                getTabletParticipantProfilePage().isNameVisible(user));
        Assert.assertTrue(String.format("Participant Email %s is not displayed on the popover", email),
                getTabletParticipantProfilePage().isEmailVisible(email));
    }

    /**
     * Verify connect label shown on not connected user profile popover
     *
     * @throws Exception
     * @step. I see Connect label on Other user profile popover
     */
    @Then("I see Connect label on Other user profile popover")
    public void ISeeConnectLabelOnOtherUserProfilePopover() throws Exception {
        Assert.assertTrue("Connect label is not shown",
                getTabletParticipantProfilePage().isConnectButtonVisible());
    }

    /**
     * Verify connect button shown on not connected user profile popover
     *
     * @throws Exception
     * @step. I see Connect Button on Other user profile popover
     */
    @Then("I see Connect Button on Other user profile popover")
    public void ISeeConnectButtonOnOtherUserProfilePopover() throws Exception {
        Assert.assertTrue("Connect button is not shown",
                getTabletParticipantProfilePage().isConnectButtonVisible());
    }

    /**
     * Click on Connect button on not connected user profile popover
     *
     * @throws Exception
     * @step. ^I tap Connect button on not connected user profile popover$
     */
    @When("^I tap Connect button on not connected user profile popover$")
    public void ITapConnectButtonOnNotConnectedUserProfilePopover() throws Exception {
        getTabletParticipantProfilePage().tapConnectButton();
    }

    /**
     * Open the details page of corresponding device on conversation details page
     *
     * @param deviceIndex the device index. Starts from 1
     * @throws Exception
     * @step. ^I open details page of device number (\d+)$
     */
    @When("^I open details page of device number (\\d+) on iPad$")
    public void IOpenDeviceDetails(int deviceIndex) throws Exception {
        getTabletParticipantProfilePage().openDeviceDetailsPage(deviceIndex);
    }

}
