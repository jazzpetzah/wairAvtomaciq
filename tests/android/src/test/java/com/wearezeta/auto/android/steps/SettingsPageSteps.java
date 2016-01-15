package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SettingsPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SettingsPage getSettingsPage() throws Exception {
        return pagesCollection.getPage(SettingsPage.class);
    }

    /**
     * Checks to see that the settings page is visible
     *
     * @throws Exception
     * @step. ^I see settings page$
     */
    @Then("^I see settings page$")
    public void ISeeSettingsPage() throws Exception {
        Assert.assertTrue("Settings page is not visible", getSettingsPage()
                .waitUntilVisible());
    }

    /**
     * Tap the corresponding menu item
     *
     * @param name the name of the corresponding menu item
     * @throws Exception
     * @step. ^I select \"(.*)\" settings menu item$
     */
    @When("^I select \"(.*)\" settings menu item$")
    public void ISelectSettingsMenuItem(String name) throws Exception {
        getSettingsPage().selectMenuItem(name);
    }

    /**
     * Verify whether the particular settings menu item is visible or not
     *
     * @param shouldNotSee equals to null if the corresponding menu item should be visible
     * @param name         the name of the corresponding menu item
     * @throws Exception
     * @step. ^I (do not )?see \"(.*)\" settings menu item$
     */
    @When("^I (do not )?see \"(.*)\" settings menu item$")
    public void ISeeSettingsMenuItem(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Settings menu item '%s' is not visible", name),
                    getSettingsPage().waitUntilMenuItemVisible(name));
        } else {
            Assert.assertTrue(String.format("Settings menu item '%s' is visible, but should be hidden", name),
                    getSettingsPage().waitUntilMenuItemInvisible(name));
        }
    }

    /**
     * Click the corresponding button on sign out alert to confirm it
     *
     * @throws Exception
     * @step. ^I confirm sign out$
     */
    @And("^I confirm sign out$")
    public void IConfirmSignOut() throws Exception {
        getSettingsPage().confirmLogout();
    }

    /**
     * Verify whether password confirmation dialog is visible
     *
     * @throws Exception
     * @step. ^I see device removal password confirmation dialog$"
     */
    @Then("^I see device removal password confirmation dialog$")
    public void ISeePasswordConfirmation() throws Exception {
        Assert.assertTrue("The password confirmation is not visible",
                getSettingsPage().waitUntilPasswordConfirmationIsVisible());
    }

    /**
     * Type the password into the confirmation dialog
     *
     * @param passwordAlias password string or an alias
     * @throws Exception
     * @step. ^I enter (.*) into the device removal password confirmation dialog$
     */
    @When("^I enter (.*) into the device removal password confirmation dialog$")
    public void IEnterPassword(String passwordAlias) throws Exception {
        final String password = usrMgr.replaceAliasesOccurences(passwordAlias,
                ClientUsersManager.FindBy.PASSWORD_ALIAS);
        getSettingsPage().enterConfirmationPassword(password);
    }

    /**
     * Tap OK button on the device removal password confirmation dialog
     *
     * @throws Exception
     * @step. ^I tap OK button on the device removal password confirmation dialog$
     */
    @And("^I tap OK button on the device removal password confirmation dialog$")
    public void ITapOKButtonOnPasswordConfirmationDialog() throws Exception {
        getSettingsPage().tapOKButtonOnPasswordConfirmationDialog();
    }
}
