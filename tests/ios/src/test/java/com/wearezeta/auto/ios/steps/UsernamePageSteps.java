package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.UsernamePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jcodec.common.Assert;

public class UsernamePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UsernamePage getUsernamePageSteps () throws Exception {
        return pagesCollection.getPage(UsernamePage.class);
    }

    /**
     * Verify visibility of Username page
     *
     * @throws Exception
     * @step. ^I see Username page$
     */
    @Then("^I see Username page$")
    public void ISeeUsernamePage() throws Exception {
        Assert.assertTrue("Username page is not visible", getUsernamePageSteps().isUsernamePageVisible());
    }

    /**
     * Tap Save button on Username page
     *
     * @throws Exception
     * @step. ^I tap Save button on Username page$
     */
    @When("^I tap Save button on Username page$")
    public void ITapSaveUsernameButton() throws Exception {
        getUsernamePageSteps().tapSaveButton();
    }
}
