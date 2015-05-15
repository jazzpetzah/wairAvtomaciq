package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;
import com.wearezeta.auto.ios.pages.TabletContactListPage;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.*;

public class TabletContactListPageSteps {

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
