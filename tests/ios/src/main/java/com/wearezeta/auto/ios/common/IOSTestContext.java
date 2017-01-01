package com.wearezeta.auto.ios.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.test_context.TestContext;
import com.wearezeta.auto.ios.tools.FastLoginContainer;
import cucumber.api.Scenario;

public class IOSTestContext extends TestContext {
    private final Scenario scenario;
    private final FastLoginContainer fastLoginContainer;

    public IOSTestContext(Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(pagesCollection);
        this.scenario = scenario;
        this.fastLoginContainer = new FastLoginContainer();
    }

    public Scenario getScenario() {
        return scenario;
    }

    public IOSPagesCollection getPagesCollection() {
        return (IOSPagesCollection) super.getPagesCollection();
    }

    public FastLoginContainer getFastLoginContainer() {
        return fastLoginContainer;
    }

    @Override
    public void reset() {
        super.reset();
        getFastLoginContainer().reset();
    }
}
