package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.TakeoverPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TakeoverPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private TakeoverPage getTakeoverPage() throws Exception {
        return pagesCollection.getPage(TakeoverPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Check if takeover screen appars for specified user and all message on it is correct
     *
     * @throws Exception
     * @step. ^I see takeover screen from users? \"(.*)\"$
     */
    @When("^I see takeover screen from users? \"(.*)\"$")
    public void ISeeTakeoverScreen(String nameAliases) throws Exception {
        Assert.assertTrue("Takeover screeen is not visible", getTakeoverPage().waitForTakeoverScreenVisible());
        String name;
        for (String nameAlias : CommonSteps.splitAliases(nameAliases)) {
            name = usrMgr.findUserByNameOrNameAlias(nameAlias).getName();
            Assert.assertTrue(String.format("Takeover header from user %s is not visible", name),
                getTakeoverPage().isTakeoverScreenHeaderCorrect(name));
        }
        Assert.assertTrue("Takeover screeen text is not visible or not as expected",
            getTakeoverPage().isTakeoverScreenTextCorrect());
    }

    /**
     * Tap on show device button in takeover screen
     *
     * @throws Exception
     * @step. ^I tap send anyway(?: button)?$
     */
    @Then("^I tap send anyway(?: button)?$")
    public void ITapTakeoverSendBnt() throws Exception {
        getTakeoverPage().tapSendAnywayBnt();
    }

    /**
     * Tap on show device button in takeover screen
     *
     * @throws Exception
     * @step. ^I tap show device(?: button)?$
     */
    @Then("^I tap show device(?: button)?$")
    public void ITapTakeoverShowBnt() throws Exception {
        getTakeoverPage().tapShowDeviceBnt();
    }

}
