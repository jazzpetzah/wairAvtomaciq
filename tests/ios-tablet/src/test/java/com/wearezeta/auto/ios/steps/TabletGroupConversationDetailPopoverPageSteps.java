package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletGroupConversationDetailPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationDetailPopoverPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletGroupConversationDetailPopoverPage getTabletGroupConversationDetailPopoverPage() throws Exception {
        return pagesCollection.getPage(TabletGroupConversationDetailPopoverPage.class);
    }

    /**
     * Opens the ellipses menu on the ipad popover
     *
     * @throws Exception
     * @step. ^I press conversation menu button on iPad$
     */
    @When("^I press conversation menu button on iPad$")
    public void IPressConversationMenuButtonOniPad() throws Exception {
        getTabletGroupConversationDetailPopoverPage().openConversationMenuOnPopover();
    }

    /**
     * Presses leave button in ellipsis menu
     *
     * @throws Exception
     * @step. ^I press leave converstation button on iPad$
     */
    @When("^I press leave converstation button on iPad$")
    public void IPressLeaveConverstationButtonOniPad() throws Exception {
        getTabletGroupConversationDetailPopoverPage().leaveConversation();
    }

    /**
     * Presses the confirmation leave button
     *
     * @throws Exception
     * @step. ^I press leave on iPad$
     */
    @Then("^I press leave on iPad$")
    public void i_press_leave_on_iPad() throws Exception {
        getTabletGroupConversationDetailPopoverPage().confirmLeaveConversation();
    }

    /**
     * Selects a user on the group conversation popover
     *
     * @param name of user I select
     * @throws Exception
     * @step. ^I select user on iPad group popover (.*)$
     */
    @When("^I select user on iPad group popover (.*)$")
    public void ISelectUserOniPadGroupPopover(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getTabletGroupConversationDetailPopoverPage().selectUserByNameOniPadPopover(name);
    }

    /**
     * Checks that the number of participants in a group is correct on the
     * popover
     *
     * @param expectedNumber the expected number of participants in that group
     * @throws Exception
     * @step. ^I see that number of participants (\\d+) is correct on iPad
     * popover$
     */
    @Then("^I see that number of participants (\\d+) is correct on iPad popover$")
    public void ISeeThatNumberOfParticipantsIsCorrectOniPadPopover(int expectedNumber) throws Exception {
        Assert.assertTrue(String.format("The actual number of participants in the current group conversation " +
                        "seems to be different from the expected number %s", expectedNumber),
                getTabletGroupConversationDetailPopoverPage().isNumberOfPeopleInGroupEqualToExpected(expectedNumber));
    }

    /**
     * Clicks on the corresponding button in the iPad popover ellipsis menu
     *
     * @throws Exception
     * @param actionName one of possible action names
     * @step. ^I select (Mute|Unmute|Rename) action from iPad ellipsis menu$$
     */
    @When("^I select (Mute|Unmute|Rename) action from iPad ellipsis menu$")
    public void ISelectOption(String actionName) throws Exception {
        getTabletGroupConversationDetailPopoverPage().selectEllipsisMenuAction(actionName);
    }

    /**
     * Waits until popover is closed
     *
     * @throws Exception
     * @step. ^I wait until popover is closed$
     */
    @When("^I wait until popover is closed$")
    public void IWaitUntilPopoverIsClosed() throws Exception {
        Assert.assertTrue("Popover is still shown",
                getTabletGroupConversationDetailPopoverPage()
                        .waitConversationInfoPopoverToClose());
    }

    /**
     * Tap on the screen to dismiss popover
     *
     * @throws Exception
     * @step I dismiss popover on iPad$
     */
    @When("^I dismiss popover on iPad$")
    public void IDismissPopover() throws Exception {
        getTabletGroupConversationDetailPopoverPage().dismissPopover();
    }

}
