package com.wearezeta.auto.android.steps.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupConnectedUserDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupConnectedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private GroupConnectedUserDetailsOverlayPage getGroupConnectedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(GroupConnectedUserDetailsOverlayPage.class);
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
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name
     * @param userNameAlias the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name) of user (.*) on Group connected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name) of user (.*) on Group connected user details page$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, user));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, user));
        }
    }

    /**
     * Wait until user info visible or invisible, user info which could be common friends message, or address book message
     *
     * @param shouldNotSee equals null means the user info should be visible
     * @param userInfo     the expected user info you want to see
     * @throws Exception
     * @step. ^I( do not)? see user info "(.*)" on Group connected user details page$
     */
    @Then("^I( do not)? see user info \"(.*)\" on Group connected user details page$")
    public void IseeUserInfo(String shouldNotSee, String userInfo) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The user info '%s' is invisible", userInfo),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserInfoVisible(userInfo));
        } else {
            Assert.assertTrue(String.format("The user info '%s' is still visible", userInfo),
                    getGroupConnectedUserDetailsOverlayPage().waitUntilUserInfoInvisible(userInfo));
        }
    }
}
