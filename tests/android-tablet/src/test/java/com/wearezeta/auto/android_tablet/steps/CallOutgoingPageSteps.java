package com.wearezeta.auto.android_tablet.steps;


import com.wearezeta.auto.android_tablet.pages.TabletCallOutgoingPage;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOutgoingPageSteps {

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();
    
    private final ElementState specialButtonState;
    private final ElementState muteButtonState;

    public CallOutgoingPageSteps() throws Exception {
        this.muteButtonState = new ElementState(getPage().getMuteButtonStateFunction());
        this.specialButtonState = new ElementState(getPage().getSpecialButtonStateFunction());
    }

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
     * Press on the mute button in the outgoing calling controls
     *
     * @throws Exception
     * @step. ^I press mute button$
     */
    @When("^I press mute button for outgoing call$")
    public void WhenIPressMuteButton() throws Exception {
        getPage().toggleMute();
    }

    /**
     * Press on the Speaker button in the outgoing calling controls
     *
     * @throws Exception
     * @step. ^I press speaker button$
     */
    @When("^I press speaker button for outgoing call$")
    public void WhenIPressSpeakerButton() throws Exception {
        getPage().toggleSpeaker();
    }
    
    /**
     * Press on the Video button in the outgoing calling controls
     *
     * @throws Exception
     * @step. ^I press video button$
     */
    @When("^I press video button for outgoing call$")
    public void WhenIPressVideoButton() throws Exception {
        getPage().toggleVideo();
    }
    
    /**
     * Remembers the state of the video button in outgoing calling page
     *
     * @throws Exception
     * @step. ^I remember state of video button for outgoing call$
     */
    @When("^I remember state of video button for outgoing call$")
    public void IRememberStateOfVideoButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the speaker button in outgoing calling page
     *
     * @throws Exception
     * @step. ^I remember state of speaker button for outgoing call$
     */
    @When("^I remember state of speaker button for outgoing call$")
    public void IRememberStateOfSpeakerButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the special action button in outgoing calling page
     *
     * @throws Exception
     * @step. ^I remember state of special action button for outgoing call$
     */
    @When("^I remember state of special action button for outgoing call$")
    public void IRememberStateOfSpacialActionButton() throws Exception {
        specialButtonState.remember();
    }
    
    /**
     * Remembers the state of the mute button in outgoing calling page
     *
     * @throws Exception
     * @step. ^I remember state of mute button for outgoing call$
     */
    @When("^I remember state of mute button for outgoing call$")
    public void IRememberStateOfMuteButton() throws Exception {
        muteButtonState.remember();
    }
    
    /**
     * Verifies change of video button state in outgoing calling page
     *
     * @throws Exception
     * @step. ^I see state of video button has changed for outgoing call$
     */
    @Then("^I see state of video button has changed for outgoing call$")
    public void VerifyStateOfSpacialVideoHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    /**
     * Verifies change of speaker button state in outgoing calling page
     *
     * @throws Exception
     * @step. ^I see state of speaker button has changed for outgoing call$
     */
    @Then("^I see state of speaker button has changed for outgoing call$")
    public void VerifyStateOfSpacialSpeakerHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;
    
    /**
     * Verifies change of special action button state in outgoing calling page
     *
     * @throws Exception
     * @step. ^I see state of special action button has changed for outgoing call$
     */
    @Then("^I see state of special action button has changed for outgoing call$")
    public void VerifyStateOfSpecialActionButtonHasChanged() throws Exception {
        if (!specialButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of special action button has not changed");
        }
    }
    
    /**
     * Verifies change of mute button state in outgoing calling page
     *
     * @throws Exception
     * @step. ^I see state of mute button has changed for outgoing call$
     */
    @Then("^I see state of mute button has changed for outgoing call$")
    public void VerifyStateOfMuteButtonHasChanged() throws Exception {
        if (!muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of mute button has not changed");
        }
    }
}
