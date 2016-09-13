package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.LikersPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;


public class LikersPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private LikersPage getLikersPage() throws Exception {
        return pagesCollection.getPage(LikersPage.class);
    }

    /**
     * Tap X button to close likers list
     *
     * @throws Exception
     * @step. ^I close likers list$
     */
    @And("^I close likers list$")
    public void ICloseLikersList() throws Exception {
        getLikersPage().tapCloseButton();
    }

    /**
     * Verify whether the particular liker name is present in likers list and optionally on particular position
     *
     * @param name     user name/alias
     * @param position optional parameter representing position of user in likers list
     * @throws Exception
     * @step. ^I see user (.*) in likers list(?:at position number)?( \d+)?
     */
    @Then("^I see user (.*) in likers list(?: at position number )?(\\d+)?")
    public void ISeeUserInLikersListAtPosition(String name, Integer position) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        if (position == null) {
            Assert.assertTrue(String.format("User name '%s' is not visible in Likers list", name),
                    getLikersPage().isLikerVisible(name));
        } else {
            Assert.assertTrue(String.format("User %s is not presented on position nubmer %s", name, position),
                    getLikersPage().isLikerByPositionVisible(name, position));
        }
    }

    /**
     * Verify Likers page is opened
     *
     * @throws Exception
     * @step. ^I see Likers page$
     */
    @Then("^I see Likers page$")
    public void ISeeLikersPage() throws Exception {
        Assert.assertTrue("Likers page is not visible", getLikersPage().likersPageIsVisible());
    }
}
