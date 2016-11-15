package com.wearezeta.auto.win.steps.webapp;


import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.webapp.ContactListPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(ContactListPageSteps.class.getName());
    private final TestContext webContext;
    private final TestContext wrapperContext;

    public ContactListPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    //TODO move to webapp
    @Then("^I verify active conversation is at index (\\d+)$")
    public void IVerifyActiveConversationIsAtIndex(int expectedIndex) throws Exception {
        final int actualIndex = webContext.getPagesCollection().getPage(ContactListPage.class).getActiveConversationIndex();
        Assert.assertTrue(String.format(
                "The index of active item in Conversations list does not equal to %s (current value is %s)", expectedIndex,
                actualIndex), actualIndex == expectedIndex);
    }
    
    @When("^I type shortcut combination to archive a conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortcutForArchive();
    }
    
    @When("^I type shortcut combination to mute or unmute a conversation$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutToMute();
    }
    
    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutToSearch();
    }

    @When("^I type shortcut combination for next conversation$")
    public void ITypeShortcutCombinationForNextConv() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutForNextConv();
    }

    @When("^I type shortcut combination for previous conversation$")
    public void ITypeShortcutCombinationForPrevConv() throws Exception {
        webContext.getPagesCollection().getPage(ContactListPage.class).pressShortCutForPrevConv();
    }
    
    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = webContext.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = webContext.getPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        contactListPage.openContextMenuWithContextClickForConversation(name);
    }

}
