package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallOngoingPage;
import com.wearezeta.auto.common.misc.ElementState;

import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOngoingPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    
    private final ElementState specialButtonState;
    private final ElementState muteButtonState;

    public CallOngoingPageSteps() throws Exception {
        this.muteButtonState = new ElementState(getPage().getMuteButtonStateFunction());
        this.specialButtonState = new ElementState(getPage().getSpecialButtonStateFunction());
    }

    private CallOngoingPage getPage() throws Exception {
        return pagesCollection.getPage(CallOngoingPage.class);
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
     * Press on the mute button in the calling controls
     *
     * @throws Exception
     * @step. ^I press mute button$
     */
    @When("^I press mute button for ongoing call$")
    public void WhenIPressMuteButton() throws Exception {
        getPage().toggleMute();
    }

    /**
     * Press on the Speaker button in the calling controls
     *
     * @throws Exception
     * @step. ^I press speaker button$
     */
    @When("^I press speaker button for ongoing call$")
    public void WhenIPressSpeakerButton() throws Exception {
        getPage().toggleSpeaker();
    }
    
    /**
     * Remembers the state of the speaker button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of speaker button$
     */
    @When("^I remember state of speaker button for ongoing call$")
    public void IRememberStateOfSpeakerButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the special action button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of special action button$
     */
    @When("^I remember state of special action button for ongoing call$")
    public void IRememberStateOfSpacialActionButton() throws Exception {
        specialButtonState.remember();
    }
    
    /**
     * Remembers the state of the mute button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of mute button$
     */
    @When("^I remember state of mute button for ongoing call$")
    public void IRememberStateOfMuteButton() throws Exception {
        muteButtonState.remember();
    }
    
    /**
     * Verifies change of speaker button state
     *
     * @throws Exception
     * @step. ^I see state of speaker button has changed$
     */
    @Then("^I see state of speaker button has changed for ongoing call$")
    public void VerifyStateOfSpacialSpeakerHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;
    
    /**
     * Verifies change of special action button state
     *
     * @throws Exception
     * @step. ^I see state of special action button has changed$
     */
    @Then("^I see state of special action button has changed for ongoing call$")
    public void VerifyStateOfSpecialActionButtonHasChanged() throws Exception {
        if (!specialButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of special action button has not changed");
        }
    }
    
    /**
     * Verifies change of mute button state
     *
     * @throws Exception
     * @step. ^I see state of mute button has changed$
     */
    @Then("^I see state of mute button has changed for ongoing call$")
    public void VerifyStateOfMuteButtonHasChanged() throws Exception {
        if (!muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE)) {
            throw new AssertionError("State of mute button has not changed");
        }
    }
}
