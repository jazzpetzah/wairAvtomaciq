package com.wearezeta.auto.android_tablet.steps;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationsListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletConversationsListPage getConversationsListPage()
			throws Exception {
		return (TabletConversationsListPage) pagesCollection
				.getPage(TabletConversationsListPage.class);
	}

	/**
	 * Wait until conversations list is fully loaded and successful load
	 * 
	 * @step. ^I see (?:the |\\s*)[Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@Given("^I see (?:the |\\s*)[Cc]onversations list$")
	public void ISeeConversationsList() throws Exception {
		getConversationsListPage().verifyConversationsListIsLoaded();
	}

	/**
	 * Wait until conversations list is fully loaded and there are (no)
	 * conversations
	 * 
	 * @step. ^I see (?:the |\\s*)[Cc]onversations list with (no
	 *        )?conversations?$
	 * @param shouldBeNoConversations
	 *            is set to null if "no" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Given("^I see (?:the |\\s*)[Cc]onversations list with (no )?conversations?$")
	public void ISeeConversationsListPlusItem(String shouldBeNoConversations)
			throws Exception {
		getConversationsListPage().verifyConversationsListIsLoaded();
		if (shouldBeNoConversations == null) {
			Assert.assertTrue(
					"No conversations are visible in the conversations list, but some are expected",
					getConversationsListPage().isAnyConversationVisible());
		} else {
			Assert.assertTrue(
					"Some conversations are visible in the conversations list, but zero is expected",
					getConversationsListPage().isNoConversationsVisible());
		}
	}

	/**
	 * Tap my own avatar on top of conversations list
	 * 
	 * @step. ^I tap my avatar on top of (?:the |\\s*)[Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@When("^I tap my avatar on top of (?:the |\\s*)[Cc]onversations list$")
	public void ITapMyAvatar() throws Exception {
		getConversationsListPage().tapMyAvatar();
	}

	/**
	 * Tap Search input field on top of Conversations list
	 * 
	 * @step. ^I tap (?:the |\\s*)Search input$
	 * 
	 * @throws Exception
	 */
	@When("^I tap (?:the |\\s*)Search input$")
	public void ITapSearchInput() throws Exception {
		getConversationsListPage().tapSearchInput();
	}

	/**
	 * Tap the corresponding conversation is the list
	 * 
	 * @step. ^I tap conversation (.*)"
	 * 
	 * @param name
	 *            conversation name
	 * @throws Exception
	 */
	@When("^I tap (?:the )conversation (.*)")
	public void ITapConversation(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		getConversationsListPage().tapConversation(name);
	}

	/**
	 * Verify whether the particular conversation is visible in the
	 * conversations list or not
	 * 
	 * @step.^I (do not )?see (?:the |\\s*)conversation (.*) in my conversations
	 *          list$
	 * @param shouldNotSee
	 *            equals to null if "do not" part is not present
	 * @param name
	 *            conversation name
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:the |\\s*)conversation (.*) in my conversations list$")
	public void ISeeOrNotTheConversation(String shouldNotSee, String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (shouldNotSee == null) {
			Assert.assertTrue(String.format(
					"There is no '%s' conversation in the conversations list",
					name), getConversationsListPage()
					.waitUntilConversationIsVisible(name));
		} else {
			Assert.assertTrue(
					String.format(
							"The conversation '%s' should not exist in the conversations list",
							name), getConversationsListPage()
							.waitUntilConversationIsInvisible(name));
		}
	}

	/**
	 * Verify whether the particular conversation name is silenced or not
	 * 
	 * @step. ^I see (?:the) conversation (.*) in my conversations list is (not
	 *        )?silenced$
	 * 
	 * @param name
	 *            conversation name/alias
	 * @param shouldNotBeSilenced
	 *            equals to null if the "not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I see (?:the) conversation (.*) in my conversations list is (not )?silenced$")
	public void ISeeConversationSilenced(String name, String shouldNotBeSilenced)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (shouldNotBeSilenced == null) {
			Assert.assertTrue(
					String.format("The conversation '%s' is not silenced", name),
					getConversationsListPage().waitUntilConversationIsSilenced(
							name));
		} else {
			Assert.assertTrue(String.format(
					"The conversation '%s' should not be silenced", name),
					getConversationsListPage()
							.waitUntilConversationIsNotSilenced(name));
		}
	}

	/**
	 * Verify whether missed call indicator is shown near the corresponding
	 * convo list item
	 * 
	 * @step. ^I (do not )?see missed call notification near (.*) conversation
	 *        list item$
	 * 
	 * @param shouldNotSee
	 *            equals to null if "do not " part does not exist in the step
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
	 */
	@Then("^I (do not )?see missed call notification near (.*) conversations list item$")
	public void ISeeMissedCallNotification(String shouldNotSee, String convoName)
			throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		if (shouldNotSee == null) {
			Assert.assertTrue(
					String.format(
							"Missed call notification is not visible for conversations list item '%s'",
							convoName), getConversationsListPage()
							.waitUntilMissedCallNotificationVisible(convoName));
		} else {
			Assert.assertTrue(
					String.format(
							"Missed call notification is still visible for conversations list item '%s'",
							convoName),
					getConversationsListPage()
							.waitUntilMissedCallNotificationInvisible(convoName));
		}
	}

	private static enum SwipeType {
		SHORT, LONG;
	}

	/**
	 * Perform long/short swipe down on conversations list
	 * 
	 * @step. ^I do (long|short) swipe down on conversations list$
	 *
	 * @param swipeTypeStr
	 *            see SwipeType enum for the list of available values
	 * @throws Exception
	 */
	@When("^I do (long|short) swipe down on conversations list$")
	public void IDoSwipeDown(String swipeTypeStr) throws Exception {
		final SwipeType swipeType = SwipeType.valueOf(swipeTypeStr
				.toUpperCase());
		switch (swipeType) {
		case SHORT:
			getConversationsListPage().doShortSwipeDown();
			break;
		case LONG:
			getConversationsListPage().doLongSwipeDown();
			break;
		default:
			throw new IllegalStateException(String.format(
					"Swipe type '%s' is not supported", swipeTypeStr));
		}
	}

	/**
	 * Swipe left to show the conversation view
	 * 
	 * @step. ^I swipe left to show (?:the |\\s*)conversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe left to show (?:the |\\s*)conversation view$")
	public void ISwipeLeft() throws Exception {
		getConversationsListPage().doSwipeLeft();
	}

	/**
	 * It is mandatory to locate PlayPause button by coordinates because
	 * Selendroid constantly throws InvalidElementStateException if we try to
	 * get it with findElement(s). And this happens only in tablets
	 * 
	 */
	private Map<String, Rectangle> playPauseButtonCoords = new HashMap<>();

	/**
	 * Verifies whether Play/Pause button is visible next to the corrresponding
	 * conversation name item.
	 * 
	 * @step. ^I see (?:Play|Pause) button next to the conversation name (.*)
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * 
	 * @throws Exception
	 */
	@When("^I see (?:Play|Pause) button next to the conversation name (.*)")
	public void ISeePlayPauseButton(String convoName) throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"Play/Pause button is not visible next to conversation item '%s'",
						convoName), getConversationsListPage()
						.waitUntilPlayPauseButtonVisibleNextTo(convoName));
	}

	private BufferedImage previousPlayPauseBtnState = null;

	/**
	 * Save the current state of PlayPause button into the internal data
	 * structure
	 * 
	 * @step. ^I remember the state of (?:Play|Pause) button next to the
	 *        conversation name (.*)
	 * 
	 * @param convoName
	 *            conversation name for which the screenshot should be taken
	 * @throws Exception
	 */
	@When("^I remember the state of (?:Play|Pause) button next to the conversation name (.*)")
	public void IRememberTheStateOfPlayPauseButton(String convoName)
			throws Exception {
		convoName = usrMgr.findUserByNameOrNameAlias(convoName).getName();
		previousPlayPauseBtnState = getConversationsListPage()
				.getScreenshotOfPlayPauseButton(
						playPauseButtonCoords.get(convoName)).orElseThrow(
						IllegalStateException::new);
	}

	private final static double MAX_SIMILARITY_THRESHOLD = 0.6;
	private final static int STATE_CHANGE_TIMEOUT_SECONDS = 5;

	/**
	 * Verify whether the current screenshot of PlayPause button is different
	 * from the previous one
	 * 
	 * @step. ^I see the state of (?:Play|Pause) button next to the conversation
	 *        name (.*) is changed$
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
	 */
	@Then("^I see the state of (?:Play|Pause) button next to the conversation name (.*) is changed$")
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
			final BufferedImage currentPlayPauseBtnState = getConversationsListPage()
					.getScreenshotOfPlayPauseButton(
							playPauseButtonCoords.get(convoName)).orElseThrow(
							IllegalStateException::new);
			score = ImageUtil.getOverlapScore(currentPlayPauseBtnState,
					previousPlayPauseBtnState,
					ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
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
	 * Tap PlayPause button next to the particular conversation name
	 * 
	 * @step. ^I tap (?:Play|Pause) button next to the conversation name (.*)
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
	 */
	@When("^I tap (?:Play|Pause) button next to the conversation name (.*)")
	public void ITapPlayPauseButton(String convoName) throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		getConversationsListPage().tapPlayPauseMediaButton(
				playPauseButtonCoords.get(convoName));
	}

	/**
	 * Stores the coordinates of the corresponding conversation item into the
	 * internal structure. This step is mandatory to call before any steps,
	 * which use Play/Pause button
	 * 
	 * @step. ^I remember the coordinates of conversation item (.*)
	 * 
	 * @param convoName
	 *            conversation name/alias
	 * @throws Exception
	 */
	@When("^I remember the coordinates of conversation item (.*)")
	public void IRememberConvoItemCoords(String convoName) throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		playPauseButtonCoords.put(convoName, getConversationsListPage()
				.calcPlayPauseButtonCoordinates(convoName));
	}
}
