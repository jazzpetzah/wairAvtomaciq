package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.CallPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    /**
     * Verifies visibility of call controls for the given conversation
     *
     * @throws Exception
     * @step. ^I( do not)? see the (incoming|outgoing|ongoing) call controls for conversation (.*)$
     */
    @And("^I( do not)? see the( incoming| outgoing| ongoing| join)? call controls for conversation (.*)$")
    public void ISeeCallControlsForConversation(String doNot, String direction, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage page = webappPagesCollection.getPage(CallPage.class);
        if (doNot == null) {
            if (direction.equals(" incoming")) {
                assertThat(String.format("Incoming call controls not visible for conversation %s", conversation),
                        page.isIncomingCallVisibleForConversation(conversation));
            } else if (direction.equals(" outgoing")) {
                assertThat(String.format("Outgoing call controls not visible for conversation %s", conversation),
                        page.isOutgoingCallVisibleForConversation(conversation));
            } else if (direction.equals(" ongoing")) {
                assertThat(String.format("Ongoing call controls not visible for conversation %s", conversation),
                        page.isOngoingCallVisibleForConversation(conversation));
            } else if (direction.equals(" join")) {
                assertThat(String.format("Join call controls not visible for conversation %s", conversation),
                        page.isJoinCallButtonVisibleForConversation(conversation));
            }
        } else {
            assertThat(String.format("Call controls for conversation %s still visible", conversation),
                    page.isCallControlsNotVisibleForConversation(conversation));
        }
    }

    /**
     * Verifies visibility of avatars in a call for the given conversation
     *
     * @throws Exception
     * @step. ^I( do not)? see the (incoming|outgoing|ongoing) call controls for conversation (.*)$
     */
    @And("^I see row of avatars on call controls with users? (.*)$")
    public void ISeeRowOfAvatarsOnCall(String participants) throws Exception {
        CallPage page = webappPagesCollection.getPage(CallPage.class);
        for (String alias : splitAliases(participants)) {
            String id = usrMgr.findUserByNameOrNameAlias(alias).getId();
            assertThat(String.format("Avatar of user %s not visible", alias), page.isAvatarVisibleInCallControls(id));
        }
    }

    /**
     * Click the accept call button in conversation list
     *
     * @throws Exception
     * @step. ^I click the accept call button in conversation list$
     * @step. ^I accept the call from conversation (.*)$
     */
    @And("^I accept the call from conversation (.*)$")
    public void IClickAcceptCallButtonInConversationView(String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(CallPage.class)
                .clickAcceptCallButton(conversation);
    }

    /**
     * Click the decline call button in conversation list
     *
     * @throws Exception
     * @step. ^I click the decline call button in conversation list$
     * @step. ^I ignore the call from conversation (.*)$
     */
    @And("^I ignore the call from conversation (.*)$")
    public void IClickDeclineCallButtonInConversationView(String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(CallPage.class)
                .clickDeclineCallButton(conversation);
    }

    /**
     * Joins ongoing call by clicking the join call bar
     *
     * @throws Exception
     * @step. ^I join call of conversation (.*)$
     */
    @When("^I join call of conversation (.*)$")
    public void IJoinCall(String conversation) throws Exception {
        webappPagesCollection.getPage(CallPage.class).clickJoinCallButton(conversation);
    }

    /**
     * Verifies if mute call button is visible for conversation
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I see mute call button for conversation (.*)
     */
    @When("^I( do not)? see mute call button for conversation (.*)")
    public void ISeeMuteCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Mute call button not visible for conversation %s", conversation),
                    contactListPage.isMuteCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Mute call button still visible for conversation %s", conversation),
                    contactListPage.isMuteCallButtonNotVisibleForConversation(conversation));
        }
    }

    /**
     * Verifies if video button is visible for conversation
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I see video button for conversation(.*)
     */
    @When("^I( do not)? see video button for conversation (.*)")
    public void ISeeVideoButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Video call button not visible for conversation %s", conversation),
                    contactListPage.isVideoButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Video call button still visible for conversation %s", conversation),
                    contactListPage.isVideoButtonNotVisibleForConversation(conversation));
        }
    }

    /**
     * Verifies if hang up button is visible for conversation
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I( do not)? see hang up button for conversation (.*)$
     */
    @When("^I( do not)? see hang up button for conversation (.*)$")
    public void ISeeEndCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Hang up button not visible for conversation %s", conversation),
                    contactListPage.isEndCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Hang up button still visible for conversation %s", conversation),
                    contactListPage.isEndCallButtonNotVisibleForConversation(conversation));
        }
    }

    /**
     * Clicks hang up button for conversation
     *
     * @throws Exception
     * @step. ^I hang up call with conversation (.*)$
     */
    @When("^I hang up call with conversation (.*)$")
    public void IClickEndCallButton(String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        contactListPage.clickEndCallButton(conversation);
    }

    /**
     * Checks if your self video is visible
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see my self video view$
     */
    @When("^I( do not)? see my self video view$")
    public void IDoNotSeeMySelfVideoView(String doNot)
            throws Exception {
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat("Self video not visible", contactListPage.isSelfVideoVisible());
        } else {
            assertThat("Self video still visible", contactListPage.isSelfVideoNotVisible());
        }
    }

    /**
     * TODO: Checks if the name of user you’re calling is visible
     *
     * @param nameAlias name of user whom you’re calling
     * @throws Exception
     * @step. ^I see name of user (.*) in calling banner in conversation list$
     * @step. ^I see the name of user (.*) in calling banner for conversation (.*)$
     */
    @Then("^I see the name of user (.*) in calling banner for conversation (.*)$")
    public void ISeeNameOfUserInCalling(String nameAlias, String conversation) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(nameAlias);
        Assert.assertTrue(webappPagesCollection.getPage(
                CallPage.class).isUserNameVisibleInCallingBanner(user.getName()));
    }

    /**
     * Verifies if accept video call button is visible for conversation
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I( do not)? see accept video call button for conversation (.*)
     */
    @When("^I( do not)? see accept video call button for conversation (.*)")
    public void ISeeAcceptVideoCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Accept video call button not visible for conversation %s", conversation),
                    contactListPage.isAcceptVideoCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Accept video call button still visible for conversation %s", conversation),
                    contactListPage.isAcceptVideoCallButtonNotVisibleForConversation(conversation));
        }
    }

    /**
     * Verifies if decline call button is visible for conversation
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param conversation conversation name string
     * @throws Exception
     * @step. ^I( do not)? see decline call button for conversation (.*)
     */
    @When("^I( do not)? see decline call button for conversation (.*)")
    public void ISeeDeclineCallButton(String doNot, String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Decline call button not visible for conversation %s", conversation),
                    contactListPage.isDeclineCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Decline call button still visible for conversation %s", conversation),
                    contactListPage.isDeclineCallButtonNotVisibleForConversation(conversation));
        }
    }

    /**
     * Clicks mute call button in conversation list
     *
     * @throws Exception
     * @step. ^I click mute call button for conversation (.*)$
     */
    @When("^I click mute call button for conversation (.*)$")
    public void IClickMuteCallButton(String conversation) throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        contactListPage.clickMuteCallButton(conversation);
    }

    /**
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I see mute button for conversation (.*) is( not)? pressed$
     */
    @When("^I see mute button for conversation (.*) is( not)? pressed$")
    public void ISeeMuteButtonNotPressed(String conversation, String doNot)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = webappPagesCollection
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Mute call button not pressed for conversation %s", conversation),
                    contactListPage.isMuteCallButtonPressed(conversation));
        } else {
            assertThat(String.format("Mute call button still pressed for conversation %s", conversation),
                    contactListPage.isMuteCallButtonNotPressed(conversation));
        }
    }
}