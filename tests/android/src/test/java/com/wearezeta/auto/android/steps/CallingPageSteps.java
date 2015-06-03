package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.CallingLockscreenPage;
import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class CallingPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private CallingOverlayPage getCallingOverlayPage() throws Exception {
		return (CallingOverlayPage) pagesCollection
				.getPage(CallingOverlayPage.class);
	}

	private CallingLockscreenPage getCallingLockscreenPage() throws Exception {
		return (CallingLockscreenPage) pagesCollection
				.getPage(CallingLockscreenPage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final long CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS = 5000;

	/**
	 * Ignores an incoming call
	 * 
	 * @step. ^I click the ignore call button$
	 * 
	 * @throws Exception
	 */
	@When("I click the ignore call button")
	public void IClickIgnoreCallButton() throws Exception {
		getCallingOverlayPage().muteConversation();
	}

	/**
	 * Verify that incoming calling UI is visible and that the correct caller
	 * name is shown
	 * 
	 * @step. ^I see incoming calling message for contact (.*)$
	 * 
	 * @param expectedCallerName
	 *            User name who calls
	 * @throws Exception
	 */
	@When("^I see incoming calling message for contact (.*)$")
	public void ISeeIncomingCallingMesage(String expectedCallerName)
			throws Exception {
		expectedCallerName = usrMgr.findUserByNameOrNameAlias(
				expectedCallerName).getName();
		final long millisecondsStarted = System.currentTimeMillis();
		String actualCallerName;
		do {
			Thread.sleep(500);
			actualCallerName = getCallingOverlayPage().getCallersName();
		} while (System.currentTimeMillis() - millisecondsStarted <= CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS
				&& !actualCallerName.equals(expectedCallerName));
		Assert.assertEquals(
				String.format(
						"The current caller name '%s' differs from the expected value '%s' after %s seconds timeout",
						actualCallerName, expectedCallerName,
						CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS / 1000),
				expectedCallerName, actualCallerName);
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
		String callersName = getCallingOverlayPage().getCallersName();
		Assert.assertEquals(contact, callersName);
	}

	/**
	 * Wait for call message changes
	 * 
	 * @step. ^I wait for call message changes from (.*) for (.*) seconds$
	 * @param oldMessage
	 *            call message that should disappear
	 * @param sec
	 *            timeout
	 * @throws Exception
	 */
	@When("^I wait for call message changes from (.*) for (.*) seconds$")
	public void IWaitFormCallMessageChanges(String oldMessage, int sec)
			throws Exception {
		getCallingOverlayPage().waitForCallersNameChanges(oldMessage, sec);
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
		Assert.assertTrue(getCallingOverlayPage().waitUntilNotVisible());
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
		Assert.assertTrue(
				"Calling lockscreen is not visible, but it should be",
				getCallingLockscreenPage().isVisible());
	}

	/**
	 * Checks to see that the user calling in the lock screen is the correct
	 * user
	 * 
	 * @step. ^I see a call from (.*) in the call lock screen$
	 * 
	 * @param expectedCallerName
	 *            The username to compare the "is calling" message to.
	 * 
	 * @throws Exception
	 */
	@When("I see a call from (.*) in the call lock screen$")
	public void ISeeACallFromUserInLockScreen(String expectedCallerName)
			throws Exception {
		expectedCallerName = usrMgr.findUserByNameOrNameAlias(
				expectedCallerName).getName();
		final long millisecondsStarted = System.currentTimeMillis();
		String actualCallerName;
		do {
			Thread.sleep(500);
			actualCallerName = getCallingLockscreenPage().getCallersName();
		} while (System.currentTimeMillis() - millisecondsStarted <= CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS
				&& !actualCallerName.equals(expectedCallerName));
		Assert.assertEquals(
				String.format(
						"The current caller name '%s' differs from the expected value '%s' after %s seconds timeout",
						actualCallerName, expectedCallerName,
						CALLER_NAME_VISIBILITY_TIMEOUT_MILLISECONDS / 1000),
				expectedCallerName, actualCallerName);
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
		getCallingOverlayPage().acceptCall();
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
		getCallingLockscreenPage().acceptCall();
	}

	// FIXME: replace multiple assertTrue calls with loops

	/**
	 * Check calling Big bar
	 * 
	 * @step. ^I see calling overlay Big bar$
	 * 
	 * @throws Exception
	 */
	@When("^I see calling overlay Big bar$")
	public void WhenISeeCallingOverlayBigBar() throws Exception {
		Assert.assertTrue(getCallingOverlayPage().callingOverlayIsVisible());
		Assert.assertTrue(getCallingOverlayPage()
				.incominCallerAvatarIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingMessageIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingDismissIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingSpeakerIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingMicMuteIsVisible());
		Assert.assertFalse(getCallingOverlayPage()
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
		Assert.assertTrue(getCallingOverlayPage()
				.ongoingCallMicrobarIsVisible());
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
		Assert.assertTrue(getCallingOverlayPage().ongoingCallMinibarIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingMessageIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingDismissIsVisible());
		Assert.assertFalse(getCallingOverlayPage().callingSpeakerIsVisible());
		Assert.assertTrue(getCallingOverlayPage().callingMicMuteIsVisible());
	}

}
