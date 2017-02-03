package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.not;

public class GroupPopoverPageSteps {

    private static final String MAILTO = "mailto:";
    private static final String CAPTION_OPEN_CONVERSATION = "OPEN CONVERSATION";
    private static final String CAPTION_PENDING = "PENDING";
    private static final String CAPTION_UNBLOCK = "UNBLOCK";
    private static final String CAPTION_PROFILE = "PROFILE";
    private static final String TOOLTIP_REMOVE_FROM_CONVERSATION = "Remove from conversation";
    private static final String TOOLTIP_LEAVE_CONVERSATION = "Leave conversation";
    private static final String TOOLTIP_ADD_PEOPLE_TO_CONVERSATION = "Add people to conversation";
    private static final String SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_WIN = "(Ctrl + Shift + K)";
    private static final String SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_MAC = "(⌘⇧K)";
    private static final String TOOLTIP_BACK = "Back";
    private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";
    private static final String TOOLTIP_CHANGE_CONVERSATION_NAME = "Change conversation name";
    private static final String TOOLTIP_PENDING = "Pending";
    private static final String TOOLTIP_UNBLOCK = "Unblock";
    private static final String TOOLTIP_OPEN_YOUR_PROFILE = "Open your profile";

    private final WebAppTestContext context;

    public GroupPopoverPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I( do not)? see Group Participants popover$")
    public void ISeeUserProfilePopupPage(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            context.getPagesCollection().getPage(GroupPopoverContainer.class)
                    .waitUntilVisibleOrThrowException();
        } else {
            context.getPagesCollection().getPage(GroupPopoverContainer.class)
                    .waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I click Leave button on Group Participants popover$")
    public void IClickLeaveGroupChat() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickLeaveGroupChat();
    }

    @When("^I do not see Leave button on Group Participants popover$")
    public void IDoNotSeeLeaveButtonOnGroupPopoverC() throws Exception {
        Assert.assertTrue("Leave button is still visible", context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).isLeaveButtonOnGroupPopoverInvisible());
    }

    @When("^I do not see Add People button on Group Participants popover$")
    public void IDoNotSeeAddPeopleButtonOnGroupPopoverC() throws Exception {
        Assert.assertTrue("Add people button is still visible", context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).isAddPeopleButtonOnGroupPopoverInvisible());
    }

    @When("^I click confirm leave group conversation on Group Participants popover$")
    public void IClickConfirmLeaveGroupChat() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .confirmLeaveGroupChat();
    }

    @Then("^I see correct back button tool tip on Group Participants popover$")
    public void ThenISeeCorrectBackButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).getBackButtonToolTip()
                .equals(TOOLTIP_BACK));
    }

    @When("^I click back button on Group Participants popover$")
    public void WhenIClickBackButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickBackButton();
    }

    @Then("^I see correct pending button tool tip on Group Participants popover$")
    public void ThenISeeCorrectPendingButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).getPendingButtonToolTip()
                .equals(TOOLTIP_PENDING));
    }

    @When("^I click on participant (.*) on Group Participants popover$")
    public void IClickOnParticipant(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickOnParticipant(name);
    }

    @Then("^I see Pending button on Group Participants popover$")
    public void ISeePendingButton() throws Exception {
        final String pendingButtonMissingMessage = "Pending button is not visible on Group Participants popover";
        final String pendingButtonWrongCaptionMessage = "Pending button has wrong caption on Group Participants popover";
        Assert.assertTrue(pendingButtonMissingMessage, context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).isPendingButtonVisible());
        Assert.assertTrue(
                pendingButtonWrongCaptionMessage,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getPendingButtonCaption().trim()
                        .equals(CAPTION_PENDING));
    }

    @Then("^I click Pending button on Group Participants popover$")
    public void IClickPendingButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickPendingButton();
    }

    @Then("^I see profile button on Group Participants popover$")
    public void ISeeProfileButton() throws Exception {
        final String pendingButtonMissingMessage = "Profile button is not visible on Group Participants popover";
        final String pendingButtonWrongCaptionMessage = "Profile button has wrong caption on Group Participants popover";
        Assert.assertTrue(pendingButtonMissingMessage, context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).isProfileButtonVisible());
        Assert.assertTrue(
                pendingButtonWrongCaptionMessage,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getProfileButtonCaption().trim()
                        .equals(CAPTION_PROFILE));
    }

    @Then("^I click profile button on Group Participants popover$")
    public void IClickProfileButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickProfileButton();
    }

    @Then("^I see correct profile button tool tip on Group Participants popover$")
    public void ThenISeeCorrectProfileButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).getProfileButtonToolTip()
                .equals(TOOLTIP_OPEN_YOUR_PROFILE));
    }

    @Then("^I click Unblock button on Group Participants popover$")
    public void IClickUnblockButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickUnblockButton();
    }

    @Then("^I see correct Unblock button tool tip on Group Participants popover$")
    public void ThenISeeCorrectUnblockButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).getUnblockButtonToolTip()
                .equals(TOOLTIP_UNBLOCK));
    }

    @Then("^I see Unblock button on Group Participants popover$")
    public void ISeeUnblockButton() throws Exception {
        final String openUnblockButtonMissingMessage = "Unblock button is not visible on Group Participants popover";
        final String openUnblockButtonWrongCaptionMessage = "Unblock button has wrong caption on Group Participants popover";
        Assert.assertTrue(openUnblockButtonMissingMessage,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .isUnblockButtonVisible());
        Assert.assertTrue(
                openUnblockButtonWrongCaptionMessage,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getUnblockButtonCaption().trim()
                        .equals(CAPTION_UNBLOCK));
    }

    @When("^I confirm Unblock from group chat on Group Participants popover$")
    public void IConfirmUnblockUser() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickConfirmUnblockButton();
    }

    @Then("^I click confirm connect button on Group Participants popover$")
    public void IClickConfirmConnectButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickConfirmConnectButton();
    }

    @Then("^I click ignore connect button on Group Participants popover$")
    public void IClickIgnoreConnectButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickIgnoreConnectButton();
    }

    @When("^I click Remove button on Group Participants popover$")
    public void IRemoveUserFromGroupChat() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickRemoveFromGroupChat();
    }

    @When("^I (do not )?see Remove button on Group Participants popover$")
    public void ISeeRemoveUserFromGroupChat(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue("Remove Button is not visible on Group Participants Popover",
                    context.getPagesCollection().getPage(GroupPopoverContainer.class).isRemoveButtonVisible());
        } else {
            Assert.assertTrue("Remove Button is visible on Group Participants Popover",
                    context.getPagesCollection().getPage(GroupPopoverContainer.class).isRemoveButtonInvisible());
        }
    }

    @When("^I confirm remove from group chat on Group Participants popover$")
    public void IConfirmRemoveFromGroupChat() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .confirmRemoveFromGroupChat();
    }

    @When("^I see (.*) displayed on Group Participants popover$")
    public void ISeeContactsDisplayed(String contactsAliases) throws Exception {
        List<String> contacts = context.getUsersManager().splitAliases(contactsAliases);
        for (String contact : contacts) {
            contact = context.getUsersManager().replaceAliasesOccurences(contact,
                    FindBy.NAME_ALIAS);
            Assert.assertTrue(context.getPagesCollection().getPage(
                    GroupPopoverContainer.class).isParticipantVisible(contact));
        }
    }

    @When("^I see (\\d+) common friends on Group Participants popover$")
    public void ISeeCommonFriendsOnPopover(int amount) throws Exception {
        assertThat("Common friends are not correct", context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .getCommonFriends(), containsString(Integer.toString(amount)));
    }

    @When("^I( do not)? see user (.*) in verified section$")
    public void ISeeUserInVerifiedSection(String donot, String contactsAliases) throws Exception {
        List<String> contacts = context.getUsersManager().splitAliases(contactsAliases);
        for (String contact : contacts) {
            contact = context.getUsersManager().replaceAliasesOccurences(contact,
                    FindBy.NAME_ALIAS);
            if (donot == null) {
                Assert.assertTrue(context.getPagesCollection().getPage(
                        GroupPopoverContainer.class).isParticipantVerified(contact));
            } else {
                Assert.assertFalse(context.getPagesCollection().getPage(
                        GroupPopoverContainer.class).isParticipantVerified(contact));
            }
        }
    }

    @When("^I change group conversation title to (.*) on Group Participants popover$")
    public void IChangeGroupChatTitleTo(String title) throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .setConversationTitle(title);
    }

    @When("I click on titlebar$")
    public void IClickOnTitlebar() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickTitlebar();
    }

    @When("I can not change group conversation title on Group Participants popover$")
    public void ICanNotChangeGroupChatTitle() throws Exception {
        Assert.assertTrue("",
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .isTitleTextareaNotVisible());
    }

    @Then("^I see conversation title (.*) on Group Participants popover$")
    public void ISeeConversationTitle(String title) throws Exception {
        Assert.assertEquals(title,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getConversationTitle());
    }

    @When("^I click Add People button on Group Participants popover$")
    public void IClickAddPeopleButton() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickAddPeopleButton();
    }

    @When("^I see message that everyone is already added on Group Participants popover$")
    public void ISeeEveryoneAlreadyAddedMessage() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                GroupPopoverContainer.class).isEveryoneAlreadyAddedMessageShown());
    }

    @Then("I see (\\d+) participants in the Group Participants popover")
    public void ISeeXParticipants(int amount) throws Exception {
        assertThat("People information under conversation name",
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getPeopleCountInfo(), equalTo(String.valueOf(amount)
                        + " PEOPLE"));
        assertThat("Actual amount of people in popover", context.getPagesCollection()
                        .getPage(GroupPopoverContainer.class).getPeopleCount(),
                equalTo(amount));
    }

    @When("^I input user name (.*) in search field on Group Participants popover$")
    public void ISearchForUser(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .searchForUser(name);
    }

    @When("^I select user (.*) from Group Participants popover search results$")
    public void ISelectUserFromSearchResults(String user) throws Exception {
        user = context.getUsersManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .selectUserFromSearchResult(user);
    }

    @When("^I select the first (\\d+) participants from Group Participants popover search results$")
    public void ISelectFirstUsersFromSearchResults(int amount) throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .selectUsersFromSearchResult(amount);
    }

    @When("^I choose to create group conversation from Group Participants popover$")
    public void IChooseToCreateGroupConversation() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickCreateGroupConversation();
    }

    @When("^I click open conversation from Group Participants popover$")
    public void IClickOpenConversation() throws Exception {
        context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .clickOpenConvButton();
    }

    @Then("^I see correct add people button tool tip$")
    public void ThenISeeCorrectAddPeopleButtonToolTip() throws Exception {
        String tooltip = TOOLTIP_ADD_PEOPLE_TO_CONVERSATION + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_ADD_PEOPLE_TO_CONVERSATION_MAC;
        }
        assertThat(context.getPagesCollection().getPage(GroupPopoverContainer.class)
                .getAddPeopleButtonToolTip(), equalTo(tooltip));
    }

    @Then("^I see correct leave conversation button tool tip$")
    public void ThenISeeCorrectLeaveConversationButtonToolTip()
            throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class)
                .getLeaveGroupChatButtonToolTip()
                .equals(TOOLTIP_LEAVE_CONVERSATION));
    }

    @Then("^I see correct rename conversation button tool tip$")
    public void ThenISeeCorrectRenameConversationButtonToolTip()
            throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class)
                .getRenameConversationToolTip()
                .equals(TOOLTIP_CHANGE_CONVERSATION_NAME));
    }

    @Then("^I see correct remove from group button tool tip on Group Participants popover$")
    public void ThenISeeCorrectRemoveFromGroupChatButtonToolTip()
            throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class)
                .getRemoveFromGroupChatButtonToolTip()
                .equals(TOOLTIP_REMOVE_FROM_CONVERSATION));
    }

    @When("^I see username (.*) on Group Participants popover$")
    public void IseeUserNameOnUserProfilePage(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertEquals(name,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getUserName());
    }

    @When("^I see an avatar on Group Participants popover")
    public void IseeAvatarOnUserProfilePage() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                GroupPopoverContainer.class).isAvatarVisible());
    }

    @Then("^I( do not)? see Mail of user (.*) on Group Participants popover$")
    public void ISeeMailOfUser(String not, String userAlias) throws Exception {
        GroupPopoverContainer groupPopoverContainer = context.getPagesCollection()
                .getPage(GroupPopoverContainer.class);
        if (not == null) {
            ClientUser user = context.getUsersManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
            assertThat(groupPopoverContainer.getUserMail().toLowerCase(), equalTo(user.getEmail()));

        } else {
            if (groupPopoverContainer.isUserMailVisible()) {
                assertThat(groupPopoverContainer.getUserMail(), equalTo(""));
            }
        }

    }

    @Then("^I see Pending text box on Group Participants popover$")
    public void ISeePendingTextBox() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(
                GroupPopoverContainer.class).isPendingTextBoxVisible());
    }

    @Then("^I see correct open conversation button tool tip on Group Participants popover$")
    public void ThenISeeCorrectOpenConversationButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection()
                .getPage(GroupPopoverContainer.class)
                .getOpenConvButtonToolTip().equals(TOOLTIP_OPEN_CONVERSATION));
    }

    @Then("^I see open conversation button on Group Participants popover$")
    public void ISeeOpenConversationButton() throws Exception {
        final String openConvButtonMissingMessage = "Open conversation button is not visible on Group Participants popover";
        final String openConvButtonWrongCaptionMessage = "Open conversation button has wrong caption on Group Participants popover";
        Assert.assertTrue(openConvButtonMissingMessage, context.getPagesCollection()
                .getPage(GroupPopoverContainer.class).isOpenConvButtonVisible());
        Assert.assertTrue(
                openConvButtonWrongCaptionMessage,
                context.getPagesCollection().getPage(GroupPopoverContainer.class)
                        .getOpenConvButtonCaption().trim()
                        .equals(CAPTION_OPEN_CONVERSATION));
    }

    @When("^I see unique username (.*) on Group Participants popover$")
    public void ISeeUniqueUsernameOnGroupParticipants(String userAlias) throws Exception {
        ClientUser user = context.getUsersManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        // username given. strict check for username
        String uniqueUsername = user.getUniqueUsername();
        Assert.assertThat("Unique username is NOT on Group Participants popover",
                context.getPagesCollection().getPage(GroupPopoverContainer.class).getUniqueUsername(),
                equalTo(uniqueUsername));

    }
}
