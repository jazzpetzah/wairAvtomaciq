package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.VideoMessagePreviewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class VideoMessagePreviewPageSteps {
    private VideoMessagePreviewPage getVideoMessagePreviewPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(VideoMessagePreviewPage.class);
    }

    /**
     * Send or cancel recorded video from Video Preview
     *
     * @param doNotSend equals null means tap on send button, otherwise tap on cancel button
     * @throws Exception
     * @step. ^I( do not)? send recorded video from video message preview$
     */
    @When("^I( do not)? send recorded video from video message preview$")
    public void ISendVideo(String doNotSend) throws Exception {
        if (doNotSend == null) {
            getVideoMessagePreviewPage().tapOnSendVideoButton();
        } else {
            getVideoMessagePreviewPage().tapOnCancelVideoButton();
        }
    }

    /**
     * Verify the video message preview is visible
     *
     * @throws Exception
     * @step. ^I see video message preview$
     */
    @Then("^I see video message preview$")
    public void ISeeVideoMessagePreview() throws Exception {
        Assert.assertTrue("The video message preview should be visible",
                getVideoMessagePreviewPage().isVideoPreviewVisible());
        Assert.assertTrue("The video message preview toolbar should be visible",
                getVideoMessagePreviewPage().isToolbarVisible());
    }

    /**
     * Verify the video message compressing overlay is visible
     *
     * @throws Exception
     * @step. ^I see video message compressing overlay$
     */
    @Then("^I see video message compressing overlay$")
    public void ISeeVideoMessageCompressingOverlay() throws Exception {
        Assert.assertTrue("The video message compressing overlay should be visible",
                getVideoMessagePreviewPage().isCompressingOverlayVisible());
    }
}
