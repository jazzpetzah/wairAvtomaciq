package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletConversationDetailPopoverPage;

import cucumber.api.java.en.When;

public class TabletConversationDetailPopoverPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletConversationDetailPopoverPage getTabletConversationDetailPopoverPage() throws Exception {
        return pagesCollection.getPage(TabletConversationDetailPopoverPage.class);
    }

    /**
     * Verifies that other peoples profile page in the popover is visible
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I see (.*) user profile page in iPad popover$
     */
    @When("^I see (.*) user profile page in iPad popover$")
    public void ISeeUserProfilePageIniPadPopover(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue(String.format("Other user name '%s' is not visible", name),
                getTabletConversationDetailPopoverPage().isOtherUserProfileNameVisible(name));
    }

    /**
     * Presses the + button on the ipad popover
     *
     * @throws Exception
     * @step. ^I press Add button on iPad popover$
     */
    @When("^I press Add button on iPad popover$")
    public void IPressAddButtonOniPadPopover() throws Exception {
        getTabletConversationDetailPopoverPage().addContactTo1on1Chat();
    }
}
