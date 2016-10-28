package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.web.pages.PreferencesPage;
import cucumber.api.java.en.When;

public class PreferencesPageSteps {

    private final WrapperTestContext context;

    public PreferencesPageSteps() {
        this.context = new WrapperTestContext();
    }

    public PreferencesPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I close preferences$")
    public void IClosePreferences() throws Exception {
        context.getWebappPagesCollection().getPage(PreferencesPage.class).closePreferences();
    }

    @When("^I open account in preferences$")
    public void IOpenAccount() throws Exception {
        context.getWebappPagesCollection().getPage(PreferencesPage.class).openAccount();
    }
    
    @When("^I open devices in preferences$")
    public void IOpenDevices() throws Exception {
        context.getWebappPagesCollection().getPage(PreferencesPage.class).openDevices();
    }
    
    @When("^I open options in preferences$")
    public void IOpenOptions() throws Exception {
        context.getWebappPagesCollection().getPage(PreferencesPage.class).openOptions();
    }
    
    @When("^I open about in preferences$")
    public void IOpenAbout() throws Exception {
        context.getWebappPagesCollection().getPage(PreferencesPage.class).openAbout();
    }

}
