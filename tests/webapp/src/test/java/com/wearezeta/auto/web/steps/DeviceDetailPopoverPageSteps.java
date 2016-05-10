package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.popovers.DeviceDetailPopoverPage;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeviceDetailPopoverPageSteps {

    private final TestContext context;
    
    
    public DeviceDetailPopoverPageSteps() {
        this.context = new TestContext();
    }

    public DeviceDetailPopoverPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I verify id of device (.*) of user (.*) on device detail page of Single User Profile popover$")
    public void IVerifyDeviceId(String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String id = context.getDeviceManager().getDeviceId(user, deviceName);
        assertThat(context.getPagesCollection().getPage(DeviceDetailPopoverPage.class).getDeviceId(), equalTo(id));
    }

    @When("^I verify fingerprint of device (.*) of user (.*) on device detail page of Single User Profile popover$")
    public void IVerifyFingerPrint(String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String fingerprint = context.getDeviceManager().getDeviceFingerprint(user, deviceName);
        assertThat(context.getPagesCollection().getPage(DeviceDetailPopoverPage.class).getFingerPrint(), equalTo(fingerprint));
    }

    @When("^I verify device on Device Detail popover$")
    public void IVerifyDevice() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPopoverPage.class).verifyDevice();
    }

    @When("^I click back button on the Device Detail popover$")
    public void IClickBackButton() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPopoverPage.class).clickBackButton();
    }

    @When("^I click reset session on the Device Detail popover$")
    public void IClickResetSession() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPopoverPage.class).clickResetSession();
    }
}
