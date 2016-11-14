package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import org.apache.log4j.Logger;

public class VideoCallPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(VideoCallPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public VideoCallPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

}
