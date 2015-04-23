package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PendingRequestsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I click on Ignore button on Pending requests page$")
	public void IClickOnIgnoreButtonPendingRequests() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage
				.clickIgnoreButton();
	}

	@When("^I click on Ignore button on Pending requests page (.*) times$")
	public void IClickOnIgnoreButtonPendingRequests(int numberOfIgnores)
			throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage
				.clickIgnoreButtonMultiple(numberOfIgnores);
	}

	@When("I click Connect button on Pending request page")
	public void IClickOnConnectButtonPendingRequest() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage
				.clickConnectButton();
	}

	/**
	 * Clicks the connect button on the pending requests page a specific number
	 * of times
	 * 
	 * @step. ^I click on Connect button on Pending requests page (.*) times$
	 * 
	 * @param numberOfConnects
	 *            number of clicks integer
	 * @throws AssertionError
	 *             if connect button is not visible
	 */
	@When("^I click on Connect button on Pending requests page (.*) times$")
	public void IClickOnConnectButtonPendingRequests(int numberOfConnects)
			throws Throwable {
		PagesCollection.contactListPage = PagesCollection.pendingRequestsPage
				.clickConnectButtonMultiple(numberOfConnects);
	}

	@When("I see Pending request page")
	public void ISeePendingRequestPage() throws Exception {
		Assert.assertTrue("Pending Requests page is not shown",
				PagesCollection.pendingRequestsPage.isConnectButtonDisplayed());
	}

	@When("I see Hello connect message from user (.*) on Pending request page")
	public void ISeeHelloConnectMessageFrom(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		Assert.assertEquals(user,
				PagesCollection.pendingRequestsPage.getRequesterName());
		Assert.assertTrue(PagesCollection.pendingRequestsPage
				.isAutoMessageCorrect());
	}

}
