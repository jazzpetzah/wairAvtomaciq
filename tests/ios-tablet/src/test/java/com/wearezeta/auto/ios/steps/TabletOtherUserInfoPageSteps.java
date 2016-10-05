package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletOtherUserInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletOtherUserInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletOtherUserInfoPage getTabletOtherUserInfoPage() throws Exception {
        return pagesCollection.getPage(TabletOtherUserInfoPage.class);
    }

    /**
     * Clicks remove button on the other user info popover
     *
     * @throws Exception
     * @step. ^I tap Remove button on iPad popover$
     */
    @When("^I tap Remove button on iPad popover$")
    public void ITapRemoveOniPad() throws Exception {
        getTabletOtherUserInfoPage().removeFromConversationOniPad();
    }

    /**
     * Clicks the confirm REMOVE button
     *
     * @throws Exception
     * @step. ^I confirm removal on iPad popover$
     */
    @When("^I confirm removal on iPad popover$")
    public void IConfirmRemovalOniPad() throws Exception {
        getTabletOtherUserInfoPage().confirmRemove();
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
                getTabletOtherUserInfoPage().isNameVisible(user));
        Assert.assertTrue(String.format("Participant Email %s is not displayed on the popover", email),
                getTabletOtherUserInfoPage().isEmailVisible(email));
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
                getTabletOtherUserInfoPage().isConnectButtonVisible());
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
                getTabletOtherUserInfoPage().isConnectButtonVisible());
    }

    /**
     * Click on Connect button on not connected user profile popover
     *
     * @throws Exception
     * @step. ^I tap Connect button on not connected user profile popover$
     */
    @When("^I tap Connect button on not connected user profile popover$")
    public void ITapConnectButtonOnNotConnectedUserProfilePopover() throws Exception {
        getTabletOtherUserInfoPage().tapConnectButton();
    }

    /**
     * Click on Back button on user profile popover (usually to return to group chat info page )
     *
     * @throws Exception
     * @step. ^I tap Go Back button on user profile popover$
     */
    @When("^I tap Go Back button on user profile popover$")
    public void ITapGOButtonOnUserProfilePopover() throws Exception {
        getTabletOtherUserInfoPage().tapGoBackButton();
    }

    /**
     * Closes the single other user view in group popover
     *
     * @throws Exception
     * @step. ^I exit the other user group info iPad popover$
     */
    @When("^I exit the other user group info iPad popover$")
    public void IExitTheOtherUserGroupInfoiPadPopover() throws Exception {
        getTabletOtherUserInfoPage().exitOtherUserGroupChatPopover();
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
        getTabletOtherUserInfoPage().openDeviceDetailsPage(deviceIndex);
    }

}
