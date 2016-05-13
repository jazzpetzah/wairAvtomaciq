package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.VideoMessagePreviewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class VideoMessagePreviewPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private VideoMessagePreviewPage getVideoMessagePreviewPage() throws Exception {
        return pagesCollection.getPage(VideoMessagePreviewPage.class);
    }

    /**
     * Send or cancel recorded video from Video Preview
     *
     * @param doNotSend equals null means tap on send button, otherwise tap on cancel button
     * @throws Exception
     * @step. ^I( do not)? send recored video from video message preview$
     */
    @When("^I( do not)? send recored video from video message preview$")
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
}
