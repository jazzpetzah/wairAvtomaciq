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

        // resize local screenshot to cutout
        BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), elementWidth, elementHeight);

        // Write images to disk
        String resizedScreenshotName = "target/resizedScreenshot" + System.currentTimeMillis() + ".png";
        String localScreenShareVideoName = "target/localScreenshare" + System.currentTimeMillis() + ".png";
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
        for (String callee : webContext.getUserManager().splitAliases(callees)) {
            final ClientUser userAs = webContext.getUserManager().findUserByNameOrNameAlias(callee);

            // get screenshot from remote user
            BufferedImage remoteScreenshot = webContext.getCallingManager().getScreenshot(userAs);

            // get local screenshot and resize to remote size
            Optional<BufferedImage> localScreenshot = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).
                    getPage(MainWirePage.class).getScreenshot();
            Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());
            BufferedImage resizedScreenshot = ImageUtil.scaleTo(localScreenshot.get(), remoteScreenshot.getWidth(),
                    remoteScreenshot.getHeight());

            // Write images to disk
            long currentTimeMillis = System.currentTimeMillis();
            String resizedScreenshotName = "target/resizedScreenshot" + currentTimeMillis + ".png";
            String remoteScreenshotName = "target/remoteScreenshot" + currentTimeMillis + ".png";
            ImageUtil.storeImage(resizedScreenshot, new File(resizedScreenshotName));
            ImageUtil.storeImage(remoteScreenshot, new File(remoteScreenshotName));
            String reportPath = "../artifact/tests/macosx/";

            // do feature Matching + homography to find objects
            assertThat("Not enough good matches between "
                    + "<a href='" + reportPath + resizedScreenshotName + "'>screenshot</a> and <a href='" + reportPath + remoteScreenshotName + "'>remote</a>",
                    ImageUtil.getMatches(resizedScreenshot, remoteScreenshot), greaterThan(38));
        }
    }
}
