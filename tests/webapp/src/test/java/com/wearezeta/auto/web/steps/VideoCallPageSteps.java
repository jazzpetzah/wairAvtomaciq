package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import org.junit.Assert;

public class VideoCallPageSteps {

    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    /**
     * End the current video call by clicking the End video call button on video call page
     *
     * @throws Exception
     * @step. ^I end the video call$
     */
    @When("^I end the video call$")
    public void IEndTheCall() throws Exception {

        Assert.assertTrue("End Video Call button is not visible",
                webappPagesCollection.getPage(VideoCallPage.class).isEndVideoCallButtonVisible());

        webappPagesCollection.getPage(VideoCallPage.class).clickEndVideoCallButton();

        Assert.assertTrue("End Video Call button is still visible",
                webappPagesCollection.getPage(VideoCallPage.class).isEndVideoCallButtonNotVisible());
    }

    /**
     * Verifies if the current video call page is visible
     *
     * @throws Exception
     * @step. ^I see video call page$
     */
    @And("^I see video call page$")
    public void ISeeVideoCallPage() throws Exception {

        Assert.assertTrue("Video call page is not visible",
                webappPagesCollection.getPage(VideoCallPage.class).isVisible());
    }
}
