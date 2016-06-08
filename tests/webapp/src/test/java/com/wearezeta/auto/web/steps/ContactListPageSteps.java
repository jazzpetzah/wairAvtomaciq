package com.wearezeta.auto.web.steps;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.ContactListPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ContactListPageSteps {

    private static final Logger log = ZetaLogger
            .getLog(ContactListPageSteps.class.getSimpleName());

    private static final String TOOLTIP_SILENCE = "Silence";
    private static final String SHORTCUT_SILENCE_WIN = "(Ctrl + Alt + S)";
    private static final String SHORTCUT_SILENCE_MAC = "(⌘⌥S)";
    
    private final TestContext context;
    
    public ContactListPageSteps() {
        this.context = new TestContext();
    }

    public ContactListPageSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Checks that contact list is loaded and waits for profile avatar to be
     * shown
     *
     * @throws AssertionError if contact list is not loaded or avatar does not appear at
     *                        the top of Contact List
     * @step. ^I see my avatar on top of Contact list$
     */
    @Given("^I see my avatar on top of Contact list$")
    public void ISeeMyNameOnTopOfContactList() throws Exception {
        Assert.assertTrue("No contact list loaded.", context.getPagesCollection()
                .getPage(ContactListPage.class).waitForContactListVisible());
        context.getPagesCollection().getPage(ContactListPage.class)
                .waitForSelfProfileButton();
    }

    @Given("^I verify a badge is shown on self profile button$")
    public void ISeeBadgeOnSelfProfileButton() throws Exception {
        Assert.assertTrue("No badge visible.", context.getPagesCollection()
                .getPage(ContactListPage.class).waitForBadgeVisible());
    }

    /**
     * Checks that we can see conversation with specified name in Contact List
     *
     * @param name conversation name string
     * @throws AssertionError if conversation name does not appear in Contact List
     * @step. I see Contact list with name (.*)
     */
    @Given("I see Contact list with name (.*)")
    public void GivenISeeContactListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        log.debug("Looking for contact with name " + name);
        Assert.assertTrue("No contact list loaded.", context.getPagesCollection()
                .getPage(ContactListPage.class).waitForContactListVisible());
        for (int i = 0; i < 5; i++) {
            if (context.getPagesCollection().getPage(ContactListPage.class)
                    .isConvoListEntryWithNameExist(name)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new AssertionError("Conversation list entry '" + name
                + "' is not visible after timeout expired");
    }

    /**
     * Checks that we can see conversation with specified name in archive List
     *
     * @param name conversation name string
     * @throws Exception if conversation name does not appear in archive List
     * @step. I see archive list with name (.*)
     */
    @Given("I see archive list with name (.*)")
    public void GivenISeeArchiveListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        log.debug("Looking for contact with name " + name);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.",
                contactListPage.waitForContactListVisible());

        for (int i = 0; i < 5; i++) {
            if (contactListPage.isArchiveListEntryWithNameExist(name)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new AssertionError("Conversation list entry '" + name
                + "' is not visible after timeout expired");
    }

    /**
     * Opens conversation by choosing it from Contact List
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I open conversation with (.*)
     */
    @Given("^I open conversation with (.*)")
    public void GivenIOpenConversationWith(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class).openConversation(
                contact);
    }

    /**
     * Verifies whether the particular conversation is selected in the list
     *
     * @param convoName conversation name
     * @throws Exception
     * @step. ^I see conversation with (.*) is selected in conversations list$
     */
    @Then("^I see conversation with (.*) is selected in conversations list$")
    public void ISeeConversationIsSelected(String convoName) throws Exception {
        convoName = context.getUserManager().replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation '%s' should be selected",
                convoName),
                context.getPagesCollection().getPage(ContactListPage.class)
                        .isConversationSelected(convoName));
    }

    /**
     * Unarchives conversation 'name'
     *
     * @param name conversation name string
     * @throws Exception
     * @step. I unarchive conversation with (.*)
     */
    @Given("^I unarchive conversation (.*)")
    public void GivenIUnarchiveConversation(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class)
                .unarchiveConversation(name);
    }

    /**
     * Clicks the self name item in the convo list to open self profile page
     *
     * @throws Exception
     * @step. ^I open self profile$
     */
    @When("^I open self profile$")
    public void IOpenSelfProfile() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openSelfProfile();
    }

    /**
     * Archive conversation by choosing it from Contact List
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I archive conversation (.*)$
     */
    @When("^I archive conversation (.*)$")
    public void IClickArchiveButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        contactListPage.clickArchiveConversation();
    }

    /**
     * Types shortcut combination to archive conversation
     */
    @When("^I type shortcut combination to archive the conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).pressShortcutForArchive();
    }

    /**
     * Open archived conversations
     *
     * @throws Exception
     * @step. ^I open archive$
     */
    @When("^I open archive$")
    public void IOpenArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openArchive();
    }
    
    /**
     * Close archive
     *
     * @throws Exception
     * @step. ^I close archive$
     */
    @When("^I close archive$")
    public void ICloseArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).closeArchive();
    }

    /**
     * Checks that we cannot see conversation with specified name in Contact
     * List
     *
     * @param name conversation name string
     * @throws AssertionError if conversation name appear in Contact List
     * @step. ^I do not see Contact list with name (.*)$
     */
    @Given("^I do not see Contact list with name (.*)$")
    public void IDoNotSeeContactListWithName(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isConvoListEntryNotVisible(name));
    }

    /**
     * Checks that one connection request is displayed in Conversation List or
     * not
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I(do not)? see connection request from one user$
     */
    @When("^I( do not)? see connection request from one user$")
    public void IDoNotSeeIncomingConnection(String doNot) throws Exception {
        IDoNotSeeXIncomingConnection(doNot, 1);
    }

    /**
     * Checks that connection requests are displayed in Conversation List or not
     *
     * @param doNot  is set to null if "do not" part does not exist
     * @param amount amount of requests
     * @throws Exception
     * @step. ^I(do not)? see connection request from one user$
     */
    @When("^I( do not)? see connection requests? from (\\d+) user$")
    public void IDoNotSeeXIncomingConnection(String doNot, int amount)
            throws Exception {
        if (doNot == null) {
            if (amount == 1) {
                assertThat(
                        context.getPagesCollection().getPage(ContactListPage.class)
                                .getIncomingPendingItemText(),
                        equalTo(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
            } else {
                assertThat(context.getPagesCollection().getPage(ContactListPage.class)
                        .getIncomingPendingItemText(), equalTo(amount
                        + WebAppLocators.Common.CONTACT_LIST_X_PEOPLE_WAITING));
            }
        } else {
            String itemText = "";
            try {
                itemText = context.getPagesCollection().getPage(ContactListPage.class)
                        .getIncomingPendingItemText();
            } catch (AssertionError e) {
                log.debug(e.getMessage());
            }
            assertThat(itemText, equalTo(""));
        }
    }

    /**
     * Opens list of connection requests from Contact list
     *
     * @throws Exception
     * @step. ^I open the list of incoming connection requests$
     */
    @Given("^I open the list of incoming connection requests$")
    public void IOpenIncomingConnectionRequestsList() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class)
                .openConnectionRequestsList();
    }

    /**
     * Opens People Picker in Contact List
     *
     * @throws Exception
     * @step. ^I open People Picker from Contact List$
     */
    @When("^I open People Picker from Contact List$")
    public void IOpenStartUI() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openStartUI();
    }

    /**
     * Silence the particular conversation from the list
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I set muted state for conversation (.*)
     */
    @When("^I set muted state for conversation (.*)")
    public void ISetMutedStateFor(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        contactListPage.clickMuteConversation();
    }

    /**
     * Set unmuted state for the particular conversation from the list if it is
     * already muted
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I set unmuted state for conversation (.*)
     */
    @When("^I set unmuted state for conversation (.*)")
    public void ISetUnmutedStateFor(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        contactListPage.clickUnmuteConversation();
    }

    /**
     * Verify that conversation is muted by checking mute icon
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I see that conversation (.*) is muted$
     */
    @When("^I see that conversation (.*) is muted$")
    public void ISeeConversationIsMuted(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);

        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isConversationMuted(contact));
    }

    /**
     * Verify that conversation is muted by checking mute icon is invisible
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I see that conversation (.*) is not muted$
     */
    @When("^I see that conversation (.*) is not muted$")
    public void ISeeConversationIsNotMuted(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);

        Assert.assertFalse(context.getPagesCollection().getPage(ContactListPage.class)
                .isConversationMuted(contact));
    }

    /**
     * Verify whether the particular conversations list item has expected index
     *
     * @param convoNameAlias conversation name/alias
     * @param expectedIndex  the expected index (starts from 1)
     * @throws Exception
     * @step. ^I verify that (.*) index in Contact list is (\\d+)$
     */
    @Then("^I verify that (.*) index in Contact list is (\\d+)$")
    public void IVerifyContactIndex(String convoNameAlias, int expectedIndex)
            throws Exception {
        convoNameAlias = context.getUserManager().replaceAliasesOccurences(convoNameAlias,
                FindBy.NAME_ALIAS);
        final int actualIndex = context.getPagesCollection().getPage(
                ContactListPage.class).getItemIndex(convoNameAlias);
        Assert.assertTrue(
                String.format(
                        "The index of '%s' item in Conevrsations list does not equal to %s (current value is %s)",
                        convoNameAlias, expectedIndex, actualIndex),
                actualIndex == expectedIndex);
    }

    private static final int ARCHIVE_BTN_VISILITY_TIMEOUT = 5; // seconds

    /**
     * Verify whether Archive button at the bottom of the convo list is visible
     * or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see Archive button at the bottom of my Contact list$
     */
    @Then("^I( do not)? see Archive button at the bottom of my Contact list$")
    public void IVerifyArchiveButtonVisibility(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            context.getPagesCollection().getPage(ContactListPage.class)
                    .waitUntilArchiveButtonIsVisible(
                            ARCHIVE_BTN_VISILITY_TIMEOUT);
        } else {
            context.getPagesCollection().getPage(ContactListPage.class)
                    .waitUntilArchiveButtonIsNotVisible(
                            ARCHIVE_BTN_VISILITY_TIMEOUT);
        }
    }

    /**
     * Verify whether missed call notification in the conversation list is present for the given
     * conversation.
     *
     * @param conversationName   name of the conversation
     * @param shouldNotBeVisible is set to null if "do not" part does not exist in the step
     * @throws Exception
     * @step. I(do not)? see missed call notification for conversation (.*)
     */
    @Then("^I( do not)? see missed call notification in the conversation list for conversation (.*)$")
    public void isCallMissedVisibleForContact(String shouldNotBeVisible,
                                              String conversationName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(
                    String.format(
                            "The call notification in conversation '%s' should be visible",
                            conversationName),
                    context.getPagesCollection().getPage(ContactListPage.class)
                            .isMissedCallVisibleForContact(conversationName));
        } else {
            assertTrue(
                    String.format(
                            "The call notification in conversation '%s' should NOT be visible",
                            conversationName),
                    context.getPagesCollection().getPage(ContactListPage.class)
                            .isMissedCallInvisibleForContact(conversationName));
        }
    }

    /**
     * Verify whether joined call notification is present for the given
     * conversation.
     *
     * @param conversationName   name of the conversation
     * @param shouldNotBeVisible is set to null if "do not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see joined group call notification for conversation
     * (.*)$
     */
    //TODO: rename to "^I( do not)? see joined group call notification in the conversation list for conversation (.*)$"
    @Then("^I( do not)? see joined group call notification for conversation (.*)$")
    public void isJoinedGroupCallNotificationVisibleForConversation(
            String shouldNotBeVisible, String conversationName)
            throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(
                    String.format(
                            "The joined group call notification in conversation '%s' should be visible",
                            conversationName),
                    context.getPagesCollection()
                            .getPage(ContactListPage.class)
                            .isJoinedGroupCallNotificationVisibleForConversation(
                                    conversationName));
        } else {
            assertTrue(
                    String.format(
                            "The joined group call notification in conversation '%s' should NOT be visible",
                            conversationName),
                    context.getPagesCollection()
                            .getPage(ContactListPage.class)
                            .isJoinedGroupCallNotificationInvisibleForConversation(
                                    conversationName));
        }
    }

    /**
     * Verify whether unjoined group call notification is present for the given
     * conversation.
     *
     * @param conversationName   name of the conversation
     * @param shouldNotBeVisible is set to null if "do not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see unjoined group call notification for conversation
     * (.*)$
     */
    //TODO: rename to "^I( do not)? see unjoined group call notification in the conversation list for conversation (.*)$"
    @Then("^I( do not)? see unjoined group call notification for conversation (.*)$")
    public void isUnjoinedGroupCallNotificationVisibleForConversation(
            String shouldNotBeVisible, String conversationName)
            throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            assertTrue(
                    String.format(
                            "The unjoined group call notification in conversation '%s' should be visible",
                            conversationName),
                    context.getPagesCollection()
                            .getPage(ContactListPage.class)
                            .isUnjoinedGroupCallNotificationVisibleForConversation(
                                    conversationName));
        } else {
            assertTrue(
                    String.format(
                            "The unjoined group call notification in conversation '%s' should NOT be visible",
                            conversationName),
                    context.getPagesCollection()
                            .getPage(ContactListPage.class)
                            .isUnjoinedGroupCallNotificationInvisibleForConversation(
                                    conversationName));
        }
    }

    /*
     * Verify if ping icon in contact list in conversation with user is colored
     * to expected accent color
     *
     * @step.
     * "^I verify ping icon in conversation with (\\w+) has (\\w+) color$"
     *
     * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
     * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
     *
     * @throws Exception
     */
    @Given("^I verify ping icon in conversation with (\\w+) has (\\w+) color$")
    public void IVerifyPingIconColor(String conversationName, String colorName)
            throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor pingIconColor = context.getPagesCollection().getPage(
                ContactListPage.class).getCurrentPingIconAccentColor(
                conversationName);
        Assert.assertEquals(expectedColor, pingIconColor);
    }

    /*
     * Verify if unread dot in contact list in conversation with user is colored
     * to expected accent color
     *
     * @step.
     * "^I verify unread dot in conversation with (\\w+) has (\\w+) color$"
     *
     * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
     * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
     *
     * @throws Exception
     */
    @Given("^I verify unread dot in conversation with (\\w+) has (\\w+) color$")
    public void IVerifyUnreadDotColor(String conversationName, String colorName)
            throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor unreadDotColor = context.getPagesCollection().getPage(
                ContactListPage.class).getCurrentUnreadDotAccentColor(
                conversationName);
        Assert.assertEquals(expectedColor, unreadDotColor);
    }

    /*
     * Verify if there is a ping icon in contact list in conversation with user
     *
     * @step. "^I see ping icon in conversation with (\\w+)"
     *
     * @throws Exception
     */
    @Given("^I see ping icon in conversation with (\\w+)")
    public void ISeePingIcon(String conversationName) throws Exception {
        conversationName = context.getUserManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue("No ping visible.",
                context.getPagesCollection().getPage(ContactListPage.class)
                        .isPingIconVisibleForConversation(conversationName));
    }

    /**
     * Verifies whether the conversation with previously remembered users is
     * selected in the conversation list
     *
     * @throws Exception
     * @step. ^I see previously remembered user selected in the conversations
     * list$
     */
    @Then("^I see previously remembered user selected in the conversations list$")
    public void ISeePreviouslyRememberedUserSelectedInConversationList()
            throws Exception {
        final List<String> selectedTopPeople = StartUIPageSteps
                .getSelectedTopPeople();
        if (selectedTopPeople != null) {
            assert selectedTopPeople.size() == 1 : "Count of selected Top People is expected to be 1";
            String oneSelectedTopPeople = selectedTopPeople.get(0);
            oneSelectedTopPeople = context.getUserManager().replaceAliasesOccurences(
                    oneSelectedTopPeople, FindBy.NAME_ALIAS);
            log.debug("Looking for contact with name " + selectedTopPeople);
            ContactListPage contactListPage = context.getPagesCollection()
                    .getPage(ContactListPage.class);
            Assert.assertTrue("No contact list loaded.",
                    contactListPage.waitForContactListVisible());
            for (int i = 0; i < 5; i++) {
                if (contactListPage
                        .isConvoListEntryWithNameExist(oneSelectedTopPeople)) {
                    return;
                }
                Thread.sleep(1000);
            }
            throw new AssertionError("Conversation list entry '"
                    + selectedTopPeople
                    + "' is not visible after timeout expired");
        } else
            throw new Error("Top People are not selected");
    }

    /**
     * Click on options button for conversation
     *
     * @param contact conversation name string
     * @throws Exception
     * @step. ^I click on options button for conversation (.*)$
     */

    @When("^I click on options button for conversation (.*)$")
    public void IClickOnOptionsButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickOptionsButtonForContact(contact);
    }

    @Then("I see a conversation option (.*) on the page$")
    public void ISeeAConversationOptionOnPage(String option) throws Throwable {
        assertThat(context.getPagesCollection().getPage(ContactListPage.class).getConvOptions().toString(), containsString(option));
    }

    /**
     * Verifies whether silence button tool tip is correct or not.
     *
     * @throws Exception
     * @step. ^I see correct tooltip for silence button in options popover$
     */
    @Then("^I see correct tooltip for silence button in options popover$")
    public void ISeeCorrectTooltipForSilenceButton() throws Exception {
        String tooltip = TOOLTIP_SILENCE + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_SILENCE_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_SILENCE_MAC;
        }
        assertThat("Silence button tooltip",
                context.getPagesCollection().getPage(ContactListPage.class)
                        .getMuteButtonToolTip(), equalTo(tooltip));
    }

    /**
     * Types shortcut combination to mute or unmute the conversation
     *
     * @param contact
     * @throws Exception
     * @step. ^I type shortcut combination to mute the conversation (.*)$
     */
    @When("^I type shortcut combination to mute or unmute the conversation (.*)$")
    public void ITypeShortcutCombinationToMuteOrUnmute(String contact)
            throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class)
                .pressShortCutToMuteOrUnmute(contact);

    }

    /**
     * Click the leave option
     *
     * @throws Exception
     * @step. ^I click the option to leave in the options popover$
     */
    @When("^I click the option to leave in the options popover$")
    public void IClickLeaveButton() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveConversation();
    }

    /**
     * Click the block option
     *
     * @throws Exception
     * @step. ^I click the option to block in the options popover$
     */
    @When("^I click the option to block in the options popover$")
    public void IClickBlockButton() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickBlockConversation();
    }

    /**
     * Verifies the modal is visible
     *
     * @throws Exception
     * @step. ^I see a leave warning modal$
     */
    @Then("^I see a leave warning modal$")
    public void ISeeALeaveWarning() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isLeaveWarningModalVisible());
    }

    /**
     * Click the cancel button
     *
     * @throws Throwable
     * @step. ^I click cancel button in the leave warning$
     */
    @Then("^I click cancel button in the leave warning$")
    public void IClickCancelButtonOnLeaveWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnLeaveWarning();
    }

    /**
     * Verifies the modal is visible
     *
     * @throws Exception
     * @step. ^I see a block warning modal$
     */
    @Then("^I see a block warning modal$")
    public void ISeeABlockWarning() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isBlockWarningModalVisible());
    }

    /**
     * Click the cancel button
     *
     * @throws Throwable
     * @step. ^I click cancel button in the block warning$
     */
    @Then("^I click cancel button in the block warning$")
    public void IClickCancelButtonOnBlockWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnBlockWarning();
    }

    /**
     * Click the block button
     *
     * @throws Throwable
     * @step. ^I click block button in the block warning$
     */
    @Then("^I click block button in the block warning$")
    public void IClickBlockButtonOnBlockWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickBlockOnBlockWarning();
    }

    /**
     * Verifies a conversation is on top of conversation list
     *
     * @param conv
     * @throws Exception
     * @step. ^I see conversation (.*) is on the top$
     */
    @When("^I see conversation (.*) is on the top$")
    public void ISeeConversationWithNameOnTop(String conv) throws Exception {
        conv = context.getUserManager().replaceAliasesOccurences(conv, FindBy.NAME_ALIAS);
        int itemIndex = context.getPagesCollection().getPage(ContactListPage.class)
                .getItemIndex(conv);
        assertThat("Conversation is not on the top", itemIndex, equalTo(1));

    }

    /**
     * Click the delete option
     *
     * @throws Exception
     * @step. ^I click delete in the options popover$
     */
    @When("^I click delete in the options popover$")
    public void IClickDeleteButton() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickDeleteConversation();
    }

    /**
     * Verifies the delete modal is visible
     *
     * @throws Exception
     * @step. ^I see a delete warning modal for group conversations$
     */
    @Then("^I see a delete warning modal for group conversations$")
    public void ISeeDeleteWarningForGroup() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isDeleteWarningModalForGroupVisible());
    }

    /**
     * Click the delete button in the delete warning
     *
     * @throws Throwable
     * @step. ^I click delete button in the delete warning$
     */
    @Then("^I click delete button in the delete warning for group conversations$")
    public void IClickDeleteButtonOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickDeleteOnDeleteWarning();
    }

    /**
     * Click the leave button in the leave warning
     *
     * @throws Throwable
     * @step. ^I click leave button in the leave warning$
     */
    @Then("^I click leave button in the leave warning$")
    public void IClickLeaveButtonOnLeaveWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveOnLeaveWarning();
    }

    /**
     * Click Leave checkbox on a delete warning modal for group conversations
     *
     * @throws Throwable
     * @step. ^I click Leave checkbox on a delete warning modal for group
     * conversations$
     */
    @Then("^I click Leave checkbox on a delete warning modal for group conversations$")
    public void IClickLeaveCheckboxOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveCheckboxOnDeleteWarning();
    }

    /**
     * Click the cancel button in the delete warning
     *
     * @throws Throwable
     * @step. ^I click cancel button in the delete warning$
     */
    @Then("^I click cancel button in the delete warning for group conversations$")
    public void IClickCancelButtonOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnDeleteWarning();
    }

    /**
     * Verifies the delete modal is visible
     *
     * @throws Exception
     * @step. ^I see a delete warning modal for 1:1 conversations$
     */
    @Then("^I see a delete warning modal for 1:1 conversations$")
    public void ISeeDeleteWarningForSingle() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isDeleteWarningModalSingleVisible());
    }

    /**
     * Click the delete button in the delete warning
     *
     * @throws Throwable
     * @step. ^I click delete button in the delete warning for 1:1 conversation$
     */
    @Then("^I click delete button in the delete warning for 1:1 conversations$")
    public void IClickDeleteButtonOnDeleteWarningForSingle() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickDeleteOnDeleteWarningSingle();
    }

    /**
     * Click the cancel button in the delete warning
     *
     * @throws Throwable
     * @step. ^I click cancel button in the delete warning for 1:1 conversation$
     */
    @Then("^I click cancel button in the delete warning for 1:1 conversations$")
    public void IClickCancelButtonOnDeleteWarningForSingle() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnDeleteWarningSingle();
    }
}
