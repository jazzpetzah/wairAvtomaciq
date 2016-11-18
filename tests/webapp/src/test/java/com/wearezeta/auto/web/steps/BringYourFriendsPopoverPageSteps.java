package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

public class BringYourFriendsPopoverPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(BringYourFriendsPopoverPageSteps.class.getName());
    private String invitationLink = null;
    private final TestContext context;

    public BringYourFriendsPopoverPageSteps() {
        this.context = new TestContext();
    }

    public BringYourFriendsPopoverPageSteps(TestContext context) {
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

    @When("^I remember invitation link on Bring Your Friends popover$")
    public void IRemeberInvitationLink() throws Exception {
        invitationLink = context.getPagesCollection().getPage(
                BringYourFriendsPopoverPage.class).parseInvitationLink();
        LOG.info("Invitation link: " + invitationLink);
    }

    @When("^I navigate to previously remembered invitation link$")
    public void INavigateToNavigationLink() throws Exception {
        if (invitationLink == null) {
            throw new RuntimeException(
                    "Invitation link has not been remembered before!");
        }

        YouAreInvitedPage youAreInvitedPage = context.getPagesCollection()
                .getPage(YouAreInvitedPage.class);
        youAreInvitedPage.setUrl(invitationLink);
        youAreInvitedPage.navigateTo();
    }
}
