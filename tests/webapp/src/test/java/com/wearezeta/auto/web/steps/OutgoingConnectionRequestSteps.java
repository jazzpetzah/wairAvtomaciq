package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.OutgoingConnectionRequestPage;
import com.wearezeta.auto.web.pages.PendingConnectionsPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertTrue;

public class OutgoingConnectionRequestSteps {

    private final TestContext context;

    public OutgoingConnectionRequestSteps(TestContext context) {
        this.context = context;
    }

    /* Conversation list */

    @When("^I click cancel request in the options popover$")
    public void IClickCancelRequestButton() throws Exception {
        Assert.assertTrue("Cancel request button is not shown in the option popover",
                context.getPagesCollection().getPage(OutgoingConnectionRequestPage.class).isCancelRequestButtonClickable());
        context.getPagesCollection().getPage(OutgoingConnectionRequestPage.class)
                .clickCancelRequest();
    }

    /* Conversation view */

    @Then("^I see cancel pending request button in the conversation view$")
    public void ISeeCancelRequestButton() throws Exception {
        assertTrue("Cancel request is NOT visible in conversation list", context.getPagesCollection().getPage(
                OutgoingConnectionRequestPage.class).isCancelRequestButtonVisible());
    }

    @Then("^I click cancel pending request button in the conversation view$")
    public void IClickOnCancelRequestButton() throws Exception {
        context.getPagesCollection().getPage(OutgoingConnectionRequestPage.class).clickCancelPendingRequestButton();
    }

    @Then("^I see (\\d+) common friends$")
    public void ISeeCommonFriends(int count) throws Exception {
        String commonFriends = context.getPagesCollection().getPage(PendingConnectionsPage.class).getCommonFriends();
        Assert.assertThat("Wrong amount of common friends", commonFriends, startsWith(Integer.toString(count)));
    }
}
