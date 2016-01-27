package com.wearezeta.auto.ios.steps;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.GroupChatPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupChatPage getGroupChatPage() throws Exception {
        return pagesCollection.getPage(GroupChatPage.class);
    }

    @Then("^I see group chat page with users (.*)$")
    public void ThenISeeGroupChatPage(String participantNameAliases) throws Exception {
        participantNameAliases = usrMgr.replaceAliasesOccurences(participantNameAliases,
                ClientUsersManager.FindBy.NAME_ALIAS);
        final List<String> participantNames = CommonSteps.splitAliases(participantNameAliases).stream().
                map(x -> {
                    if (x.contains(" ")) {
                        return x.substring(0, x.indexOf(" "));
                    } else {
                        return x;
                    }
                }).collect(Collectors.toList());
        Assert.assertTrue(
                String.format("Users '%s' are not visible in the group conversation", participantNameAliases),
                getGroupChatPage().isChatMessageContainsStringsExist(participantNames));
    }

    /**
     * Verifies that group chat is empty and has only system message
     *
     * @param participantNameAliases user names comma separated
     * @throws Exception
     */
    @Then("^I see empty group chat page with users (.*) with only system message$")
    public void ISeeGroupChatPageWithUsersAndOnlySystemMessage(
            String participantNameAliases) throws Exception {
        ThenISeeGroupChatPage(participantNameAliases);
    }

    /**
     * Click open conversation details button in group chat
     *
     * @throws Exception if group chat info page was not created
     * @step. ^I open group conversation details$
     */
    @When("^I open group conversation details$")
    public void IOpenConversationDetails() throws Exception {
        getGroupChatPage().openConversationDetails();
    }

    @When("^I swipe up on group chat page$")
    public void ISwipeUpOnGroupChatPage() throws Exception {
        getGroupChatPage().swipeUp(1000);
    }

    @When("I see you renamed conversation to (.*) message shown in Group Chat")
    public void ISeeYouRenamedMessageInGroupChat(String name) throws Exception {
        Assert.assertTrue(getGroupChatPage()
                .isYouRenamedConversationMessageVisible());
    }

    @Then("I see You Left message in group chat")
    public void ISeeYouLeftMessage() throws Exception {
        Assert.assertTrue(getGroupChatPage().isYouLeftMessageShown());
    }

    @When("^I can see You Added (.*) message$")
    public void ICanSeeYouAddedContact(String contact) throws Throwable {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(getGroupChatPage()
                .isYouAddedUserMessageShown(contact));
    }

    @When("I swipe right on group chat page")
    public void ISwipeRightOnGroupChatPage() throws Throwable {
        getGroupChatPage().swipeRight(1000);
    }

}
