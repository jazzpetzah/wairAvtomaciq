package com.wearezeta.auto.common.test_context;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.wire_actors.SEBridge;

public class TestContext {
    private final ClientUsersManager userManager;
    private final SEBridge deviceManager;
    private final CommonSteps commonSteps;
    private final CommonCallingSteps2 callingManager;
    protected final AbstractPagesCollection<? extends BasePage> pagesCollection;

    public TestContext(AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        this.userManager = new ClientUsersManager();
        this.deviceManager = new SEBridge();
        this.callingManager = new CommonCallingSteps2(() -> this);
        this.commonSteps = new CommonSteps(() -> this);
        this.pagesCollection = pagesCollection;
    }

    public TestContext(ClientUsersManager userManager, SEBridge deviceManager,
                       CommonCallingSteps2 callingManager, CommonSteps commonSteps,
                       AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        this.userManager = userManager;
        this.deviceManager = deviceManager;
        this.callingManager = callingManager;
        this.commonSteps = commonSteps;
        this.pagesCollection = pagesCollection;
    }

    public ClientUsersManager getUserManager() {
        return userManager;
    }

    public SEBridge getDeviceManager() {
        return deviceManager;
    }

    public void reset() {
        try {
            this.getUserManager().resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getDeviceManager().reset();
        try {
            this.getCallingManager().cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getPagesCollection().clearAllPages();
    }

    public CommonCallingSteps2 getCallingManager() {
        return callingManager;
    }

    public CommonSteps getCommonSteps() {
        return commonSteps;
    }

    public AbstractPagesCollection<? extends BasePage> getPagesCollection() {
        return pagesCollection;
    }
}
