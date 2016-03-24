package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.InvitationsPage;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class InvitationsPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private InvitationsPage getInvitationsPage() throws Exception {
        return pagesCollection.getPage(InvitationsPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verify that a particular user is visible in the invites list
     *
     * @param alias user alias
     * @throws Exception
     * @step. ^I see (.*) in the invites list$
     */
    @Then("^I see (.*) in the invites list$")
    public void ISeeUser(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        Assert.assertTrue(String.format("User '%s' is not visible on invites page", name),
                getInvitationsPage().waitUntilUserNameIsVisible(name));
    }

    private ElementState avatarState = null;

    /**
     * Store a screenshot of user avatar for a future comparison
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) avatar in the invites list$
     */
    @When("^I remember the state of (.*) avatar in the invites list$")
    public void IRememberTheStateOfAvatar(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        this.avatarState = new ElementState(
                () -> getInvitationsPage().getAvatarScreenshot(name).orElseThrow(IllegalStateException::new)
        ).remember();
    }

    /**
     * Tap the Invote button next to a particular user name
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I tap Invite button next to (.*)
     */
    @When("^I tap Invite button next to (.*)")
    public void ITapInviteButton(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        getInvitationsPage().tapInviteButtonFor(name);
    }

    /**
     * Select the corresponding email on the invitation alert
     *
     * @param alias email address/alias
     * @throws Exception
     * @step. ^I select (.*) email on invitation sending alert$
     */
    @When("^I select (.*) email on invitation sending alert$")
    public void ISelectEmailOnInvitationAlert(String alias) throws Exception {
        final String email = usrMgr.replaceAliasesOccurences(alias, ClientUsersManager.FindBy.EMAIL_ALIAS);
        getInvitationsPage().selectEmailOnAlert(email);
    }

    /**
     * Confirm selection on invitation alert
     *
     * @throws Exception
     * @step. ^I confirm invitation sending alert$
     */
    @When("^I confirm invitation sending alert$")
    public void IConfirmInvitationAlert() throws Exception {
        getInvitationsPage().confirmInvitationAlert();
    }

    /**
     * Verify that avatar state of a user in the list is changed since the last snapshot
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I verify the state of (.*) avatar in the invites list is changed$"
     */
    @Then("^I verify the state of (.*) avatar in the invites list is changed$")
    public void IVerifyTheAvatarStateIsChanged(String alias) throws Exception {
        if (this.avatarState == null) {
            throw new IllegalStateException("Please take a screenshot of previous avatar state first");
        }
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        final double minSimilarity = 0.97;
        Assert.assertTrue(String.format("User avatar for '%s' seems to be the same", name),
                this.avatarState.isChanged(10, minSimilarity));
    }

    /**
     * Verify that invitation email exists in user's mailbox
     *
     * @param alias user name/alias
     * @throws Throwable
     * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
     */
    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Throwable {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(alias);
        Assert.assertTrue(String.format("Invitation email for %s has not been received", user.getEmail()),
                getInvitationsPage().isInvitationMessageReceivedBy(user));
    }

    /**
     * Tap on search field in invites page
     *
     * @throws Exception
     * @throws Exception
     * @step. ^I tap search in invites page?
     */
    @When("^I tap search in invites page$")
    public void WhenITapSearchFieldInInvitePage() throws Exception {
        getInvitationsPage().tapOnInviteSearchField();
    }

    /**
     * Tap on close button in invites search field
     *
     * @throws Exception
     * @step. ^I tap invites page close button?
     */
    @When("I tap invites page close button$")
    public void WhenITapCloseBtnInInvitePage() throws Exception {
        getInvitationsPage().tapOnInvitePageCloseBtn();
    }

    /**
     * Broadcast the link parsed from the recent invitation email for receiver
     *
     * @param receiver email/alias
     * @throws Throwable
     * @step. ^I broadcast the invitation for (.*)
     */
    @When("^I broadcast the invitation for (.*)")
    public void IBroadcastInvitation(String receiver) throws Throwable {
        final ClientUser user = usrMgr.findUserByEmailOrEmailAlias(receiver);
        final String code = getInvitationsPage().getRecentInvitationCode(user);
        AndroidCommonUtils.broadcastInvitationCode(code);
    }
}
