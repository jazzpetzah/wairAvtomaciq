package com.wearezeta.auto.web.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.DeviceLimitPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceLimitPageSteps {

    private final TestContext context;
    
    public DeviceLimitPageSteps() {
        this.context = new TestContext();
    }

    public DeviceLimitPageSteps(TestContext context) {
        this.context = context;
    }

    @When("I click on Sign Out on the device limit page$")
    public void IClickSignOutButton() throws Exception {
        context.getPagesCollection().getPage(DeviceLimitPage.class)
                .clickSignOutButton();
    }

    @When("I click button to manage devices$")
    public void IClickManageDevicesButton() throws Exception {
        context.getPagesCollection().getPage(DeviceLimitPage.class)
                .clickManageDevicesButton();
    }

    @Then("I am informed about the device limit$")
    public void IAmInformedAboutDeviceLimit() throws Exception {
        assertThat("Device limit info not shown", context.getPagesCollection()
                .getPage(DeviceLimitPage.class).isDeviceLimitInfoShown());
    }

    /**
     * Verify if you see correct label of device in the device list
     *
     * @param label model and label of the device
     * @throws Exception
     */
    @Then("I( do not)? see a device named (.*) with label (.*) under managed devices$")
    public void ISeeACertainDevice(String doNot, String name, String label) throws Exception {
        if(doNot == null) {
            assertThat(context.getPagesCollection().getPage(DeviceLimitPage.class)
                    .getDevicesNames(), hasItem(name));
        } else {
            assertThat(context.getPagesCollection().getPage(DeviceLimitPage.class)
                    .getDevicesNames(), not(hasItem(name)));
        }
    }

    @Then("I see (\\d+) devices under managed devices$")
    public void ISeeXDevices(int size) throws Exception {
        assertThat(context.getPagesCollection().getPage(DeviceLimitPage.class)
                .getDevicesNames(), hasSize(size));
    }

    @Then("I click remove device button for device named (.*) on the device limit page")
    public void IClickRemoveDevice(String deviceName) throws Exception {
        context.getPagesCollection().getPage(DeviceLimitPage.class).clickRemoveDeviceButton(deviceName);
    }

    @Then("I click remove button for device named (.*) on the device limit page")
    public void IClickRemove(String deviceName) throws Exception {
        context.getPagesCollection().getPage(DeviceLimitPage.class).clickRemoveButton(deviceName);
    }

    @Then("I click cancel button for device named (.*) on the device limit page")
    public void IClickCancel(String deviceName) throws Exception {
        context.getPagesCollection().getPage(DeviceLimitPage.class).clickCancelButton(deviceName);
    }

    @Then("I enter password \"([^\"]*)\" to remove device named (.*) on the device limit page")
    public void IEnterPassword(String password, String deviceName) throws Exception {
        try {
            password = context.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getPagesCollection().getPage(DeviceLimitPage.class).enterPassword(deviceName, password);
    }
}
