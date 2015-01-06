package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PendingRequestsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	@When("^I click on Ignore button on Pending requests page$")
	public void IClickOnIgnoreButtonPendingRequests() throws Throwable{
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage.clickIgnoreButton();
	}
	
	@When("^I click on Ignore button on Pending requests page (.*) times$")
	public void IClickOnIgnoreButtonPendingRequests(int numberOfIgnores) throws Throwable{
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage.clickIgnoreButtonMultiple(numberOfIgnores);
	}
	
	@When("I click Connect button on Pending request page")
	public void IClickOnConnectButtonPendingRequest() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage.clickConnectButton();
	}
	
	@When("I see Pending request page")
	public void ISeePendingRequestPage(){
		Assert.assertTrue("Pending Requests page is not shown", PagesCollection.pendingRequestsPage.isConnectButtonDisplayed());
	}
	
	@When("I see Hello connect message from user (.*) on Pending request page")
	public void ISeeHelloConnectMessageFrom(String user){
		user = usrMgr.findUserByNameAlias(user).getName();
		Assert.assertEquals(user, PagesCollection.pendingRequestsPage.getRequesterName());
		Assert.assertTrue(PagesCollection.pendingRequestsPage.isAutoMessageCorrect());
	}

}
