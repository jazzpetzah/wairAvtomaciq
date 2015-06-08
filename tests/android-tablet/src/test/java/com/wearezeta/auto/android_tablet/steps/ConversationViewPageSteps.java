package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationViewPage;
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

}
