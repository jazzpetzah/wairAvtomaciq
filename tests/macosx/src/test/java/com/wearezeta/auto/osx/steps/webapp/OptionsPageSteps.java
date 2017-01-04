package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.osx.pages.webapp.OptionsPage;
import com.wearezeta.auto.web.common.WebAppTestContext;
import cucumber.api.java.en.When;


public class OptionsPageSteps {
    
    private final WebAppTestContext webContext;
    
    public OptionsPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @When("^I click button to import contacts from address book via preferences$")
    public void IClickAddressbookImportButton() throws Exception {
        webContext.getPagesCollection().getPage(OptionsPage.class).clickImportAddressbookButton();
    }

}
