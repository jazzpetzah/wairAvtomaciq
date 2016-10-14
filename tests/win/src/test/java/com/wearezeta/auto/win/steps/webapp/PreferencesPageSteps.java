package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.pages.webapp.PreferencesPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PreferencesPageSteps {

    private final TestContext context;

    public PreferencesPageSteps() {
        this.context = new TestContext();
    }

    public PreferencesPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I close preferences$")
    public void IClosePreferences() throws Exception {
        context.getPagesCollection().getPage(PreferencesPage.class).closePreferences();
    }

    @When("^I open account in preferences$")
    public void IOpenAccount() throws Exception {
        context.getPagesCollection().getPage(PreferencesPage.class).openAccount();
    }
    
    @When("^I open devices in preferences$")
    public void IOpenDevices() throws Exception {
        context.getPagesCollection().getPage(PreferencesPage.class).openDevices();
    }
    
    @When("^I open options in preferences$")
    public void IOpenOptions() throws Exception {
        context.getPagesCollection().getPage(PreferencesPage.class).openOptions();
    }
    
    @When("^I open about in preferences$")
    public void IOpenAbout() throws Exception {
        context.getPagesCollection().getPage(PreferencesPage.class).openAbout();
    }
    
    @Then("^I type shortcut combination to open preferences$")
    public void ITypeShortcutCombinationToOpenPreference() throws Exception {
        WebappPagesCollection.getInstance().getPage(PreferencesPage.class)
                .pressShortCutForPreferences();
    }

}
