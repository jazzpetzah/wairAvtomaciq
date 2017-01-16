package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;
import com.wearezeta.auto.common.log.ZetaLogger;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.getSizeOfAppInMB;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.isHibernateBlocked;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.WebAppTestContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class CommonOSXSteps {

    public static final Logger LOG = ZetaLogger.getLog(CommonOSXSteps.class.getName());

    private final WebAppTestContext webContext;

    public CommonOSXSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I click menu bar item \"(.*)\" and menu item \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(
                MainWirePage.class).clickMenuBarItem(menuBarItemName, menuItemName);
    }

    @When("^I click menu bar item \"(.*)\" and menu items \"(.*)\" and \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName, String menuItemName2) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(
                MainWirePage.class).clickMenuBarItem(menuBarItemName, menuItemName, menuItemName2);
    }

    @When("^I click menu bar item with name \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(MainWirePage.class).
                clickMenuBarItem(menuBarItemName);
    }

    @When("^Mac would enter hibernate mode when closing the lid$")
    public void enterHibernate() throws Exception {
        assertThat("Hibernate is blocked", isHibernateBlocked(), is(false));
    }

    @When("^I kill the app$")
    public void KillApp() throws Exception {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
//        clearDrivers();
    }

    @When("^I restart the app$")
    public void restartApp() throws Exception {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
//        context.getOSXPagesCollection().getPage(MainWirePage.class).pressShortCutForQuit();
//        Thread.sleep(2000);
//        startApp();
    }

    @Then("^I verify app has quit$")
    public void IVerifyAppHasQuit() throws Exception {
        int exitCode = killAllApps();
        assertEquals(1, exitCode);
    }

    @Then("^I verify the app is not bigger than (\\d+) MB$")
    public void IVerifyAppIsNotTooBig(long expectedSize) throws Exception {
        assertThat(getSizeOfAppInMB(), lessThan(expectedSize));
    }
}
