package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ForwardPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ForwardPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ForwardPage getPage() throws Exception {
        return pagesCollection.getPage(ForwardPage.class);
    }

    /**
     * Tap the corresponding button on Forward page
     *
     * @param name one of available button names
     * @throws Exception
     * @step. ^I tap (Send|Close) button on Forward page$
     */
    @And("^I tap (Send|Close) button on Forward page$")
    public void ITapButton(String name) throws Exception {
        getPage().tapButton(name);
    }

    /**
     * Select the corresponding conversation from the list on Forward page
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I select (.*) conversation on Forward page$
     */
    @Then("^I select (.*) conversation on Forward page$")
    public void ISelectConversation(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        getPage().selectConversation(name);
    }

    /**
     * Verify whether a conversation is visible in the list of conversations available
     * for message forwarding
     *
     * @step. ^I (do not )?see (.*) conversation on Forward page$
     * @param shouldNotSee equals to null if the conversation should be visible
     * @param name conversation name/alias
     * @throws Exception
     */
    @When("^I (do not )?see (.*) conversation on Forward page$")
    public void ISeeConversation(String shouldNotSee, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The '%s' conversation is expected to be visible", name),
                    getPage().isConversationVisible(name));
        } else {
            Assert.assertTrue(String.format("The '%s' conversation is expected to be invisible", name),
                    getPage().isConversationInvisible(name));
        }
    }
}
