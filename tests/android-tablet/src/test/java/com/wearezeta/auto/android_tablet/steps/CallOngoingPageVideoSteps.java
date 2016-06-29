package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletCallOngoingVideoPage;
import com.wearezeta.auto.common.misc.ElementState;

import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class CallOngoingPageVideoSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private final ElementState specialButtonState = new ElementState(() -> getPage().getSpecialButtonScreenshot());
    private final ElementState muteButtonState = new ElementState(() -> getPage().getMuteButtonScreenshot());

    private TabletCallOngoingVideoPage getPage() throws Exception {
        return pagesCollection.getPage(TabletCallOngoingVideoPage.class);
    }

    /**
     * Hangs up the current video call
     *
     * @throws Exception
     * @step. ^I hang up ongoing video call$
     */
    @When("^I hang up ongoing video call$")
    public void IHangUp() throws Exception {
        getPage().hangup();
    }

    /**
     * Verifies presence of ongoing call
     *
     * @throws Exception
     * @step. ^I (do not )?see ongoing video call$
     */
    @When("^I (do not )?see ongoing video call$")
    public void ISeeOutgoingCall(String not) throws Exception {
        if (not == null) {
            assertTrue("Ongoing video call not visible", getPage().waitUntilVisible());
        } else {
            assertTrue("Ongoing video call should not be visible", getPage().waitUntilNotVisible());
        }
    }

    /**
     * Tap the corresponding button on video overlay
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (mute|video) button for ongoing video call$
     */
    @When("^I tap (mute|video) button for ongoing video call$")
    public void WhenITapButton(String btnName) throws Exception {
        switch (btnName) {
            case "mute":
                getPage().toggleMute();
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
     * @step. ^I remember state of (video|mute) button for ongoing call$
     */
    @When("^I remember state of (video|mute) button for ongoing video call$")
    public void IRememberStateOfButton(String btnName) throws Exception {
        switch (btnName) {
            case "mute":
                muteButtonState.remember();
                break;
            case "video":
                specialButtonState.remember();
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
     * @step. ^I see state of (video|mute) button has changed for ongoing call$
     */
    @Then("^I see state of (video|mute) button has changed for ongoing video call$")
    public void VerifyStateOfSpacialVideoHasChanged(String btnName) throws Exception {
        boolean isChanged;
        switch (btnName) {
            case "mute":
                isChanged = muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            case "video":
                isChanged = specialButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
        Assert.assertTrue(String.format("The sate of '%s' button is not changed", btnName), isChanged);
    }
}
