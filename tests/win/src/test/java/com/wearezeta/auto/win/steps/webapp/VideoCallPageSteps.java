package com.wearezeta.auto.win.steps.webapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.win.common.WrapperTestContext;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class VideoCallPageSteps {
    private final WrapperTestContext context;

    public VideoCallPageSteps() {
        this.context = new WrapperTestContext();
    }

    public VideoCallPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I maximize video call via titlebar$")
    public void IMaximizeVideoCall() throws Exception {
        context.getWebappPagesCollection().getPage(VideoCallPage.class).clickMaximizeVideoCallButton();
    }

    @Then("^I see video call is (minimized|maximized)$")
    public void ISeeVideoCallMinimized(String videoCallSize) throws Exception {
        VideoCallPage videoCallPage = context.getWebappPagesCollection().getPage(VideoCallPage.class);
        if (videoCallSize.equals("minimized")) {
            //Assert.assertTrue("Video is in portrait mode", videoCallPage.isVideoNotInPortrait());
            Assert.assertTrue("Minimize Video Call button is visible", videoCallPage.isMinimizeVideoCallButtonNotVisible());
        } else {
            //Assert.assertTrue("Video is not in portrait mode", videoCallPage.isVideoInPortrait());
            Assert.assertTrue("Minimize Video Call button is not visible", videoCallPage.isMinimizeVideoCallButtonVisible());
        }
    }

    @Then("^I click on screen share button$")
    public void IClickScreenShareButton() throws Exception {
        VideoCallPage videoCallPage = context.getWebappPagesCollection().getPage(VideoCallPage.class);
        videoCallPage.clickScreenShareButton();
    }

    @Then("^I verify my self video shows my screen$")
    public void IVerifyMySelfVideoShowsScreen() throws Exception {
        VideoCallPage videoCallPage = context.getWebappPagesCollection().getPage(VideoCallPage.class);

        // get position and size of self video element
        WebElement element = videoCallPage.getLocalScreenShareVideoElement();
        final Point elementLocation = element.getLocation();
        final Dimension elementSize = element.getSize();

        // get local screenshot
        MainWirePage mainWirePage = context.getWinPagesCollection().getPage(MainWirePage.class);
        Optional<BufferedImage> localScreenshot = mainWirePage.getScreenshot();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

        // cutout from screenshot
        int x = mainWirePage.getX();
        int y = mainWirePage.getY();
        BufferedImage localScreenShareVideo = localScreenshot.get().getSubimage(x + elementLocation.getX(),
                y + elementLocation.getY(), elementSize.getWidth(), elementSize.getHeight());

        // resize local screenshot to cutout
        BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), localScreenShareVideo.getWidth(),
                localScreenShareVideo.getHeight());

        // Write images to disk
        String resizedScreenshotName = "target/resizedScreenshot" + System.currentTimeMillis() + ".png";
        String localScreenShareVideoName = "target/remoteScreenshot" + System.currentTimeMillis() + ".png";
        ImageUtil.storeImage(resizedScreenshot, new File(resizedScreenshotName));
        ImageUtil.storeImage(localScreenShareVideo, new File(localScreenShareVideoName));
        String reportPath = "../artifact/tests/macosx/";

        // do feature Matching + homography to find objects
        assertThat("Not enough good matches between "
                + "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + localScreenShareVideoName + "'>self video</a>",
                ImageUtil.getMatches(resizedScreenshot, localScreenShareVideo), greaterThan(5));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            final ClientUser userAs = context.getUserManager().findUserByNameOrNameAlias(callee);

            // get screenshot from remote user
            BufferedImage remoteScreenshot = context.getCallingManager().getScreenshot(userAs);

            // get local screenshot and resize to remote size
            Optional<BufferedImage> localScreenshot = context.getWinPagesCollection().getPage(MainWirePage.class).getScreenshot();
            Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());
            BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), remoteScreenshot.getWidth(),
                    remoteScreenshot.getHeight());

            // Write images to disk
            String resizedScreenshotName = "target/resizedScreenshot" + System.currentTimeMillis() + ".png";
            String remoteScreenshotName = "target/remoteScreenshot" + System.currentTimeMillis() + ".png";
            ImageUtil.storeImage(resizedScreenshot, new File(resizedScreenshotName));
            ImageUtil.storeImage(remoteScreenshot, new File(remoteScreenshotName));
            String reportPath = "../artifact/tests/macosx/";

            // do feature Matching + homography to find objects
            assertThat("Not enough good matches between "
                    + "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + remoteScreenshotName + "'>remote</a>",
                    ImageUtil.getMatches(resizedScreenshot, remoteScreenshot), greaterThan(40));
        }
    }

    @When("^I minimize video call$")
    public void IMinimizeVideoCall() throws Exception {
        context.getWebappPagesCollection().getPage(VideoCallPage.class).clickMinimizeVideoCallButton();
    }

    @When("^I click on video button$")
    public void IClickVideoButton() throws Exception {
        VideoCallPage videoCallPage = context.getWebappPagesCollection().getPage(VideoCallPage.class);
        videoCallPage.clickVideoButton();
    }

    @Then("^I see my self video is (off|on)$")
    public void ISeeSelfVideoOff(String onOffToggle) throws Exception {
        VideoCallPage videoCallPage = context.getWebappPagesCollection().getPage(VideoCallPage.class);
        if ("off".equals(onOffToggle)) {
            assertTrue("Disabled video icon is still shown", videoCallPage.isDisabledVideoIconVisible());
        } else {
            assertTrue("Disabled video icon is not shown", videoCallPage.isDisabledVideoIconInvisible());
        }
    }
}
