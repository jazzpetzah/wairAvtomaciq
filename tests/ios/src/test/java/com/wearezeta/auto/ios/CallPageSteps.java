package com.wearezeta.auto.ios;


import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.StartedCallPage;

import cucumber.api.java.en.When;

public class CallPageSteps {
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that calling UI is visible
	 * 
	 * @step. ^I see calling message for contact (.*)$
	 * @param contact
	 * 		User name whom we call
	 * @throws Exception
	 */
	@When("^I see calling message for contact (.*)$")
	public void ISeeCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isCallingMessageVisible(contact.toUpperCase()));
	}
	
	/**
	 * 
	 * Verify that calling UI buttons are visible
	 * @step. ^I see mute call, end call and speakers buttons$
	 * @throws Exception
	 */
	@When("^I see mute call, end call and speakers buttons$")
	public void ISeeCallingPageButtons() throws Exception {

		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isEndCallVisible());
		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isMuteCallVisible());
		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isSpeakersVisible());
	}
	
	/**
	 * Click on end call button
	 * @step. ^I end started call$
	 * @throws Exception
	 */
	@When("^I end started call$")
	public void IEndStartedCall() throws Exception {

		((StartedCallPage)PagesCollection.callPage).clickEndCallButton();
	}
	
	/**
	 * Verify that calling page is not visible
	 * @step. ^I dont see calling page$
	 * @throws Exception
	 */
	@When("^I dont see calling page$")
	public void IDontSeeCallPage() throws Exception {

		Assert.assertFalse(((StartedCallPage)PagesCollection.callPage).isCallingMessageVisible());
	}
}
