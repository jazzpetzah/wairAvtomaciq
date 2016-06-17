package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletCallOngoingAudioPage;
import com.wearezeta.auto.common.misc.ElementState;

import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallOngoingPageAudioSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private final ElementState muteButtonState = new ElementState(() -> getPage().getMuteButtonScreenshot());

    private TabletCallOngoingAudioPage getPage() throws Exception {
        return pagesCollection.getPage(TabletCallOngoingAudioPage.class);
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
    public void VerifyStateOfSpacialVideoHasChanged() throws Exception {
        Assert.assertTrue("The sate of mute button is not changed",
                muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE));
    }

    /**
     * Verify whether the particular button is viisile
     *
     * @param shouldNotSee equals to null is the button should be visible
     * @param btnName      button name
     * @throws Exception
     * @step. ^I (do not )?see (speaker|mute) button for ongoing call$
     */
    @Then("^I (do not )?see (speaker|mute) button for ongoing call$")
    public void ISeeButton(String shouldNotSee, String btnName) throws Exception {
        boolean condition;
        switch (btnName) {
            case "mute":
                condition = (shouldNotSee == null) ?
                        getPage().toggleMuteIsVisible() : getPage().toggleMuteIsNotVisible();
                break;
            case "speaker":
                condition = (shouldNotSee == null) ?
                        getPage().toggleSpeakerIsVisible() : getPage().toggleSpeakerIsNotVisible();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
        Assert.assertTrue(String.format((shouldNotSee == null) ?
                "'%s' button is not visible" : "'%s' button is not visible, but should be hidden", btnName), condition);
    }
}
