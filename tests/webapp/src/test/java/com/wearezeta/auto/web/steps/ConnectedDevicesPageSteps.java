package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ConnectedDevicesPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ConnectedDevicesPageSteps {

    
    private final TestContext context;
    
    public ConnectedDevicesPageSteps() {
        this.context = new TestContext();
    }

    public ConnectedDevicesPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I( do not)? see connected devices dialog$")
    public void ISeeDialog(String doNot) throws Exception {
        if(doNot == null) {
            assertThat("Dialog not shown", context.getPagesCollection().getPage(ConnectedDevicesPage.class).isDialogShown());
        } else {
            assertThat("Dialog still shown", context.getPagesCollection().getPage(ConnectedDevicesPage.class).isDialogNotShown());
        }
    }

    @When("^I click OK on connected devices dialog$")
    public void IClickOK() throws Exception {
        context.getPagesCollection().getPage(ConnectedDevicesPage.class).clickOKButton();
    }

    @Then("^I see (.*) on connected devices dialog$")
    public void ISeeDevice(String deviceName) throws Exception {
        ConnectedDevicesPage page = context.getPagesCollection().getPage(ConnectedDevicesPage.class);
        assertThat(page.getDialogText(), containsString(deviceName.toUpperCase()));
    }
}
