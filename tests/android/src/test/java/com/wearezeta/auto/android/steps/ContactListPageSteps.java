package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verify whether conversations list is visible or not
     *
     * @throws Exception
     * @step. ^I see Contact list with (no )?contacts$
     */
    @Given("^I see Contact list with (no )?contacts$")
    public void GivenISeeContactList(String shouldNotBeVisible)
            throws Exception {
        getContactListPage().verifyContactListIsFullyLoaded();
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(
                    "No conversations are visible in the conversations list, but some are expected",
                    getContactListPage().isAnyConversationVisible());
        } else {
            Assert.assertTrue(
                    "Some conversations are visible in the conversations list, but zero is expected",
                    getContactListPage().isNoConversationsVisible());

        }
    }

    /**
     * Verify whether conversations list is visible
     *
     * @throws Exception
     * @step. ^I see Contact list$
     */
    @Given("^I see Contact list$")
    public void GivenISeeContactList() throws Exception {
        getContactListPage().verifyContactListIsFullyLoaded();
    }

    /**
     * Taps on a given contact name
     *
     * @param contactName the contact to tap on
     * @throws Exception
     * @step. ^I tap on contact name (.*)$
     */
    @When("^I tap on contact name (.*)$")
    public void WhenITapOnContactName(String contactName) throws Exception {
        try {
            contactName = usrMgr.findUserByNameOrNameAlias(contactName)
                    .getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getContactListPage().tapOnName(contactName);
    }

    /**
     * Taps on the currently logged-in user's avatar
     *
     * @throws Exception
     * @step. ^I tap on my avatar$
     */
    @When("^I tap on my avatar$")
    public void WhenITapOnMyAvatar() throws Exception {
        getContactListPage().tapOnMyAvatar();
    }

    /**
     * Swipes down on the contact list to return the search list page
     *
     * @throws Exception
     * @step. ^I swipe down contact list$
     */
    @When("^I swipe down contact list$")
    public void ISwipeDownContactList() throws Exception {
        getContactListPage().doLongSwipeDown();
    }

    /**
     * Swipes right on a contact to archive the conversation with that contact
     * (Perhaps this step should only half swipe to reveal the archive button,
     * since the "I swipe archive conversation" step exists)
     *
     * @param contact the contact or conversation name on which to swipe
     * @throws Exception
     * @step. ^I swipe right on a (.*)$
     */
    @When("^I swipe right on a (.*)$")
    public void ISwipeRightOnContact(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently - seems bad...
        }
        getContactListPage().swipeRightOnConversation(1000, contact);
    }

    /**
     * Makes a short swipe on teh contact to get the 3 dots for the option menu
     *
     * @param contact to swipe on
     * @throws Exception
     * @step. ^I short swipe right on a (.*)
     */
    @When("^I short swipe right on a (.*)$")
    public void IShortSwipeRightOnAUser(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getContactListPage().swipeShortRightOnConversation(1000, contact);

    }

    /**
     * Presses on search bar in the conversation List to open search (people
     * picker)
     *
     * @throws Exception
     * @step. ^I open [Ss]earch by tap$
     */
    @When("^I open [Ss]earch by tap")
    public void WhenITapOnSearchBox() throws Exception {
        getContactListPage().tapOnSearchBox();
    }

    /**
     * Swipes up on the contact list to reveal archived conversations
     *
     * @throws Exception
     * @step. ^I swipe up contact list$
     */
    @When("^I swipe up contact list$")
    public void ISwipeUpContactList() throws Exception {
        getContactListPage().doLongSwipeUp();
    }

    /**
     * Asserts that two given contact names exist in the conversation list
     * (should this maybe be set to a list?)
     *
     * @param contact1 The first contact to check in the conversation list
     * @param contact2 The second contact to check in the conversation list
     * @throws Exception
     * @step. ^I see (.*) and (.*) chat in contact list$
     */
    @Then("^I see (.*) and (.*) chat in contact list$")
    public void ISeeGroupChatInContactList(String contact1, String contact2)
            throws Exception {
        contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
        contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
        // FIXME: the step should be more universal
        Assert.assertTrue(getContactListPage().isContactExists(
                contact1 + ", " + contact2)
                || getContactListPage().isContactExists(
                contact2 + ", " + contact1));
    }

    /**
     * Check to see that a given username appears in the contact list
     *
     * @param userName     the username to check for in the contact list
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see contact list with name (.*)$
     */
    @Then("^I( do not)? see contact list with name (.*)$")
    public void ISeeUserNameInContactList(String shouldNotSee, String userName)
            throws Exception {
        try {
            userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getContactListPage().verifyContactListIsFullyLoaded();
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The conversation '%s' is not visible in the list",
                    userName), getContactListPage().isContactExists(userName));
        } else {
            Assert.assertTrue(String.format("The conversation '%s' is  visible in the list, but should be hidden",
                    userName), getContactListPage().waitUntilContactDisappears(
                    userName));
        }
    }

    /**
     * Checks to see that the muted symbol appears or not for the given contact.
     *
     * @param contact
     * @param shouldNotBeMuted is set to null if 'not' part does not exist
     * @throws Exception
     * @step. "^Contact (.*) is (not )?muted$
     */
    @Then("^Contact (.*) is (not )?muted$")
    public void ContactIsMutedOrNot(String contact, String shouldNotBeMuted)
            throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        if (shouldNotBeMuted == null) {
            Assert.assertTrue(
                    String.format(
                            "The conversation '%s' is supposed to be muted, but it is not",
                            contact),
                    getContactListPage().isContactMuted(contact));
        } else {
            Assert.assertTrue(
                    String.format(
                            "The conversation '%s' is supposed to be not muted, but it is",
                            contact), getContactListPage()
                            .waitUntilContactNotMuted(contact));
        }
    }

    private BufferedImage previousPlayPauseBtnState = null;

    /**
     * Save the current state of PlayPause button into the internal data
     * structure
     *
     * @param convoName conversation name for which the screenshot should be taken
     * @throws Exception
     * @step. ^I remember the state of PlayPause button next to the (.*)
     * conversation$
     */
    @When("^I remember the state of PlayPause button next to the (.*) conversation$")
    public void IRememberTheStateOfPlayPauseButton(String convoName)
            throws Exception {
        convoName = usrMgr.findUserByNameOrNameAlias(convoName).getName();
        previousPlayPauseBtnState = getContactListPage()
                .getScreenshotOfPlayPauseButtonNextTo(convoName).orElseThrow(
                        IllegalStateException::new);
    }

    private final static double MAX_SIMILARITY_THRESHOLD = 0.6;
    private final static int STATE_CHANGE_TIMEOUT_SECONDS = 5;

    /**
     * Verify whether the current screenshot of PlayPause button is different
     * from the previous one
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I see the state of PlayPause button next to the (.*) conversation
     * is changed$
     */
    @Then("^I see the state of PlayPause button next to the (.*) conversation is changed$")
    public void ISeeThePlayPauseButtonStateIsChanged(String convoName)
            throws Exception {
        if (previousPlayPauseBtnState == null) {
            throw new IllegalStateException(
                    "Please take a screenshot of previous button state first");
        }
        convoName = usrMgr.findUserByNameOrNameAlias(convoName).getName();
        final long millisecondsStarted = System.currentTimeMillis();
        double score = 1;
        while (System.currentTimeMillis() - millisecondsStarted <= STATE_CHANGE_TIMEOUT_SECONDS * 1000) {
            final BufferedImage currentPlayPauseBtnState = getContactListPage()
                    .getScreenshotOfPlayPauseButtonNextTo(convoName)
                    .orElseThrow(IllegalStateException::new);
            score = ImageUtil.getOverlapScore(currentPlayPauseBtnState,
                    previousPlayPauseBtnState, ImageUtil.RESIZE_TO_MAX_SCORE);
            if (score < MAX_SIMILARITY_THRESHOLD) {
                break;
            }
            Thread.sleep(500);
        }
        Assert.assertTrue(
                String.format(
                        "The current and previous states of PlayPause button seems to be very similar after %d seconds (%.2f >= %.2f)",
                        STATE_CHANGE_TIMEOUT_SECONDS, score,
                        MAX_SIMILARITY_THRESHOLD),
                score < MAX_SIMILARITY_THRESHOLD);
    }

    /**
     * Verify that Play/Pause media content button is visible in Conversation
     * List
     *
     * @param convoName conversation name for which button presence is checked
     * @step. ^I see PlayPause media content button for conversation (.*)
     */
    @Then("^I see PlayPause media content button for conversation (.*)")
    public void ThenISeePlayPauseButtonForConvo(String convoName)
            throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(getContactListPage().isPlayPauseMediaButtonVisible(
                convoName));
    }

    /**
     * Tap PlayPause button next to the particular conversation name
     *
     * @param convoName conversation name/alias
     * @throws Exception
     * @step. ^I tap PlayPause button next to the (.*) conversation$
     */
    @When("^I tap PlayPause button next to the (.*) conversation$")
    public void ITapPlayPauseButton(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName,
                FindBy.NAME_ALIAS);
        getContactListPage().tapPlayPauseMediaButton(convoName);
    }

    /**
     * Open Search by clicking the Search button in the right top corner of
     * convo list
     *
     * @throws Exception
     * @step. ^I open Search by UI button$
     */
    @When("^I open Search by UI button$")
    public void IOpenPeoplePicker() throws Exception {
        getContactListPage().tapOnSearchButton();
    }

    /**
     * Tap the corresponding item in conversation settings menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I select (.*) From conversation settings menu$
     */
    @And("^I select (.*) from conversation settings menu$")
    public void ISelectConvoSettingsMenuItem(String itemName) throws Exception {
        getContactListPage().selectConvoSettingsMenuItem(itemName);
    }

    private Map<String, BufferedImage> previousUnreadIndicatorState = new HashMap<>();

    /**
     * Save the state of conversation idicator into the internal field for the
     * future comparison
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I remember unread messages indicator state for conversation (.*)
     */
    @When("^I remember unread messages indicator state for conversation (.*)")
    public void IRememberUnreadIndicatorState(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        this.previousUnreadIndicatorState.put(name,
                getContactListPage().getMessageIndicatorScreenshot(name)
                        .orElseThrow(IllegalStateException::new));
    }

    private static final double MAX_UNREAD_DOT_SIMILARITY_THRESHOLD = 0.97;

    /**
     * Verify whether unread dot state is changed for the particular
     * conversation in comparison to the previous state
     *
     * @param name conversation name/alias
     * @throws Exception
     * @step. ^I see unread messages indicator state is changed for conversation
     * (.*)"
     */
    @Then("^I see unread messages indicator state is changed for conversation (.*)")
    public void ISeeUnreadIndicatorStateIsChanged(String name)
            throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        if (!this.previousUnreadIndicatorState.containsKey(name)) {
            throw new IllegalStateException(
                    String.format(
                            "Please invoke the correspoding step to make a screenshot of previous state of '%s' conversation",
                            name));
        }
        final int maxTries = 3;
        int ntry = 1;
        double score = 1;
        do {
            final BufferedImage currentIndicatorState = getContactListPage()
                    .getMessageIndicatorScreenshot(name).orElseThrow(
                            IllegalStateException::new);
            score = ImageUtil.getOverlapScore(
                    this.previousUnreadIndicatorState.get(name),
                    currentIndicatorState, ImageUtil.RESIZE_NORESIZE);
            if (score < MAX_UNREAD_DOT_SIMILARITY_THRESHOLD) {
                break;
            }
            Thread.sleep(500);
            ntry++;
        } while (ntry <= maxTries);
        Assert.assertTrue(
                String.format(
                        "The current and previous states of Unread Dot seems to be very similar (%.2f >= %.2f)",
                        score, MAX_UNREAD_DOT_SIMILARITY_THRESHOLD),
                score < MAX_UNREAD_DOT_SIMILARITY_THRESHOLD);
    }

    /**
     * Presses Delete on the delete confirmation alert
     *
     * @throws Exception
     * @step. ^I press DELETE on the confirm alert$
     */
    @When("^I press DELETE on the confirm alert$")
    public void IPressDELETEOnTheConfirmAlert() throws Exception {
        getContactListPage().confirmDeleteConversationAlert();
    }

    /**
     * Presses the leave as well during delete checkbox
     *
     * @throws Exception
     * @step. ^I click the Leave check box$
     */
    @When("^I click the Leave check box$")
    public void IClickLeave() throws Exception {
        getContactListPage().checkLeaveWhileDeleteCheckbox();
    }

    /**
     * Verifies that specific items are visible in the conversation settings menu
     *
     * @param name name of item to check in the menu (ARCHIVE, BLOCK, CANCEL,...)
     * @throws Exception
     * @step. ^I see (.*) button in conversation settings menu$
     */
    @Then("^I see (.*) button in conversation settings menu$")
    public void ISeeButtonInConversationSettingsMenuAtPosition(String name) throws Exception {
        Assert.assertTrue("The converastion settings menu item is not visible",
                getContactListPage().isConvSettingsMenuItemVisible(name));
    }


    /**
     * Tap the Invite Other People button at the bottom of conversations list
     *
     * @throws Exception
     * @step. ^I tap Invite button at the bottom of conversations list$
     */
    @When("^I tap Invite button at the bottom of conversations list$")
    public void ITapInviteButton() throws Exception {
        getContactListPage().tapInviteButton();
    }

    /**
     * Verifies if Leave check box is visible or not
     *
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see the Leave check box$
     */
    @Then("^I( do not)? see the Leave check box$")
    public void ISeeTheLeaveCheckBox(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(getContactListPage().isLeaveCheckBoxVisible());
        } else {
            Assert.assertFalse(getContactListPage().isLeaveCheckBoxVisible());
        }
    }

    /**
     * Verifies that the three dot button to open the option menu is visible
     *
     * @throws Exception
     * @step. ^I see three dots option menu button$
     */
    @When("^I see three dots option menu button$")
    public void ISeeThreeDotsOptionMenuButton() throws Exception {
        Assert.assertTrue("Three dot button is not visible",
                getContactListPage().isThreeDotButtonVisible());
    }

    /**
     * Taps the three dot button to open the option menu
     *
     * @throws Exception
     * @step. ^I press the three dots option menu button$
     */
    @When("^I press the three dots option menu button$")
    public void IPressTheThreeDotsOptionMenuButton() throws Exception {
        getContactListPage().tapThreeDotOptionMenuButton();
    }

}