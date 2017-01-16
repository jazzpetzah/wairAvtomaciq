package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.AboutPage;
import cucumber.api.java.en.When;

public class AboutPageSteps {

    private final WebAppTestContext context;

    public AboutPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I click support link on about page$")
    public void IClosePreferences() throws Exception {
        context.getPagesCollection().getPage(AboutPage.class).clickSupport();
    }

}
