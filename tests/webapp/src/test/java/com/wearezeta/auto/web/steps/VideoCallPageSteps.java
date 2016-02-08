package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.When;
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

        Assert.assertTrue("End Video Call button in not visible",

                webappPagesCollection.getPage(VideoCallPage.class).isEndVideoCallButtonVisible());

        webappPagesCollection.getPage(VideoCallPage.class).clickEndVideoCallButton();
    }

}
