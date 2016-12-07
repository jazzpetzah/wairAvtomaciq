package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.single.SingleUnconnectedUsersDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleUnconnectedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SingleUnconnectedUsersDetailsOverlayPage getSingleUnconnectedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(SingleUnconnectedUsersDetailsOverlayPage.class);
    }

    /**
     * Tap on button
     *
     * @param buttonNam button name
     * @throws Exception
     * @step. ^I tap (connect) button on Single unconnected user details page$
     */
    @When("^I tap (connect) button on Single unconnected user details page$")
    public void ITapButton(String buttonNam) throws Exception {
        getSingleUnconnectedUserDetailsOverlayPage().tapButton(buttonNam);
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name
     * @param userNameAlias the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name) of user (.*) on Single unconnected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name) of user (.*) on Single unconnected user details page$")
    public void ISeeUserData(String shouldNotSee, String type, String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, user));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, user));
        }
    }

    /**
     * Wait until user info visible or invisible, user info which could be common friends message, or address book message
     *
     * @param shouldNotSee equals null means the user info should be visible
     * @param userInfo     the expected user info you want to see
     * @throws Exception
     * @step. ^I( do not)? see user info "(.*)" on Single unconnected user details page$
     */
    @Then("^I( do not)? see user info \"(.*)\" on Single unconnected user details page$")
    public void IseeUserInfo(String shouldNotSee, String userInfo) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The user info '%s' is invisible", userInfo),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserInfoVisible(userInfo));
        } else {
            Assert.assertTrue(String.format("The user info '%s' is still visible", userInfo),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserInfoInvisible(userInfo));
        }
    }
}
