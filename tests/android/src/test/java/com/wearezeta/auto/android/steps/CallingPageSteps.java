package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.CallingLockscreenPage;
import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.android.pages.DialogPage;
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
		PagesCollection.callingOverlayPage = (CallingOverlayPage) PagesCollection.loginPage
				.instantiatePage(CallingOverlayPage.class);
		PagesCollection.callingOverlayPage.muteConversation();
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
	 * Verify that started call message is visible
	 * 
	 * @step. ^I see started call message for contact (.*)$
	 * @param contact
	 *            contact name with whom you have a call
	 * @throws Exception
	 */
	@When("^I see started call message for contact (.*)$")
	public void ISeeStartedCallMesage(String contact) throws Exception {
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
	@When("^I cannot see the call bar$")
	public void ICannotSeeTheCallBar() throws Exception {
		Assert.assertFalse(PagesCollection.callingOverlayPage.isVisible());
	}

	/**
	 * Checks to see that the calling lock screen appears when a user calls
	 * while Wire is minimised or the phone is locked
	 * 
	 * @step. ^I see the call lock screen$
	 * 
	 * @throws Exception
	 */
	@When("I see the call lock screen$")
	public void ISeeTheCallLockScreen() throws Exception {
		PagesCollection.callingLockscreenPage = (CallingLockscreenPage) PagesCollection.loginPage
				.instantiatePage(CallingLockscreenPage.class);
		Assert.assertFalse(PagesCollection.callingOverlayPage.isVisible());
	}

	/**
	 * Checks to see that the user calling in the lock screen is the correct
	 * user
	 * 
	 * @step. ^I see a call from (.*) in the call lock screen$
	 * 
	 * @param contact
	 *            The username to compare the "is calling" message to.
	 * 
	 * @throws Exception
	 */
	@When("I see a call from (.*) in the call lock screen$")
	public void ISeeACallFromUserInLockScreen(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String callersName = PagesCollection.callingLockscreenPage
				.getCallersName();
		Assert.assertEquals(contact, callersName);
	}

	/**
	 * Answers the call from the the calling overlay page
	 * 
	 * @step. ^I answer the call from the overlay bar$
	 * 
	 * @throws Exception
	 */
	@When("I answer the call from the overlay bar$")
	public void IAnswerCallFromTheOverlayBar() throws Exception {
		PagesCollection.callingOverlayPage = (CallingOverlayPage) PagesCollection.loginPage
				.instantiatePage(CallingOverlayPage.class);
		PagesCollection.dialogPage = (DialogPage) PagesCollection.loginPage
				.instantiatePage(DialogPage.class);
		PagesCollection.callingOverlayPage.acceptCall();
	}

	/**
	 * Answers the call from the lock screen and sets up the calling overlay
	 * page
	 * 
	 * @step. ^I answer the call from the lock screen$
	 * 
	 * @throws Exception
	 */
	@When("I answer the call from the lock screen$")
	public void IAnswerCallFromTheLockScreen() throws Exception {
		PagesCollection.callingOverlayPage = PagesCollection.callingLockscreenPage
				.acceptCall();
	}

	/**
	 * Check calling Big bar
	 * 
	 * @step. ^I see calling overlay Big bar$
	 * 
	 * @throws Exception
	 */
	@When("^I see calling overlay Big bar$")
	public void WhenISeeCallingOverlayBigBar() throws Exception {
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingOverlayIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.incominCallerAvatarIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingMessageIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingDismissIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingSpeakerIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingMicMuteIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.ongoingCallMinibarIsVisible());
	}

	/**
	 * Check calling Micro bar
	 * 
	 * @step. ^I see calling overlay Micro bar$
	 * 
	 * @throws Exception
	 */
	@When("^I see calling overlay Micro bar$")
	public void WhenISeeCallingOverlayMicroBar() throws Exception {
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingOverlayIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.ongoingCallMicrobarIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.incominCallerAvatarIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.callingMessageIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.callingDismissIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.callingSpeakerIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.callingMicMuteIsVisible());
	}

	/**
	 * Check calling Mini bar
	 * 
	 * @step. ^I see calling overlay Mini bar$
	 * 
	 * @throws Exception
	 */
	@When("^I see calling overlay Mini bar$")
	public void WhenISeeCallingOverlayMiniBar() throws Exception {
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingOverlayIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.ongoingCallMinibarIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingMessageIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingDismissIsVisible());
		Assert.assertFalse(PagesCollection.callingOverlayPage
				.callingSpeakerIsVisible());
		Assert.assertTrue(PagesCollection.callingOverlayPage
				.callingMicMuteIsVisible());
	}

}
