package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.ios.pages.VideoCallingOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class VideoCallingOverlayPageSteps {

    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;

    private final ElementState muteButtonState = new ElementState(() -> getVideoCallingOverlayPage().getMuteButtonScreenshot());
    private final ElementState videoButtonState = new ElementState(() -> getVideoCallingOverlayPage().getVideoButtonScreenshot());

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    public VideoCallingOverlayPage getVideoCallingOverlayPage() throws Exception {
        return pagesCollection.getPage(VideoCallingOverlayPage.class);
    }

    /**
     * Verify whether video calling overlay is visible or not
     *
     * @param shouldNotBeVisible equals to null if the overlay should be visible
     * @throws Exception
     * @step. ^I (do not )?see Video Calling overlay$
     */
    @Then("^I (do not )?see Video Calling overlay$")
    public void ISeeCallingOverlay(String shouldNotBeVisible) throws Exception {
        final String switchCameraBtnName = "Switch Camera";
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Video calling overlay is not visible",
                    getVideoCallingOverlayPage().isButtonVisible(switchCameraBtnName));
        } else {
            Assert.assertTrue("Video calling overlay is visible, but should be hidden",
                    getVideoCallingOverlayPage().isButtonInvisible(switchCameraBtnName));
        }
    }

    /**
     * Tap the corresponding button on video calling overlay
     *
     * @param name one of possible button names
     * @throws Exception
     * @step. ^I tap (Mute|Leave|Call Video|Call Speaker|Switch Camera) button on (?:the |\s*)Video Calling overlay$
     */
    @When("^I tap (Mute|Leave|Call Video|Call Speaker|Switch Camera) button on (?:the |\\s*)Video Calling overlay$")
    public void ITapButton(String name) throws Exception {
        getVideoCallingOverlayPage().tapButtonByName(name);
    }

    /**
     * Tap the corresponding button on video calling overlay
     *
     * @param shouldNotBeVisible equals to null if the button should be visible
     * @param name               one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (Mute|Leave|Call Video|Call Speaker|Switch Camera) button on (?:the |\s*)Video Calling overlay$
     */
    @When("^I (do not )?see (Mute|Leave|Call Video|Call Speaker|Switch Camera) button on (?:the |\\s*)Video Calling overlay$")
    public void ISeeButton(String shouldNotBeVisible, String name) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("The '%s' button is not visible on video calling overlay", name),
                    getVideoCallingOverlayPage().isButtonVisible(name));
        } else {
            Assert.assertTrue(String.format("The '%s' button is visible on video calling overlay, but should be shown",
                    name), getVideoCallingOverlayPage().isButtonInvisible(name));
        }
    }

    /**
     * Remembers the state of the certain button in calling page
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I remember state of (Mute|Video) button on Video Calling overlay$
     */
    @When("^I remember state of (Mute|Video) button on Video Calling overlay$")
    public void IRememberStateOfButton(String btnName) throws Exception {
        switch (btnName) {
            case "Mute":
                muteButtonState.remember();
                break;
            case "Video":
                videoButtonState.remember();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

    /**
     * Verifies change of certain button state
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I see state of (Mute|Video) button has changed on Video Calling overlay$
     */
    @Then("^I see state of (Mute|Video) button has changed on Video Calling overlay$")
    public void VerifyStateOfButtonHasChanged(String btnName) throws Exception {
        boolean isChanged;
        switch (btnName) {
            case "Mute":
                isChanged = muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            case "Video":
                isChanged = videoButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
        Assert.assertTrue(String.format("The sate of '%s' button is not changed", btnName), isChanged);
    }
}
