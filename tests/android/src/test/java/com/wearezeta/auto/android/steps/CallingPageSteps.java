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
		Assert.assertTrue(String.format(
				"The current caller name differs from the expected value '%s'",
				expectedCallerName), getCallingOverlayPage()
				.waitUntilNameAppearsOnCallingBarCaption(expectedCallerName));
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
		Assert.assertTrue(
				String.format("The name '%s' is not visible on calling bar",
						contact),
				getCallingOverlayPage().waitUntilNameAppearsOnCallingBarAvatar(
						contact));
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
	 * @step. ^I (do not )?see calling overlay Big bar$
	 * 
	 * @param shouldNotSee
	 *            equals to null if 'do not' does not exist in the step
	 *            signature
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see calling overlay Big bar$")
	public void WhenISeeCallingOverlayBigBar(String shouldNotSee)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(getCallingOverlayPage().waitUntilVisible());
			Assert.assertTrue(getCallingOverlayPage().callingDismissIsVisible());
			Assert.assertTrue(getCallingOverlayPage().callingSpeakerIsVisible());
			Assert.assertTrue(getCallingOverlayPage().callingMicMuteIsVisible());
		} else {
			Assert.assertTrue(
					"Calling bar is still visible, but should be hidden",
					getCallingOverlayPage().waitUntilNotVisible());
		}
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

	/**
	 * Press on the join group call button
	 * 
	 * @step. ^I press join group call button$
	 * 
	 * @throws Exception
	 */
	@When("^I press join group call button$")
	public void WhenIPressJoinGroupCallButton() throws Exception {
		getCallingOverlayPage().joinGroupCall();
	}

	/**
	 * Check whether expected number of users present in call
	 * 
	 * @step. ^I see (\\d+) users? take part in call$
	 * 
	 * @throws Exception
	 */
	@When("^I see (\\d+) users? take part in call$")
	public void ISeeXUsersTakePartInGroupCall(final int expectedUsersCount)
			throws Exception {
		int actualUsersCount;
		int ntries = 0;
		do {
			actualUsersCount = getCallingOverlayPage()
					.numberOfParticipantsInGroupCall();
			if (actualUsersCount == expectedUsersCount) {
				return;
			} else {
				Thread.sleep(1500);
				ntries++;
			}
		} while (ntries < 3);
		throw new AssertionError(
				String.format(
						"The actual count of users in call (%s) does not equal to the expected count (%s)",
						actualUsersCount, expectedUsersCount));
	}

	/**
	 * Check if alert notifying that group call is full appears
	 * 
	 * @step. ^I see group call is full alert$
	 * 
	 * @throws Exception
	 */
	@When("^I see group call is full alert$")
	public void ISeeGroupCallIsFullAlert() throws Exception {
		Assert.assertTrue(getCallingOverlayPage().isGroupCallFullAlertVisible());
	}

	/**
	 * Check whether expected number of users present in group call
	 * 
	 * @step. ^I close group call is full alert$
	 * 
	 * @throws Exception
	 */
	@When("^I close group call is full alert$")
	public void ICloseGroupCallIsFullAlert() throws Exception {
		getCallingOverlayPage().closeGroupCallFullAlert();
	}

	/**
	 * Check that answer call alert appears if there is another call during
	 * ongoing call
	 * 
	 * @step. ^I see answer call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I see answer call alert$")
	public void ISeeAnswerCallAlert() throws Exception {
		Assert.assertTrue(getCallingOverlayPage().isAnswerCallAlertVisible());
	}

	/**
	 * Cancels another call on answer call alert
	 * 
	 * @step. ^I cancel new call from answer call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I cancel new call from answer call alert$")
	public void ICancelNewCallFromAlert() throws Exception {
		getCallingOverlayPage().answerCallCancel();
	}

	/**
	 * Accepts another call on answer call alert
	 * 
	 * @step. ^I start new call from answer call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I start new call from answer call alert$")
	public void IStartNewCallFromAnswerCallAlert() throws Exception {
		getCallingOverlayPage().answerCallContinue();
	}

	/**
	 * Check that end current call alert appears if there is another call during
	 * ongoing call
	 * 
	 * @step. ^I see end current call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I see end current call alert$")
	public void ISeeEndCurrentCallAlert() throws Exception {
		Assert.assertTrue(getCallingOverlayPage()
				.isEndCurrentCallAlertVisible());
	}

	/**
	 * Cancels another call on end current call alert
	 * 
	 * @step. ^I cancel new call from end current call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I cancel new call from end current call alert$")
	public void ICancelNewCallFromEndCurrentCallAlert() throws Exception {
		getCallingOverlayPage().endCurrentCallCancel();
	}

	/**
	 * I start another call on answer call alert
	 * 
	 * @step. ^I start new call from end current call alert$
	 * 
	 * @throws Exception
	 */
	@When("^I start new call from end current call alert$")
	public void IStartNewCallFromEndCurrentCallAlert() throws Exception {
		getCallingOverlayPage().endCurrentCallContinue();
	}

}
