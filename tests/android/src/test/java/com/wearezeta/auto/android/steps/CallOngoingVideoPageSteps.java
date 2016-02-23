package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallOngoingVideoPage;
import com.wearezeta.auto.common.misc.ElementState;

import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOngoingVideoPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    
    private final ElementState specialButtonState;
    private final ElementState muteButtonState;

    public CallOngoingVideoPageSteps() throws Exception {
        this.muteButtonState = new ElementState(getPage().getMuteButtonStateFunction());
        this.specialButtonState = new ElementState(getPage().getSpecialButtonStateFunction());
    }

    private CallOngoingVideoPage getPage() throws Exception {
        return pagesCollection.getPage(CallOngoingVideoPage.class);
    }

    /**
     * Hangs up the current call
     *
     * @throws Exception
     * @step. ^I hang up ongoing video call$
     */
    @When("^I hang up ongoing video call$")
    public void IHangUp()
            throws Exception {
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
     * Press on the mute button in the calling controls
     *
     * @throws Exception
     * @step. ^I press mute button for ongoing video call$
     */
    @When("^I press mute button for ongoing video call$")
    public void WhenIPressMuteButton() throws Exception {
        getPage().toggleMute();
    }
    
    /**
     * Press on the Video button in the calling controls
     *
     * @throws Exception
     * @step. ^I press video button for ongoing video call$
     */
    @When("^I press video button for ongoing video call$")
    public void WhenIPressVideoButton() throws Exception {
        getPage().toggleVideo();
    }
    
    /**
     * Remembers the state of the video button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of video button for ongoing video call$
     */
    @When("^I remember state of video button for ongoing video call$")
    public void IRememberStateOfVideoButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the special action button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of special action button for ongoing video call$
     */
    @When("^I remember state of special action button for ongoing video call$")
    public void IRememberStateOfSpacialActionButton() throws Exception {
        specialButtonState.remember();
    }
    
    /**
     * Remembers the state of the mute button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of mute button for ongoing video call$
     */
    @When("^I remember state of mute button for ongoing video call$")
    public void IRememberStateOfMuteButton() throws Exception {
        muteButtonState.remember();
    }
    
    /**
     * Verifies change of video button state
     *
     * @throws Exception
     * @step. ^I see state of video button has changed for ongoing video call$
     */
    @Then("^I see state of video button has changed for ongoing video call$")
    public void VerifyStateOfSpacialVideoHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    
    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;
    
    /**
     * Verifies change of special action button state
     *
     * @throws Exception
     * @step. ^I see state of special action button has changed for ongoing video call$
     */
    @Then("^I see state of special action button has changed for ongoing video call$")
    public void VerifyStateOfSpecialActionButtonHasChanged() throws Exception {
        if (!specialButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of special action button has not changed");
        }
    }
    
    /**
     * Verifies change of mute button state
     *
     * @throws Exception
     * @step. ^I see state of mute button has changed for ongoing video call$
     */
    @Then("^I see state of mute button has changed for ongoing video call$")
    public void VerifyStateOfMuteButtonHasChanged() throws Exception {
        if (!muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of mute button has not changed");
        }
    }
}
