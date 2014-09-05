package com.wearezeta.auto.ios;

import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PendingRequestsPageSteps {
	
	@When("I click on Ignore button on Pending requests page")
	public void IClickOnIgnoreButtonPendingRequests() throws Throwable{
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage.clickIgnoreButton();
	}
	
	@When("I click Connect button on Pending request page")
	public void IClickOnConnectButtonPendingRequest() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage.clickConnectButton();
	}

}
