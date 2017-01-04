package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletCallIncomingPage;

import cucumber.api.java.en.When;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.ScreenOrientation;

import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;

public class CallIncomingPageSteps {
    private TabletCallIncomingPage getPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletCallIncomingPage.class);
    }

    /**
     * Verifies presence of incoming call
     *
     * @param not         equals to null means should see incoming call
     * @param isVideoCall equals to null means it is the video incoming call view
     * @throws Exception
     * @step. ^I (do not )?see incoming (video )?call$
     */
    @When("^I (do not )?see incoming (video )?call$")
    public void ISeeIncomingCall(String not, String isVideoCall) throws Exception {
        String subtitle = isVideoCall == null ? "calling" : "video calling";
        if (not == null) {
            assertTrue("Incoming call not visible", getPage().waitUntilVisible(subtitle));
        } else {
            assertTrue("Incoming call should not be visible", getPage().waitUntilNotVisible(subtitle));
        }
    }

    /**
     * Ignores or accept an incoming call
     *
     * @param action either 'accept' or 'ignore'
     * @throws Exception
     * @step. ^I swipe to (ignore|accept) the call$
     */
    @When("^I swipe to (ignore|accept) the call$")
    public void ISwipeTo(String action) throws Exception {
        final ScreenOrientation currentOrientation = AndroidTabletTestContextHolder.getInstance().getTestContext()
                .getScreenOrientationHelper().getOriginalOrientation()
                .orElseThrow(() -> new IllegalStateException("Could not get device orientation"));
        switch (action.toLowerCase()) {
            case "ignore":
                if (currentOrientation == LANDSCAPE) {
                    getPage().ignoreCallLandscape();
                } else if (currentOrientation == ScreenOrientation.PORTRAIT) {
                    getPage().ignoreCallPortrait();
                }
                break;
            case "accept":
                if (currentOrientation == LANDSCAPE) {
                    getPage().acceptCallLandscape();
                } else if (currentOrientation == ScreenOrientation.PORTRAIT) {
                    getPage().acceptCallPortrait();
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action '%s'", action));
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
    public void ISeeIncomingCallingMesage(String expectedCallerName) throws Exception {
        expectedCallerName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(expectedCallerName).getName();
        Assert.assertTrue(String.format(
                "The current caller name differs from the expected value '%s'",
                expectedCallerName), getPage()
                .waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
    }
}
