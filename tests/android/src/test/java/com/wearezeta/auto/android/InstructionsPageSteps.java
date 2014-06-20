package com.wearezeta.auto.android;

import java.io.IOException;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.InstructionsPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.PersonalInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class InstructionsPageSteps {

	@When("^I swipe to personal info screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		PagesCollection.instructionsPage = (InstructionsPage) PagesCollection.androidPage;
		PagesCollection.personalInfoPaga = (PersonalInfoPage)(PagesCollection.instructionsPage.swipeLeft(500));
		PagesCollection.personalInfoPaga.waitForEmailFieldVisible();
	}
	
	@Then("^I swipe from Instructions page to Contact list page$")
	public void ISwipeFromInstructionsPageToContactListPage() throws Throwable {

		PagesCollection.instructionsPage.waitInstructionsPage();
		PagesCollection.contactListPage =  (ContactListPage) PagesCollection.instructionsPage.swipeRight(500);
	}
	
}
