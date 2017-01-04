package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.details_overlay.common.DeviceDetailsPage;
import org.junit.Assert;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceDetailsPageSteps {
    private DeviceDetailsPage getDeviceDetailsPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(DeviceDetailsPage.class);
    }

    @When("^I tap (Verify|Back|Remove Device) (?:button|switcher) on Device Details page$")
    public void ITapButton(String name) throws Exception {
        getDeviceDetailsPage().tapButton(name);
    }

    /**
     * Verify fingerprint is not empty
     *
     * @throws Exception
     * @step. ^I see fingerprint is not empty on Device Details page$
     */
    @Then("^I see fingerprint is not empty on Device Details page$")
    public void ISeeFingertprintIsNotEmpty() throws Exception {
        Assert.assertTrue("Fingerprint is emtpy", getDeviceDetailsPage().verifyFingerPrintNotEmpty());
    }
}
