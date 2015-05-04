package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.calling.IncomingCallPage;
import com.wearezeta.auto.osx.pages.calling.StartedCallPage;
import com.wearezeta.auto.osx.pages.floating.CallingFloatingPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallPageSteps {

	ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CallPageSteps.class
			.getSimpleName());

	/**
	 * Accepts visible incoming call
	 * 
	 * @step. ^I accept call$
	 *
	 * @throws Exception
	 */
	@When("^I accept call$")
	public void IAcceptCall() throws Exception {
		PagesCollection.callPage = ((IncomingCallPage) PagesCollection.callPage)
				.joinCall();
	}

	/**
	 * Ignore current call
	 * 
	 * @step. ^I ignore call$
	 *
	 * @throws Exception
	 */
	@When("^I ignore call$")
	public void IIgnoreCall() throws Exception {
		((IncomingCallPage) PagesCollection.callPage).ignoreCall();
		PagesCollection.callPage = null;
	}

	/**
	 * Cancel current call
	 * 
	 * @step. ^I cancel call$
	 */
	@When("^I cancel call$")
	public void ICancelCall() {
		((StartedCallPage) PagesCollection.callPage).cancelCallButton();
		PagesCollection.callPage = null;
	}

	/**
	 * Mute or unmute microphone during call
	 * 
	 * @step. ^I change microphone state$
	 */
	@When("^I change microphone state$")
	public void IChangeMicrophoneState() {
		((StartedCallPage) PagesCollection.callPage).toggleMute();
	}

	/**
	 * Checks that microphone muted in call
	 * 
	 * @step. ^I see microphone muted$
	 * 
	 * @throws AssertionError
	 *             if microphone is not muted
	 */
	@When("^I see microphone muted$")
	public void ISeeMicrophoneMuted() {
		Assert.assertTrue("Microphone is not muted, but expected to be",
				((StartedCallPage) PagesCollection.callPage)
						.isMicrophoneMuted());
	}

	/**
	 * Checks that microphone unmuted in call
	 * 
	 * @step. ^I see microphone unmuted$
	 * 
	 * @throws AssertionError
	 *             if microphone is muted
	 */
	@When("^I see microphone unmuted$")
	public void ISeeMicrophoneUnmuted() {
		Assert.assertFalse("Microphone is muted, but expected not to be",
				((StartedCallPage) PagesCollection.callPage)
						.isMicrophoneMuted());
	}

	/**
	 * Checks that incoming call bar appears in UI
	 * 
	 * @step. ^I see incoming call from (.*)$
	 * 
	 * @param contact
	 *            conversation with call
	 * 
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no incoming call in UI
	 */
	@When("^I see incoming call from (.*)$")
	public void ISeeIncomingCallFrom(String contact) throws Exception {
		PagesCollection.callPage = (IncomingCallPage) PagesCollection.mainMenuPage
				.instantiatePage(IncomingCallPage.class);
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(((IncomingCallPage) PagesCollection.callPage)
				.isIncomingCallVisible(contact.toUpperCase()));
	}

	/**
	 * Checks that pending call bar appears in UI
	 * 
	 * @step. ^I see pending call to (.*)$
	 * 
	 * @param contact
	 *            conversation with call
	 * 
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no pending call in UI
	 */
	@When("^I see pending call to (.*)$")
	public void ISeePendingCallFrom(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(((StartedCallPage) PagesCollection.callPage)
				.isPendingCallVisible(contact.toUpperCase()));
	}

	/**
	 * Checks that started call bar appears in UI
	 * 
	 * @step. ^I see ongoing call with (.*)$
	 * 
	 * @param contact
	 *            conversation with call
	 * 
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no ongoing call in UI
	 */
	@Then("^I see ongoing call with (.*)$")
	public void ISeeOngoingCallWithContact(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(((StartedCallPage) PagesCollection.callPage)
				.isOngoingCallVisible(contact.toUpperCase()));
	}

	/**
	 * Checks that incoming call popup appears when Wire is minimized
	 * 
	 * @step. ^I see incoming call popup from (.*)$
	 * 
	 * @param contact
	 *            conversation with call
	 * 
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if there is no ongoing call in UI
	 */
	@When("^I see incoming call popup from (.*)$")
	public void ISeeIncomingCallFromContactWhileMinimized(String contact)
			throws Exception {
		PagesCollection.callingFloatingPage = (CallingFloatingPage) PagesCollection.contactListPage
				.instantiatePage(CallingFloatingPage.class);
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.callingFloatingPage
				.isCallFromUserVisible(contact));
	}

	/**
	 * Answer call from popup when Wire is minimized
	 * 
	 * @step. ^I answer call from popup$
	 * 
	 * @throws Exception
	 */
	@When("^I answer call from popup$")
	public void IAnswerCallWhileMinimized() throws Exception {
		PagesCollection.callPage = PagesCollection.callingFloatingPage
				.answerCall();
	}
}
