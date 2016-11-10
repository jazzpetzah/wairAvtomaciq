package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.win.pages.win.AboutPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AboutPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();
    @SuppressWarnings("unused")
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();
    
    private final WrapperTestContext context;

    public AboutPageSteps() {
        this.context = new WrapperTestContext();
    }

    public AboutPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    /**
     * Closes the about window
     *
     * @step. ^I close the about window$
     *
     * @throws Exception
     */
    @When("^I close the about window$")
    public void ICloseApp() throws Exception {
        winPagesCollection.getPage(AboutPage.class).closeWindow();
    }

    /**
     * Minimizes the about window
     *
     * @step. ^I minimize the about window$
     *
     * @throws Exception
     */
    @When("^I minimize the about window$")
    public void IMinimizeApp() throws Exception {
        winPagesCollection.getPage(AboutPage.class).minimizeWindow();
    }

    /**
     * Verifies whether the about window is visible or not
     *
     * @step. ^I verify about window is visible$
     * @param not is set to null if "not" part does not exist
     *
     * @throws Exception
     */
    @Then("^I verify about window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("About window not visible within timeout", winPagesCollection.getPage(AboutPage.class).isVisible());
        } else {
            boolean notVisible = winPagesCollection.getPage(AboutPage.class).isNotVisible();
            assertTrue("About window is visible", notVisible);
        }

    }

}
