package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import org.junit.Assert;

public class VideoCallPageSteps {

    private final TestContext context;
    
    public VideoCallPageSteps() {
        this.context = new TestContext();
    }

    public VideoCallPageSteps(TestContext context) {
        this.context = context;
    }
    
    /**
     * End the current video call by clicking the End video call button on video call page
     *
     * @throws Exception
     * @step. ^I end the video call$
     */
    @When("^I end the video call$")
    public void IEndTheCall() throws Exception {
        context.getPagesCollection().getPage(VideoCallPage.class).clickEndVideoCallButton();
    }

    /**
     * Verifies if End video call button is visible
     *
     * @throws Exception
     * @step. ^I see video call page$
     */
    @And("^I see end video call button$")
    public void ISeeEndVideoCallButton() throws Exception {
        Assert.assertTrue("End video call button is not visible",
                context.getPagesCollection().getPage(VideoCallPage.class).isEndVideoCallButtonVisible());
    }

    /**
     * Click mute button on video call page
     *
     * @throws Exception
     * @step. ^I click mute button on video call page$
     */
    @When("^I click mute button on video call page$")
    public void IClickMuteButton() throws Exception {
        context.getPagesCollection().getPage(VideoCallPage.class).clickMuteCallButton();
    }

    /**
     * Checks if mute button on video call page pressed
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I see mute button on video call page is( not)? pressed$
     */
    @When("^I see mute button on video call page is( not)? pressed$")
    public void ISeeMuteButtonNotPressed(String doNot) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        if (doNot == null) {
            Assert.assertTrue("Mute call button is not pressed", videoCallPage.isMuteCallButtonPressed());
        } else {
            Assert.assertTrue("Mute call button is pressed", videoCallPage.isMuteCallButtonNotPressed());
        }
    }
    /**
     * Checks if the duration timer is visible during a video call
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I can see the video call timer$
     */
    @Then("^I( do not)? see the video call timer$")
    public void ICanSeeDurationTimer(String doNot) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        if (doNot == null) {
            Assert.assertTrue("Duration Timer is not visible", videoCallPage.isDurationTimerVisible());
        } else {
            Assert.assertTrue("Duration Timer is visible", videoCallPage.isDurationTimerNotVisible());
        }
    }
}
