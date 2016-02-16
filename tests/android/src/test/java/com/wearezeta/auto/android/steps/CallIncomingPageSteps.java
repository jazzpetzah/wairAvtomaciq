package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallIncomingPage;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallIncomingPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private CallIncomingPage getPage() throws Exception {
        return pagesCollection.getPage(CallIncomingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;

    
     /**
     * Verifies presence of incoming call
     *
     * @throws Exception
     * @step. ^I (do not )?see incoming call$
     */
    @When("^I (do not )?see incoming call$")
    public void ISwipeToIgnoreCall(String not) throws Exception {
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible());
        }else{
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible());
        }
    }
    /**
     * Ignores an incoming call
     *
     * @throws Exception
     * @step. ^I swipe to ignore the call$
     */
    @When("^I swipe to ignore the call$")
    public void ISwipeToIgnoreCall() throws Exception {
        getPage().ignoreCall();
    }
    
    /**
     * Accepts an incoming call
     *
     * @throws Exception
     * @step. ^I swipe to accept the call$
     */
    @When("^I swipe to accept the call$")
    public void ISwipeToAcceptCall() throws Exception {
        getPage().ignoreCall();
    }
//
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
//    /**
//     * Checks to see that the calling lock screen appears when a user calls
//     * while Wire is minimised or the phone is locked
//     *
//     * @throws Exception
//     * @step. ^I see the call lock screen$
//     */
//    @When("I see the call lock screen$")
//    public void ISeeTheCallLockScreen() throws Exception {
//        Assert.assertTrue(
//                "Calling lockscreen is not visible, but it should be",
//                getCallingLockscreenPage().isVisible());
//    }
//
//    /**
//     * Checks to see that the user calling in the lock screen is the correct
//     * user
//     *
//     * @param expectedCallerName The username to compare the "is calling" message to.
//     * @throws Exception
//     * @step. ^I see a call from (.*) in the call lock screen$
//     */
//    @When("I see a call from (.*) in the call lock screen$")
//    public void ISeeACallFromUserInLockScreen(String expectedCallerName)
//            throws Exception {
//        expectedCallerName = usrMgr.findUserByNameOrNameAlias(
//                expectedCallerName).getName();
//        Assert.assertTrue(
//                String.format(
//                        "The current caller name differs from the expected value '%s' after %s seconds timeout",
//                        expectedCallerName,
//                        CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS / 1000),
//                getCallingLockscreenPage().waitUntilCallerNameExists(expectedCallerName));
//    }
//
//    /**
//     * Answers the call from the the calling overlay page
//     *
//     * @throws Exception
//     * @step. ^I answer the call from the overlay bar$
//     */
//    @When("I answer the call from the overlay bar$")
//    public void IAnswerCallFromTheOverlayBar() throws Exception {
//        getPage().acceptCall();
//    }
//
//    /**
//     * Answers the call from the lock screen and sets up the calling overlay
//     * page
//     *
//     * @throws Exception
//     * @step. ^I answer the call from the lock screen$
//     */
//    @When("I answer the call from the lock screen$")
//    public void IAnswerCallFromTheLockScreen() throws Exception {
//        getCallingLockscreenPage().acceptCall();
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
//    /**
//     * Press on the join group call button
//     *
//     * @throws Exception
//     * @step. ^I press join group call button$
//     */
//    @When("^I press join group call button$")
//    public void WhenIPressJoinGroupCallButton() throws Exception {
//        getPage().joinGroupCall();
//    }
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

}
