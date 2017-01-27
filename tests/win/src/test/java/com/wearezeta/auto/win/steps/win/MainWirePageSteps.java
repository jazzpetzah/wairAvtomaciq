package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.When;

import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class MainWirePageSteps {

    private final WebAppTestContext webContext;

    public MainWirePageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I close the app$")
    public void ICloseApp() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).closeWindow();
    }

    @When("^I type shortcut combination to quit the app$")
    public void ITypeShortcutCombinationtoQuit() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).pressShortCutForQuit();
    }

    @When("^I type shortcut combination to open preferences$")
    public void ITypeShortcutCombinationForPreferences() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).pressShortCutForPreferences();
    }

    @When("^I minimize the app$")
    public void IMinimizeApp() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).minimizeWindow();
    }

    @When("^I maximize the app$")
    public void IMaximizeApp() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    @When("^I verify app is in fullscreen$")
    public void IVerifyAppFullscreen() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    @When("^I verify app is in minimum size$")
    public void IVerifyAppMini() throws Exception {
        Assert.assertTrue(webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).isMini());
    }

    @When("^I resize the app to the max by hand$")
    public void IResizeToMaxByHand() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeToMaxByHand();
    }

    @When("^I resize the app to the min by hand$")
    public void IResizeToMinByHand() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeToMinByHand();
    }

    @When("^I ensure initial positioning$")
    public void IEnsureInitialPositioning() throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).ensurePosition();
    }

    @When("^I change position of the app to X (\\d+) and Y (\\d+)$")
    public void IPositioningTo(int x, int y) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).positionByHand(x, y);
    }

    @When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
    public void IVerifyPosition(int x, int y) throws Exception {
        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        assertThat("Expected X coordinate does not match the actual value", mainWirePage.getX(), equalTo(x));
        assertThat("Expected Y coordinate does not match the actual value", mainWirePage.getY(), equalTo(y));
    }

    @When("^I resize the app to width (\\d+) px and height (\\d+) px$")
    public void IResizeTo(int width, int height) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).resizeByHand(width, height);
    }

    @When("^I verify app width is (\\d+) px and height is (\\d+) px$")
    public void IVerifySizeOf(int width, int height) throws Exception {
        MainWirePage mainWirePage = webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class);
        Assert.assertTrue(mainWirePage.isApproximatelyHeight(height));
        Assert.assertTrue(mainWirePage.isApproximatelyWidth(width));
    }

}
