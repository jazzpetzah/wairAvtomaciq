package com.wearezeta.auto.osx.steps.webapp;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;

public class LoginPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class.getName());

    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public LoginPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
}
