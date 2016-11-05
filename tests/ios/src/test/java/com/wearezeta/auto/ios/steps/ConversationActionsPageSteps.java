package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.ConversationActionsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class ConversationActionsPageSteps {
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

    @Then("^I confirm (Block|Remove|Delete|Leave|Cancel Request) conversation action$")
    public void IConfirmAction(String actionName) throws Exception {
        getPage().confirmAction(actionName);
    }

    /**
     * Tap specified button in action menu
     *
     * @param buttonTitle Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel
     * @throws Exception
     * @step. ^I tap (Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel|Rename) conversation action button$
     */
    @And("^I tap (Mute|Unmute|Delete|Leave|Archive|Unarchive|Block|Cancel Request|Cancel|Rename) conversation action button$")
    public void ITapXButtonInActionMenu(String buttonTitle) throws Exception {
        getPage().tapMenuItem(buttonTitle);
    }
}