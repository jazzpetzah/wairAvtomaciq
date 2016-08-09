package com.wearezeta.auto.web.steps;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import org.junit.Assert;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

    /**
     * Click minimize button on video call page
     *
     * @throws Exception
     * @step. ^I minimize video call$
     */
    @When("^I minimize video call$")
    public void IMinimizeVideoCall() throws Exception {
        context.getPagesCollection().getPage(VideoCallPage.class).clickMinimizeVideoCallButton();
    }

    /**
     * Maximizes video call
     *
     * @throws Exception
     * @step. ^I maximize video call$
     */
    @When("^I maximize video call via button on remote video$")
    public void IMaximizeVideoCallWithRemoteVideoButton() throws Exception {
        context.getPagesCollection().getPage(VideoCallPage.class).clickMaximizeVideoCallButtonOnRemoteVideo();
    }

    /**
     * Maximizes video call
     *
     * @throws Exception
     * @step. ^I maximize video call$
     */
    @When("^I maximize video call via titlebar$")
    public void IMaximizeVideoCall() throws Exception {
        context.getPagesCollection().getPage(VideoCallPage.class).clickMaximizeVideoCallButton();
    }

    /**
     * Checks if the video call minimized/maximized
     *
     * @param videoCallSize is either minimized|maximized
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @Then("^I see video call is (minimized|maximized)$")
    public void ISeeVideoCallMinimized(String videoCallSize) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        if (videoCallSize.equals("minimized")) {
            //Assert.assertTrue("Video is in portrait mode", videoCallPage.isVideoNotInPortrait());
            Assert.assertTrue("Minimize Video Call button is visible", videoCallPage.isMinimizeVideoCallButtonNotVisible());
        } else {
            //Assert.assertTrue("Video is not in portrait mode", videoCallPage.isVideoInPortrait());
            Assert.assertTrue("Minimize Video Call button is not visible", videoCallPage.isMinimizeVideoCallButtonVisible());
        }
    }

    /**
     * Turn off and on the camera on video call page
     *
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @When("^I click on video button$")
    public void IClickVideoButton() throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
            videoCallPage.clickVideoButton();
    }

    /**
     * Checks if the camera on video call page off or on
     *
     * @param videoButtonState pressed|unpressed
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @When("^I see video button (unpressed|pressed)$")
    public void ISeeVideoButtonPressed(String videoButtonState) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        if (videoButtonState.equals("unpressed")) {
            Assert.assertTrue("Video Button is pressed", videoCallPage.isVideoButtonUnPressed());
        } else {
            Assert.assertTrue("Video Button is not pressed", videoCallPage.isVideoButtonPressed());
        }
    }

    /**
     * Checks if the self video is black
     *
     * @throws Exception
     * @step. ^I see my self video is black$
     */
    @Then("^I see my self video is( not)? black$")
    public void ISeeSelfVideoBlack(String not) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        Optional<BufferedImage> selfVideo = videoCallPage.getSelfVideo();
        Assert.assertTrue("Self video is not present", selfVideo.isPresent());
        BufferedImage image = selfVideo.get();
        Color pixel = new Color(image.getRGB(image.getWidth() / 2, image.getHeight() / 2));
        if(not == null) {
            assertThat("RGB red", pixel.getRed(), lessThan(2));
            assertThat("RGB green", pixel.getGreen(), lessThan(2));
            assertThat("RGB blue", pixel.getBlue(), lessThan(2));
        } else {
            assertThat("All RGB values summarized", pixel.getRed() + pixel.getGreen() + pixel.getGreen(), greaterThan(20));
        }
    }
    
    /**
     * Checks whether the self video is on or off
     *
     * @throws Exception
     * @step. ^I see my self video is (off|on)$
     */
    @Then("^I see my self video is (off|on)$")
    public void ISeeSelfVideoOff(String onOffToggle) throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        if ("off".equals(onOffToggle)) {
            assertTrue("Disabled video icon is still shown", videoCallPage.isDisabledVideoIconVisible());
        }else{
            assertTrue("Disabled video icon is not shown", videoCallPage.isDisabledVideoIconInvisible());
        }
    }

    /**
     * Check if minimized video is black
     *
     * @throws Exception
     */
    @Then("^I see minimized video is black$")
    public void ISeeMinimizedVideoBlack() throws Exception {
        VideoCallPage videoCallPage = context.getPagesCollection().getPage(VideoCallPage.class);
        Optional<BufferedImage> minimizedRemoteVideo = videoCallPage.getMinimizedRemoteVideo();
        Assert.assertTrue("Minimized remote video is not present", minimizedRemoteVideo.isPresent());
        BufferedImage image = minimizedRemoteVideo.get();
        Color pixel = new Color(image.getRGB(20, 20));
        assertThat("RGB red", pixel.getRed(), lessThan(2));
        assertThat("RGB green", pixel.getGreen(), lessThan(2));
        assertThat("RGB blue", pixel.getBlue(), lessThan(2));
    }
}
