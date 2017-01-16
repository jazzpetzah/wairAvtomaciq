package com.wearezeta.auto.android_tablet.steps.details_overlay.group;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.group.TabletGroupUnconnectedUsersDetailsOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupUnconnectedUserDetailsOverlayPageSteps {
    private TabletGroupUnconnectedUsersDetailsOverlayPage getGroupUnconnectedUserDetailsOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletGroupUnconnectedUsersDetailsOverlayPage.class);
    }

    /**
     * Tap all buttons
     *
     * @param buttonName which could be +connect, -remove, connect
     * @throws Exception
     * @step ^I tap on (\\+connect|\\-remove|connect) button on Group unconnected user details page$
     */
    @When("^I tap on (\\+connect|\\-remove|connect) button on Group unconnected user details page$")
    public void ITapButton(String buttonName) throws Exception {
        getGroupUnconnectedUserDetailsOverlayPage().tapButton(buttonName);
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee equals null means the item should be visible
     * @param type         which could be user name, unique user name
     * @param text         the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Group unconnected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Group unconnected user details page$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getGroupUnconnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getGroupUnconnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, text));
        }
    }
}
