package com.wearezeta.auto.android_tablet.steps.cursor;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.cursor.TabletEphemeralOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class EphemeralOverlayPageSteps {
    private TabletEphemeralOverlayPage getExtendedCursorEphemeralOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext()
                .getPagesCollection().getPage(TabletEphemeralOverlayPage.class);
    }

    /**
     * Set Ephemeral timeout
     *
     * @param timeoutStr which could be Off, 5 seconds, 15 seconds and 1 minute
     * @throws Exception
     * @step. ^I set timeout to (Off|5 seconds|15 seconds|30 seconds|1 minute|5 minutes|1 day) on Extended cursor ephemeral overlay$
     */
    @When("^I set timeout to (Off|5 seconds|15 seconds|30 seconds|1 minute|5 minutes|1 day) on Extended cursor ephemeral overlay$")
    public void ISetTimeout(String timeoutStr) throws Exception {
        getExtendedCursorEphemeralOverlayPage().setTimeout(timeoutStr);
    }

    /**
     * Verify I see the ephemeral timer with expected value
     *
     * @param expectedTimerValue which could be 5 seconds/15 seconds
     * @throws Exception
     * @step. ^I see current ephemeral timeout is "(.*)"
     */
    @Then("^I see current ephemeral timeout is \"(.*)\"")
    public void ISeeCurrentTimer(String expectedTimerValue) throws Exception {
        Assert.assertTrue(String.format("The ephemeral timeout with text '%s' should be visible", expectedTimerValue),
                getExtendedCursorEphemeralOverlayPage().waitUntilTimerVisible(expectedTimerValue));
    }
}
