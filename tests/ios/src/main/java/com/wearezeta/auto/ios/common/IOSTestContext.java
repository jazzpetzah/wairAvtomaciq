package com.wearezeta.auto.ios.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.test_context.TestContext;
import cucumber.api.Scenario;

public class IOSTestContext extends TestContext {
    private final Scenario scenario;

    public IOSTestContext(Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(pagesCollection);
        this.scenario = scenario;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public IOSPagesCollection getPagesCollection() {
        return (IOSPagesCollection) super.getPagesCollection();
    }
}
