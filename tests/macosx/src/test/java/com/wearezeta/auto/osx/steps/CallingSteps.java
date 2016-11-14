package com.wearezeta.auto.osx.steps;


import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import org.apache.log4j.Logger;


public class CallingSteps {

    private static final Logger LOG = ZetaLogger.getLog(com.wearezeta.auto.web.steps.CallingSteps.class
            .getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public CallingSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

}
