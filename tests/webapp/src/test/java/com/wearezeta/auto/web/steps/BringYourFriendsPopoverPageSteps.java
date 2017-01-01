package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class BringYourFriendsPopoverPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(BringYourFriendsPopoverPageSteps.class.getName());
    private String invitationLink = null;
    private final WebAppTestContext context;

    public BringYourFriendsPopoverPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("^I( do not)? see Invite People popover$")
    public void ISeeInvitatePeoplePopover(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                    .isVisible();
        } else {
            context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                    .isNotVisible();
        }
    }

    @Then("^I( do not)? see Share Contacts button$")
    public void ISeeShareContactsButton(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                    .isShareContactsButtonVisible();
        } else {
            context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                    .isShareContactsButtonNotVisible();
        }
    }

    @Then("^I click Share Contacts button$")
    public void IClickShareContactsButton() throws Exception {
        context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                .clickShareContactsButton();
    }

    @Then("^I click Invite People button$")
    public void IClickInvitePeopleButton() throws Exception {
        context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                .clickInvitePeopleButton();
    }

    @When("^I see username starting with (.*) in invitation on Bring Your Friends popover$")
    public void ISeeUsernameInInvitation(String username) throws Exception {
        username = context.getUserManager().replaceAliasesOccurences(username, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertEquals("Usernames are different in invitation on Bring your friend popover", username, context.getPagesCollection().getPage(
                BringYourFriendsPopoverPage.class).getUsernameFromInvitation());
    }

}
