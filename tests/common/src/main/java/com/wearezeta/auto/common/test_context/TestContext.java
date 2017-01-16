package com.wearezeta.auto.common.test_context;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.wire_actors.RemoteDevicesManager;

public class TestContext {
    private final ClientUsersManager usersManager;
    private final RemoteDevicesManager devicesManager;
    private final CommonSteps commonSteps;
    private final CommonCallingSteps2 callingManager;
    private final AbstractPagesCollection<? extends BasePage> pagesCollection;

    public TestContext(AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        this.usersManager = new ClientUsersManager();
        this.devicesManager = new RemoteDevicesManager();
        this.callingManager = new CommonCallingSteps2(this.usersManager);
        this.commonSteps = new CommonSteps(this.usersManager, this.devicesManager);
        this.pagesCollection = pagesCollection;
    }

    public TestContext(ClientUsersManager userManager, RemoteDevicesManager devicesManager,
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

    public RemoteDevicesManager getDevicesManager() {
        return devicesManager;
    }

    public void reset() {
        try {
            this.getUsersManager().resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.getDevicesManager().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
