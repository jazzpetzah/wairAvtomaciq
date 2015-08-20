package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletContactListPage;

import cucumber.api.java.en.*;

public class TabletContactListPageSteps {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletContactListPage getTabletContactListPage() throws Exception {
		return (TabletContactListPage) pagesCollecton
				.getPage(TabletContactListPage.class);
	}

	/**
	 * Swipes down on Contact list on iPad
	 * 
	 * @step ^I swipe down contact list on iPad$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe down contact list on iPad$")
	public void ISwipeDownContactListOniPad() throws Exception {
		getTabletContactListPage().swipeDown(500);
	}

	/**
	 * Opens archived conversations on iPad
	 * 
	 * @step ^I open archived conversations on iPad$
	 * 
	 * @throws Exception
	 */
	@When("^I open archived conversations on iPad$")
	public void IOpenArchivedConvOnIpad() throws Exception {
		getTabletContactListPage().swipeUp(500);
	}

	/**
	 * Verifies that mute a call button in landscape in conv list is not shown
	 * 
	 * @step. ^I dont see mute call button in conversation list on iPad$
	 * @throws Throwable
	 */
	@Then("^I dont see mute call button in conversation list on iPad$")
	public void IDontSeeMuteCallButtonInConversationLisOniPad()
			throws Throwable {
		Assert.assertFalse("Mute call button is still visible",
				getTabletContactListPage().isMuteCallButtonVisible());
	}

}
