package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.InvitationsPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

import java.awt.image.BufferedImage;

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

    private BufferedImage previousAvatarState = null;

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
        this.previousAvatarState = getInvitationsPage().getAvatarScreenshot(name)
                .orElseThrow(IllegalStateException::new);
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
        if (this.previousAvatarState == null) {
            throw new IllegalStateException("Please take a screenshot of previous avatar state first");
        }
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        final BufferedImage currentAvatarState = getInvitationsPage().getAvatarScreenshot(name)
                .orElseThrow(IllegalStateException::new);
        final double minSimilarity = 0.97;
        final double similarity = ImageUtil.getOverlapScore(currentAvatarState, previousAvatarState,
                ImageUtil.RESIZE_TO_MAX_SCORE);
        Assert.assertTrue(String.format("User avatar for '%s' seems to be the same (%.2f >= %.2f)", name,
                similarity, minSimilarity), similarity < minSimilarity);
    }

    /**
     * Verify that invitation email exists in user's mailbox
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
     */
    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Exception {
        final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
        Assert.assertTrue(String.format("Invitation email for %s has not been received", email),
                getInvitationsPage().isInvitationMessageReceivedBy(email));
    }

    /**
     * Waits for the invite button to appear or disappear in the conversations list
     *
     * @throws Exception
     * @step. ^I( do not)? see invite more people button (in contacts list)?$
     */
    @When("^I( do not)? see invite more people button (in contacts list)?$")
    public void WhenISeeInvitePeopleContactsButton(String shouldNotSee, String inContacts) throws Exception {
        if (shouldNotSee == null && inContacts != null)
            Assert.assertTrue("The invite more people button is not visible in contacts list",
                    getInvitationsPage().waitForInviteMorePeopleContactsButtonVisible());
        else if (inContacts != null)
            Assert.assertTrue("The invite more people button is still visible in contacts list",
                    getInvitationsPage().waitForInviteMorePeopleContactsButtonNotVisible());
        else
            Assert.assertTrue("Received unrecognized parameters", false);
    }

    /**
     * Waits for the invite button to appear or disappear in the search
     *
     * @throws Exception
     * @step. ^I( do not)? see invite more people button (in search)?
     */
    @When("^I( do not)? see invite more people button (in search)?$")
    public void WhenISeeInvitePeopleSearchButton(String shouldNotSee, String inSearch) throws Exception {
        if (shouldNotSee == null && inSearch != null)
            Assert.assertTrue("The invite more people button is not visible in search",
                    getInvitationsPage().waitForInviteMorePeopleSearchButtonVisible());
        else if (inSearch != null)
            Assert.assertTrue("The invite more people button is still visible in search",
                    getInvitationsPage().waitForInviteMorePeopleSearchButtonNotVisible());
        else
            Assert.assertTrue("Received unrecognized parameters", false);
    }

    /**
     * Tap on search field in invites page
     *
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
}
