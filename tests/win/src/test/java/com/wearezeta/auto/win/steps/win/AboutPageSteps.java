package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.win.pages.win.AboutPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

    private final WebAppTestContext webContext;

    public AboutPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I close the about window$")
    public void ICloseApp() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).closeWindow();
    }

    @When("^I minimize the about window$")
    public void IMinimizeApp() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).minimizeWindow();
    }

    @Then("^I verify about window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("About window not visible within timeout", webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).isVisible());
        } else {
            boolean notVisible = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(AboutPage.class).isNotVisible();
            assertTrue("About window is visible", notVisible);
        }

    }

}
