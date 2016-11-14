package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.StartUIPage;
import com.wearezeta.auto.web.common.TestContext;
import cucumber.api.java.en.When;

public class StartUIPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public StartUIPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I click button to import contacts from address book via search UI")
    public void IClickAddressbookImportButton() throws Exception {
        webContext.getPagesCollection().getPage(StartUIPage.class).clickImportAddressbookButton();
    }
}
