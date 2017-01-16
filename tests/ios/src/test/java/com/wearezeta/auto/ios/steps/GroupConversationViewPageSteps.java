package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import org.junit.Assert;

import com.wearezeta.auto.ios.pages.GroupConversationViewPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupConversationViewPageSteps {
    private GroupConversationViewPage getGroupConversationViewPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(GroupConversationViewPage.class);
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
        contact = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("YOU ADDED contact to group is not shown",
                getGroupConversationViewPage().isYouAddedUserMessageShown(contact));
    }

}
