package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.DeviceDetailsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceDetailsPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private DeviceDetailsPage getDeviceDetailsPage() throws Exception {
        return pagesCollection.getPage(DeviceDetailsPage.class);
    }

    /**
     * Tap the Verify switcher
     *
     * @step. ^I tap Verify switcher on device details page$
     *
     * @throws Exception
     */
    @When("^I tap Verify switcher on Device Details page$")
    public void ITapVerifySwitcher() throws Exception {
        getDeviceDetailsPage().tapVerifySwitcher();
    }

    /**
     * Navigate back to the previous page
     *
     * @step. ^I navigate back from device details page$
     *
     * @throws Exception
     */
    @And("^I navigate back from Device Details page$")
    public void INavigateBack() throws Exception {
        getDeviceDetailsPage().tapBackButton();
    }

    /**
     * Verify fingerprint is not empty
     * 
     * @step. ^I see fingerprint is not empty$
     * @throws Exception
     */
    @Then("^I see fingerprint is not empty$")
    public void ISeeFingertprintIsNotEmpty() throws Exception {
        Assert.assertTrue("Fingerprint is emtpy", getDeviceDetailsPage().verifyFingerPrintNotEmpty());
    }

    /**
     * Taps Remove Device on the device detail page
     * @step. ^I tap Remove Device on device detail page$
     * @throws Throwable
     */
    @When("^I tap Remove Device on device detail page$")
    public void ITapRemoveDeviceOnDeviceDetailPage() throws Exception {
        getDeviceDetailsPage().tapRemoveDevice();
    }
}
