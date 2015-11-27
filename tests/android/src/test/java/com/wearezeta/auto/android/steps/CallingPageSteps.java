package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.CallingLockscreenPage;
import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class CallingPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private CallingOverlayPage getCallingOverlayPage() throws Exception {
        return pagesCollection.getPage(CallingOverlayPage.class);
    }

    private CallingLockscreenPage getCallingLockscreenPage() throws Exception {
        return pagesCollection.getPage(CallingLockscreenPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;

    /**
     * Ignores an incoming call
     *
     * @throws Exception
     * @step. ^I click the ignore call button$
     */
    @When("I click the ignore call button")
    public void IClickIgnoreCallButton() throws Exception {
        getCallingOverlayPage().ignoreCall();
    }

    /**
     * Verify that incoming calling UI is visible and that the correct caller
     * name is shown
     *
     * @param expectedCallerName User name who calls
     * @throws Exception
     * @step. ^I see incoming calling message for contact (.*)$
     */
    @When("^I see incoming calling message for contact (.*)$")
    public void ISeeIncomingCallingMesage(String expectedCallerName)
            throws Exception {
        expectedCallerName = usrMgr.findUserByNameOrNameAlias(
                expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'",
                expectedCallerName), getCallingOverlayPage()
                .waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
    }

    /**
     * Verify that started call message is visible
     *
     * @param contact contact name with whom you have a call
     * @throws Exception
     * @step. ^I see started call message for contact (.*)$
     */
    @When("^I see started call message for contact (.*)$")
    public void ISeeStartedCallMesage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(
                String.format("The name '%s' is not visible on calling bar",
                        contact),
                getCallingOverlayPage().waitUntilNameAppearsOnCallingBarAvatar(
                        contact));
    }

    /**
     * Checks to see that the calling lock screen appears when a user calls
     * while Wire is minimised or the phone is locked
     *
     * @throws Exception
     * @step. ^I see the call lock screen$
     */
    @When("I see the call lock screen$")
    public void ISeeTheCallLockScreen() throws Exception {
        Assert.assertTrue(
                "Calling lockscreen is not visible, but it should be",
                getCallingLockscreenPage().isVisible());
    }

    /**
     * Checks to see that the user calling in the lock screen is the correct
     * user
     *
     * @param expectedCallerName The username to compare the "is calling" message to.
     * @throws Exception
     * @step. ^I see a call from (.*) in the call lock screen$
     */
    @When("I see a call from (.*) in the call lock screen$")
    public void ISeeACallFromUserInLockScreen(String expectedCallerName)
            throws Exception {
        expectedCallerName = usrMgr.findUserByNameOrNameAlias(
                expectedCallerName).getName();
        Assert.assertTrue(
                String.format(
                        "The current caller name differs from the expected value '%s' after %s seconds timeout",
                        expectedCallerName,
                        CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS / 1000),
                getCallingLockscreenPage().waitUntilCallerNameExists(expectedCallerName));
    }

    /**
     * Answers the call from the the calling overlay page
     *
     * @throws Exception
     * @step. ^I answer the call from the overlay bar$
     */
    @When("I answer the call from the overlay bar$")
    public void IAnswerCallFromTheOverlayBar() throws Exception {
        getCallingOverlayPage().acceptCall();
    }

    /**
     * Answers the call from the lock screen and sets up the calling overlay
     * page
     *
     * @throws Exception
     * @step. ^I answer the call from the lock screen$
     */
    @When("I answer the call from the lock screen$")
    public void IAnswerCallFromTheLockScreen() throws Exception {
        getCallingLockscreenPage().acceptCall();
    }

    // FIXME: replace multiple assertTrue calls with loops

    /**
     * Check calling Big bar
     *
     * @param shouldNotSee equals to null if 'do not' does not exist in the step
     *                     signature
     * @throws Exception
     * @step. ^I (do not )?see calling overlay Big bar$
     */
    @When("^I (do not )?see calling overlay Big bar$")
    public void WhenISeeCallingOverlayBigBar(String shouldNotSee)
            throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Big calling bar is not visible",
                    getCallingOverlayPage().waitUntilVisible()
                            && getCallingOverlayPage()
                            .callingDismissIsVisible()
                            && getCallingOverlayPage()
                            .callingSpeakerIsVisible()
                            && getCallingOverlayPage()
                            .callingMicMuteIsVisible());
        } else {
            Assert.assertTrue(
                    "Calling bar is still visible, but should be hidden",
                    getCallingOverlayPage().waitUntilNotVisible());
        }
    }

    /**
     * Check calling Micro bar
     *
     * @throws Exception
     * @step. ^I see calling overlay Micro bar$
     */
    @When("^I see calling overlay Micro bar$")
    public void WhenISeeCallingOverlayMicroBar() throws Exception {
        Assert.assertTrue("Calling Microbar is not visible",
                getCallingOverlayPage().ongoingCallMicrobarIsVisible());
    }

    /**
     * Check calling Mini bar
     *
     * @throws Exception
     * @step. ^I see calling overlay Mini bar$
     */
    @When("^I see calling overlay Mini bar$")
    public void WhenISeeCallingOverlayMiniBar() throws Exception {
        Assert.assertTrue("Calling Minibar is not visible",
                getCallingOverlayPage().ongoingCallMinibarIsVisible());
    }

    /**
     * Press on the join group call button
     *
     * @throws Exception
     * @step. ^I press join group call button$
     */
    @When("^I press join group call button$")
    public void WhenIPressJoinGroupCallButton() throws Exception {
        getCallingOverlayPage().joinGroupCall();
    }

    /**
     * Check whether expected number of users present in call
     *
     * @throws Exception
     * @step. ^I see (\\d+) users? take part in call$
     */
    @When("^I see (\\d+) users? take part in call$")
    public void ISeeXUsersTakePartInGroupCall(final int expectedUsersCount)
            throws Exception {
        int actualUsersCount;
        int ntries = 0;
        do {
            actualUsersCount = getCallingOverlayPage()
                    .numberOfParticipantsInGroupCall();
            if (actualUsersCount == expectedUsersCount) {
                return;
            } else {
                Thread.sleep(3000);
                ntries++;
            }
        } while (ntries < 3);
        throw new AssertionError(
                String.format(
                        "The actual count of users in call (%s) does not equal to the expected count (%s)",
                        actualUsersCount, expectedUsersCount));
    }

    /**
     * Check if alert notifying that group call is full appears
     *
     * @throws Exception
     * @step. ^I see group call is full alert$
     */
    @When("^I see group call is full alert$")
    public void ISeeGroupCallIsFullAlert() throws Exception {
        Assert.assertTrue(getCallingOverlayPage().isGroupCallFullAlertVisible());
    }

    /**
     * Check whether expected number of users present in group call
     *
     * @throws Exception
     * @step. ^I close group call is full alert$
     */
    @When("^I close group call is full alert$")
    public void ICloseGroupCallIsFullAlert() throws Exception {
        getCallingOverlayPage().closeGroupCallFullAlert();
    }

    /**
     * Check that answer call alert appears if there is another call during
     * ongoing call
     *
     * @throws Exception
     * @step. ^I see answer call alert$
     */
    @When("^I see answer call alert$")
    public void ISeeAnswerCallAlert() throws Exception {
        Assert.assertTrue(getCallingOverlayPage().isAnswerCallAlertVisible());
    }

    /**
     * Cancels another call on answer call alert
     *
     * @throws Exception
     * @step. ^I cancel new call from answer call alert$
     */
    @When("^I cancel new call from answer call alert$")
    public void ICancelNewCallFromAlert() throws Exception {
        getCallingOverlayPage().answerCallCancel();
    }

    /**
     * Accepts another call on answer call alert
     *
     * @throws Exception
     * @step. ^I start new call from answer call alert$
     */
    @When("^I start new call from answer call alert$")
    public void IStartNewCallFromAnswerCallAlert() throws Exception {
        getCallingOverlayPage().answerCallContinue();
    }

    /**
     * Check that end current call alert appears if there is another call during
     * ongoing call
     *
     * @throws Exception
     * @step. ^I see end current call alert$
     */
    @When("^I see end current call alert$")
    public void ISeeEndCurrentCallAlert() throws Exception {
        Assert.assertTrue(getCallingOverlayPage()
                .isEndCurrentCallAlertVisible());
    }

    /**
     * Cancels another call on end current call alert
     *
     * @throws Exception
     * @step. ^I cancel new call from end current call alert$
     */
    @When("^I cancel new call from end current call alert$")
    public void ICancelNewCallFromEndCurrentCallAlert() throws Exception {
        getCallingOverlayPage().endCurrentCallCancel();
    }

    /**
     * I start another call on answer call alert
     *
     * @throws Exception
     * @step. ^I start new call from end current call alert$
     */
    @When("^I start new call from end current call alert$")
    public void IStartNewCallFromEndCurrentCallAlert() throws Exception {
        getCallingOverlayPage().endCurrentCallContinue();
    }

}
