package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.PendingRequestsPage;

import cucumber.api.java.en.When;

public class PendingRequestsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private PendingRequestsPage getPendingRequestsPage() throws Exception {
		return pagesCollecton.getPage(PendingRequestsPage.class);
	}

	@When("^I click on Ignore button on Pending requests page$")
	public void IClickOnIgnoreButtonPendingRequests() throws Throwable {
		getPendingRequestsPage().clickIgnoreButton();
	}

	@When("^I click on Ignore button on Pending requests page (.*) times$")
	public void IClickOnIgnoreButtonPendingRequests(int numberOfIgnores)
			throws Throwable {
		getPendingRequestsPage().clickIgnoreButtonMultiple(numberOfIgnores);
	}

	@When("I click Connect button on Pending request page")
	public void IClickOnConnectButtonPendingRequest() throws Throwable {
		getPendingRequestsPage().clickConnectButton();
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
		getPendingRequestsPage().clickConnectButtonMultiple(numberOfConnects);
	}

	@When("I see Pending request page")
	public void ISeePendingRequestPage() throws Exception {
		Assert.assertTrue("Pending Requests page is not shown",
				getPendingRequestsPage().isConnectButtonDisplayed());
	}

	@When("I see Hello connect message from user (.*) on Pending request page")
	public void ISeeHelloConnectMessageFrom(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		Assert.assertEquals(user, getPendingRequestsPage().getRequesterName());
		// Assert.assertTrue(getPendingRequestsPage().isAutoMessageCorrect());
	}

	/**
	 * Verifies that you both know section is visible
	 * 
	 * @step. ^I see YOU BOTH KNOW people section$
	 * @throws Exception
	 */
	@When("^I see YOU BOTH KNOW people section$")
	public void ISeeYOUBOTHKNOWPeopleSection() throws Exception {
		Assert.assertTrue(getPendingRequestsPage().isYouBothKnowDisplayed());
	}

	/**
	 * Clicks on a person in the you both know section
	 * 
	 * @step. ^I click person in YOU BOTH KNOW section$
	 * @throws Exception
	 */
	@When("^I click person in YOU BOTH KNOW section$")
	public void IClickPersonInYOUBOTHKNOWSection() throws Exception {
		getPendingRequestsPage().clickYouBothKnowPeopleIcon();
	}

	/**
	 * Verify that name and surname exists on the page
	 * 
	 * @step. ^I see user (.*) found on Pending request page$
	 * @throws Exception
	 */
	@When("^I see user (.*) found on Pending request page$")
	public void WhenISeeUserFoundOnPendingRequestPage(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertEquals(contact, getPendingRequestsPage()
				.getRequesterName());
	}

}
