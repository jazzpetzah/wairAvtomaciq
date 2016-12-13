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
     * @param text the name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single unconnected user details page$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Single unconnected user details page$")
    public void ISeeUserData(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getSingleUnconnectedUserDetailsOverlayPage().waitUntilUserDataInvisible(type, text));
        }
    }
}
