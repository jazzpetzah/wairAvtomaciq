package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ConversationActionsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class ConversationActionsPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ConversationActionsPage getPage() throws Exception {
        return pagesCollection.getPage(ConversationActionsPage.class);
    }

    /**
     * Verify if conversation actions menu is visible
     *
     * @throws Exception
     * @step. I see conversation actions menu
     */
    @When("^I see conversation actions menu$")
    public void ISeeConversationActionsMenu() throws Exception {
        Assert.assertTrue("Conversation actions menu is not visible", getPage().isMenuVisible());
    }

    /**
     * Checks that specified button is exist in displayed action menu
     *
     * @param buttonTitle Mute | Unmute | Delete | Leave | Archive | Block | Cancel Request | Cancel
     * @throws Exception
     * @step. ^I see (Silence|Delete|Leave|Archive|Block|Cancel Request|Cancel) conversation action button$
     */
    @And("^I see (Mute|Unmute|Delete|Leave|Archive|Block|Cancel Request|Cancel) conversation action button$")
    public void ISeeXButtonInActionMenu(String buttonTitle) throws Exception {
        Assert.assertTrue("There is no button " + buttonTitle.toUpperCase()
                + " in opened action menu.", getPage().isItemVisible(buttonTitle));
    }

    /**
     * Tap the corresponding button to confirm/decline conversation action
     *
     * @param what either 'confirm' or 'decline'
     * @param actionName one of available action names
     * @throws Exception
     * @step. ^I (confirm|decline)  (Block|Remove|Delete|Cancel Request|Leave|Connect) conversation action$
     */
    @Then("^I (confirm|decline) (Block|Remove|Delete|Cancel Request|Leave|Connect) conversation action$")
    public void IDoAction(String what, String actionName) throws Exception {
        if (what.equals("confirm")) {
            getPage().confirmAction(actionName);
        } else {
            getPage().declineAction(actionName);
        }
    }

    /**
     * Tap specified button in action menu
     *
     * @param buttonTitle Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel
     * @throws Exception
     * @step. ^I tap (Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel|Rename)
     * conversation action button$
     */
    @And("^I tap (Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel|Rename) conversation action button$")
    public void ITapXButtonInActionMenu(String buttonTitle) throws Exception {
        getPage().tapMenuItem(buttonTitle);
    }

    /**
     * Checks that conversation name appears in displayed action menu
     *
     * @param conversation conversation name
     * @throws Exception
     * @step. I see actions menu for (.*) conversation$
     * [Ll]ist$
     */
    @And("^I see actions menu for (.*) conversation$")
    public void ISeeConversationNameInActionMenu(String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("There is no conversation name %s in opened action menu.",
                conversation), getPage().isVisibleForConversation(conversation));
    }
}