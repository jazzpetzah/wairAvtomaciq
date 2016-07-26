package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ImageFullScreenPage getImageFullScreenPage() throws Exception {
        return pagesCollection.getPage(ImageFullScreenPage.class);
    }

    @When("^I see Full Screen Page opened$")
    public void ISeeFullScreenPage() throws Exception {
        Assert.assertTrue(getImageFullScreenPage().isImageFullScreenShown());
    }

    @When("I tap on fullscreen page")
    public void ITapFullScreenPage() throws Exception {
        getImageFullScreenPage().tapOnFullScreenPage();
    }

    @When("I see sender first name (.*) on fullscreen page")
    public void ISeeSenderName(String sender) throws Exception {
        String senderFirstName = usrMgr.findUserByNameOrNameAlias(sender)
                .getName().split(" ")[0].toUpperCase();
        Assert.assertTrue(String.format("Sender name '%s' does not exist on the page", senderFirstName),
                getImageFullScreenPage().isSenderNameVisible(senderFirstName));
    }

    @When("I see send date on fullscreen page")
    public void ISeeSendDate() throws Exception {
        final String actualTimestamp = getImageFullScreenPage().getTimeStamp().trim();
        // TODO: Probably, the verification regex could be improved?
        Assert.assertTrue(String.format("The actual timestamp '%s' seems to be incorrect", actualTimestamp),
                actualTimestamp.length() > 0 && actualTimestamp.matches(".*\\b\\d+:\\d+\\b.*"));
    }

    @When("I see download button shown on fullscreen page")
    public void ISeeDownloadButtonOnFullscreenPage() throws Exception {
        Assert.assertTrue(getImageFullScreenPage().isDownloadButtonVisible());
    }

    @When("I tap download button on fullscreen page")
    public void ITapDownloadButtonOnFullscreenPage() throws Exception {
        getImageFullScreenPage().tapDownloadButton();
    }

    @When("I verify image caption and download button are not shown")
    public void IDontSeeImageCaptionAndDownloadButton() throws Exception {
        Assert.assertTrue("Image download button is visible, but should be hidden",
                getImageFullScreenPage().isDownloadButtonInvisible());
        Assert.assertTrue("Image timestamp label is visible, but should be hidden",
                getImageFullScreenPage().isSentTimeInvisible());
    }

    @When("I tap close fullscreen page button")
    public void ITapCloseFullscreenButton() throws Exception {
        getImageFullScreenPage().tapCloseButton();
    }

    /**
     * Presses the sketch button on the image fullscreen page
     *
     * @throws Exception
     * @step. ^I press Sketch button on image fullscreen page$
     */
    @When("^I press Sketch button on image fullscreen page$")
    public void IPressSketchButtonOnImageFullscreenPage() throws Exception {
        getImageFullScreenPage().tapSketchButton();
    }

    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;

    /**
     * Verify whether the particular picture is animated
     *
     * @throws Exception
     * @step. ^I see the picture on image fullscreen page is animated$
     */
    @Then("^I see the picture on image fullscreen page is animated$")
    public void ISeePictureIsAnimated() throws Exception {
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        final double avgThreshold = ImageUtil.getAnimationThreshold(getImageFullScreenPage()::getPreviewPictureScreenshot,
                maxFrames, screenshotingDelay);
        Assert.assertTrue(String.format("The picture in the image preview view seems to be static (%.2f >= %.2f)",
                avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
    }
}
