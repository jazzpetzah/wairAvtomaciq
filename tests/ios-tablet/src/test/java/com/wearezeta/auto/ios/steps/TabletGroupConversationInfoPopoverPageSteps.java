package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletGroupConversationInfoPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationInfoPopoverPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletGroupConversationInfoPopoverPage getTabletGroupConversationDetailPopoverPage() throws Exception {
        return pagesCollection.getPage(TabletGroupConversationInfoPopoverPage.class);
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
