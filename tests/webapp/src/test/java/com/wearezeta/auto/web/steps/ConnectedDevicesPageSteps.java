package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.ConnectedDevicesPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ConnectedDevicesPageSteps {

    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    @When("^I( do not)? see connected devices dialog$")
    public void ISeeDialog(String doNot) throws Exception {
        if(doNot == null) {
            assertThat("Dialog not shown", webappPagesCollection.getPage(ConnectedDevicesPage.class).isDialogShown());
        } else {
            assertThat("Dialog still shown", webappPagesCollection.getPage(ConnectedDevicesPage.class).isDialogNotShown());
        }
    }

    @When("^I click OK on connected devices dialog$")
    public void IClickOK() throws Exception {
        webappPagesCollection.getPage(ConnectedDevicesPage.class).clickOKButton();
    }

    @Then("^I see (.*) on connected devices dialog$")
    public void ISeeDevice(String deviceName) throws Exception {
        assertThat(webappPagesCollection.getPage(ConnectedDevicesPage.class).getDialogText(), containsString(deviceName
                .toUpperCase()));
    }
}
