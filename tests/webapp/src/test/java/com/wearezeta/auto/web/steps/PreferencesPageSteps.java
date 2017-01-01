package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.PreferencesPage;
import cucumber.api.java.en.When;

public class PreferencesPageSteps {

    private final WebAppTestContext context;

    public PreferencesPageSteps(WebAppTestContext context) {
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

}
