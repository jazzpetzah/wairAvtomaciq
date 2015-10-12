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
	 * @step. ^I see Contact list with (no )?contacts$
	 * 
	 * @throws Exception
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
	 * @step. ^I see Contact list$
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list$")
	public void GivenISeeContactList() throws Exception {
		getContactListPage().verifyContactListIsFullyLoaded();
	}

	/**
	 * Taps on a given contact name
	 * 
	 * @step. ^I tap on contact name (.*)$
	 * @param contactName
	 *            the contact to tap on
	 * @throws Exception
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
	 * @step. ^I tap on my avatar$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on my avatar$")
	public void WhenITapOnMyAvatar() throws Exception {
		getContactListPage().tapOnMyAvatar();
	}

	/**
	 * Swipes down on the contact list to return the search list page
	 * 
	 * @step. ^I swipe down contact list$
	 * @throws Exception
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
	 * @step. ^I swipe right on a (.*)$
	 * @param contact
	 *            the contact or conversation name on which to swipe
	 * @throws Exception
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
	 * Presses on search bar in the conversation List to open search (people
	 * picker)
	 * 
	 * @step. ^I open [Ss]earch by tap$
	 * @throws Exception
	 */
	@When("^I open [Ss]earch by tap")
	public void WhenITapOnSearchBox() throws Exception {
		getContactListPage().tapOnSearchBox();
	}

	/**
	 * Swipes up on the contact list to reveal archived conversations
	 * 
	 * @step. ^I swipe up contact list$
	 * @throws Exception
	 */
	@When("^I swipe up contact list$")
	public void ISwipeUpContactList() throws Exception {
		getContactListPage().doLongSwipeUp();
	}

	/**
	 * Asserts that two given contact names exist in the conversation list
	 * (should this maybe be set to a list?)
	 * 
	 * @step. ^I see (.*) and (.*) chat in contact list$
	 * @param contact1
	 *            The first contact to check in the conversation list
	 * @param contact2
	 *            The second contact to check in the conversation list
	 * @throws Exception
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
	 * @step. ^I( do not)? see contact list with name (.*)$
	 * @param userName
	 *            the username to check for in the contact list
	 * @param shouldNotSee
	 *            equals to null if "do not" part does not exist
	 * @throws Exception
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
			Assert.assertTrue(getContactListPage().isContactExists(userName, 1));
		} else {
			Assert.assertTrue(getContactListPage().waitUntilContactDisappears(
					userName));
		}
	}

	/**
	 * Checks to see that the muted symbol appears or not for the given contact.
	 * 
	 * @step. "^Contact (.*) is (not )?muted$
	 * @param contact
	 * @param shouldNotBeMuted
	 *            is set to null if 'not' part does not exist
	 * @throws Exception
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
	 * @step. ^I remember the state of PlayPause button next to the (.*)
	 *        conversation$
	 * 
	 * @param convoName
	 *            conversation name for which the screenshot should be taken
	 * @throws Exception
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
	 * @step. ^I see the state of PlayPause button next to the (.*) conversation
	 *        is changed$
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
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
	 * @step. ^I see PlayPause media content button for conversation (.*)
	 * @param convoName
	 *            conversation name for which button presence is checked
	 * 
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
	 * @step. ^I tap PlayPause button next to the (.*) conversation$
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
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
	 * @step. ^I open Search by UI button$
	 * 
	 * @throws Exception
	 */
	@When("^I open Search by UI button$")
	public void IOpenPeoplePicker() throws Exception {
		getContactListPage().tapOnSearchButton();
	}

	/**
	 * Tap the corresponding item in conversation settings menu
	 * 
	 * @step. ^I select (.*) From conversation settings menu$
	 * 
	 * @param itemName
	 *            menu item name
	 * @throws Exception
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
	 * @step. ^I remember unread messages indicator state for conversation (.*)
	 * 
	 * @param name
	 *            conversation name/alias
	 * @throws Exception
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
	 * Verify whether unread dot state is changed for the particular conversation 
	 * in comparison to the previous state
	 * 
	 * @step. ^I see unread messages indicator state is changed for conversation (.*)"
	 * 
	 * @param name conversation name/alias
	 * @throws Exception
	 */
	@Then("^I see unread messages indicator state is changed for conversation (.*)")
	public void ISeeUnreadIndiciatorStateISChanged(String name)
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
}