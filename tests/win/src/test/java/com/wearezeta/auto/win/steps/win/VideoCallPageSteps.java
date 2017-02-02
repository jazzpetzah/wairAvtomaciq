package com.wearezeta.auto.win.steps.win;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.MatcherAssert.assertThat;

public class VideoCallPageSteps {

    private final WebAppTestContext webContext;

    public VideoCallPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @Then("^I verify my self video shows my screen$")
    public void IVerifyMySelfVideoShowsScreen() throws Exception {
        VideoCallPage videoCallPage = webContext.getPagesCollection().getPage(VideoCallPage.class);

        // get position and size of self video element
        WebElement element = videoCallPage.getLocalScreenShareVideoElement();
        final Point elementLocation = element.getLocation();
        final Dimension elementSize = element.getSize();

        // get local screenshot
        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                MainWirePage.class);
        Optional<BufferedImage> localScreenshot = mainWirePage.getScreenshot();
        Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

        // cutout from screenshot
        int x = mainWirePage.getX();
        int y = mainWirePage.getY();
        BufferedImage localScreenShareVideo = localScreenshot.get().getSubimage(x + elementLocation.getX(),
                y + elementLocation.getY(), elementSize.getWidth(), elementSize.getHeight());

        // resize local video cutout to full screenshot
        BufferedImage resizedLocalVideo = ImageUtil.resizeImage(localScreenShareVideo, 4);

        // Convert screenshots to data uris
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(localScreenshot.get(), "png", baos);
        String localScreenshotDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ImageIO.write(resizedLocalVideo, "png", baos2);
        String localScreenShareDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos2.toByteArray());

        // do feature Matching + homography to find objects
        assertThat("Not enough good matches between "
                        + "<img width='200px' src='" + localScreenshotDataURI + "' /> and <img width='200px' src='" +
                localScreenShareDataURI + "' />",
                ImageUtil.getMatches(localScreenshot.get(), resizedLocalVideo), greaterThan(40));
    }

    @Then("^I verify (.*) sees my screen$")
    public void IVerifyUserXVideoShowsScreen(String callees) throws Exception {
        for (String callee : webContext.getUsersManager().splitAliases(callees)) {
            final ClientUser userAs = webContext.getUsersManager().findUserByNameOrNameAlias(callee);

            // get screenshot from remote user
            BufferedImage remoteScreenshot = webContext.getCallingManager().getScreenshot(userAs);

            // get local screenshot and resize to remote size
            Optional<BufferedImage> localScreenshot = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).
                    getPage(MainWirePage.class).getScreenshot();
            Assert.assertTrue("Fullscreen screenshot cannot be captured", localScreenshot.isPresent());

            BufferedImage resizedRemoteScreenshot = ImageUtil.resizeImage(remoteScreenshot, 2);

            // Convert screenshots to data uris
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(localScreenshot.get(), "png", baos);
            String localScreenshotDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ImageIO.write(resizedRemoteScreenshot, "png", baos2);
            String remoteScreenshotDataURI = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos2.toByteArray
                    ());

            // do feature Matching + homography to find objects
            assertThat("Not enough good matches between "
                            + "<img width='200px' src='" + localScreenshotDataURI + "' /> and <img width='200px' src='" +
                    remoteScreenshotDataURI + "' />",
                    ImageUtil.getMatches(localScreenshot.get(), resizedRemoteScreenshot), greaterThan(35));
        }
    }
}
