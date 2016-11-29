package com.wearezeta.auto.android.steps.details_overlay.single;


import com.wearezeta.auto.android.pages.details_overlay.single.SingleConnectedUserDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleConnectedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SingleConnectedUserDetailsOverlayPage getSingleConnectedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(SingleConnectedUserDetailsOverlayPage.class);
    }

    /**
     * Tap on all buttons
     *
     * @param buttonName which could be create group/X/open menu
     * @throws Exception
     * @step. ^I tap (create group|X|open menu) button on Single connected user details page$
     */
    @When("^I tap (create group|X|open menu) button on Single connected user details page$")
    public void ITapAddContactButton(String buttonName) throws Exception {
        getSingleConnectedUserDetailsOverlayPage().tapButton(buttonName);
    }

    /**
     * Selects a tab in the single participant view
     *
     * @param tabName Name of the tab to select
     * @throws Exception
     * @step. ^I switch tab to "(.*)" in Single connected user details page$
     */
    @When("^I switch tab to \"(.*)\" in Single connected user details page$")
    public void ISelectSingleParticipantTab(String tabName) throws Exception {
        getSingleConnectedUserDetailsOverlayPage().switchToTab(tabName);
    }

    /**
     * Verify user data (User name, Unique user name, User info)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name, user info
     * @param userNameAlias the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) of user (.*) on Single connected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) of user (.*) on Single connected user details page$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSingleConnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, user));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSingleConnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, user));
        }
    }

    /**
     * Verify the shield icon visible
     *
     * @param shouldNotSee equals null means the shield icon should be visible
     * @throws Exception
     * @step. ^I( do not)? see shield icon on Single connected user details page$
     */
    @Then("^I( do not)? see shield icon on Single connected user details page$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Shield icon is invisible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilShieldIconVisible());
        } else {
            Assert.assertTrue("Shield icon is still visible",
                    getSingleConnectedUserDetailsOverlayPage().waitUntilShieldIconInvisible());
        }
    }
}
