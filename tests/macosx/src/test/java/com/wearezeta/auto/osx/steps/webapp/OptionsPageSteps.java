package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.OptionsPage;
import com.wearezeta.auto.web.common.TestContext;
import cucumber.api.java.en.When;


public class OptionsPageSteps {
    
    private final TestContext webContext;
    private final TestContext wrapperContext;

    
    public OptionsPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    
    @When("^I click button to import contacts from address book via preferences$")
    public void IClickAddressbookImportButton() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickImportAddressbookButton();
    }

}
