package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.UniqueUsernamePage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jcodec.common.Assert;

public class UniqueUsernamePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UniqueUsernamePage getUsernamePageSteps () throws Exception {
        return pagesCollection.getPage(UniqueUsernamePage.class);
    }

    /**
     * Verify visibility of Unique Username page
     *
     * @throws Exception
     * @step. ^I see Unique Username page$
     */
    @Then("^I see Unique Username page$")
    public void ISeeUsernamePage() throws Exception {
        Assert.assertTrue("Unique Username page is not visible", getUsernamePageSteps().isUsernamePageVisible());
    }

    /**
     * Tap Save button on Unique Username page
     *
     * @throws Exception
     * @step. ^I tap (Save) button on Unique Username page$
     */
    @When("^I tap (Save) button on Unique Username page$")
    public void ITapButtonOnUniqueUsernamePage(String buttonName) throws Exception {
        getUsernamePageSteps().tapButtonByName(buttonName);
    }

    /**
     * Fill in name input an string
     *
     * @param name string to be input
     * @throws Exception
     * @step. ^I fill in name input on Unique Username page (.*) username$
     */
    @When("^I fill in name input on Unique Username page (.*) username$")
    public void IFillInNameInInputOnUniqueUsernamePage(String name) throws Exception {
        getUsernamePageSteps().inputStringInNameInput(name);
    }
}
