package com.wearezeta.auto.web.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.DeviceLimitPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceLimitPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    @When("I click on Sign Out on the device limit page$")
    public void IClickSignOutButton() throws Exception {
        webappPagesCollection.getPage(DeviceLimitPage.class)
                .clickSignOutButton();
    }

    @When("I click button to manage devices$")
    public void IClickManageDevicesButton() throws Exception {
        webappPagesCollection.getPage(DeviceLimitPage.class)
                .clickManageDevicesButton();
    }

    @Then("I am informed about the device limit$")
    public void IAmInformedAboutDeviceLimit() throws Exception {
        assertThat("Device limit info not shown", webappPagesCollection
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
            assertThat(webappPagesCollection.getPage(DeviceLimitPage.class)
                    .getDevicesNames(), hasItem(name));
        } else {
            assertThat(webappPagesCollection.getPage(DeviceLimitPage.class)
                    .getDevicesNames(), not(hasItem(name)));
        }
    }

    @Then("I see (\\d+) devices under managed devices$")
    public void ISeeXDevices(int size) throws Exception {
        assertThat(webappPagesCollection.getPage(DeviceLimitPage.class)
                .getDevicesNames(), hasSize(size));
    }

    @Then("I click remove device button for device named (.*) on the device limit page")
    public void IClickRemoveDevice(String deviceName) throws Exception {
        webappPagesCollection.getPage(DeviceLimitPage.class).clickRemoveDeviceButton(deviceName);
    }

    @Then("I click remove button for device named (.*) on the device limit page")
    public void IClickRemove(String deviceName) throws Exception {
        webappPagesCollection.getPage(DeviceLimitPage.class).clickRemoveButton(deviceName);
    }

    @Then("I click cancel button for device named (.*) on the device limit page")
    public void IClickCancel(String deviceName) throws Exception {
        webappPagesCollection.getPage(DeviceLimitPage.class).clickCancelButton(deviceName);
    }

    @Then("I enter password \"([^\"]*)\" to remove device named (.*) on the device limit page")
    public void IEnterPassword(String password, String deviceName) throws Exception {
        try {
            password = usrMgr.findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        webappPagesCollection.getPage(DeviceLimitPage.class).enterPassword(deviceName, password);
    }
}
