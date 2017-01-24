package com.wearezeta.auto.android_tablet.steps.details_overlay.single;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.single.TabletSingleBlockedUserDetailsOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleBlockedUserDetailsOverlayPageSteps {
    private TabletSingleBlockedUserDetailsOverlayPage getSingleBlockedUserDetailsOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletSingleBlockedUserDetailsOverlayPage.class);
    }

    /**
     * Tap on all buttons
     *
     * @param buttonName which could be unblock button
     * @throws Exception
     * @step. ^I tap (unblock) button on Single blocked user details (?:page|popover)$
     */
    @When("^I tap (unblock) button on Single blocked user details (?:page|popover)$")
    public void ITapAddContactButton(String buttonName) throws Exception {
        getSingleBlockedUserDetailsOverlayPage().tapButton(buttonName);
    }

    /**
     * Verify the button is visible
     *
     * @param shouldNotSee equals null means the button should be visible
     * @param name         button name
     * @throws Exception
     * @step. ^I( do not)? see (unblock) button on Single blocked user details page
     */
    @Then("^I( do not)? see (unblock) button on Single blocked user details page$")
    public void ISeeButton(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The button '%s' is invisible", name),
                    getSingleBlockedUserDetailsOverlayPage().waitUntilButtonVisible(name));
        } else {
            Assert.assertTrue(String.format("The button '%s' is still visible", name),
                    getSingleBlockedUserDetailsOverlayPage().waitUntilButtonInvisible(name));
        }
    }

    /**
     * Verify whether Blocked connection popover is visible or not
     *
     * @param shouldNotSee equals to null means the blocked popover should be visible
     * @throws Exception
     * @step. ^I( do not)? see Single blocked user details popover$
     */
    @Then("^I( do not)? see Single blocked user details popover$")
    public void ISeePopover(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Single blocked user details popover is invisible",
                    getSingleBlockedUserDetailsOverlayPage().waitUntilPopoverVisible());
        } else {
            Assert.assertTrue("Single Blocked user details popover is still visible",
                    getSingleBlockedUserDetailsOverlayPage().waitUntilPopoverInvisible());
        }
    }
}
