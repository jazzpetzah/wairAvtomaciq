package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationViewPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ConversationViewPageSteps {
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
	 * @step. ^I see (?:the )[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I see (?:the )[Cc]onversation view$")
	public void ISeeConversationView() throws Exception {
		Assert.assertTrue("The conversation view is not currentyl visible",
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

}
