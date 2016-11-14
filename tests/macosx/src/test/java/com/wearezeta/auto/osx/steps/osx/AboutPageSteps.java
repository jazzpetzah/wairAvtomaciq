package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.AboutPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AboutPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public AboutPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I close the about window$")
    public void ICloseApp() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AboutPage.class).closeWindow();
    }

    @When("^I minimize the about window$")
    public void IMinimizeApp() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AboutPage.class).minimizeWindow();
    }

    @Then("^I verify about window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("About window not visible within timeout",
                    wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AboutPage.class).isVisible());
        } else {
            boolean notVisible = wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AboutPage.class)
                    .isNotVisible();
            System.out.println("notVisible: " + notVisible);
            assertTrue("About window is visible", notVisible);
        }
    }
}
