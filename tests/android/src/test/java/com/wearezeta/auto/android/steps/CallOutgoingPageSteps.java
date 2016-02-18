package com.wearezeta.auto.android.steps;


import com.wearezeta.auto.android.pages.CallOutgoingPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOutgoingPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private CallOutgoingPage getPage() throws Exception {
        return pagesCollection.getPage(CallOutgoingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;

    /**
     * Hangs up the current call
     *
     * @throws Exception
     * @step. ^I hang up$
     */
    @When("^I hang up outgoing call$")
    public void IHangUp()
            throws Exception {
        getPage().hangup();
    }

    /**
     * Verifies presence of outgoing call
     *
     * @throws Exception
     * @step. ^I (do not )?see outgoing call$
     */
    @When("^I (do not )?see outgoing call$")
    public void ISeeOutgoingCall(String not) throws Exception {
        if (not == null) {
            assertTrue("Outgoing call not visible", getPage().waitUntilVisible());
        } else {
            assertTrue("Outgoing call should not be visible", getPage().waitUntilNotVisible());
        }
    }
}
