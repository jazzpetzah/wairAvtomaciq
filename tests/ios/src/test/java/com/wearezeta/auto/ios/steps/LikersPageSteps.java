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
     * Verify whether the particular liker name is present in likers list
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I see (.*) in likers list$
     */
    @Then("^I see (.*) in likers list$")
    public void ISeeXInLikeersList(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("User name '%s' is not visible in Likers list", name),
                getLikersPage().isLikerVisible(name));
    }
}
