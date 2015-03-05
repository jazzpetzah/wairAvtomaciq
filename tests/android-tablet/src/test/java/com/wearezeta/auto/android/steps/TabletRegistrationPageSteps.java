package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletPagesCollection;

import cucumber.api.java.en.Then;

public class TabletRegistrationPageSteps {
	
	/**
	 * Initializes profile page opened after automatic login
	 * 
	 * @step. ^I passed login automatically$
	 * 
	 */
	@Then("^I passed login automatically$")
	public void IPassedLoginAutomatically() throws Throwable {
		TabletPagesCollection.personalInfoPage = TabletPagesCollection.registrationPage.initProfilePage();
		PagesCollection.personalInfoPage = TabletPagesCollection.personalInfoPage;
	}
}