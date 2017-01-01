package com.wearezeta.auto.common.test_context;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.wire_actors.SEBridge;

public class TestContext {
    private final ClientUsersManager usersManager;
    private final SEBridge devicesManager;
    private final CommonSteps commonSteps;
    private final CommonCallingSteps2 callingManager;
    private final AbstractPagesCollection<? extends BasePage> pagesCollection;

    public TestContext(AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        this.usersManager = new ClientUsersManager();
        this.devicesManager = new SEBridge();
        this.callingManager = new CommonCallingSteps2(this.usersManager);
        this.commonSteps = new CommonSteps(this.usersManager, this.devicesManager);
        this.pagesCollection = pagesCollection;
    }

    public TestContext(ClientUsersManager userManager, SEBridge devicesManager,
                       CommonCallingSteps2 callingManager, CommonSteps commonSteps,
                       AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        this.usersManager = userManager;
        this.devicesManager = devicesManager;
        this.callingManager = callingManager;
        this.commonSteps = commonSteps;
        this.pagesCollection = pagesCollection;
    }

    public ClientUsersManager getUsersManager() {
        return usersManager;
    }

    public SEBridge getDevicesManager() {
        return devicesManager;
    }

    public void reset() {
        try {
            this.getUsersManager().resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getDevicesManager().reset();
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
