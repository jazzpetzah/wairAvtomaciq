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
}
