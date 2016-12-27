package com.wearezeta.auto.android_tablet.steps.details_overlay.single;


import com.wearezeta.auto.android_tablet.pages.details_overlay.single.TabletSingleConnectedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.steps.AndroidTabletPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleConnectedUserDetailsOverlayPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletSingleConnectedUserDetailsOverlayPage getSingleConnectedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(TabletSingleConnectedUserDetailsOverlayPage.class);
    }

    /**
     * Tap on all buttons
     *
     * @param buttonName which could be create group/X/open menu
     * @throws Exception
     * @step. ^I tap (create group|X|open menu) button on Single connected user details popover$
     */
    @When("^I tap (create group|X|open menu) button on Single connected user details popover$")
    public void ITapAddContactButton(String buttonName) throws Exception {
        getSingleConnectedUserDetailsOverlayPage().tapButton(buttonName);
    }

    /**
     * Selects a tab in the single participant view
     *
     * @param tabName Name of the tab to select
     * @throws Exception
     * @step. ^I switch tab to "(.*)" in Single connected user details popover$
     */
    @When("^I switch tab to \"(.*)\" in Single connected user details popover$")
    public void ISelectSingleParticipantTab(String tabName) throws Exception {
        getSingleConnectedUserDetailsOverlayPage().switchToTab(tabName);
    }

    /**
     * Tap in the center of popover
     *
     * @throws Exception
     * @step. ^I tap (center|outside) of Single connected user details popover$
     */
    @When("^I tap (center|outside) of Single connected user details popover$")
    public void ITapInTheCenterOfPopover(String type) throws Exception {
        switch (type.toLowerCase()) {
            case "center":
                getSingleConnectedUserDetailsOverlayPage().tapInTheCenterOfPopover();
                break;
            case "outside":
                getSingleConnectedUserDetailsOverlayPage().tapOutsideOfPopover();
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify type '%s'", type));
        }
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee equals null means the item should be visible
     * @param type         which could be user name, unique user name
     * @param text         the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single connected user details popover$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single connected user details popover$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSingleConnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSingleConnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, text));
        }
    }

    /**
     * Verify the shield icon visible
     *
     * @param shouldNotSee equals null means the shield icon should be visible
     * @throws Exception
     * @step. ^I( do not)? see shield icon on Single connected user details popover$
     */
    @Then("^I( do not)? see shield icon on Single connected user details popover$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Shield icon is invisible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilShieldIconVisible());
        } else {
            Assert.assertTrue("Shield icon is still visible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilShieldIconInvisible());
        }
    }

    /**
     * Verify whether Single connected user details popover is visible or not
     *
     * @param shouldNotBeVisible equals to null is "no not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see Single connected user details popover$
     */
    @Then("^I( do not)? see Single connected user details popover$")
    public void ISeePopover(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Single connected user popover is not visible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilPopoverVisible());
        } else {
            Assert.assertTrue("Single connected user popover is still visible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilPopoverInvisible());
        }
    }
}
