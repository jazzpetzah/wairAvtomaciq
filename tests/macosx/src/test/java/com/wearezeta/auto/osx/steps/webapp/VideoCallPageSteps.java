package com.wearezeta.auto.osx.steps.webapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class VideoCallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();
    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();
    private final OSXPagesCollection osxPagesCollection = OSXPagesCollection.getInstance();

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
        Optional<BufferedImage> localScreenshot = osxPagesCollection.getPage(MainWirePage.class).getScreenshot();
        BufferedImage localScreenShareVideo = osxPagesCollection.getPage(MainWirePage.class).getElementScreenshot
                (videoCallPage.getLocalScreenShareVideoElement()).get();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());
        BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), localScreenShareVideo.getWidth(),
                localScreenShareVideo.getHeight());
        ImageUtil.storeImage(localScreenShareVideo, new File("/var/tmp/localScreenshot.png"));
        ImageUtil.storeImage(resizedScreenshot, new File("/var/tmp/resizedScreenshot.png"));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String userAlias) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAlias);
        BufferedImage remoteScreenshot = commonCallingSteps.getScreenshot(userAs);
        Optional<BufferedImage> localScreenshot = osxPagesCollection.getPage(MainWirePage.class).getScreenshot();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());
        BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), remoteScreenshot.getWidth(),
                remoteScreenshot.getHeight());
        ImageUtil.storeImage(remoteScreenshot, new File("/var/tmp/remoteScreenshot.png"));
        ImageUtil.storeImage(resizedScreenshot, new File("/var/tmp/resizedScreenshot.png"));
    }
}
