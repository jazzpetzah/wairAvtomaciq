package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.popovers.DeviceDetailPopoverPage;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeviceDetailPopoverPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    @When("^I verify id of device (.*) of user (.*) on device detail page of Single User Profile popover$")
    public void IVerifyDeviceId(String deviceName, String userAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userAlias);
        String id = SEBridge.getInstance().getDeviceId(user, deviceName);
        assertThat(webappPagesCollection.getPage(DeviceDetailPopoverPage.class).getDeviceId(), equalTo(id));
    }

    @When("^I verify fingerprint of device (.*) of user (.*) on device detail page of Single User Profile popover$")
    public void IVerifyFingerPrint(String deviceName, String userAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userAlias);
        String fingerprint = SEBridge.getInstance().getDeviceFingerprint(user, deviceName);
        assertThat(webappPagesCollection.getPage(DeviceDetailPopoverPage.class).getFingerPrint(), equalTo(fingerprint));
    }

    @When("^I verify device on Device Detail popover$")
    public void IVerifyDevice() throws Exception {
        webappPagesCollection.getPage(DeviceDetailPopoverPage.class).verifyDevice();
    }

    @When("^I click back button on the Device Detail popover$")
    public void IClickBackButton() throws Exception {
        webappPagesCollection.getPage(DeviceDetailPopoverPage.class).clickBackButton();
    }
}
