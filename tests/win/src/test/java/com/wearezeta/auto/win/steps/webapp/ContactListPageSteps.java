package com.wearezeta.auto.win.steps.webapp;


import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.webapp.ContactListPage;
import cucumber.api.java.en.Then;

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

}
