package com.wearezeta.auto.win.steps.webapp;


import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;

import org.apache.log4j.Logger;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public ConversationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

}
