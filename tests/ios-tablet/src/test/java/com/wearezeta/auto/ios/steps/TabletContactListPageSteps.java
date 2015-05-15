package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;
import com.wearezeta.auto.ios.pages.TabletContactListPage;
import com.wearezeta.auto.ios.pages.TabletDialogPage;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.*;

public class TabletContactListPageSteps {
	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Swipes down on Contact list on iPad
	 * 
	 * @step ^I swipe down contact list on iPad$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe down contact list on iPad$")
	public void ISwipeDownContactListOniPad() throws Exception {
		if (TabletPagesCollection.tabletContactListPage == null) {
			TabletPagesCollection.tabletContactListPage = (TabletContactListPage) PagesCollection.loginPage
					.instantiatePage(TabletContactListPage.class);
		}
		PagesCollection.peoplePickerPage = (PeoplePickerPage) TabletPagesCollection.tabletContactListPage
				.swipeDown(500);
	}
	
}
