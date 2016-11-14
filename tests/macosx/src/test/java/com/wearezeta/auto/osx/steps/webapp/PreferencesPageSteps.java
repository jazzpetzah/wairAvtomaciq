package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PreferencesPage;
import cucumber.api.java.en.When;

public class PreferencesPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public PreferencesPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public PreferencesPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I close preferences$")
    public void IClosePreferences() throws Exception {
        webContext.getPagesCollection().getPage(PreferencesPage.class).closePreferences();
    }

    @When("^I open account in preferences$")
    public void IOpenAccount() throws Exception {
        webContext.getPagesCollection().getPage(PreferencesPage.class).openAccount();
    }
    
    @When("^I open devices in preferences$")
    public void IOpenDevices() throws Exception {
        webContext.getPagesCollection().getPage(PreferencesPage.class).openDevices();
    }
    
    @When("^I open options in preferences$")
    public void IOpenOptions() throws Exception {
        webContext.getPagesCollection().getPage(PreferencesPage.class).openOptions();
    }
    
    @When("^I open about in preferences$")
    public void IOpenAbout() throws Exception {
        webContext.getPagesCollection().getPage(PreferencesPage.class).openAbout();
    }

}
