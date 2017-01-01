package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.LikersPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;


public class LikersPageSteps {
    private LikersPage getLikersPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(LikersPage.class);
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
     * @step. ^I see user (.*) in likers list(?: at position number )?(\d+)?$
     */
    @Then("^I see user (.*) in likers list(?: at position number )?(\\d+)?$")
    public void ISeeUserInLikersListAtPosition(String name, Integer position) throws Exception {
        name = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        if (position == null) {
            Assert.assertTrue(String.format("User name '%s' is not visible in Likers list", name),
                    getLikersPage().isLikerVisible(name));
        } else {
            Assert.assertTrue(String.format("User %s is not presented on position nubmer %s", name, position),
                    getLikersPage().isLikerByPositionVisible(name, position));
        }
    }
}
