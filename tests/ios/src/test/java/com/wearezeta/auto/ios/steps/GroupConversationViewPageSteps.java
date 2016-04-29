package com.wearezeta.auto.ios.steps;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.GroupConversationViewPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupConversationViewPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupConversationViewPage getGroupConversationViewPage() throws Exception {
        return pagesCollection.getPage(GroupConversationViewPage.class);
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
                getGroupConversationViewPage().isChatMessageContainsStringsExist(participantNames));
    }

    /**
     * Verifies that group chat is empty and has only system message
     *
     * @param participantNameAliases user names comma separated
     * @throws Exception
     */
    @Then("^I see empty group chat page with users (.*) with only system message$")
    public void ISeeGroupChatPageWithUsersAndOnlySystemMessage(String participantNameAliases) throws Exception {
        ThenISeeGroupChatPage(participantNameAliases);
    }

    @When("^I swipe up on group chat page$")
    public void ISwipeUpOnGroupChatPage() throws Exception {
        getGroupConversationViewPage().swipeUp(1000);
    }

    @When("^I see You Renamed Conversation message shown in conversation view$")
    public void ISeeYouRenamedMessageInGroupChat() throws Exception {
        Assert.assertTrue("You Renamed Conversation message is not visible in the conversation view",
                getGroupConversationViewPage().isYouRenamedConversationMessageVisible());
    }

    @Then("^I see You Left message in group chat$")
    public void ISeeYouLeftMessage() throws Exception {
        Assert.assertTrue("You Left message is not shown", getGroupConversationViewPage().isYouLeftMessageShown());
    }

    @When("^I can see You Added (.*) message$")
    public void ICanSeeYouAddedContact(String contact) throws Throwable {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("YOU ADDED contact to group is not shown",
                getGroupConversationViewPage().isYouAddedUserMessageShown(contact));
    }

}