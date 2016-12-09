package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletOutgoingPendingConnectionPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class OutgoingPendingConnectionPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletOutgoingPendingConnectionPage getOutgoingPendingConnectionPage()
            throws Exception {
        return (TabletOutgoingPendingConnectionPage) pagesCollection
                .getPage(TabletOutgoingPendingConnectionPage.class);
    }

    /**
     * Verify whether Incoming connections page is visible
     *
     * @throws Exception
     * @step. ^I see (?:the |\\s*)Outgoing Pending Connection page$
     */
    @Given("^I see (?:the |\\s*)Outgoing Pending Connection page$")
    public void ISeeOutgoingPendingConnectionPage() throws Exception {
        Assert.assertTrue("Outgoing pending connection page is not shown",
                getOutgoingPendingConnectionPage().waitUntilVisible());
    }

    /**
     * Verify whether the particular name is visible on Outgoing Pending
     * Connection page
     *
     * @param userNameAlias name or alias
     * @throws Exception
     * @step. ^I see name (.*) on (?:the |\\s*)Outgoing Pending Connection page$
     */
    @Then("^I see (.*) name on (?:the |\\s*)Outgoing Pending Connection page$")
    public void ISeeNamel(String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        Assert.assertTrue(String.format("The user name '%s' is still invisible", user.getName()),
                getOutgoingPendingConnectionPage().waitUntilNameVisible(user));
    }
}
