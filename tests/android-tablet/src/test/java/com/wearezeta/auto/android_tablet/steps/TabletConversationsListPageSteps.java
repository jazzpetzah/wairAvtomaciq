package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class TabletConversationsListPageSteps {
	@SuppressWarnings("unused")
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
	 * @step. ^I see the [Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@When("^I see the [Cc]onversations list$")
	public void ISeeConversationsList() throws Exception {
		getConversationsListPage().verifyConversationsListIsLoaded();
	}
	
	/**
	 * Tap my own avatar on top of conversations list
	 * 
	 * @step. ^I tap my avatar$
	 * 
	 * @throws Exception
	 */
	@When("^I tap my avatar$")
	public void ITapMyAvatar() throws Exception {
		getConversationsListPage().tapMyAvatar();
	}
	
}
