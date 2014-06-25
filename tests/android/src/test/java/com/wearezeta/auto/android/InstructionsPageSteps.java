package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class InstructionsPageSteps {

	@When("^I swipe to personal info screen$")
	public void WhenISwipeToPersonalInfoScreen() throws IOException {
		PagesCollection.personalInfoPaga = (PersonalInfoPage)(PagesCollection.instructionsPage.swipeLeft(500));
		PagesCollection.personalInfoPaga.waitForEmailFieldVisible();
	}
	
	@When("^I see connection request from (.*)$")
	public void WhenISeeConnectionRequestFrom(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue(PagesCollection.instructionsPage.isConnectDialogDispalayed());
		contact = CommonSteps.CONNECTION_NAME + contact.toUpperCase();
		Assert.assertEquals(PagesCollection.instructionsPage.getConnectionRequestHeader(),contact);
	}
	
	@When("^I confirm connection request$")
	public void WhenIConfirmConnectionRequest() throws Throwable {
		PagesCollection.instructionsPage.acceptAllConnections();
	}
	
	@When("^I swipe from Instructions page to Contact list page$")
	public void WhenISwipeFromInstructionsPageToContactListPage() throws Throwable {

		PagesCollection.instructionsPage.waitInstructionsPage();
		PagesCollection.contactListPage =  (ContactListPage) PagesCollection.instructionsPage.swipeRight(500);
	}
	
}
