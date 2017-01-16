package com.wearezeta.auto.web.steps;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.ImageUtil;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.ContactListPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.not;

public class ContactListPageSteps {

    private BufferedImage backgroundScreenshot = null;

    private static final Logger log = ZetaLogger.getLog(ContactListPageSteps.class.getSimpleName());

    private static final String TOOLTIP_MUTE = "Mute";
    private static final String SHORTCUT_MUTE_WIN = "(Ctrl + Alt + S)";
    private static final String SHORTCUT_MUTE_MAC = "(⌘⌥S)";

    private final WebAppTestContext context;

    public ContactListPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Given("^I verify a badge is shown on gear button$")
    public void ISeeBadgeOnSelfProfileButton() throws Exception {
        Assert.assertTrue("No badge visible.", context.getPagesCollection()
                .getPage(ContactListPage.class).waitForBadgeVisible());
    }

    @Given("I see Contact list with name (.*)")
    public void GivenISeeContactListWithName(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        log.debug("Looking for contact with name " + name);
        Assert.assertTrue("No contact list loaded.", context.getPagesCollection()
                .getPage(ContactListPage.class).waitForContactListVisible());
        assertThat("Conversation list entry '" + name + "' is not visible after timeout expired", context.getPagesCollection().
                getPage(ContactListPage.class).isConvoListEntryWithNameExist(name));
    }

    @Given("I see archive list with name (.*)")
    public void GivenISeeArchiveListWithName(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        log.debug("Looking for contact with name " + name);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);

        for (int i = 0; i < 5; i++) {
            if (contactListPage.isArchiveListEntryWithNameExist(name)) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new AssertionError("Conversation list entry '" + name
                + "' is not visible after timeout expired");
    }

    @Given("^I open conversation with (.*)")
    public void IOpenConversationWith(String conversation) throws Exception {
        conversation = context.getUsersManager().replaceAliasesOccurences(conversation, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue(String.format("Conversation with name '%s' is not visible", conversation),
                contactListPage.isConversationVisible(conversation));
        contactListPage.openConversation(conversation);
        Assert.assertTrue(String.format("Conversation '%s' should be selected", conversation),
                contactListPage.isConversationSelected(conversation));
    }

    @Then("^I see conversation with (.*) is selected in conversations list$")
    public void ISeeConversationIsSelected(String convoName) throws Exception {
        convoName = context.getUsersManager().replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation '%s' should be selected",
                convoName),
                context.getPagesCollection().getPage(ContactListPage.class)
                        .isConversationSelected(convoName));
    }

    @Given("^I unarchive conversation (.*)")
    public void GivenIUnarchiveConversation(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class)
                .unarchiveConversation(name);
    }

    @When("^I open preferences by clicking the gear button$")
    public void IOpenPreferences() throws Exception {
        assertTrue("gear button is NOT clickable",
                context.getPagesCollection().getPage(ContactListPage.class).isPreferencesButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class).openPreferences();
    }

    @When("^I archive conversation (.*)")
    public void IClickArchiveButton(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        Assert.assertTrue("Archive button is not clickable", contactListPage.isArchiveButtonClickable());
        contactListPage.clickArchiveConversation();
    }

    @When("^I type shortcut combination to archive the conversation$")
    public void ITypeShortcutCombinationToArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).pressShortcutForArchive();
    }

    @When("^I open archive$")
    public void IOpenArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openArchive();
    }

    @When("^I close archive$")
    public void ICloseArchive() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).closeArchive();
    }

    @Given("^I do not see Contact list with name (.*)$")
    public void IDoNotSeeContactListWithName(String name) throws Exception {
        name = context.getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isConvoListEntryNotVisible(name));
    }

    @When("^I( do not)? see connection request from one user$")
    public void IDoNotSeeIncomingConnection(String doNot) throws Exception {
        IDoNotSeeXIncomingConnection(doNot, 1);
    }

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

    @Given("^I open the list of incoming connection requests$")
    public void IOpenIncomingConnectionRequestsList() throws Exception {
        ContactListPage contactListPage = context.getPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue("ConnectionRequestsList is not visible", contactListPage.isConnectionRequestsListVisible());
        contactListPage.openConnectionRequestsList();
        Assert.assertTrue("ConnectionRequestList is not selected", contactListPage.isConnectionRequestsListSelected());
    }

    @When("^I open search by clicking the people button$")
    public void IOpenStartUI() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).openStartUI();
    }

    @When("^I set muted state for conversation (.*)")
    public void ISetMutedStateFor(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        Assert.assertTrue("Mute button is not clickable", contactListPage.isMuteButtonClickable());
        contactListPage.clickMuteConversation();
    }

    @When("^I set unmuted state for conversation (.*)")
    public void ISetUnmutedStateFor(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getPagesCollection()
                .getPage(ContactListPage.class);
        contactListPage.clickOptionsButtonForContact(contact);
        Assert.assertTrue("Unmute button is not clickable", contactListPage.isUnmuteButtonClickable());
        contactListPage.clickUnmuteConversation();
    }

    @When("^I see that conversation (.*) is muted$")
    public void ISeeConversationIsMuted(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Mute button NOT visible for conversation '%s'", contact), context.getPagesCollection().
                getPage(ContactListPage.class).isConversationMuted(contact));
    }

    @When("^I see that conversation (.*) is not muted$")
    public void ISeeConversationIsNotMuted(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Mute button visible for conversation '%s'", contact), context.getPagesCollection().
                getPage(ContactListPage.class).isConversationNotMuted(contact));
    }

    @Then("^I verify that (.*) index in Contact list is (\\d+)$")
    public void IVerifyContactIndex(String convoNameAlias, int expectedIndex)
            throws Exception {
        convoNameAlias = context.getUsersManager().replaceAliasesOccurences(convoNameAlias,
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

    @Then("^I( do not)? see missed call notification in the conversation list for conversation (.*)$")
    public void isCallMissedVisibleForContact(String shouldNotBeVisible,
            String conversationName) throws Exception {
        conversationName = context.getUsersManager().replaceAliasesOccurences(conversationName,
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
        conversationName = context.getUsersManager().replaceAliasesOccurences(conversationName,
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
        conversationName = context.getUsersManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        final AccentColor expectedColor = AccentColor.getByName(colorName);
        final AccentColor unreadDotColor = context.getPagesCollection().getPage(
                ContactListPage.class).getCurrentUnreadDotAccentColor(
                        conversationName);
        Assert.assertEquals(expectedColor, unreadDotColor);
    }

    /*
     * Verifies whether unread dot for given conversation is visible or not.
     *
     * @throws Exception
     */
    @Then("^I (do not )?see unread dot in conversation (\\w+)$")
    public void IVerifySeeUnreadDot(String not, String conversationName) throws Exception {
        conversationName = context.getUsersManager().replaceAliasesOccurences(conversationName, FindBy.NAME_ALIAS);
        if (not == null) {
            assertTrue(String.format("Unread dot for conversation %s is NOT visible", conversationName),
                    context.getPagesCollection().getPage(ContactListPage.class).isUnreadDotVisibleForConversation(
                            conversationName));
        } else {
            assertTrue(String.format("Unread dot for conversation %s IS visible", conversationName),
                    context.getPagesCollection().getPage(ContactListPage.class).isUnreadDotInvisibleForConversation(
                            conversationName));
        }
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
        conversationName = context.getUsersManager().replaceAliasesOccurences(conversationName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue("No ping visible.",
                context.getPagesCollection().getPage(ContactListPage.class)
                        .isPingIconVisibleForConversation(conversationName));
    }

    @Then("^I see previously remembered user selected in the conversations list$")
    public void ISeePreviouslyRememberedUserSelectedInConversationList()
            throws Exception {
        final List<String> selectedTopPeople = StartUIPageSteps
                .getSelectedTopPeople();
        if (selectedTopPeople != null) {
            assert selectedTopPeople.size() == 1 : "Count of selected Top People is expected to be 1";
            String oneSelectedTopPeople = selectedTopPeople.get(0);
            oneSelectedTopPeople = context.getUsersManager().replaceAliasesOccurences(
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
        } else {
            throw new Error("Top People are not selected");
        }
    }

    @When("^I click on options button for conversation (.*)$")
    public void IClickOnOptionsButton(String contact) throws Exception {
        contact = context.getUsersManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickOptionsButtonForContact(contact);
    }

    @Then("I see a conversation option (.*) on the page$")
    public void ISeeAConversationOptionOnPage(String option) throws Throwable {
        assertThat(context.getPagesCollection().getPage(ContactListPage.class).getConvOptions().toString(), containsString(
                option));
    }

    @Then("^I see correct tooltip for mute button in options popover$")
    public void ISeeCorrectTooltipForSilenceButton() throws Exception {
        String tooltip = TOOLTIP_MUTE + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_MUTE_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_MUTE_MAC;
        }
        assertThat("Mute button tooltip",
                context.getPagesCollection().getPage(ContactListPage.class)
                        .getMuteButtonToolTip(), equalTo(tooltip));
    }

    @When("^I type shortcut combination to mute or unmute$")
    public void ITypeShortcutCombinationToMuteOrUnmute() throws Exception {
        context.getPagesCollection().getPage(ContactListPage.class).pressShortCutToMuteOrUnmute();

    }

    @When("^I click the option to mute in the options popover$")
    public void IClickMuteButton() throws Exception {
        Assert.assertTrue("Mute button is not clickable", context.getPagesCollection().getPage(ContactListPage.class)
                .isMuteButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickMuteConversation();
    }

    @When("^I click the option to leave in the options popover$")
    public void IClickLeaveButton() throws Exception {
        Assert.assertTrue("Leave button is not clickable", context.getPagesCollection().getPage(ContactListPage.class)
                .isLeaveButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveConversation();
    }

    @When("^I click the option to block in the options popover$")
    public void IClickBlockButton() throws Exception {
        Assert.assertTrue("Block button is not shown in the option popover",
                context.getPagesCollection().getPage(ContactListPage.class).isBlockButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class).clickBlockButton();
    }

    @Then("^I see a leave warning modal$")
    public void ISeeALeaveWarning() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isLeaveWarningModalVisible());
    }

    @Then("^I click cancel button in the leave warning$")
    public void IClickCancelButtonOnLeaveWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnLeaveWarning();
    }

    @Then("^I see a block warning modal$")
    public void ISeeABlockWarning() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isBlockWarningModalVisible());
    }

    @Then("^I click cancel button in the block warning$")
    public void IClickCancelButtonOnBlockWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnBlockWarning();
    }

    @Then("^I click block button in the block warning$")
    public void IClickBlockButtonOnBlockWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickBlockOnBlockWarning();
    }

    @When("^I see conversation (.*) is on the top$")
    public void ISeeConversationWithNameOnTop(String conv) throws Exception {
        conv = context.getUsersManager().replaceAliasesOccurences(conv, FindBy.NAME_ALIAS);
        int itemIndex = context.getPagesCollection().getPage(ContactListPage.class)
                .getItemIndex(conv);
        assertThat("Conversation is not on the top", itemIndex, equalTo(1));

    }

    @When("^I click delete in the options popover$")
    public void IClickDeleteButton() throws Exception {
        Assert.assertTrue("Delete button is not shown in the option popover",
                context.getPagesCollection().getPage(ContactListPage.class).isDeleteButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class).clickDeleteConversation();
    }

    @Then("^I see a delete warning modal for group conversations$")
    public void ISeeDeleteWarningForGroup() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isDeleteWarningModalForGroupVisible());
    }

    @Then("^I click delete button in the delete warning for group conversations$")
    public void IClickDeleteButtonOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickDeleteOnDeleteWarning();
    }

    @Then("^I click leave button in the leave warning$")
    public void IClickLeaveButtonOnLeaveWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveOnLeaveWarning();
    }

    @Then("^I click Leave checkbox on a delete warning modal for group conversations$")
    public void IClickLeaveCheckboxOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickLeaveCheckboxOnDeleteWarning();
    }

    @Then("^I click cancel button in the delete warning for group conversations$")
    public void IClickCancelButtonOnDeleteWarning() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnDeleteWarning();
    }

    @Then("^I see a delete warning modal for 1:1 conversations$")
    public void ISeeDeleteWarningForSingle() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                .isDeleteWarningModalSingleVisible());
    }

    @Then("^I click delete button in the delete warning for 1:1 conversations$")
    public void IClickDeleteButtonOnDeleteWarningForSingle() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickDeleteOnDeleteWarningSingle();
    }

    @Then("^I click cancel button in the delete warning for 1:1 conversations$")
    public void IClickCancelButtonOnDeleteWarningForSingle() throws Throwable {
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelOnDeleteWarningSingle();
    }

    @When("^I click cancel request in the options popover$")
    public void IClickCancelRequestButton() throws Exception {
        Assert.assertTrue("Cancel request button is not shown in the option popover",
                context.getPagesCollection().getPage(ContactListPage.class).isCancelRequestButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickCancelRequest();
    }

    @When("^I click archive in the options popover$")
    public void IClickArchiveButton() throws Exception {
        Assert.assertTrue("Archive button is not shown in the option popover",
                context.getPagesCollection().getPage(ContactListPage.class).isArchiveButtonClickable());
        context.getPagesCollection().getPage(ContactListPage.class)
                .clickArchiveConversation();
    }

    @When("^I see unread number (\\d+) in page title$")
    public void ISeeUnreadNumberInPageTitle(Integer number) throws Exception {
        String pageTitle = context.getPagesCollection().getPage(ContactListPage.class).getPageTitle();
        String unreadNumber = "(" + number + ")";
        assertThat("Unread number is not visible in page title", pageTitle, containsString(unreadNumber));
    }

    @Then("^I do not see unread number in page title$")
    public void IDoNotSeeUnreadNumberInPageTitle() throws Exception {
        String pageTitle = context.getPagesCollection().getPage(ContactListPage.class).getPageTitle();
        assertThat("Part of unread number is visible", pageTitle, not(containsString("(")));
    }

    @When("^I remember the background image of the conversation list$")
    public void IRememberBigProfileImage() throws Exception {
        backgroundScreenshot = context.getPagesCollection().getPage(ContactListPage.class).getBackgroundPicture();
    }

    @Then("^I verify that the background image of the conversation list has( not)? changed$")
    public void IVerifyBackgroundImageHasChanged(String not) throws Exception {
        final int THRESHOLD = 120;

        if (not == null) {
            ContactListPage contactListPage = context.getPagesCollection().getPage(ContactListPage.class);

            Wait<ContactListPage> wait = new FluentWait<>(contactListPage)
                    .withTimeout(15, TimeUnit.SECONDS)
                    .pollingEvery(4, TimeUnit.SECONDS)
                    .ignoring(AssertionError.class);

            wait.until(page -> {
                int actualMatch = THRESHOLD + 1;
                try {
                    actualMatch = ImageUtil.getMatches(page.getBackgroundPicture(), backgroundScreenshot);
                } catch (Exception e) {
                }
                assertThat("Image has not changed", actualMatch, lessThan(THRESHOLD));
                return actualMatch;
            });
        } else {
            BufferedImage actualPicture = context.getPagesCollection().getPage(ContactListPage.class).getBackgroundPicture();
            assertThat("Image has changed", ImageUtil.getMatches(actualPicture, backgroundScreenshot), greaterThan(THRESHOLD));
        }
    }
}
