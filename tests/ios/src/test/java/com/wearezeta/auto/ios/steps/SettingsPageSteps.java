package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class SettingsPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SettingsPage getSettingsPage() throws Exception {
        return pagesCollection.getPage(SettingsPage.class);
    }

    /**
     * Verify whether Settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see settings page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getSettingsPage().waitUntilVisible());
    }

    /**
     * Select the corresponding Settings menu item
     *
     * @param itemName
     * @throws Exception
     * @step. ^I select settings item (.*)
     */
    @When("^I select settings item (.*)")
    public void ISelectItem(String itemName) throws Exception {
        getSettingsPage().selectItem(itemName);
    }

    /**
     * Apply current settings and close the view
     *
     * @throws Exception
     * @step. ^I accept current settings$
     */
    @And("^I accept current settings$")
    public void IAcceptCurrentSettings() throws Exception {
        getSettingsPage().apply();
    }

    /**
     * Tap back button on settings page
     *
     * @throws Exception
     * @step. ^I switch to the previous settings tab$
     */
    @And("^I switch to the previous settings tab$")
    public void ISwitchToThePreviousSettingsTab() throws Exception {
        getSettingsPage().goBack();
    }

    /**
     * Verify that alert settings are set to default values
     *
     * @step. ^I verify sound alerts settings are set to default values$
     *
     * @throws Exception
     */
    @When("^I verify sound alerts settings are set to default values$")
    public void IVerifyAllIsDefaultValue() throws Exception {
        Assert.assertTrue("Sound alerts settings are NOT set to their default values", getSettingsPage()
            .isSoundAlertsSetToDefault());
    }

    /**
     * Verify whether the corresponding settings menu item is visible
     *
     * @step. ^I see settings item (.*)$
     *
     * @param itemName the expected item name
     * @throws Exception
     */
    @Then("^I see settings item (.*)$")
    public void ISeeSettingsItem(String itemName) throws Exception {
        Assert.assertTrue(String.format("Settings menu item '%s' is not visible", itemName),
            getSettingsPage().isItemVisible(itemName));
    }

    /**
     * Verify Device label (Verified|Not Verified)
     * 
     * @step. ^I see the label (Verified|Not Verified) is shown for the device (.*)$
     * 
     * @param deviceName name of device
     * @param label label of device
     * @throws Exception
     */
    @Then("^I see the label (Verified|Not Verified) is shown for the device (.*)$")
    public void ISeeForDeviceALabelB(String deviceName, String label) throws Exception {
        Assert.assertTrue(String.format("Label '%s' is not visible for device '%s'", label, deviceName), getSettingsPage()
            .verificationLabelVisibility(deviceName, label));

    }

}
