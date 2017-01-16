package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.VideoCallPage;
import cucumber.api.java.en.Then;

public class VideoCallPageSteps {
    private final WebAppTestContext webContext;

    public VideoCallPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @Then("^I click on screen share button$")
    public void IClickScreenShareButton() throws Exception {
        VideoCallPage videoCallPage = webContext.getPagesCollection().getPage(VideoCallPage.class);
        videoCallPage.clickScreenShareButton();
    }
}
