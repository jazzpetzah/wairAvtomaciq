package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallOngoingPage;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class CallOngoingPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private CallOngoingPage getPage() throws Exception {
        return pagesCollection.getPage(CallOngoingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;

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
    @When("^I press mute button$")
    public void WhenIPressMuteButton() throws Exception {
        getPage().toggleMute();
    }

    /**
     * Press on the Speaker button in the calling controls
     *
     * @throws Exception
     * @step. ^I press speaker button$
     */
    @When("^I press speaker button$")
    public void WhenIPressSpeakerButton() throws Exception {
        getPage().toggleSpeaker();
    }
    
    /**
     * Press on the Video button in the calling controls
     *
     * @throws Exception
     * @step. ^I press video button$
     */
    @When("^I press video button$")
    public void WhenIPressVideoButton() throws Exception {
        getPage().toggleVideo();
    }
    
    /**
     * Remembers the state of the video button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of video button$
     */
    @When("^I remember state of video button$")
    public void IRememberStateOfVideoButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the speaker button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of speaker button$
     */
    @When("^I remember state of speaker button$")
    public void IRememberStateOfSpeakerButton() throws Exception {
        IRememberStateOfSpacialActionButton();
    }
    
    /**
     * Remembers the state of the special action button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of special action button$
     */
    @When("^I remember state of special action button$")
    public void IRememberStateOfSpacialActionButton() throws Exception {
        getPage().rememberSpecialActionButtonState();
    }
    
    /**
     * Remembers the state of the mute button in calling page
     *
     * @throws Exception
     * @step. ^I remember state of mute button$
     */
    @When("^I remember state of mute button$")
    public void IRememberStateOfMuteButton() throws Exception {
        getPage().rememberMuteButtonState();
    }
    
    /**
     * Verifies change of video button state
     *
     * @throws Exception
     * @step. ^I see state of video button has changed$
     */
    @Then("^I see state of video button has changed$")
    public void VerifyStateOfSpacialVideoHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    /**
     * Verifies change of speaker button state
     *
     * @throws Exception
     * @step. ^I see state of speaker button has changed$
     */
    @Then("^I see state of speaker button has changed$")
    public void VerifyStateOfSpacialSpeakerHasChanged() throws Exception {
        VerifyStateOfSpecialActionButtonHasChanged();
    }
    
    /**
     * Verifies change of special action button state
     *
     * @throws Exception
     * @step. ^I see state of special action button has changed$
     */
    @Then("^I see state of special action button has changed$")
    public void VerifyStateOfSpecialActionButtonHasChanged() throws Exception {
        if (!getPage().specialActionButtonStateHasChanged()) {
            throw new AssertionError("State of special action button has not changed");
        }
    }
    
    /**
     * Verifies change of mute button state
     *
     * @throws Exception
     * @step. ^I see state of mute button has changed$
     */
    @Then("^I see state of mute button has changed$")
    public void VerifyStateOfMuteButtonHasChanged() throws Exception {
        if (!getPage().muteButtonStateHasChanged()) {
            throw new AssertionError("State of mute button has not changed");
        }
    }

//    /**
//     * Checks to see if join group call overlay is present or not
//     *
//     * @param name text on the button
//     * @throws Exception
//     * @step. ^I see \"(.*)\" button$
//     */
//    @Then("^I( do not)? see \"(.*)\" button$")
//    public void WhenISeeGroupCallJoinButton(String shouldNotSee, String name) throws Exception {
//        if (shouldNotSee == null) {
//            Assert.assertTrue(name + " button with not visible in group call overlay",
//                    getCallingOverlayPage().waitUntilJoinGroupCallButtonVisible(name));
//        } else {
//            Assert.assertTrue(name + " button with not visible in group call overlay",
//                    getCallingOverlayPage().waitUntilJoinGroupCallButtonNotVisible(name));
//        }
//    }
}
