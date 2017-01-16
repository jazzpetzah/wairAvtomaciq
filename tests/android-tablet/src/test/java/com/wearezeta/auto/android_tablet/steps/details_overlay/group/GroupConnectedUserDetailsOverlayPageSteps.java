package com.wearezeta.auto.android_tablet.steps.details_overlay.group;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.group.TabletGroupConnectedUserDetailsOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupConnectedUserDetailsOverlayPageSteps {

    private TabletGroupConnectedUserDetailsOverlayPage getGroupConnectedUserDetailsOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletGroupConnectedUserDetailsOverlayPage.class);
    }

    /**
     * Tap all buttons
     *
     * @param buttonName which could be +connect, -remove, connect
     * @throws Exception
     * @step ^I tap on (open conversation|remove) button on Group connected user details page$
     */
    @When("^I tap on (open conversation|remove) button on Group connected user details page$")
    public void ITapButton(String buttonName) throws Exception {
        getGroupConnectedUserDetailsOverlayPage().tapButton(buttonName);
    }

    /**
     * Selects a tab in the single participant view
     *
     * @param tabName Name of the tab to select
     * @throws Exception
     * @step. ^I switch tab to "(.*)" in Group connected user details page$
     */
    @When("^I switch tab to \"(.*)\" in Group connected user details page$")
    public void ISelectSingleParticipantTab(String tabName) throws Exception {
        getGroupConnectedUserDetailsOverlayPage().switchToTab(tabName);
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee equals null means the item should be visible
     * @param type         which could be user name, unique user name
     * @param text         the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Group connected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Group connected user details page$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, text));
        }
    }
}
