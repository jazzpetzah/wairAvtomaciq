package com.wearezeta.auto.osx.steps.webapp;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;

public class RegistrationPageSteps {
    private static final Logger LOG = ZetaLogger.getLog(RegistrationPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public RegistrationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

}
