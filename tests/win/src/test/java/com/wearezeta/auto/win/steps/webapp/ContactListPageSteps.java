package com.wearezeta.auto.win.steps.webapp;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.win.pages.webapp.ContactListPage;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ContactListPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(ContactListPageSteps.class.getName());
    private static final int ARCHIVE_BTN_VISILITY_TIMEOUT = 5; // seconds
    private final WrapperTestContext context;

    public ContactListPageSteps() {
        this.context = new WrapperTestContext();
    }

    public ContactListPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        contactListPage.openContextMenuForContact(name);
    }

    @Given("^I see my avatar on top of Contact list$")
    public void ISeeMyNameOnTopOfContactList() throws Exception {
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        contactListPage.waitForSelfProfileButton();
    }

    @Given("I see Contact list with name (.*)")
    public void GivenISeeContactListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        LOG.debug("Looking for contact with name " + name);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        for (int i = 0; i < 5; i++) {
            if (contactListPage.isConvoListEntryWithNameExist(name)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new AssertionError("Conversation list entry '" + name + "' is not visible after timeout expired");
    }

    @Given("I see archive list with name (.*)")
    public void GivenISeeArchiveListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        LOG.debug("Looking for contact with name " + name);
        Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
        for (int i = 0; i < 5; i++) {
            if (contactListPage.isArchiveListEntryWithNameExist(name)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new AssertionError("Conversation list entry '" + name + "' is not visible after timeout expired");
    }

    @Given("^I open conversation with (.*)")
    public void GivenIOpenConversationWith(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation with name '%s' is not visible", contact), context.
                getWebappPagesCollection().getPage(ContactListPage.class).isConversationVisible(contact));
        context.getWebappPagesCollection().getPage(ContactListPage.class).openConversation(contact);
        Assert.assertTrue(String.format("Conversation '%s' should be selected", contact), context.getWebappPagesCollection().
                getPage(ContactListPage.class).isConversationSelected(contact));
    }

    @Then("^I see conversation with (.*) is selected in conversations list$")
    public void ISeeConversationIsSelected(String convoName) throws Exception {
        convoName = context.getUserManager().replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation '%s' should be selected", convoName), context.getWebappPagesCollection().
                getPage(ContactListPage.class).isConversationSelected(convoName));
    }

    @Given("^I unarchive conversation (.*)")
    public void GivenIUnarchiveConversation(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(ContactListPage.class).unarchiveConversation(name);
    }

    @When("^I open preferences by clicking the gear button$")
    public void IOpenPreferences() throws Exception {
        context.getWebappPagesCollection().getPage(com.wearezeta.auto.web.pages.ContactListPage.class).openPreferences();
    }

    @When("^I archive conversation (.*)$")
    public void IClickArchiveButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        // TODO
        // contactListPage.clickArchiveConversation();
    }

    @When("^I open archive$")
    public void IOpenArchive() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).openArchive();
    }

    @Given("^I do not see Contact list with name (.*)$")
    public void IDoNotSeeContactListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isConvoListEntryNotVisible(name));
    }

    @When("^I( do not)? see connection request from one user$")
    public void IDoNotSeeIncomingConnection(String doNot) throws Exception {
        IDoNotSeeXIncomingConnection(doNot, 1);
    }

    @When("^I( do not)? see connection requests? from (\\d+) user$")
    public void IDoNotSeeXIncomingConnection(String doNot, int amount) throws Exception {
        if (doNot == null) {
            if (amount == 1) {
                assertThat(context.getWebappPagesCollection().getPage(ContactListPage.class).getIncomingPendingItemText(),
                        equalTo(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
            } else {
                assertThat(context.getWebappPagesCollection().getPage(ContactListPage.class).getIncomingPendingItemText(),
                        equalTo(amount + WebAppLocators.Common.CONTACT_LIST_X_PEOPLE_WAITING));
            }
        } else {
            String itemText = "";
            try {
                itemText = context.getWebappPagesCollection().getPage(ContactListPage.class).getIncomingPendingItemText();
            } catch (AssertionError e) {
                LOG.debug(e.getMessage());
            }
            assertThat(itemText, equalTo(""));
        }
    }

    @Given("^I open the list of incoming connection requests$")
    public void IOpenIncomingConnectionRequestsList() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).openConnectionRequestsList();
    }

    @When("^I open search by clicking the people button$")
    public void IOpenStartUI() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).openStartUI();
    }

    @When("^I set muted state for conversation (.*)")
    public void ISetMutedStateFor(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        // TODO
        // contactListPage.clickMuteConversation();
    }

    @When("^I set unmuted state for conversation (.*)")
    public void ISetUnmutedStateFor(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        // TODO
        // contactListPage.clickUnmuteConversation();
    }

    @When("^I see that conversation (.*) is muted$")
    public void ISeeConversationIsMuted(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isConversationMuted(contact));
    }

    @When("^I see that conversation (.*) is not muted$")
    public void ISeeConversationIsNotMuted(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isConversationNotMuted(contact));
    }

    @Then("^I verify that (.*) index in Contact list is (\\d+)$")
    public void IVerifyContactIndex(String convoNameAlias, int expectedIndex) throws Exception {
        convoNameAlias = context.getUserManager().replaceAliasesOccurences(convoNameAlias, FindBy.NAME_ALIAS);
        final int actualIndex = context.getWebappPagesCollection().getPage(ContactListPage.class).getItemIndex(convoNameAlias);
        Assert.assertTrue(String.format(
                "The index of '%s' item in Conevrsations list does not equal to %s (current value is %s)", convoNameAlias,
                expectedIndex, actualIndex), actualIndex == expectedIndex);
    }

    @Then("^I verify active conversation is at index (\\d+)$")
    public void IVerifyActiveConversationIsAtIndex(int expectedIndex) throws Exception {
        final int actualIndex = context.getWebappPagesCollection().getPage(ContactListPage.class).getActiveConversationIndex();
        Assert.assertTrue(String.format(
                "The index of active item in Conversations list does not equal to %s (current value is %s)", expectedIndex,
                actualIndex), actualIndex == expectedIndex);
    }

    @Then("^I( do not)? see Archive button at the bottom of my Contact list$")
    public void IVerifyArchiveButtonVisibility(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            context.getWebappPagesCollection().getPage(ContactListPage.class).waitUntilArchiveButtonIsVisible(
                    ARCHIVE_BTN_VISILITY_TIMEOUT);
        } else {
            context.getWebappPagesCollection().getPage(ContactListPage.class).waitUntilArchiveButtonIsNotVisible(
                    ARCHIVE_BTN_VISILITY_TIMEOUT);
        }
    }

    @Then("^I( do not)? see missed call notification for conversation (.*)$")
    public void isCallMissedVisibleForContact(String shouldNotBeVisible, String conversationName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(String.format("The call notification in conversation '%s' should be visible", conversationName), context.
                    getWebappPagesCollection().getPage(ContactListPage.class).isMissedCallVisibleForContact(conversationName));
        } else {
            assertTrue(String.format("The call notification in conversation '%s' should NOT be visible", conversationName),
                    context.getWebappPagesCollection().getPage(ContactListPage.class).isMissedCallInvisibleForContact(
                            conversationName));
        }
    }

    @Then("^I( do not)? see joined group call notification for conversation (.*)$")
    public void isJoinedGroupCallNotificationVisibleForConversation(String shouldNotBeVisible, String conversationName) throws
            Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(String.format("The joined group call notification in conversation '%s' should be visible",
                    conversationName), context.getWebappPagesCollection().getPage(ContactListPage.class).
                            isJoinedGroupCallNotificationVisibleForConversation(conversationName));
        } else {
            assertTrue(String.format("The joined group call notification in conversation '%s' should NOT be visible",
                    conversationName), context.getWebappPagesCollection().getPage(ContactListPage.class).
                            isJoinedGroupCallNotificationInvisibleForConversation(conversationName));
        }
    }

    @Then("^I( do not)? see unjoined group call notification for conversation (.*)$")
    public void isUnjoinedGroupCallNotificationVisibleForConversation(String shouldNotBeVisible, String conversationName) throws
            Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(String.format("The unjoined group call notification in conversation '%s' should be visible",
                    conversationName), context.getWebappPagesCollection().getPage(ContactListPage.class).
                            isUnjoinedGroupCallNotificationVisibleForConversation(conversationName));
        } else {
            assertTrue(String.format("The unjoined group call notification in conversation '%s' should NOT be visible",
                    conversationName), context.getWebappPagesCollection().getPage(ContactListPage.class).
                            isUnjoinedGroupCallNotificationInvisibleForConversation(conversationName));
        }
    }

    @Given("^I verify ping icon in conversation with (\\w+) has (\\w+) color$")
    public void IVerifyPingIconColor(String conversationName, String colorName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor pingIconColor = context.getWebappPagesCollection().getPage(ContactListPage.class).
                getCurrentPingIconAccentColor(conversationName);
        Assert.assertEquals(expectedColor, pingIconColor);
    }

    @Given("^I verify unread dot in conversation with (\\w+) has (\\w+) color$")
    public void IVerifyUnreadDotColor(String conversationName, String colorName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor unreadDotColor = context.getWebappPagesCollection().getPage(ContactListPage.class).
                getCurrentUnreadDotAccentColor(conversationName);
        Assert.assertEquals(expectedColor, unreadDotColor);
    }

    @Given("^I see ping icon in conversation with (\\w+)")
    public void ISeePingIcon(String conversationName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        Assert.assertTrue("No ping visible.", context.getWebappPagesCollection().getPage(ContactListPage.class).
                isPingIconVisibleForConversation(conversationName));
    }

    @Then("^I see previously remembered user selected in the conversations list$")
    public void ISeePreviouslyRememberedUserSelectedInConversationList() throws Exception {
        final List<String> selectedTopPeople = StartUIPageSteps.getSelectedTopPeople();
        ContactListPage contactListPage = context.getWebappPagesCollection().getPage(ContactListPage.class);
        if (selectedTopPeople != null) {
            assert selectedTopPeople.size() == 1 : "Count of selected Top People is expected to be 1";
            String oneSelectedTopPeople = selectedTopPeople.get(0);
            oneSelectedTopPeople = context.getUserManager().replaceAliasesOccurences(oneSelectedTopPeople, FindBy.NAME_ALIAS);
            LOG.debug("Looking for contact with name " + selectedTopPeople);
            Assert.assertTrue("No contact list loaded.", contactListPage.waitForContactListVisible());
            for (int i = 0; i < 5; i++) {
                if (contactListPage.isConvoListEntryWithNameExist(oneSelectedTopPeople)) {
                    return;
                }
                Thread.sleep(1000);
            }
            throw new AssertionError("Conversation list entry '" + selectedTopPeople + "' is not visible after timeout expired");
        } else {
            throw new Error("Top People are not selected");
        }
    }

    @When("^I click on options button for conversation (.*)$")
    public void IClickOnOptionsButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickOptionsButtonForContact(contact);
    }

    @Then("^I see correct tooltip for silence button in options popover$")
    public void ISeeCorrectTooltipForSilenceButton() throws Exception {
        // TODO
        // String tooltip = TOOLTIP_SILENCE + " ";
        // if (WebAppExecutionContext.isCurrentPlatformWindows()) {
        // tooltip = tooltip + SHORTCUT_SILENCE_WIN;
        // } else {
        // tooltip = tooltip + SHORTCUT_SILENCE_MAC;
        // }
        // assertThat("Silence button tooltip",
        // context.getWebappPagesCollection()
        // .getPage(ContactListPage.class).getMuteButtonToolTip(),
        // equalTo(tooltip));
    }

    @When("^I type shortcut combination to mute or unmute a conversation$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).pressShortCutToMute();
    }

    @When("^I type shortcut combination to archive a conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).pressShortCutToArchive();
    }

    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).pressShortCutToSearch();
    }

    @When("^I type shortcut combination for next conversation$")
    public void ITypeShortcutCombinationForNextConv() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).pressShortCutForNextConv();
    }

    @When("^I type shortcut combination for previous conversation$")
    public void ITypeShortcutCombinationForPrevConv() throws Exception {
        context.getWebappPagesCollection().getPage(ContactListPage.class).pressShortCutForPrevConv();
    }

    @Then("^I see a leave warning modal$")
    public void ISeeALeaveWarning() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isLeaveWarningModalVisible());
    }

    @Then("^I click cancel button in the leave warning$")
    public void IClickCancelButtonOnLeaveWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickCancelOnLeaveWarning();
    }

    @Then("^I see a block warning modal$")
    public void ISeeABlockWarning() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isBlockWarningModalVisible());
    }

    @Then("^I click cancel button in the block warning$")
    public void IClickCancelButtonOnBlockWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickCancelOnBlockWarning();
    }

    @Then("^I click block button in the block warning$")
    public void IClickBlockButtonOnBlockWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickBlockOnBlockWarning();
    }

    @When("^I see conversation (.*) is on the top$")
    public void ISeeConversationWithNameOnTop(String conv) throws Exception {
        conv = context.getUserManager().replaceAliasesOccurences(conv, FindBy.NAME_ALIAS);
        int itemIndex = context.getWebappPagesCollection().getPage(ContactListPage.class).getItemIndex(conv);
        assertThat("Conversation is not on the top", itemIndex, equalTo(1));
    }

    @Then("^I see a delete warning modal for group conversations$")
    public void ISeeDeleteWarningForGroup() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).
                isDeleteWarningModalForGroupVisible());
    }

    @Then("^I click delete button in the delete warning for group conversations$")
    public void IClickDeleteButtonOnDeleteWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickDeleteOnDeleteWarning();
    }

    @Then("^I click leave button in the leave warning$")
    public void IClickLeaveButtonOnLeaveWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickLeaveOnLeaveWarning();
    }

    @Then("^I click Leave checkbox on a delete warning modal for group conversations$")
    public void IClickLeaveCheckboxOnDeleteWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickLeaveCheckboxOnDeleteWarning();
    }

    @Then("^I click cancel button in the delete warning for group conversations$")
    public void IClickCancelButtonOnDeleteWarning() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickCancelOnDeleteWarning();
    }

    @Then("^I see a delete warning modal for 1:1 conversations$")
    public void ISeeDeleteWarningForSingle() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ContactListPage.class).isDeleteWarningModalSingleVisible());
    }

    @Then("^I click delete button in the delete warning for 1:1 conversations$")
    public void IClickDeleteButtonOnDeleteWarningForSingle() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickDeleteOnDeleteWarningSingle();
    }

    @Then("^I click cancel button in the delete warning for 1:1 conversations$")
    public void IClickCancelButtonOnDeleteWarningForSingle() throws Throwable {
        context.getWebappPagesCollection().getPage(ContactListPage.class).clickCancelOnDeleteWarningSingle();
    }
}
