package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.single.SingleConnectedUserDevicesPage;
import com.wearezeta.auto.ios.pages.details_overlay.single.SingleConnectedUserProfilePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.List;

public class SingleConnectedUserDevicesPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SingleConnectedUserDevicesPage getPage() throws Exception {
        return pagesCollection.getPage(SingleConnectedUserDevicesPage.class);
    }

}
