package com.wearezeta.auto.web.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.DeviceDetailPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceDetailPageSteps {

    private final WebAppTestContext context;

    public DeviceDetailPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I click back button on device details in preferences$")
    public void IClickBackButton() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPage.class).clickBackButton();
    }

    @Then("I see a device named (.*) with label (.*) in the device details")
    public void ISeeACertainDeviceInDevicesSection(String name, String label)
            throws Exception {
        assertThat(context.getPagesCollection().getPage(DeviceDetailPage.class).getDeviceName(),
                equalTo(name + context.getTestname().hashCode()));
        assertThat(context.getPagesCollection().getPage(DeviceDetailPage.class).getDeviceName(),
                equalTo(name + context.getTestname().hashCode()));
    }

    @When("I click the remove device link")
    public void IClickTheRemoveDeviceButton() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPage.class)
                .clickRemoveDeviceLink();
    }

    @When("I type password \"([^\"]*)\" into the device remove form")
    public void ITypePassword(String password) throws Exception {
        try {
            password = context.getUserManager().findUserByPasswordAlias(password).getPassword();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        context.getPagesCollection().getPage(DeviceDetailPage.class).setPassword(password);
    }

    @When("I click the remove button")
    public void IClickTheRemoveDeviceButtonOnForm() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPage.class)
                .clickRemoveButton();
    }

    @When("I click the reset session button")
    public void IClickTheResetSessionButtonOnForm() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPage.class).clickResetSessionButton();
    }

    @When("^I verify device on device details$")
    public void IVerifyDevice() throws Exception {
        context.getPagesCollection().getPage(DeviceDetailPage.class).verifyDevice();
    }
}
