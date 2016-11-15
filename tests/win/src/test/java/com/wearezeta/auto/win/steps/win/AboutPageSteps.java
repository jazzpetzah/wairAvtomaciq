package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.win.AboutPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AboutPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I close the about window$")
    public void ICloseApp() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).closeWindow();
    }

    @When("^I minimize the about window$")
    public void IMinimizeApp() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).minimizeWindow();
    }

    @Then("^I verify about window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("About window not visible within timeout", wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).isVisible());
        } else {
            boolean notVisible = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).isNotVisible();
            assertTrue("About window is visible", notVisible);
        }

    }

}
