package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.IncomingPendingRequestsPage;

import cucumber.api.java.en.When;

public class IncomingPendingRequestsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private IncomingPendingRequestsPage getPendingRequestsPage() throws Exception {
		return pagesCollection.getPage(IncomingPendingRequestsPage.class);
	}

	@When("^I click on Ignore button on Pending requests page$")
	public void IClickOnIgnoreButtonPendingRequests() throws Exception {
		getPendingRequestsPage().clickIgnoreButton();
	}

	@When("^I click on Ignore button on Pending requests page (\\d+) times?$")
	public void IClickOnIgnoreButtonPendingRequests(int numberOfIgnores) throws Exception {
		getPendingRequestsPage().clickIgnoreButtonMultiple(numberOfIgnores);
	}

	@When("I click Connect button on Pending request page")
	public void IClickOnConnectButtonPendingRequest() throws Exception {
		getPendingRequestsPage().clickConnectButton();
	}

	/**
	 * Clicks the connect button on the pending requests page a specific number
	 * of times
	 * 
	 * @step. ^I click on Connect button on Pending requests page (\d+) times?$
	 * 
	 * @param numberOfConnects
	 *            number of clicks integer
	 * @throws AssertionError
	 *             if connect button is not visible
	 */
	@When("^I click on Connect button on Pending requests page (\\d+) times?$")
	public void IClickOnConnectButtonPendingRequests(int numberOfConnects) throws Exception {
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
		Assert.assertTrue(String.format("Connect To message is not visible for user '%s'", user),
				getPendingRequestsPage().isConnectToNameExist(user));
	}

	/**
	 * Verifies that you both know section is visible
	 * 
	 * @step. ^I see YOU BOTH KNOW people section$
	 * @throws Exception
	 */
	@When("^I see YOU BOTH KNOW people section$")
	public void ISeeYOUBOTHKNOWPeopleSection() throws Exception {
		if (!getPendingRequestsPage().isYouBothKnowDisplayed()) {
			getPendingRequestsPage().printPageSource();
			throw new AssertionError("You Both Know section is not visible on the current page");
		}
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
}
