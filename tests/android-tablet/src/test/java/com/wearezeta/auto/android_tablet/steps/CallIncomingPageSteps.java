package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.TabletCallIncomingPage;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.ScreenOrientation;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;

public class CallIncomingPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletCallIncomingPage getPage() throws Exception {
        return pagesCollection.getPage(TabletCallIncomingPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    
    private final ScreenOrientationHelper screenOrientationHelper = ScreenOrientationHelper.getInstance();
    
     /**
     * Verifies presence of incoming call
     *
     * @throws Exception
     * @step. ^I (do not )?see incoming call$
     */
    @When("^I (do not )?see incoming call$")
    public void ISeeIncomingCall(String not) throws Exception {
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible());
        }else{
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible());
        }
    }
    /**
     * Ignores an incoming call
     *
     * @throws Exception
     * @step. ^I swipe to ignore the call$
     */
    @When("^I swipe to ignore the call$")
    public void ISwipeToIgnoreCall() throws Exception {
        ScreenOrientation currentOrientation = screenOrientationHelper.getOrientation()
                .orElseThrow(()->new IllegalStateException("Could not get device orientation"));
        if (currentOrientation == LANDSCAPE) {
            getPage().ignoreCallLandscape();
        }else if (currentOrientation == ScreenOrientation.PORTRAIT) {
            getPage().ignoreCallPortrait();
        }
    }
    
    /**
     * Accepts an incoming call
     *
     * @throws Exception
     * @step. ^I swipe to accept the call$
     */
    @When("^I swipe to accept the call$")
    public void ISwipeToAcceptCall() throws Exception {
        ScreenOrientation currentOrientation = screenOrientationHelper.getOrientation()
                .orElseThrow(()->new IllegalStateException("Could not get device orientation"));
        if (currentOrientation == LANDSCAPE) {
            getPage().acceptCallLandscape();
        }else if (currentOrientation == ScreenOrientation.PORTRAIT) {
            getPage().acceptCallPortrait();
        }
    }
    
    /**
     * Verify that incoming calling UI is visible and that the correct caller
     * name is shown
     *
     * @param expectedCallerName User name who calls
     * @throws Exception
     * @step. ^I see incoming call from (.*)$
     */
    @When("^I see incoming call from (.*)$")
    public void ISeeIncomingCallingMesage(String expectedCallerName)
            throws Exception {
        expectedCallerName = usrMgr.findUserByNameOrNameAlias(
                expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'",
                expectedCallerName), getPage()
                .waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
    }
}
