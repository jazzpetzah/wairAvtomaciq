package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ConversationActionsPage;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class ConversationActionsPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ConversationActionsPage getPage() throws Exception {
        return pagesCollection.getPage(ConversationActionsPage.class);
    }

    /**
     * Verify if conversation action menu is visible
     *
     * @throws Exception
     * @step. I see conversation action menu
     */
    @When("^I see conversation action menu$")
    public void ISeeConversationActionMenu() throws Exception {
        Assert.assertTrue("Conversation action menu is not visible",
                getPage().isActionMenuVisible());
    }
}