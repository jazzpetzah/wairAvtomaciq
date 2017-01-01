package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.ConversationActionsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


public class ConversationActionsPageSteps {
    private ConversationActionsPage getPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ConversationActionsPage.class);
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
     * @step. ^I (confirm|decline)  (Block|Remove|Delete|Cancel Request|Leave|Connect|Paste) conversation action$
     */
    @Then("^I (confirm|decline) (Block|Remove|Delete|Cancel Request|Leave|Connect|Paste) conversation action$")
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
     */
    @And("^I see actions menu for (.*) conversation$")
    public void ISeeConversationNameInActionMenu(String conversation) throws Exception {
        conversation = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("There is no conversation name %s in opened action menu.",
                conversation), getPage().isVisibleForConversation(conversation));
    }

    /**
     * Verify user details on Single user profile page
     *
     * @param shouldNotSee equals to null if the corresponding details should be visible
     * @param value        user unique username or Address Book name or common friends count
     * @param fieldType    either name or email
     * @throws Exception
     * @step. I (do not )?see (unique username|Address Book name|common friends count) (".*" |\s*)on Conversation actions page
     */
    @When("^I (do not )?see (unique username|Address Book name|common friends count) (\".*\" |\\s*)on Conversation actions page$")
    public void ISeeLabel(String shouldNotSee, String fieldType, String value) throws Exception {
        value = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(value, ClientUsersManager.FindBy.NAME_ALIAS);
        value = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(value, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        if (shouldNotSee == null) {
            if (value.startsWith("\"")) {
                value = value.trim().replaceAll("^\"|\"$", "");
                Assert.assertTrue(String.format("'%s' field is expected to be visible", value),
                        getPage().isUserDetailVisible(fieldType, value));
            } else {
                Assert.assertTrue(String.format("'%s' field is expected to be visible", fieldType),
                        getPage().isUserDetailVisible(fieldType));
            }
        } else {
            if (value.startsWith("\"")) {
                value = value.trim().replaceAll("^\"|\"$", "");
                Assert.assertTrue(String.format("'%s' field is expected to be invisible", value),
                        getPage().isUserDetailInvisible(fieldType, value));
            } else {
                Assert.assertTrue(String.format("'%s' field is expected to be invisible", fieldType),
                        getPage().isUserDetailInvisible(fieldType));
            }
        }
    }
}