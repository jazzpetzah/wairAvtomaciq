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
     * Taps on the close takeover screen button
     *
     * @throws Exception
     * @step. ^I tap on (?:cancel|close) button [oi]n (?:[Tt]akeover|[Aa]lert) (?:page|screen)$
     */
    @When("^I tap on (?:cancel|close) button [oi]n (?:[Tt]akeover|[Aa]lert) (?:page|screen)$")
    public void WhenITapOnCancel() throws Exception {
        getTakeoverPage().tapCloseBtn();
    }

    /**
     * Check if takeover screen appars for specified user and all content inside it is correct
     *
     * @throws Exception
     * @step. ^I see takeover screen from users? \"(.*)\"$
     */
    @When("^I see takeover screen from users? \"(.*)\"$")
    public void ISeeTakeoverScreen(String nameAliases) throws Exception {
        Assert.assertTrue("Takeover screeen is not visible", getTakeoverPage().waitForTakeoverScreenVisible());
        String name;
        final String headerText = getTakeoverPage().getHeaderText();
        for (String nameAlias : CommonSteps.splitAliases(nameAliases)) {
            name = usrMgr.findUserByNameOrNameAlias(nameAlias).getName();
            Assert.assertTrue(String.format("Takeover header from user %s is not visible", name), headerText.contains(name));
        }
        Assert.assertTrue("Takeover screeen text is not visible or not as expected",
            getTakeoverPage().isTakeoverScreenTextCorrect());
    }

    /**
     * Check if takeover screen is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I( do not)? see takeover screen$
     */
    @Then("^I( do not)? see takeover screen$")
    public void IDoNotSeeTakeoverScreen(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Takeover page is not visible after timeout", getTakeoverPage().waitForTakeoverScreenVisible());
        } else {
            Assert.assertTrue("Takeover page is still visible after timeout",
                getTakeoverPage().waitForTakeoverScreenInvisible());
        }
    }

    /**
     * Tap on Send Anyway button in takeover screen
     *
     * @throws Exception
     * @step. ^I tap send anyway(?: button)?$
     */
    @Then("^I tap send anyway(?: button)?$")
    public void ITapTakeoverSendBnt() throws Exception {
        getTakeoverPage().tapSendAnywayBtn();
    }

    /**
     * Tap on Show Device/People button in takeover screen
     *
     * @throws Exception
     * @step. ^I tap show (?:device|people)(?: button)?$
     */
    @Then("^I tap show (?:device|people)(?: button)?$")
    public void ITapTakeoverShowBnt() throws Exception {
        getTakeoverPage().tapShowBtn();
    }

}
