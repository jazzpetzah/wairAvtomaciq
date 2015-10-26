package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.When;

import org.junit.Assert;

public class MainWirePageSteps {

    private final static int OSX_TITLEBAR_HEIGHT = 24;

    private final WinPagesCollection winPagesCollection = WinPagesCollection
            .getInstance();

    /**
     * Closes the app
     *
     * @step. ^I close the app$
     *
     * @throws Exception
     */
    @When("^I close the app$")
    public void ICloseApp() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).closeWindow();
    }

    /**
     * Types shortcut combination to quit the app
     *
     * @step. ^I type shortcut combination to quit the app$
     * @throws Exception
     */
    @When("^I type shortcut combination to quit the app$")
    public void ITypeShortcutCombinationtoQuit() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).pressShortCutForQuit();
    }

    /**
     * Types shortcut combination for preferences
     *
     * @step. ^I type shortcut combination for preferences$
     * @throws Exception
     */
    @When("^I type shortcut combination for preferences$")
    public void ITypeShortcutCombinationForPreferences() throws Exception {
        winPagesCollection.getPage(MainWirePage.class)
                .pressShortCutForPreferences();
    }

    /**
     * Minimizes the app
     *
     * @step. ^I minimize the app$
     *
     * @throws Exception
     */
    @When("^I minimize the app$")
    public void IMinimizeApp() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).minimizeWindow();
    }

    /**
     * Maximize the app
     *
     * @step. ^I maximize the app$
     *
     * @throws Exception
     */
    @When("^I maximize the app$")
    public void IMaximizeApp() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Verifies wether the app is in fullscreen or not
     *
     * @step. ^I verify app is in fullscreen$
     *
     * @throws Exception
     */
    @When("^I verify app is in fullscreen$")
    public void IVerifyAppFullscreen() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Verifies whether the app is in minimum size or not
     *
     * @step. ^I verify app is in minimum size$
     *
     * @throws Exception
     */
    @When("^I verify app is in minimum size$")
    public void IVerifyAppMini() throws Exception {
        Assert.assertTrue(winPagesCollection.getPage(MainWirePage.class)
                .isMini());
    }

    /**
     * Resizes the app to the max by hand
     *
     * @step. ^I resize the app to the max by hand$
     *
     * @throws Exception
     */
    @When("^I resize the app to the max by hand$")
    public void IResizeToMaxByHand() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).resizeToMaxByHand();
    }

    /**
     * Resizes the app to the min by hand
     *
     * @step. ^I resize the app to the min by hand$
     *
     * @throws Exception
     */
    @When("^I resize the app to the min by hand$")
    public void IResizeToMinByHand() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).resizeToMinByHand();
    }

    /**
     * Ensures that the position of the app is in the upper left corner with
     * respect to the dock (assuming it's on the left side).
     *
     * @step. ^I ensure initial positioning$
     *
     * @throws Exception
     */
    @When("^I ensure initial positioning$")
    public void IEnsureInitialPositioning() throws Exception {
        winPagesCollection.getPage(MainWirePage.class).ensurePosition();
    }

    /**
     * Repositions the app to the given coordinates
     *
     * @step. ^I change position of the app to X (\\d+) and Y (\\d+)$
     *
     * @param x the x coordinate
     * @param y the y coordinate
     *
     *
     * @throws Exception
     */
    @When("^I change position of the app to X (\\d+) and Y (\\d+)$")
    public void IPositioningTo(int x, int y) throws Exception {
        winPagesCollection.getPage(MainWirePage.class).positionByHand(x, y);
    }

    /**
     * Verifies the position of the app
     *
     * @step. ^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$
     *
     * @param x the width to verify
     * @param y the height to verify
     *
     *
     * @throws Exception
     */
    @When("^I verify app X coordinate is (\\d+) and Y coordinate is (\\d+)$")
    public void IVerifyPosition(int x, int y) throws Exception {
        MainWirePage mainWirePage = winPagesCollection
                .getPage(MainWirePage.class);
        Assert.assertTrue("Expected X coordinate " + x
                + " does not match the actual value " + mainWirePage.getX(),
                mainWirePage.isX(x));
        Assert.assertTrue("Expected Y coordinate " + y
                + " does not match the actual value " + mainWirePage.getY(),
                mainWirePage.isY(y));
    }

    /**
     * Resizes the app to the given size
     *
     * @step. ^I resize the app to width (\\d+)px and height (\\d+)px$
     *
     * @param width the width to resize to
     * @param height the height to resize to
     *
     *
     * @throws Exception
     */
    @When("^I resize the app to width (\\d+) px and height (\\d+) px$")
    public void IResizeTo(int width, int height) throws Exception {
        winPagesCollection.getPage(MainWirePage.class).resizeByHand(width,
                height);
    }

    /**
     * Verifies size of the app
     *
     * @step. ^I verify app width is (\\d+)px and height is (\\d+)px$
     *
     * @param width the width to verify
     * @param height the height to verify
     *
     *
     * @throws Exception
     */
    @When("^I verify app width is (\\d+) px and height is (\\d+) px$")
    public void IVerifySizeOf(int width, int height) throws Exception {
        MainWirePage mainWirePage = winPagesCollection
                .getPage(MainWirePage.class);
        Assert.assertTrue(mainWirePage.isApproximatelyHeight(height));
        Assert.assertTrue(mainWirePage.isApproximatelyWidth(width));
    }

}
