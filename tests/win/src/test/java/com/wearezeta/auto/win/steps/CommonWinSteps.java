package com.wearezeta.auto.win.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import org.apache.log4j.Logger;
import com.wearezeta.auto.common.log.ZetaLogger;
import static com.wearezeta.auto.win.common.WinCommonUtils.getSizeOfAppInMB;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.common.WinCommonUtils;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class CommonWinSteps {

    private static final Logger LOG = ZetaLogger.getLog(CommonWinSteps.class.getName());
    private final WebAppTestContext webContext;

    public CommonWinSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I click menu bar item \"(.*)\" and menu item \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                MainWirePage.class).clickMenuBarItem(menuBarItemName, menuItemName);
    }

    @When("^I click menu bar item \"(.*)\" and menu items \"(.*)\" and \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName, String menuItemName2) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                MainWirePage.class).clickMenuBarItem(menuBarItemName, menuItemName, menuItemName2);
    }

    @When("^Show elements for windows xpath \"(.*)\"$")
    public void showLocator(String locator) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                MainWirePage.class).showElementsForXpathLocator(locator);
    }

    @When("^I click menu bar item with name \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).clickMenuBarItem(
                menuBarItemName);
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
//        winPagesCollection.getPage(MainWirePage.class).closeWindow();
//        clearDrivers();
//        startApp();
    }

    @Then("^I verify app has quit$")
    public void IVerifyAppHasQuit() throws Exception {
        int exitCode = WinCommonUtils.killOnlyWire();
        // 128 is the error code of the taskkill command for 'no such process'
        assertEquals(String.format("The kill command found a process to kill and exited with code '%d'", exitCode), 128,
                exitCode);
    }

    @Then("^I verify the app is not bigger than (\\d+) MB$")
    public void IVerifyAppIsNotTooBig(long expectedSize) throws Exception {
        assertThat(getSizeOfAppInMB(), lessThan(expectedSize));
    }
}
