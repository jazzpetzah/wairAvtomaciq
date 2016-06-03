package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallIncomingPage;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;

public class CallIncomingPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private CallIncomingPage getPage() throws Exception {
        return pagesCollection.getPage(CallIncomingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verifies presence of incoming call
     *
     * @param not         equals to null means should see incoming call
     * @param isVideoCall equals to null means it is the video incoming call view
     * @throws Exception
     * @step. ^I (do not )?see incoming (video )?call$
     */
    @When("^I (do not )?see incoming (video )?call$")
    public void ISeeIncomingCall(String not, String isVideoCall) throws Exception {
        String subtitle = isVideoCall == null ? "Calling" : "Video Calling";
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible(subtitle));
        }else{
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible(subtitle));
        }
    }
    /**
     * Ignores an incoming call
     *
     * @throws Exception
     * @param action either 'accept' or 'ignore'
     * @step. ^I swipe to (ignore|accept) the call$
     */
    @When("^I swipe to (ignore|accept) the call$")
    public void ISwipeTo(String action) throws Exception {
        switch (action) {
            case "accept":
                getPage().acceptCall();
                break;
            case "ignore":
                getPage().ignoreCall();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action name '%s'", action));
        }
    }

    /**
     * Verify that incoming calling UI is visible and that the correct caller
     * name is shown
     *
     * @param expectedCallerName User name who calls
     * @throws Exception
     * @step. ^I see incoming call from (.*)$
     */
    @When("^I see incoming call from (.*)$")
    public void ISeeIncomingCallingMessage(String expectedCallerName)
            throws Exception {
        expectedCallerName = usrMgr.findUserByNameOrNameAlias(expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'", expectedCallerName),
                getPage().waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
    }
}
