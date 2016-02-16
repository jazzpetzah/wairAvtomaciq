package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallOngoingPage;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOngoingPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private CallOngoingPage getPage() throws Exception {
        return pagesCollection.getPage(CallOngoingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;


    /**
     * Hangs up the current call
     *
     * @throws Exception
     * @step. ^I hang up$
     */
    @When("^I hang up$")
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
            assertTrue("Outgoing/Ongoing call not visible", getPage().waitUntilVisible());
        }else{
            assertTrue("Outgoing/Ongoing call should not be visible", getPage().waitUntilNotVisible());
        }
    }
    
    /**
     * Verifies presence of ongoing call
     *
     * @throws Exception
     * @step. ^I (do not )?see ongoing call$
     */
    @When("^I (do not )?see ongoing call$")
    public void ISeeOngoingCall(String not) throws Exception {
        ISeeOutgoingCall(not);
    }
    
//    /**
//     * Checks to see if join group call overlay is present or not
//     *
//     * @param shouldNotSee is set to null if " do not" part does not exist
//     * @throws Exception
//     * @step. ^I( do not)? see join group call overlay$
//     */
//    @Then("^I( do not)? see join group call overlay$")
//    public void WhenISeeGroupCallJoinOverlay(String shouldNotSee) throws Exception {
//        if (shouldNotSee == null) {
//            Assert.assertTrue("Join group call overlay not visible", getCallingOverlayPage().waitUntilGroupCallJoinVisible());
//        } else {
//            Assert.assertTrue("Join group call overlay is visible, it should have been dismissed",
//                    getCallingOverlayPage().waitUntilGroupCallJoinNotVisible());
//        }
//    }
//
//    /**
//     * Checks to see if join group call overlay is present or not
//     *
//     * @param name text on the button
//     * @throws Exception
//     * @step. ^I see \"(.*)\" button$
//     */
//    @Then("^I( do not)? see \"(.*)\" button$")
//    public void WhenISeeGroupCallJoinButton(String shouldNotSee, String name) throws Exception {
//        if (shouldNotSee == null) {
//            Assert.assertTrue(name + " button with not visible in group call overlay",
//                    getCallingOverlayPage().waitUntilJoinGroupCallButtonVisible(name));
//        } else {
//            Assert.assertTrue(name + " button with not visible in group call overlay",
//                    getCallingOverlayPage().waitUntilJoinGroupCallButtonNotVisible(name));
//        }
//    }
}
