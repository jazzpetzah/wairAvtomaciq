package com.wearezeta.auto.android.steps.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.group.GroupUnconnectedUsersDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupUnconnectedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private GroupUnconnectedUsersDetailsOverlayPage getGroupUnconnectedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(GroupUnconnectedUsersDetailsOverlayPage.class);
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
     * Verify user data (User name, Unique user name, User info)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name, user info
     * @param userNameAlias the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) of user (.*) on Group unconnected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) of user (.*) on Group unconnected user details page$")
    public void ISeeUserNameAndEmail(String shouldNotSee, String type, String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getGroupUnconnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, user));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getGroupUnconnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, user));
        }
    }
}
