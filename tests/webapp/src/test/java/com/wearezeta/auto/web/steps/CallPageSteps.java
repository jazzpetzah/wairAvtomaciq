package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.CallPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

import com.wearezeta.auto.web.common.TestContext;
import static org.hamcrest.MatcherAssert.assertThat;

public class CallPageSteps {

    private final TestContext context;

    public CallPageSteps(TestContext context) {
        this.context = context;
    }

    @And("^I( do not)? see the( incoming| outgoing| ongoing| join)? call controls for conversation (.*)$")
    public void ISeeCallControlsForConversation(String doNot, String direction, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage page = context.getPagesCollection().getPage(CallPage.class);
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

    @And("^I see row of avatars on call controls with users? (.*)$")
    public void ISeeRowOfAvatarsOnCall(String participants) throws Exception {
        CallPage page = context.getPagesCollection().getPage(CallPage.class);
        for (String alias : context.getUserManager().splitAliases(participants)) {
            String id = context.getUserManager().findUserByNameOrNameAlias(alias).getId();
            assertThat(String.format("Avatar of user %s not visible", alias), page.isAvatarVisibleInCallControls(id));
        }
    }

    @And("^I accept the call from conversation (.*)$")
    public void IClickAcceptCallButtonInConversationView(String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(CallPage.class)
                .clickAcceptCallButton(conversation);
    }

    @And("^I ignore the call from conversation (.*)$")
    public void IClickDeclineCallButtonInConversationView(String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(CallPage.class)
                .clickDeclineCallButton(conversation);
    }

    @When("^I join call of conversation (.*)$")
    public void IJoinCall(String conversation) throws Exception {
        context.getPagesCollection().getPage(CallPage.class).clickJoinCallButton(conversation);
    }

    @When("^I( do not)? see mute call button for conversation (.*)")
    public void ISeeMuteCallButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Mute call button not visible for conversation %s", conversation),
                    contactListPage.isMuteCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Mute call button still visible for conversation %s", conversation),
                    contactListPage.isMuteCallButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I( do not)? see video button for conversation (.*)")
    public void ISeeVideoButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Video call button not visible for conversation %s", conversation),
                    contactListPage.isVideoButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Video call button still visible for conversation %s", conversation),
                    contactListPage.isVideoButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I( do not)? see hang up button for conversation (.*)$")
    public void ISeeEndCallButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Hang up button not visible for conversation %s", conversation),
                    contactListPage.isEndCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Hang up button still visible for conversation %s", conversation),
                    contactListPage.isEndCallButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I hang up call with conversation (.*)$")
    public void IClickEndCallButton(String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        contactListPage.clickEndCallButton(conversation);
    }

    @When("^I( do not)? see my self video view$")
    public void IDoNotSeeMySelfVideoView(String doNot)
            throws Exception {
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat("Self video not visible", contactListPage.isSelfVideoVisible());
        } else {
            assertThat("Self video still visible", contactListPage.isSelfVideoNotVisible());
        }
    }

    @When("^I( do not)? see accept video call button for conversation (.*)")
    public void ISeeAcceptVideoCallButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Accept video call button not visible for conversation %s", conversation),
                    contactListPage.isAcceptVideoCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Accept video call button still visible for conversation %s", conversation),
                    contactListPage.isAcceptVideoCallButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I( do not)? see decline call button for conversation (.*)")
    public void ISeeDeclineCallButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Decline call button not visible for conversation %s", conversation),
                    contactListPage.isDeclineCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Decline call button still visible for conversation %s", conversation),
                    contactListPage.isDeclineCallButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I( do not)? see join call button for conversation (.*)")
    public void ISeeJoinCallButton(String doNot, String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Join call button not visible for conversation %s", conversation),
                    contactListPage.isJoinCallButtonVisibleForConversation(conversation));
        } else {
            assertThat(String.format("Join call button still visible for conversation %s", conversation),
                    contactListPage.isJoinCallButtonNotVisibleForConversation(conversation));
        }
    }

    @When("^I click mute call button for conversation (.*)$")
    public void IClickMuteCallButton(String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        contactListPage.clickMuteCallButton(conversation);
    }

    @When("^I see mute button for conversation (.*) is( not)? pressed$")
    public void ISeeMuteButtonNotPressed(String conversation, String doNot)
            throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage contactListPage = context.getPagesCollection()
                .getPage(CallPage.class);
        if (doNot == null) {
            assertThat(String.format("Mute call button not pressed for conversation %s", conversation),
                    contactListPage.isMuteCallButtonPressed(conversation));
        } else {
            assertThat(String.format("Mute call button still pressed for conversation %s", conversation),
                    contactListPage.isMuteCallButtonNotPressed(conversation));
        }
    }

    @When("^I click upgrade to video call button for conversation (.*)$")
    public void IClickVideoCallButton(String conversation) throws Exception {
        conversation = context.getUserManager().replaceAliasesOccurences(conversation, ClientUsersManager.FindBy.NAME_ALIAS);
        CallPage page = context.getPagesCollection().getPage(CallPage.class);
        page.clickVideoCallButton(conversation);
    }
}
