package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallOngoingPage;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

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
    
//    /**
//     * Verify that incoming calling UI is visible and that the correct caller
//     * name is shown
//     *
//     * @param expectedCallerName User name who calls
//     * @throws Exception
//     * @step. ^I see incoming calling message for contact (.*)$
//     */
//    @When("^I see incoming calling message for contact (.*)$")
//    public void ISeeIncomingCallingMesage(String expectedCallerName)
//            throws Exception {
//        expectedCallerName = usrMgr.findUserByNameOrNameAlias(
//                expectedCallerName).getName();
//        Assert.assertTrue(String.format(
//                "The current caller name differs from the expected value '%s'",
//                expectedCallerName), getPage()
//                .waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
//    }
//
//    /**
//     * Verify that started call message is visible
//     *
//     * @param contact contact name with whom you have a call
//     * @throws Exception
//     * @step. ^I see started call message for contact (.*)$
//     */
//    @When("^I see started call message for contact (.*)$")
//    public void ISeeStartedCallMesage(String contact) throws Exception {
//        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
//        Assert.assertTrue(
//                String.format("The name '%s' is not visible on calling bar",
//                        contact),
//                getPage().waitUntilNameAppearsOnCallingBarAvatar(
//                        contact));
//    }
//
//    // FIXME: replace multiple assertTrue calls with loops
//
//    /**
//     * Check calling Big bar
//     *
//     * @param shouldNotSee equals to null if 'do not' does not exist in the step
//     *                     signature
//     * @throws Exception
//     * @step. ^I (do not )?see calling overlay Big bar$
//     */
//    @When("^I (do not )?see calling overlay Big bar$")
//    public void WhenISeeCallingOverlayBigBar(String shouldNotSee)
//            throws Exception {
//        if (shouldNotSee == null) {
//            Assert.assertTrue("Big calling bar is not visible",
//                    getPage().waitUntilVisible()
//                            && getPage()
//                            .callingDismissIsVisible()
//                            && getPage()
//                            .callingSpeakerIsVisible()
//                            && getPage()
//                            .callingMicMuteIsVisible());
//        } else {
//            Assert.assertTrue(
//                    "Calling bar is still visible, but should be hidden",
//                    getPage().waitUntilNotVisible());
//        }
//    }
//
//
//    /**
//     * Check whether expected number of users present in call
//     *
//     * @throws Exception
//     * @step. ^I see (\\d+) users? take part in call$
//     */
//    @When("^I see (\\d+) users? take part in call$")
//    public void ISeeXUsersTakePartInGroupCall(final int expectedUsersCount)
//            throws Exception {
//        int actualUsersCount;
//        int ntries = 0;
//        do {
//            actualUsersCount = getPage().numberOfParticipantsInGroupCall();
//            if (actualUsersCount == expectedUsersCount) {
//                return;
//            } else {
//                Thread.sleep(1500);
//                ntries++;
//            }
//        } while (ntries < 3);
//        throw new AssertionError(String.format(
//                "The actual count of users in call %s does not equal to the expected count %s",
//                actualUsersCount, expectedUsersCount));
//    }
//
//    /**
//     * Check if alert notifying that group call is full appears
//     *
//     * @throws Exception
//     * @step. ^I see group call is full alert$
//     */
//    @When("^I see group call is full alert$")
//    public void ISeeGroupCallIsFullAlert() throws Exception {
//        Assert.assertTrue(getPage().isGroupCallFullAlertVisible());
//    }
//
//    /**
//     * Check whether expected number of users present in group call
//     *
//     * @throws Exception
//     * @step. ^I close group call is full alert$
//     */
//    @When("^I close group call is full alert$")
//    public void ICloseGroupCallIsFullAlert() throws Exception {
//        getPage().closeGroupCallFullAlert();
//    }
//
//    /**
//     * Check that answer call alert appears if there is another call during
//     * ongoing call
//     *
//     * @throws Exception
//     * @step. ^I see answer call alert$
//     */
//    @When("^I see answer call alert$")
//    public void ISeeAnswerCallAlert() throws Exception {
//        Assert.assertTrue(getPage().isAnswerCallAlertVisible());
//    }
//
//    /**
//     * Cancels another call on answer call alert
//     *
//     * @throws Exception
//     * @step. ^I cancel new call from answer call alert$
//     */
//    @When("^I cancel new call from answer call alert$")
//    public void ICancelNewCallFromAlert() throws Exception {
//        getPage().answerCallCancel();
//    }
//
//    /**
//     * Accepts another call on answer call alert
//     *
//     * @throws Exception
//     * @step. ^I start new call from answer call alert$
//     */
//    @When("^I start new call from answer call alert$")
//    public void IStartNewCallFromAnswerCallAlert() throws Exception {
//        getPage().answerCallContinue();
//    }
//
//    /**
//     * Check that end current call alert appears if there is another call during
//     * ongoing call
//     *
//     * @throws Exception
//     * @step. ^I see end current call alert$
//     */
//    @When("^I see end current call alert$")
//    public void ISeeEndCurrentCallAlert() throws Exception {
//        Assert.assertTrue(getPage()
//                .isEndCurrentCallAlertVisible());
//    }
//
//    /**
//     * Cancels another call on end current call alert
//     *
//     * @throws Exception
//     * @step. ^I cancel new call from end current call alert$
//     */
//    @When("^I cancel new call from end current call alert$")
//    public void ICancelNewCallFromEndCurrentCallAlert() throws Exception {
//        getPage().endCurrentCallCancel();
//    }
//
//    /**
//     * I start another call on answer call alert
//     *
//     * @throws Exception
//     * @step. ^I start new call from end current call alert$
//     */
//    @When("^I start new call from end current call alert$")
//    public void IStartNewCallFromEndCurrentCallAlert() throws Exception {
//        getPage().endCurrentCallContinue();
//    }

}
