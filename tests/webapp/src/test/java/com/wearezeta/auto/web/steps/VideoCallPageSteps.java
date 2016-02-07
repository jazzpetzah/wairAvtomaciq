package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class VideoCallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    private static final Logger log = ZetaLogger.getLog(VideoCallPageSteps.class.getSimpleName());


    /**
     * End the current video call by clicking the end video call button
     *
     * @step. ^I end the video call$
     *
     * @throws Exception
     */
    @When("^I end the video call$")
    public void IEndTheCall() throws Exception {

        Assert.assertTrue("End Video Call button in not visible",

                webappPagesCollection.getPage(VideoCallPage.class).isEndVideoCallButtonVisible());

        webappPagesCollection.getPage(VideoCallPage.class).clickEndVideoCallButton();
    }

}
