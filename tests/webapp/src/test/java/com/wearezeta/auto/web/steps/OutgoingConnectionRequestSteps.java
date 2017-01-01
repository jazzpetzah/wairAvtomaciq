package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.OutgoingConnectionRequestPage;

import cucumber.api.java.en.Then;

import org.junit.Assert;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class OutgoingConnectionRequestSteps {

    private final WebAppTestContext context;

    public OutgoingConnectionRequestSteps(WebAppTestContext context) {
        this.context = context;
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

    @Then("^I see unique username in outgoing connection request to user (.*)$")
    public void ICanSeeUniqueUsernameToUser(String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        // username given. strict check for username
        String uniqueUsername = user.getUniqueUsername();
        assertThat(context.getPagesCollection().getPage(OutgoingConnectionRequestPage.class).getUniqueUsernameOutgoing(),
                equalTo(uniqueUsername));
    }

    @Then("^I see (\\d+) common friends$")
    public void ISeeCommonFriends(int count) throws Exception {
        String commonFriends = context.getPagesCollection().getPage(OutgoingConnectionRequestPage.class).getCommonFriends();
        Assert.assertThat("Wrong amount of common friends", commonFriends, startsWith(Integer.toString(count)));
    }
}
