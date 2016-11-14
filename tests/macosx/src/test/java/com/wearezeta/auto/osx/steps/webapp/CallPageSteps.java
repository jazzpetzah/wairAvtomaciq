package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.web.common.TestContext;

public class CallPageSteps {
    
    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public CallPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

}
