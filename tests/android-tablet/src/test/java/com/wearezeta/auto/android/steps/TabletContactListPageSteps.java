package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class TabletContactListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	/**
	 * Taps profile button (SelfName) 
	 * 
	 * @step. ^I tap on profile link$
	 * 
	 */
	@When("^I tap on profile link$")
	public void WhenITapOnProfileLink() throws Exception {
		TabletPagesCollection.contactListPage.tapOnProfileLink();
	}
}