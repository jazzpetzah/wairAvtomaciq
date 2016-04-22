package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.TabletPendingUserPopoverPage;

import cucumber.api.java.en.When;

public class TabletPendingUserPopoverPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private TabletPendingUserPopoverPage getTabletPendingUserPopoverPage() throws Exception {
        return pagesCollection.getPage(TabletPendingUserPopoverPage.class);
    }

    /**
     * Verifying pending profile popover on iPad
     *
     * @param user usern from Examples
     * @throws NoSuchUserException
     * @throws Exception
     * @step ^I see (.*) user pending profile popover on iPad$
     */
    @When("^I see (.*) user pending profile popover on iPad$")
    public void ISeeUserPendingPopoverOnIpad(String user) throws Exception {
        Assert.assertTrue("User name is not displayed",
                getTabletPendingUserPopoverPage().isUserNameDisplayed(usrMgr.findUserByNameOrNameAlias(user).getName()));
        Assert.assertTrue("Cancel Request label is not displayed",
                getTabletPendingUserPopoverPage().isCancelRequestButtonVisible());
    }

    /**
     * Verify presence of incoming pending popover for particular user
     *
     * @param user username String
     * @throws NoSuchUserException
     * @throws Exception
     * @step. I see incoming pending popover from user (.*) on iPad$
     */
    @When("^I see incoming pending popover from user (.*) on iPad$")
    public void ISeeIncomingPendingPopoverOnIpad(String user) throws Exception {
        Assert.assertTrue("User name is not displayed",
                getTabletPendingUserPopoverPage().isUserNameDisplayed(usrMgr.findUserByNameOrNameAlias(user).getName()));
        Assert.assertTrue("Connect button is not shown", getTabletPendingUserPopoverPage().isConnectButtonDisplayed());
    }

}
