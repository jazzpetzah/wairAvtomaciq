package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import cucumber.api.java.en.Then;
import org.apache.log4j.Logger;

public class VideoCallPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(VideoCallPageSteps.class.getName());

    private final TestContext webContext;

    public VideoCallPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }
    
    @Then("^I click on screen share button$")
    public void IClickScreenShareButton() throws Exception {
        VideoCallPage videoCallPage = webContext.getPagesCollection().getPage(VideoCallPage.class);
        videoCallPage.clickScreenShareButton();
    }
}
