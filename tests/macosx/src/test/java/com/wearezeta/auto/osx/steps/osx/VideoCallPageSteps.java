package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import cucumber.api.java.en.Then;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import org.apache.log4j.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class VideoCallPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(VideoCallPageSteps.class.getName());

    private final WebAppTestContext webContext;

    public VideoCallPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @Then("^I verify my self video shows my screen$")
    public void IVerifyMySelfVideoShowsScreen() throws Exception {
        VideoCallPage videoCallPage = webContext.getPagesCollection().getPage(
                VideoCallPage.class);

        // get position and size of self video element
        final Point elementLocation = videoCallPage.getLocalScreenShareVideoElementLocation();
        final Dimension elementSize = videoCallPage.getLocalScreenShareVideoElementSize();

        // get local screenshot
        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(
                MainWirePage.class);
        Optional<BufferedImage> localScreenshot = mainWirePage.getScreenshot();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

        // cutout from screenshot
        int retinaMultiplicator = OSXCommonUtils.
                screenPixelsMultiplier((ZetaOSXDriver) webContext.getChildContext().getDriver());
        int x = mainWirePage.getX() * retinaMultiplicator;
        int y = mainWirePage.getY() * retinaMultiplicator;
        int elementY = elementLocation.getY() * retinaMultiplicator;
        int elementX = elementLocation.getX() * retinaMultiplicator;
        int elementWidth = elementSize.getWidth() * retinaMultiplicator;
        int elementHeight = elementSize.getHeight() * retinaMultiplicator;

        LOG.debug("App location is X=" + x + " and Y=" + y);
        LOG.debug("Relative element location inside of app is X=" + elementX + " and Y=" + elementY);
        LOG.debug("Absolute element location is X=" + (x + elementX) + " and Y=" + (y + elementY));
        LOG.debug("Element size is Width=" + elementWidth + " and Height=" + elementHeight);

        BufferedImage localScreenShareVideo = localScreenshot.get().getSubimage(x + elementX, y + elementY,
                elementWidth, elementHeight);

        // resize local video cutout to full screenshot
        BufferedImage resizedLocalVideo = ImageUtil.resizeImage(localScreenShareVideo, 4);

        // Write images to disk
        String localScreenshotName = "target/mySelfVideo-localScreenshot" + System.currentTimeMillis() + ".png";
        String resizedLocalScreenShareVideoName = "target/mySelfVideo-resizedLocalScreenshare" + System.currentTimeMillis() + ".png";
        ImageUtil.storeImage(localScreenshot.get(), new File(localScreenshotName));
        ImageUtil.storeImage(resizedLocalVideo, new File(resizedLocalScreenShareVideoName));
        String reportPath = "../artifact/tests/macosx/";

        // do feature Matching + homography to find objects
        assertThat("Not enough good matches between "
                + "<a href='" + reportPath + localScreenshotName + "'>screenshot</a> and <a href='" + reportPath + resizedLocalScreenShareVideoName + "'>self video</a>",
                ImageUtil.getMatches(localScreenshot.get(), resizedLocalVideo), greaterThan(20));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String callees) throws Exception {
        for (String callee : webContext.getUsersManager().splitAliases(callees)) {
            final ClientUser userAs = webContext.getUsersManager().findUserByNameOrNameAlias(callee);

            // get screenshot from remote user
            BufferedImage remoteScreenshot = webContext.getCallingManager().getScreenshot(userAs);

            // get local screenshot
            Optional<BufferedImage> localScreenshot = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).
                    getPage(MainWirePage.class).getScreenshot();
            Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

            BufferedImage resizedRemoteScreenshot = ImageUtil.resizeImage(remoteScreenshot, 2);

            // Write images to disk
            long currentTimeMillis = System.currentTimeMillis();
            String localScreenshotName = "target/seesMyScreen-localScreenshot" + currentTimeMillis + ".png";
            String resizedRemoteScreenshotName = "target/seesMyScreen-resizedRemoteScreenshot" + currentTimeMillis + ".png";
            ImageUtil.storeImage(localScreenshot.get(), new File(localScreenshotName));
            ImageUtil.storeImage(resizedRemoteScreenshot, new File(resizedRemoteScreenshotName));
            String reportPath = "../artifact/tests/macosx/";

            // do feature Matching + homography to find objects
            assertThat("Not enough good matches between "
                    + "<a href='" + reportPath + localScreenshotName + "'>screenshot</a> and <a href='" + reportPath + resizedRemoteScreenshotName + "'>remote</a>",
                    ImageUtil.getMatches(localScreenshot.get(), resizedRemoteScreenshot), greaterThan(20));
        }
    }
}
