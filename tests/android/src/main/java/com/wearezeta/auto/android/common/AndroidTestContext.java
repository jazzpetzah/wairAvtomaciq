package com.wearezeta.auto.android.common;

import com.wearezeta.auto.android.pages.AndroidPagesCollection;
import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.test_context.TestContext;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.wire_actors.SEBridge;
import cucumber.api.Scenario;

public class AndroidTestContext extends TestContext {
    private Scenario scenario;
    private PerformanceCommon performanceCommon;

    public AndroidTestContext(Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(pagesCollection);
        this.scenario = scenario;
        this.performanceCommon = new PerformanceCommon(() -> this);
    }

    public AndroidTestContext(ClientUsersManager clientUsersManager, SEBridge seBridge,
                              CommonCallingSteps2 callingManager, CommonSteps commonSteps,
                              Scenario scenario, AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        super(clientUsersManager, seBridge, callingManager, commonSteps, pagesCollection);
        this.scenario = scenario;
        this.performanceCommon = new PerformanceCommon(() -> this);
    }

    public Scenario getScenario() {
        return scenario;
    }

    public AndroidPagesCollection getPagesCollection() {
        return (AndroidPagesCollection) pagesCollection;
    }

    public PerformanceCommon getPerformanceCommon() {
        return this.performanceCommon;
    }
}
