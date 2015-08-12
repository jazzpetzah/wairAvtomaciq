package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationViewPage;
import com.wearezeta.auto.android_tablet.pages.camera.ConversationViewCameraPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationViewPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletConversationViewPage getConversationViewPage()
			throws Exception {
		return (TabletConversationViewPage) pagesCollection
				.getPage(TabletConversationViewPage.class);
	}

	private ConversationViewCameraPage getConversationViewCameraPage()
			throws Exception {
		return (ConversationViewCameraPage) pagesCollection
				.getPage(ConversationViewCameraPage.class);
	}

	/**
	 * Verifies whether conversation view is currently visible
	 * 
	 * @step. ^I see (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I see (?:the |\\s*)[Cc]onversation view$")
	public void ISeeConversationView() throws Exception {
		Assert.assertTrue("The conversation view is not currently visible",
				getConversationViewPage().waitUntilVisible());
	}

	/**
	 * Tap the Show Tools button on conversation view
	 * 
	 * @step. ^I tap Show Tools button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Show Tools button on [Cc]onversation view page$")
	public void ITapShowToolsButton() throws Exception {
		getConversationViewPage().tapShowInstrumentsButton();
	}

	/**
	 * Tap the Close Tools button on conversation view
	 * 
	 * @step. ^I tap Close Tools button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Close Tools button on [Cc]onversation view page$")
	public void ITapCloseToolsButton() throws Exception {
		getConversationViewPage().tapCloseInstrumentsButton();
	}

	/**
	 * Tap the Show Details button on conversation view
	 * 
	 * @step. ^I tap Show Details button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Show Details button on [Cc]onversation view page$")
	public void ITapShowDetailsButton() throws Exception {
		getConversationViewPage().tapShowDetailsButton();
	}

	/**
	 * Verify the last conversation message contains expected text
	 * 
	 * @step. ^I see the system message contains \"(.*)\" text on
	 *        [Cc]onversation view page$
	 * 
	 * @param expectedMessage
	 *            the message to verify
	 * @throws Exception
	 */
	@Then("^I see the system message contains \"(.*)\" text on [Cc]onversation view page$")
	public void ISeeTheLastSystemMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The system message containing text '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitForSystemMessageContains(expectedMessage));
	}

	/**
	 * Verify the chat header system message contains expected text
	 * 
	 * @step. ^I see the chat header message contains \"(.*)\" text on
	 *        conversation view page$
	 * 
	 * @param expectedMessage
	 *            the message to verify
	 * @throws Exception
	 */
	@Then("^I see the chat header message contains \"(.*)\" text on conversation view page$")
	public void ISeeChatHeaderSystemMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The chat header message containing text '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitForChatHeaderMessageContains(expectedMessage));
	}

	/**
	 * Tap the text input field in the conversation view to start typing
	 * 
	 * @step. ^I tap (?:the |\\s*)text input in (?:the |\\s*)[Cc]onversation
	 *        view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap (?:the |\\s*)text input in (?:the |\\s*)[Cc]onversation view$")
	public void ITapTheTextInput() throws Exception {
		getConversationViewPage().tapTextInput();
	}

	/**
	 * Type a message into the active conversation. The text input should be
	 * already active. The message is NOT sent automatically
	 * 
	 * @step. ^I type (?:the |\\s*)message \"(.*)\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param message
	 *            the text to type
	 * @throws Exception
	 */
	@And("^I type (?:the |\\s*)message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ITypeMessage(String message) throws Exception {
		getConversationViewPage().typeMessage(message);
	}

	/**
	 * Tap Enter to send the typed message into the conversation
	 * 
	 * @step. ^I send the typed message in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I send the typed message in (?:the |\\s*)[Cc]onversation view$")
	public void ISendMessage() throws Exception {
		getConversationViewPage().sendMessage();
	}

	/**
	 * Verify whether the message is visible in the conversation view
	 * 
	 * @step. ^I (do not )?see the message \"(.*)\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param expectedMessage
	 *            the message to check
	 * @param shouldNotSee
	 *            equals to null if the message should be visible in the convo
	 *            view
	 * @throws Exception
	 */
	@Then("^I (do not )?see the message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeMessage(String shouldNotSee, String expectedMessage)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(
					String.format(
							"The expected message '%s' is not visible in the conversation view",
							expectedMessage), getConversationViewPage()
							.waitUntilMessageIsVisible(expectedMessage));
		} else {
			Assert.assertTrue(
					String.format(
							"The expected message '%s' is visible in the conversation view, but it should not",
							expectedMessage), getConversationViewPage()
							.waitUntilMessageIsNotVisible(expectedMessage));
		}
	}

	/**
	 * Swipe on the text input field to show the available instruments
	 * 
	 * @step. ^I swipe left on text input in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe left on text input in (?:the |\\s*)[Cc]onversation view$")
	public void ISwipeLeftOnTextInput() throws Exception {
		getConversationViewPage().swipeLeftOnTextInput();
	}

	/**
	 * Tap the Ping button to send Ping event from the currently opened
	 * conversation
	 * 
	 * @step. ^I tap Ping button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Ping button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapPingButton() throws Exception {
		getConversationViewPage().tapPingButton();
	}

	/**
	 * Tap the Add Picture button. The input field slider should be already
	 * opened
	 * 
	 * @step. ^I tap Add Picture button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Add Picture button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapAddPicture() throws Exception {
		getConversationViewCameraPage().tapLensButton();
	}

	/**
	 * Tap the Take Photo button. The Add Picture button should be already
	 * clicked
	 * 
	 * @step. ^I tap Take Photo button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Take Photo button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapTakePhotoButton() throws Exception {
		getConversationViewCameraPage().tapTakePhotoButton();
	}

	/**
	 * Confirm the taken photo or selected picture
	 * 
	 * @step. ^I confirm the picture for (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm the picture for (?:the |\\s*)[Cc]onversation view$")
	public void IConfirmPicture() throws Exception {
		getConversationViewCameraPage().confirmPictureSelection();
	}

	/**
	 * Verify whether there is at least one picture in the conversation view
	 * 
	 * @step. ^I see a new picture in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@Then("^I see a new picture in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeNewPicture() throws Exception {
		Assert.assertTrue(
				"No new pictures are visible in the conversation view",
				getConversationViewPage().waitUntilAPictureAppears());
	}

	/**
	 * Verify whether ping message is visible in the current conversation view
	 * 
	 * @step. ^I see the [Pp]ing message \"<(.*)>\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param expectedMessage
	 *            the text of expected ping message
	 * @throws Exception
	 */
	@Then("^I see the [Pp]ing message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ISeePingMessage(String expectedMessage) throws Exception {
		Assert.assertTrue(
				String.format(
						"The expected ping message '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitUntilPingMessageIsVisible(expectedMessage));
	}

	/**
	 * Verify whether missed call notification is visible in conversation view
	 * 
	 * @step. ^I see missed group call notification in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@Then("^I see missed group call notification in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeMissedCallNotification() throws Exception {
		Assert.assertTrue(
				"The expected missed group call notification is not visible in the conversation view",
				getConversationViewPage().waitUntilGCNIsVisible());
	}
}
