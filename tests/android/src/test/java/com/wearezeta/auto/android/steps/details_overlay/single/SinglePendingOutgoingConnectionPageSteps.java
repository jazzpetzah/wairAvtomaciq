package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.single.SinglePendingOutgoingConnectionPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class SinglePendingOutgoingConnectionPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SinglePendingOutgoingConnectionPage getSinglePendingOutgoingConnectionPage() throws Exception {
        return pagesCollection.getPage(SinglePendingOutgoingConnectionPage.class);
    }

    /**
     * Verify I see avatar
     *
     * @throws Exception
     * @step. ^I see avatar on Single pending outgoing connection page$
     */
    @Then("^I see avatar on Single pending outgoing connection page$")
    public void ISeeAvatar() throws Exception {
        Assert.assertTrue("The pending avatar is invisible",
                getSinglePendingOutgoingConnectionPage().waitUntilAvatarVisible());
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee  equals null means the item should be visible
     * @param type          which could be user name, unique user name
     * @param userNameAlias the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name) of user (.*) on Single pending outgoing connection page$
     */
    @Then("^I( do not)? see (user name|unique user name) of user (.*) on Single pending outgoing connection page$")
    public void ISeeUserDat(String shouldNotSee, String type, String userNameAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserDataVisible(type, user));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserDataInvisible(type, user));
        }
    }

    /**
     * Wait until user info visible or invisible, user info which could be common friends message, or address book message
     *
     * @param shouldNotSee equals null means the user info should be visible
     * @param userInfo     the expected user info you want to see
     * @throws Exception
     * @step. ^I( do not)? see user info "(.*)" on Single pending outgoing connection page$
     */
    @Then("^I( do not)? see user info \"(.*)\" on Single pending outgoing connection page$")
    public void IseeUserInfo(String shouldNotSee, String userInfo) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The user info '%s' is invisible", userInfo),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserInfoVisible(userInfo));
        } else {
            Assert.assertTrue(String.format("The user info '%s' is still visible", userInfo),
                    getSinglePendingOutgoingConnectionPage().waitUntilUserInfoInvisible(userInfo));
        }
    }
}
