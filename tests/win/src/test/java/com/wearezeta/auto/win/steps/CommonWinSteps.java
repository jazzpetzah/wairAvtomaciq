package com.wearezeta.auto.win.steps;

import org.apache.log4j.Logger;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
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
    private final TestContext webContext;
    private final TestContext wrapperContext;

    public CommonWinSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I click menu bar item \"(.*)\" and menu item \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName) throws Exception {
        MainWirePage mainPage = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        mainPage.clickMenuBarItem(menuBarItemName, menuItemName);
    }

    @When("^I click menu bar item \"(.*)\" and menu items \"(.*)\" and \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName, String menuItemName, String menuItemName2) throws Exception {
        MainWirePage mainPage = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        mainPage.clickMenuBarItem(menuBarItemName, menuItemName, menuItemName2);
    }

    @When("^Show elements for windows xpath \"(.*)\"$")
    public void showLocator(String locator) throws Exception {
        MainWirePage mainPage = wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        mainPage.showElementsForXpathLocator(locator);
    }

    @When("^I click menu bar item with name \"(.*)\"$")
    public void clickMenuBarItem(String menuBarItemName) throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).clickMenuBarItem(menuBarItemName);
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

    @When("^Contact (.*) sends? message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias, String msg, String deviceName, String convoType,
            String dstConvoName) throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0) ? CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            webContext.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        } else {
            webContext.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        }
    }

    @When("user (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias, String deviceName, String label) throws Exception {
        webContext.getCommonSteps().UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
    }

}
