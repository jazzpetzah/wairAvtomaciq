package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.VideoCallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class VideoCallPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.75;

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(VideoCallPageSteps.class.getSimpleName());

    private String randomMessage;


    /**
     * End the current video call by clicking the end video call button
     *
     * @step. ^I end the video call$
     *
     * @throws Exception
     */
    @When("^I end the video call$")
    public void IEndTheCall() throws Exception {
        webappPagesCollection.getPage(VideoCallPage.class).clickEndVideoCallButton();
    }


}
