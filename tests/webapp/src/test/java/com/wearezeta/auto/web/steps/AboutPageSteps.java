package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.AboutPage;
import cucumber.api.java.en.When;

public class AboutPageSteps {

    private final TestContext context;

    public AboutPageSteps() {
        this.context = new TestContext();
    }

    public AboutPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I click support link on about page$")
    public void IClosePreferences() throws Exception {
        context.getPagesCollection().getPage(AboutPage.class).clickSupport();
    }

}
