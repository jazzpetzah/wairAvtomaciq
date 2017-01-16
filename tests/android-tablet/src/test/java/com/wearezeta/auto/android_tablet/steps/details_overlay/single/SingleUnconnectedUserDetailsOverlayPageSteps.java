package com.wearezeta.auto.android_tablet.steps.details_overlay.single;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.single.TabletSingleUnconnectedUsersDetailsOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleUnconnectedUserDetailsOverlayPageSteps {
    private TabletSingleUnconnectedUsersDetailsOverlayPage getSingleUnconnectedUserDetailsOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletSingleUnconnectedUsersDetailsOverlayPage.class);
    }

    /**
     * Tap on button
     *
     * @param buttonNam button name
     * @throws Exception
     * @step. ^I tap (connect) button on Single unconnected user details (?:page|popover)$
     */
    @When("^I tap (connect) button on Single unconnected user details (?:page|popover)$")
    public void ITapButton(String buttonNam) throws Exception {
        getSingleUnconnectedUserDetailsOverlayPage().tapButton(buttonNam);
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name
     * @param text the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single unconnected user details (?:page|popover)$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single unconnected user details (?:page|popover)$")
    public void ISeeUserData(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, text));
        }
    }

    /**
     * Verify whether Single unconnected user details popover is visible or not
     *
     * @param shouldNotBeVisible equals to null is "no not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see Single unconnected user details popover$
     */
    @Then("^I( do not)? see Single unconnected user details popover$")
    public void ISeePopover(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Single unconnected user popover is not visible",
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilPopoverVisible());
        } else {
            Assert.assertTrue("Single unconnected user popover is still visible",
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilPopoverInvisible());
        }
    }
}
