package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class ImageFullScreenPageSteps {
    private ImageFullScreenPage getImageFullScreenPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ImageFullScreenPage.class);
    }

    @When("^I see Full Screen Page opened$")
    public void ISeeFullScreenPage() throws Exception {
        Assert.assertTrue(getImageFullScreenPage().isImageFullScreenShown());
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

    private ElementState pictureState = new ElementState(
            () -> getImageFullScreenPage().getPreviewPictureScreenshot().orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of the full screen picture preview")
            )
    );

    /**
     * Store current picture preview state into an internal variable
     *
     * @throws Exception
     * @step. ^I remember current fullscreen preview state$
     */
    @When("^I remember current picture preview state$")
    public void IRememberCurrentScreenState() throws Exception {
        pictureState.remember();
    }

    /**
     * Verify whether picture preview  state has been changed or not
     *
     * @param moreOrLess     either one of two possible values
     * @param score          similarity score value. Can be positive float number less than 1
     * @param timeoutSeconds screen change timeout
     * @throws Exception
     * @step. ^I verify that current picture preview similarity score is (more|less) than ([\d\.]+) within (\d+) seconds?$
     */
    @Then("^I verify that current picture preview similarity score is (more|less) than ([\\d\\.]+) within (\\d+) seconds?$")
    public void IVerifyScreenState(String moreOrLess, String score, int timeoutSeconds) throws Exception {
        if (moreOrLess.equals("less")) {
            Assert.assertTrue(
                    String.format("Current picture preview state looks too similar to the previous one after %s seconds",
                            timeoutSeconds), pictureState.isChanged(Timedelta.fromSeconds(timeoutSeconds),
                            Double.parseDouble(score)));
        } else {
            Assert.assertTrue(
                    String.format("Current picture preview state looks different to the previous one after %s seconds",
                            timeoutSeconds), pictureState.isNotChanged(Timedelta.fromSeconds(timeoutSeconds),
                            Double.parseDouble(score)));
        }
    }

}
