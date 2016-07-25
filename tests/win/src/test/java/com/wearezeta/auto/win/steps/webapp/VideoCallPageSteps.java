package com.wearezeta.auto.win.steps.webapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class VideoCallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();
    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();
    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();

    /**
     * Checks if the video call minimized/maximized
     *
     * @param videoCallSize is either minimized|maximized
     * @throws Exception
     * @step. ^I see video call is (minimized|maximized)$
     */
    @Then("^I see video call is (minimized|maximized)$")
    public void ISeeVideoCallMinimized(String videoCallSize) throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        if (videoCallSize.equals("minimized")) {
            //Assert.assertTrue("Video is in portrait mode", videoCallPage.isVideoNotInPortrait());
            Assert.assertTrue("Maximize Video Call button is not visible", videoCallPage.isMaximizeVideoCallButtonVisible());
        } else {
            //Assert.assertTrue("Video is not in portrait mode", videoCallPage.isVideoInPortrait());
            Assert.assertTrue("Minimize Video Call button is not visible", videoCallPage.isMinimizeVideoCallButtonVisible());

        }
    }

    @Then("^I click on screen share button$")
    public void IClickScreenShareButton() throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);
        videoCallPage.clickScreenShareButton();
    }

    @Then("^I verify my self video shows my screen$")
    public void IVerifyMySelfVideoShowsScreen() throws Exception {
        VideoCallPage videoCallPage = webappPagesCollection.getPage(VideoCallPage.class);

        // get position and size of self video element
        WebElement element = videoCallPage.getLocalScreenShareVideoElement();
        final Point elementLocation = element.getLocation();
        final Dimension elementSize = element.getSize();

        // get local screenshot
        MainWirePage mainWirePage = winPagesCollection.getPage(MainWirePage.class);
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
        assertThat("Not enough good matches between " +
                "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + localScreenShareVideoName + "'>self video</a>", ImageUtil.getMatches(resizedScreenshot, localScreenShareVideo), greaterThan(50));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String userAlias) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAlias);

        // get screenshot from remote user
        BufferedImage remoteScreenshot = commonCallingSteps.getScreenshot(userAs);

        // get local screenshot and resize to remote size
        Optional<BufferedImage> localScreenshot = winPagesCollection.getPage(MainWirePage.class).getScreenshot();
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
        assertThat("Not enough good matches between " +
                "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + remoteScreenshotName + "'>remote</a>", ImageUtil.getMatches(resizedScreenshot, remoteScreenshot), greaterThan(50));
    }
}
