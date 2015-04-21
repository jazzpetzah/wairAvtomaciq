package com.wearezeta.auto.ios;


import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.IncomingCallPage;
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
		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isIncomingCallMessageVisible(contact.toUpperCase()));
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
	
	/**
	 * Verify that incoming call page is not visible
	 * @step. ^I dont see incoming call page$
	 * @throws Exception
	 */
	@When("^I dont see incoming call page$")
	public void IDontSeeIncomingCallPage() throws Exception {

		Assert.assertFalse(((IncomingCallPage)PagesCollection.callPage).isCallingMessageVisible());
	}
	
	/**
	 * Verify that incoming calling UI is visible
	 * 
	 * @step. ^I see incoming calling message for contact (.*)$
	 * @param contact
	 * 		User name who calls
	 * @throws Exception
	 */
	@When("^I see incoming calling message for contact (.*)$")
	public void ISeeIncomingCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(((IncomingCallPage)PagesCollection.callPage).isCallingMessageVisible(contact.toUpperCase()));
	}
	
	/**
	 * Click on end incoming call button
	 * @step. ^I end incoming call$
	 * @throws Exception
	 */
	@When("^I end incoming call$")
	public void IEndIncomingCall() throws Exception {

		((IncomingCallPage)PagesCollection.callPage).endIncomingCallClick();
	}
	
	/**
	 * Accept incoming call by clicking accept button
	 * @step. ^I accept incoming call$
	 * @throws Exception
	 */
	@When("^I accept incoming call$")
	public void IAcceptIncomingCall() throws Exception {

		PagesCollection.callPage = ((IncomingCallPage)PagesCollection.callPage).acceptIncomingCallClick();
	}
	
	/**
	 * Verify that started call message is visible
	 * @step. ^I see started call message for contact (.*)$
	 * @param contact
	 * 			contact name with whom you have a call
	 * @throws Exception
	 */
	@When("^I see started call message for contact (.*)$")
	public void ISeeStartedCallMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(((StartedCallPage)PagesCollection.callPage).isStartedCallMessageVisible(contact.toUpperCase()));
	}
}
