package com.wearezeta.auto.osx.steps.webapp;


import org.apache.log4j.Logger;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class ContactListPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(ContactListPageSteps.class.getName());
    
    private final WebAppTestContext webContext;

    public ContactListPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = webContext.getUserManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        ContactListPage contactListPage = webContext.getPagesCollection(WebappPagesCollection.class).getPage(
                ContactListPage.class);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        contactListPage.openContextMenuWithContextClickForConversation(name);
    }
    
    //TODO move to webapp
    @Then("^I verify active conversation is at index (\\d+)$")
    public void IVerifyActiveConversationIsAtIndex(int expectedIndex) throws Exception {
        final int actualIndex = webContext.getPagesCollection().getPage(ContactListPage.class).getActiveConversationIndex();
        Assert.assertTrue(String.format(
                        "The index of active item in Conversations list does not equal to %s (current value is %s)",
                        expectedIndex, actualIndex), actualIndex == expectedIndex);
    }

}
