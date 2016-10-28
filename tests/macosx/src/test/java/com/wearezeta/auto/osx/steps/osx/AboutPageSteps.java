package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.osx.pages.osx.AboutPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

    private final WrapperTestContext context;

    public AboutPageSteps() {
        this.context = new WrapperTestContext();
    }

    public AboutPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I close the about window$")
    public void ICloseApp() throws Exception {
        context.getOSXPagesCollection().getPage(AboutPage.class).closeWindow();
    }

    @When("^I minimize the about window$")
    public void IMinimizeApp() throws Exception {
        context.getOSXPagesCollection().getPage(AboutPage.class).minimizeWindow();
    }

    @Then("^I verify about window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("About window not visible within timeout",
                    context.getOSXPagesCollection().getPage(AboutPage.class).isVisible());
        } else {
            boolean notVisible = context.getOSXPagesCollection().getPage(AboutPage.class)
                    .isNotVisible();
            System.out.println("notVisible: " + notVisible);
            assertTrue("About window is visible", notVisible);
        }
    }
}
