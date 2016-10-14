package com.wearezeta.auto.web.steps;

import java.util.List;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.DevicesPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

public class DevicesPageSteps {

    private final TestContext context;

    private String currentDeviceId = null;

    public DevicesPageSteps() {
        this.context = new TestContext();
    }

    public DevicesPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I remember the device id of the current device$")
    public void IRememberCurrentDeviceId() throws Exception {
        currentDeviceId = context.getPagesCollection().getPage(DevicesPage.class).getCurrentDeviceId();
    }

    @When("^I verify that the device id of the current device is( not)? the same$")
    public void IVerifyCurrentDeviceId(String not) throws Exception {
        if (currentDeviceId == null) {
            throw new RuntimeException(
                    "currentDeviceId was not remembered, please use the according step first");
        } else if (not == null) {
            assertThat(context.getPagesCollection().getPage(DevicesPage.class)
                    .getCurrentDeviceId(), equalTo(currentDeviceId));
        } else {
            assertThat(context.getPagesCollection().getPage(DevicesPage.class)
                    .getCurrentDeviceId(), not(equalTo(currentDeviceId)));
        }
    }

    @When("^I wait for devices$")
    public void IWaitForDevices() throws Exception {
        context.getPagesCollection().getPage(DevicesPage.class).waitForDevices();
    }

    @When("^I( do not)? see an active device named (.*)$")
    public void ISeeActiveDevice(String donot, String device)
            throws Exception {
        List<String> labels = context.getPagesCollection().getPage(DevicesPage.class).getActiveDevicesLabels();
        if (donot == null) {
            assertThat("Doesn't have device", labels, hasItem(device + context.getTestname().hashCode()));
        } else {
            assertThat("Does have device", labels, not(hasItem(device + context.getTestname().hashCode())));
        }
    }

    @Then("^I see (\\d+) active devices$")
    public void ISeeXDevices(int size) throws Exception {
        assertThat(context.getPagesCollection().getPage(DevicesPage.class).getActiveDevicesLabels(), hasSize(size));
    }

    @When("^I click on the device (.*)$")
    public void IClickOnDevice(String device) throws Exception {
        context.getPagesCollection().getPage(DevicesPage.class).clickDevice(device + context.getTestname().hashCode());
    }

    @When("^I click x to remove the device (.*)$")
    public void IClickXToRemoveDevice(String device) throws Exception {
        List<String> labels = context.getPagesCollection().getPage(DevicesPage.class).getActiveDevicesLabels();
        int index = labels.indexOf(device + context.getTestname().hashCode());
        context.getPagesCollection().getPage(DevicesPage.class).removeDevice(index);
    }

    @Then("^I( do not)? see device (.*) of user (.*) is verified in device section$")
    public void ISeeVerifiedDevice(String donot, String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String id = context.getDeviceManager().getDeviceId(user, deviceName + context.getTestname().hashCode());
        id = WebCommonUtils.removeDeviceIdPadding(id);
        context.getPagesCollection().getPage(DevicesPage.class).waitForDevices();
        List<String> devices = context.getPagesCollection().getPage(DevicesPage.class).getVerifiedDeviceIds();
        if (donot != null) {
            assertThat("Device id is in verified devices", !devices.contains(id.toUpperCase()));
        } else {
            assertThat("Device id is NOT in verified devices", devices, hasItem(id.toUpperCase()));
        }
    }
}
