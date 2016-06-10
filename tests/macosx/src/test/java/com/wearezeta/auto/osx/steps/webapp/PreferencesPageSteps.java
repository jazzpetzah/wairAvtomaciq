package com.wearezeta.auto.osx.steps.webapp;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PreferencesPageSteps {

    /**
     * Verifies whether preferences dialog is visible
     *
     * @step. ^I see [Pp]references dialog$
     *
     * @throws AssertionError if preferences dialog is not currently visible
     */
    @Then("^I see [Pp]references dialog$")
    public void ISeeSetingsDialog() throws Exception {
        Assert.assertTrue(WebappPagesCollection.getInstance()
                .getPage(SettingsPage.class).isVisible());
    }
    
    /**
     * Click on import contacts from Gmail via Setting
     *
     * @throws Exception
     */
    @When("^I click button to import contacts from Gmail$")
    public void IClickImportButton() throws Exception {
        WebappPagesCollection.getInstance().getPage(SettingsPage.class).clickImportButton();
    }

}
