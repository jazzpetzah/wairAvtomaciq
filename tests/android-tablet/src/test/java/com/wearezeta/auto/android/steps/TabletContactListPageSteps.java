package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.TabletPagesCollection;

import cucumber.api.java.en.When;

public class TabletContactListPageSteps {
	
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