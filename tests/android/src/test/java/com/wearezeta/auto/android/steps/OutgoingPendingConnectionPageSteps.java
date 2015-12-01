package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.OutgoingPendingConnectionPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class OutgoingPendingConnectionPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private OutgoingPendingConnectionPage getOPCPage() throws Exception {
        return pagesCollection.getPage(OutgoingPendingConnectionPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verify whether particular user name is visible on Outgoing pending connection page
     *
     * @param expectedName user name/alias
     * @throws Exception
     * @step. ^I see outgoing pending connection to (.*)
     */
    @Then("^I see outgoing pending connection to (.*)")
    public void GivenOPCTo(String expectedName) throws Exception {
        expectedName = usrMgr.findUserByNameOrNameAlias(expectedName).getName();
        Assert.assertTrue(String.format("User name '%s' is not visible on Outgoing Pending Connection page",
                expectedName), getOPCPage().waitUntilNameVisible(expectedName));
    }
}
