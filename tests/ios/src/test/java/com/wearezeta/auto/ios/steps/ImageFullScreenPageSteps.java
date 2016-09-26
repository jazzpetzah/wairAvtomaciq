package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
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

    @When("I tap close fullscreen page button")
    public void ITapCloseFullscreenButton() throws Exception {
        getImageFullScreenPage().tapCloseButton();
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
