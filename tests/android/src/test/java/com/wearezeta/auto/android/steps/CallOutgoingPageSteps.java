package com.wearezeta.auto.android.steps;


import com.wearezeta.auto.android.pages.AbstractCallOutgoingPage;
import com.wearezeta.auto.android.pages.CallOutgoingAudioPage;
import com.wearezeta.auto.android.pages.CallOutgoingVideoPage;
import com.wearezeta.auto.common.misc.ElementState;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallOutgoingPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private final ElementState videoButtonState = new ElementState(() -> getAudioPage().getSpecialButtonScreenshot());
    private final ElementState speakerButtonState = new ElementState(() -> getAudioPage().getSpecialButtonScreenshot());
    private final ElementState muteButtonState = new ElementState(() -> getAudioPage().getMuteButtonScreenshot());

    private CallOutgoingVideoPage getVideoPage() throws Exception {
        return pagesCollection.getPage(CallOutgoingVideoPage.class);
    }

    private CallOutgoingAudioPage getAudioPage() throws Exception {
        return pagesCollection.getPage(CallOutgoingAudioPage.class);
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
        getAudioPage().hangup();
    }

    /**
     * Verifies presence of outgoing call
     *
     * @param not       equals null means that should see outgoing call
     * @param videoCall true means it is outgoing video call, otherwise is outgoing audio all
     * @throws Exception
     * @step. ^I (do not )?see outgoing (video )?call$
     */
    @When("^I (do not )?see outgoing (video )?call$")
    public void ISeeOutgoingCall(String not, String videoCall) throws Exception {
        final boolean isVideoCall = (videoCall != null);
        if (not == null) {
            assertTrue(String.format("Outgoing %s call not visible", isVideoCall ? "video" : "audio"),
                    isVideoCall ? getVideoPage().waitUntilVisible() : getAudioPage().waitUntilVisible());
        } else {
            assertTrue(String.format("Outgoing %s call should not be visible", isVideoCall ? "video" : "audio"),
                    isVideoCall ? getVideoPage().waitUntilInvisible() : getAudioPage().waitUntilInvisible());
        }
    }

    /**
     * Tap the corresponding button onm video overlay
     *
     * @param btnName button name
     * @throws Exception
     * @step. I tap (speaker|video|mute)) button for outgoing call
     */
    @When("^I tap (speaker|video|mute) button for outgoing call$")
    public void ITapButton(String btnName) throws Exception {
        switch (btnName) {
            case "mute":
                getAudioPage().toggleMute();
                break;
            case "speaker":
                getAudioPage().toggleSpeaker();
                break;
            case "video":
                getAudioPage().toggleVideo();
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
     * @step. ^I see state of (speaker|mute) button has changed for outgoing call$
     */
    @Then("^I see state of (speaker|mute) button has changed for outgoing call$")
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
