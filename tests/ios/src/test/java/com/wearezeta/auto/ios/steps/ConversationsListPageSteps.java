package com.wearezeta.auto.ios.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.pages.*;

public class ConversationsListPageSteps {
    private ConversationsListPage getConversationsListPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ConversationsListPage.class);
    }

    private LoginPage getLoginPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(LoginPage.class);
    }

    @Given("^I see conversations list$")
    public void GivenISeeConversationsList() throws Exception {
        Assert.assertTrue("Conversations list is not visible after the timeout", getLoginPage().isContactsButtonVisible());
    }

    private Map<String, ElementState> savedConvoItemStates = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param nameAlias conversation name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) conversation item$
     */
    @When("^I remember the state of (.*) conversation item$")
    public void IRememberConvoItemState(String nameAlias) throws Exception {
        final String name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        this.savedConvoItemStates.put(name,
                new ElementState(() -> getConversationsListPage().getConversationEntryScreenshot(name)).remember()
        );
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param nameAlias          conversation name/alias
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of (.*) conversation item is (not )?changed$"
     */
    @Then("^I see the state of (.*) conversation item is (not )?changed$")
    public void IVerifyConvoState(String nameAlias, String shouldNotBeChanged) throws Exception {
        final String name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        if (!this.savedConvoItemStates.containsKey(name)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of '%s' conversation entry first", name));
        }
        final double minScore = 0.999;
        final int timeoutSeconds = 10;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(String.format("The state of '%s' conversation item seems to be the same", name),
                    this.savedConvoItemStates.get(name).isChanged(Timedelta.fromSeconds(timeoutSeconds), minScore));
        } else {
            Assert.assertTrue(String.format("The state of '%s' conversation item seems to be changed", name),
                    this.savedConvoItemStates.get(name).isNotChanged(Timedelta.fromSeconds(timeoutSeconds), minScore));
        }
    }

    private Map<Integer, ElementState> savedConvoItemStatesByIdx = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param convoIdx conversation index, starts from 1
     * @throws Exception
     * @step. ^I remember the state of conversation item number (\\d+)$
     */
    @When("^I remember the state of conversation item number (\\d+)$")
    public void IRememberConvoItemStateByIdx(int convoIdx) throws Exception {
        this.savedConvoItemStatesByIdx.put(convoIdx,
                new ElementState(() -> getConversationsListPage().getConversationEntryScreenshot(convoIdx)).remember()
        );
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param convoIdx           conversation index, starts from 1
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of conversation item number (\\d+) is (not )?changed$"
     */
    @Then("^I see the state of conversation item number (\\d+) is (not )?changed$")
    public void IVerifyConvoStateByIdx(int convoIdx, String shouldNotBeChanged) throws Exception {
        if (!this.savedConvoItemStatesByIdx.containsKey(convoIdx)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of conversation entry number %s first", convoIdx));
        }
        final double minScore = 0.97;
        final int timeoutSeconds = 10;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(String.format("The state of conversation item number %s item seems to be the same", convoIdx),
                    this.savedConvoItemStatesByIdx.get(convoIdx).isChanged(Timedelta.fromSeconds(timeoutSeconds),
                            minScore));
        } else {
            Assert.assertTrue(String.format("The state of conversation item number %s item seems to be changed", convoIdx),
                    this.savedConvoItemStatesByIdx.get(convoIdx).isNotChanged(Timedelta.fromSeconds(timeoutSeconds),
                            minScore));
        }
    }

    /**
     * Click MAYBE LATER on settings warning screen
     *
     * @throws Exception
     * @param ignoreIfInvisible equals to null if the warning should not be ignored if it was
     *                          not shown
     * @step. ^I dismiss settings warning( if visible)?$
     */
    @When("^I dismiss settings warning( if visible)?$")
    public void IDismissSettingsWarning(String ignoreIfInvisible) throws Exception {
        if (ignoreIfInvisible == null) {
            getLoginPage().dismissSettingsWarning();
        } else {
            getLoginPage().dismissSettingsWarningIfVisible();
        }
    }

    /**
     * Tap the settings gear button
     *
     * @throws Exception
     * @step. ^I tap settings gear button$
     */
    @When("^I tap settings gear button$")
    public void ITapSettingsGear() throws Exception {
        getConversationsListPage().tapSettingsGearButton();
    }

    @When("^I tap on contact name (.*)")
    public void WhenITapOnContactName(String name) throws Exception {
        name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getConversationsListPage().tapOnName(name);
    }

    /**
     * Tap conversation list item by its index
     *
     * @param idx convo index in list, starts with 1
     * @throws Exception
     * @step. ^I tap on conversation item number (\d+)$
     */
    @When("^I tap on conversation item number (\\d+)$")
    public void WhenITapOnConvoByIdx(int idx) throws Exception {
        getConversationsListPage().tapConvoItemByIdx(idx);
    }

    @When("^I tap on group chat with name (.*)")
    public void WhenITapOnGroupChatName(String chatName) throws Exception {
        WhenITapOnContactName(chatName);
    }

    /**
     * Open search by taping Contacts button
     *
     * @throws Exception
     * @step. ^I open search UI$
     */
    @When("^I open search UI$")
    public void IOpenSearchByTap() throws Exception {
        getConversationsListPage().tapContactsButton();
    }

    private final static Timedelta CONVO_LIST_UPDATE_TIMEOUT = Timedelta.fromSeconds(10);

    /**
     * Verify whether the first items in conversations list is the given item
     *
     * @param convoName conversation name
     * @throws Exception
     * @step. ^I see first item in contact list named (.*)
     */
    @Then("^I see first item in contact list named (.*)")
    public void ISeeUserNameFirstInContactList(String convoName) throws Exception {
        convoName = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        final Timedelta started = Timedelta.now();
        do {
            Thread.sleep(500);
            if (getConversationsListPage().isFirstConversationName(convoName)) {
                return;
            }
        } while (Timedelta.now().isDiffLessOrEqual(started, CONVO_LIST_UPDATE_TIMEOUT));
        throw new AssertionError(
                String.format("The conversation '%s' is not the first conversation in the list after " +
                        "%s timeout", convoName, CONVO_LIST_UPDATE_TIMEOUT.toString()));

    }

    /**
     * verifies the visibility of a specific item in the conversations list
     *
     * @param shouldNotSee equals to null if the item should be visible
     * @param value        conversation name/alias
     * @throws Exception
     * @step. ^I (do not )?see conversation (.*) in conversations list$
     */
    @Then("^I (do not )?see conversation (.*) in conversations list$")
    public void ISeeUserInContactList(String shouldNotSee, String value) throws Exception {
        value = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(value, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The conversation '%s' is not visible in the conversation list",
                    value), getConversationsListPage().isConversationInList(value));
        } else {
            Assert.assertTrue(
                    String.format("The conversation '%s' is visible in the conversation list, but should be hidden",
                            value), getConversationsListPage().isConversationNotInList(value));
        }
    }

    /**
     * Verifies the visibility of a specific item in the conversations list
     *
     * @param timeoutSeconds equals to null if the item should be visible
     * @param convoName      conversation name/alias
     * @param expectedState  either 'appears in' or 'disappears from'
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until conversation (.*) (appears in|disappears from) the list$
     */
    @Then("^I wait up to (\\d+) seconds? until conversation (.*) (appears in|disappears from) the list$")
    public void IWaitForConvo(int timeoutSeconds, String convoName, String expectedState) throws Exception {
        convoName = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        if (expectedState.equals("appears in")) {
            Assert.assertTrue(String.format("The conversation '%s' is not visible in the conversation list",
                    convoName), getConversationsListPage().isConversationInList(convoName,
                    Timedelta.fromSeconds(timeoutSeconds)));
        } else {
            Assert.assertTrue(
                    String.format("The conversation '%s' is still visible in the conversation list, but should be hidden",
                            convoName), getConversationsListPage().isConversationNotInList(convoName,
                            Timedelta.fromSeconds(timeoutSeconds)));
        }
    }

    @When("^I swipe right on a (.*)$")
    public void ISwipeRightOnContact(String contact) throws Exception {
        contact = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        getConversationsListPage().swipeRightConversationToRevealActionButtons(contact);
    }

    @Then("^I open archived conversations$")
    public void IOpenArchivedConversations() throws Exception {
        getConversationsListPage().openArchivedConversations();
    }

    /**
     * Verify whether Archive button is visible at the bottom of conversations list
     *
     * @param shouldNotSee equals to null if Archive button should be visible
     * @throws Exception
     * @step. ^I (do not )?see Archive button at the bottom of conversations list$
     */
    @Then("^I (do not )?see Archive button at the bottom of conversations list$")
    public void ISeeArchiveButton(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Archive button should be visible, but it's hidden",
                    getConversationsListPage().isArchiveButtonVisible());
        } else {
            Assert.assertTrue("Archive button should be invisible, but it's visible",
                    getConversationsListPage().isArchiveButtonInvisible());
        }
    }

    /**
     * Verify whether Contacts label is visible at the bottom of conversations list
     *
     * @param shouldNotSee equals to null if Contacts label should be visible
     * @throws Exception
     * @step. ^I (do not )?see Archive button at the bottom of conversations list$
     */
    @Then("^I (do not )?see Contacts label at the bottom of conversations list$")
    public void ISeeContactsLabel(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Contacts label should be visible, but it's hidden",
                    getConversationsListPage().contactsLabelIsVisible());
        } else {
            Assert.assertTrue("Contacts label should be invisible, but it's visible",
                    getConversationsListPage().contactLabelIsNotVisible());
        }
    }

    /**
     * Verify visibility of NO ACTIVE CONVERSATIONS message in conversation list
     *
     * @throws Exception
     * @step. ^I see NO ACTIVE CONVERSATIONS message in conversations list$
     */
    @Then("^I see NO ACTIVE CONVERSATIONS message in conversations list$")
    public void ISeeNoConversationMessage() throws Exception {
        Assert.assertTrue("NO ACTIVE CONVERSATION message is not visible",
                getConversationsListPage().noConversationsMessageIsVisible());
    }

    @When("^I tap (?:Play|Pause) button in conversations list next to (.*)")
    public void ITapPlayPauseButtonInContactListNextTo(String contact) throws Exception {
        String name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .findUserByNameOrNameAlias(contact).getName();
        getConversationsListPage().tapPlayPauseButtonNextTo(name);
    }

    @Then("^I (do not )?see (?:Play|Pause) button in conversations list next to (.*)")
    public void ISeePlayPauseButtonInContactListNextTo(String shouldNotBeVisible, String contact) throws Exception {
        String name = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .findUserByNameOrNameAlias(contact).getName();
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("Play/Pause button is expected to be visible next to '%s' conversation",
                    contact), getConversationsListPage().isPlayPauseButtonVisibleNextTo(name));
        } else {
            Assert.assertTrue(String.format("Play/Pause button is expected to be invisible next to '%s' conversation",
                    contact), getConversationsListPage().isPlayPauseButtonInvisibleNextTo(name));
        }
    }

    @When("I tap Incoming Pending Requests item in conversations list")
    public void ITapPendingRequestLinkContactList() throws Exception {
        getConversationsListPage().tapPendingRequest();
    }

    @When("I (do not )?see Pending request link in conversations list")
    public void ISeePendingRequestLinkInContacts(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Pending request link is not in conversations list",
                    getConversationsListPage().isPendingRequestInContactList());
        } else {
            Assert.assertTrue("Pending request link is shown in conversations list",
                    getConversationsListPage().pendingRequestInContactListIsNotShown());
        }
    }

    @When("I (don't )?see in conversations list group chat with (.*)")
    public void ISeeInContactsGroupChatWith(String shouldNotSee, String participantNameAliases) throws Exception {
        participantNameAliases = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(participantNameAliases,
                ClientUsersManager.FindBy.NAME_ALIAS);
        final List<String> participantNames = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .splitAliases(participantNameAliases);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("There is no conversation with '%s' in the list", participantNames),
                    getConversationsListPage().isConversationWithUsersExist(participantNames,
                            Timedelta.fromSeconds(5)));
        } else {
            Assert.assertFalse(String.format("There is conversation with '%s' in the list, which should be hidden",
                    participantNames), getConversationsListPage().isConversationWithUsersExist(participantNames,
                    Timedelta.fromSeconds(2)));
        }
    }

    private ElementState previousSettingsGearState = new ElementState(
            () -> getConversationsListPage().getSettingsGearStateScreenshot()
    );

    /**
     * Remember the current state of settings gear
     *
     * @throws Exception
     * @step. ^I remember the state of settings gear$
     */
    @When("^I remember the state of settings gear$")
    public void IRememberGearState() throws Exception {
        previousSettingsGearState.remember();
    }

    /**
     * Verify whether settings gear state is changed within the timeout
     *
     * @param shouldNotChange equals to null if the state should be changed
     * @throws Exception
     * @step. ^I wait until settings gear is (not )?changed$
     */
    @Then("^I wait until settings gear is (not )?changed$")
    public void IWaitUntilGearIsChanged(String shouldNotChange) throws Exception {
        if (previousSettingsGearState == null) {
            throw new IllegalStateException("Please take the initial screenshot of settings gear first");
        }
        final int timeoutSeconds = 10;
        final double minScore = 0.97;
        final Timedelta timeout = Timedelta.fromSeconds(timeoutSeconds);
         if (shouldNotChange == null) {
            Assert.assertTrue(String.format("The previous and the current state of settings gear " +
                            "icon seems to be equal after %s", timeout),
                    previousSettingsGearState.isChanged(timeout, minScore));
        } else {
            Assert.assertTrue(String.format("The previous and the current state of settings gear " +
                            "icon seems to be different after %s", timeout),
                    previousSettingsGearState.isNotChanged(timeout, minScore));
        }
    }

    /**
     * Tap close button on Archive page
     *
     * @throws Exception
     * @step. ^I tap close Archive page button$
     */
    @When("^I tap close Archive page button$")
    public void IClickCloseArchivePageButton() throws Exception {
        getConversationsListPage().clickCloseArchivePageButton();
    }


    /**
     * Verify visibility of Conversations hint text
     *
     * @param shouldNotBeVisible equals to null if the button should not be visible
     * @throws Exception
     * @step. ^I (do not )?see Conversations hint text$
     */
    @When("^I (do not )?see Conversations hint text$")
    public void ISeeHintText(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Conversations hint is not shown",
                    getConversationsListPage().hintTextIsVisible());
        } else {
            Assert.assertTrue("Conversations hint is is shown",
                    getConversationsListPage().hintTextIsNotVisible());
        }
    }

    /**
     * Tap on Conversations hint label
     *
     * @throws Exception
     * @step. ^I tap on Conversations hint text$
     */
    @When("^I tap on Conversations hint text$")
    public void ITapHintText() throws Exception {
        getConversationsListPage().tapHintText();
    }
}
