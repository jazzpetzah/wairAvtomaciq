package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class CallingPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Ignores an incoming call
	 * 
	 * @step. ^I click the ignore call button$
	 * 
	 * @throws Exception
	 */
	@When("I click the ignore call button")
	public void IClickIgnoreCallButton() throws Exception {
		CallingOverlayPage page = new CallingOverlayPage(
				PagesCollection.loginPage.getDriver(),
				PagesCollection.loginPage.getWait());
		page.muteConversation();
	}

	/**
	 * Verify that incoming calling UI is visible and that the correct caller
	 * name is shown
	 * 
	 * @step. ^I see incoming calling message for contact (.*)$
	 * 
	 * @param contact
	 *            User name who calls
	 * @throws Exception
	 */
	@When("^I see incoming calling message for contact (.*)$")
	public void ISeeIncomingCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String callersName = PagesCollection.callingOverlayPage
				.getCallersName();
		Assert.assertEquals(contact, callersName);
	}

	/**
	 * Checks to see that the call bar is not visible
	 * 
	 * @step. ^I cannot see the call bar$
	 * 
	 * @throws Exception
	 */
	@When("I cannot see the call bar")
	public void ICannotSeeTheCallBar() throws Exception {
		Assert.assertFalse(PagesCollection.callingOverlayPage.isVisible());
	}
}
