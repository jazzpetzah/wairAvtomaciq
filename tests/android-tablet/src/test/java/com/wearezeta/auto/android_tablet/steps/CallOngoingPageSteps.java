package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletCallOngoingPage;
import com.wearezeta.auto.common.misc.ElementState;

import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallOngoingPageSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private final ElementState muteButtonState = new ElementState(() -> getPage().getMuteButtonScreenshot());

    private TabletCallOngoingPage getPage() throws Exception {
        return pagesCollection.getPage(TabletCallOngoingPage.class);
    }

    /**
     * Hangs up the current call
     *
     * @throws Exception
     * @step. ^I hang up$
     */
    @When("^I hang up ongoing call$")
    public void IHangUp()
            throws Exception {
        getPage().hangup();
    }

    /**
     * Verifies presence of ongoing call
     *
     * @throws Exception
     * @step. ^I (do not )?see ongoing call$
     */
    @When("^I (do not )?see ongoing call$")
    public void ISeeOutgoingCall(String not) throws Exception {
        if (not == null) {
            assertTrue("Ongoing call not visible", getPage().waitUntilVisible());
        } else {
            assertTrue("Ongoing call should not be visible", getPage().waitUntilNotVisible());
        }
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
        int actualUsersCount = getPage().getNumberOfParticipants();
        if (actualUsersCount != expectedUsersCount) {
            throw new AssertionError(String.format(
                    "The actual count of users in call %s does not equal to the expected count %s",
                    actualUsersCount, expectedUsersCount));
        }
    }

    /**
     * Tap the mute button on audio call overlay
     *
     * @throws Exception
     * @step. ^I tap mute button for ongoing call$
     */
    @When("^I tap mute button for ongoing call$")
    public void WhenITapMuteButton() throws Exception {
        getPage().toggleMute();
    }

    /**
     * Remembers the state of the mute button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of mute button for ongoing call$
     */
    @When("^I remember state of mute button for ongoing call$")
    public void IRememberStateOfButton() throws Exception {
        muteButtonState.remember();
    }

    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;

    /**
     * Verifies change of mute button state
     *
     * @throws Exception
     * @step. ^I see state of mute button has changed for ongoing call$
     */
    @Then("^I see state of mute button has changed for ongoing call$")
    public void VerifyStateOfSpacialVideoHasChanged(String btnName) throws Exception {
        final boolean isChanged = muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
        Assert.assertTrue(String.format("The sate of '%s' button is not changed", btnName), isChanged);
    }
}
