package com.wearezeta.auto.android_tablet.steps;


import com.wearezeta.auto.android_tablet.pages.TabletCallOutgoingPage;
import com.wearezeta.auto.common.misc.ElementState;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallOutgoingPageSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private final ElementState videoButtonState = new ElementState(() -> getPage().getSpecialButtonScreenshot());
    private final ElementState speakerButtonState = new ElementState(() -> getPage().getSpecialButtonScreenshot());
    private final ElementState muteButtonState = new ElementState(() -> getPage().getMuteButtonScreenshot());

    private TabletCallOutgoingPage getPage() throws Exception {
        return pagesCollection.getPage(TabletCallOutgoingPage.class);
    }

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
    /**
     * Tap the corresponding button onm video overlay
     *
     * @param btnName button name
     * @throws Exception
     * @step. I tap (speaker|video|mute) button for outgoing call
     */
    @When("^I tap (speaker|video|mute) button for outgoing call$")
    public void WhenITapButton(String btnName) throws Exception {
        switch (btnName) {
            case "mute":
                getPage().toggleMute();
                break;
            case "speaker":
                getPage().toggleSpeaker();
                break;
            case "video":
                getPage().toggleVideo();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

    /**
     * Remembers the state of the certain button in calling page
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I remember state of (speaker|video|mute) button for outgoing call$
     */
    @When("^I remember state of (speaker|video|mute) button for outgoing call$")
    public void IRememberStateOfButton(String btnName) throws Exception {
        switch (btnName) {
            case "mute":
                muteButtonState.remember();
                break;
            case "speaker":
                speakerButtonState.remember();
                break;
            case "video":
                videoButtonState.remember();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;

    /**
     * Verifies change of certain button state
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I see state of (speaker|video|mute) button has changed for outgoing call$
     */
    @Then("^I see state of (speaker|video|mute) button has changed for outgoing call$")
    public void VerifyStateOfSpacialVideoHasChanged(String btnName) throws Exception {
        boolean isChanged;
        switch (btnName) {
            case "mute":
                isChanged = muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            case "speaker":
                isChanged = speakerButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            case "video":
                isChanged = videoButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
        Assert.assertTrue(String.format("The sate of '%s' button is not changed", btnName), isChanged);
    }
}
