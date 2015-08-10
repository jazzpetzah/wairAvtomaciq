package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.ArrayList;
import java.util.List;

public class CallingSteps {

	private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2
			.getInstance();

	/**
	 * Make call to a specific user. You may instantiate more than one incoming
	 * call from single caller by calling this step multiple times
	 *
	 * @step. (.*) calls (.*) using (\\w+)$
	 *
	 * @param caller
	 *            caller name/alias
	 * @param conversationName
	 *            destination conversation name
	 * @param callBackend
	 *            call backend. Available values: 'autocall', 'chrome',
	 *            'firefox'
	 * @throws Exception
	 */
	@When("(.*) calls (.*) using (\\w+)$")
	public void UserXCallsToUserYUsingCallBackend(String caller,
			String conversationName, String callBackend) throws Exception {
		commonCallingSteps.callToConversation(caller, conversationName,
				callBackend);
	}

	/**
	 * Stop call on the caller side
	 *
	 * @step. (.*) stops? all calls to (.*)
	 *
	 * @param caller
	 *            caller name/alias
	 * @param conversationName
	 *            destination conversation name
	 * @throws Exception
	 */
	@When("(.*) stops? all calls to (.*)")
	public void UserXStopsCallsToUserY(String caller, String conversationName)
			throws Exception {
		commonCallingSteps.stopCall(caller, conversationName);
	}

	/**
	 * Verify whether call status is changed to one of the expected values after
	 * N seconds timeout
	 *
	 * @step. (.*) verifies that call status to (.*) is changed to (.*) in
	 *        (\\d+) seconds?$
	 *
	 * @param caller
	 *            caller name/alias
	 * @param conversationName
	 *            destination conversation
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. Available
	 *            values: "starting", "waiting", "active", "active_muted",
	 *            "stopping", "inactive"
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String caller,
			String conversationName, String expectedStatuses, int timeoutSeconds)
			throws Exception {
		commonCallingSteps.verifyCallingStatus(caller, conversationName,
				expectedStatuses, timeoutSeconds);
	}

	/**
	 * Verify whether waiting instance status is changed to one of the expected
	 * values after N seconds timeout
	 *
	 * @step. (.*) verifies that waiting instance status is changed to (.*) in
	 *        (\\d+) seconds?$
	 *
	 * @param callee
	 *            callee name/alias
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. Available
	 *            values: "starting", "waiting", "active", "active_muted",
	 *            "stopping", "inactive"
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verifies that waiting instance status is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String callee,
			String expectedStatuses, int timeoutSeconds) throws Exception {
		commonCallingSteps.verifyAcceptingCallStatus(callee, expectedStatuses,
				timeoutSeconds);
	}

	/**
	 * Execute waiting instance as 'userAsNameAlias' user on calling server
	 * using 'callingServiceBackend' tool
	 *
	 * @step. (.*) starts? waiting instance using (\\w+)$
	 *
	 * @param callee
	 *            callee name/alias
	 * @param callingServiceBackend
	 *            available values: 'blender', 'chrome', * 'firefox'
	 * @throws Exception
	 */
	@When("(.*) starts? waiting instance using (\\w+)$")
	public void UserXStartsWaitingInstance(String callee,
			String callingServiceBackend) throws Exception {
		commonCallingSteps.startWaitingInstances(splitAliases(callee),
				callingServiceBackend);
	}

	/**
	 * Automatically accept the next incoming call for the particular user as
	 * soon as it appears in UI. Waiting instance should be already created for
	 * this particular user
	 *
	 * @step. (.*) accepts? next incoming call automatically$
	 *
	 * @param callee
	 *            callee name/alias
	 * @throws Exception
	 */
	@When("(.*) accepts? next incoming call automatically$")
	public void UserXAcceptsNextIncomingCallAutomatically(String callee)
			throws Exception {
		commonCallingSteps.acceptNextCall(callee);
	}

	/**
	 * Close all waiting instances (and incoming calls) for the particular user
	 *
	 * @step. (.*) stops? all waiting instances$
	 *
	 * @param callee
	 *            callee name/alias
	 * @throws Exception
	 */
	@When("(.*) stops? all waiting instances$")
	public void UserXStopsIncomingCalls(String callee) throws Exception {
		commonCallingSteps.stopWaitingCall(callee);
	}

	@Then("^I call (\\d+) times")
	public void ICallXTimes(int times) throws Exception, Throwable {
		ConversationPageSteps convSteps = new ConversationPageSteps();
		CommonCallingSteps2 commonCalling = CommonCallingSteps2.getInstance();
		WarningPageSteps warningSteps = new WarningPageSteps();
		List<Throwable> failures = new ArrayList<>();
		for (int i = 0; i < times; i++) {
			System.out.println("\n\n");
			System.out.println("STARTING CALL " + i);
			try {
				UserXAcceptsNextIncomingCallAutomatically("user2Name");
				UserXAcceptsNextIncomingCallAutomatically("user3Name");
				UserXAcceptsNextIncomingCallAutomatically("user4Name");
				UserXAcceptsNextIncomingCallAutomatically("user5Name");
				try {
					try {
						warningSteps.ISeeAnotherCallWarningModal("not");
					} catch (Throwable e) {
						warningSteps
								.IClickButtonInAnotherCallWarningModal("End Call");
						System.err.println(e.getMessage());
					}
					convSteps.ICallUser();
					UserXVerifesCallStatusToUserY("user2Name", "active", 60);
					UserXVerifesCallStatusToUserY("user3Name", "active", 60);
					UserXVerifesCallStatusToUserY("user4Name", "active", 60);
					UserXVerifesCallStatusToUserY("user5Name", "active", 60);
					convSteps.IWaitForCallingBar("user2Name");
					convSteps.IWaitForCallingBar("user3Name");
					convSteps.IWaitForCallingBar("user4Name");
					convSteps.IWaitForCallingBar("user5Name");
					convSteps.IEndTheCall();
					convSteps.IDoNotCallingBar();
					System.out.println("CALL " + i + " SUCCESSFUL");
				} catch (Throwable e) {
					System.out.println("CALL " + i + " FAILED");
					failures.add(e);
					try {
						convSteps.IEndTheCall();
						convSteps.IDoNotCallingBar();
					} catch (Throwable ex) {
						System.err.println("Cannot stop call " + i + " " + ex);
					}
				}
				commonCalling.stopWaitingCall("user2Name");
				commonCalling.stopWaitingCall("user3Name");
				commonCalling.stopWaitingCall("user4Name");
				commonCalling.stopWaitingCall("user5Name");
			} catch (Throwable e) {
				System.err.println("Can not waiting stop call " + i + " " + e);
				try {
					convSteps.IEndTheCall();
					convSteps.IDoNotCallingBar();
				} catch (Throwable ex) {
					System.err.println("Can not stop call " + i + " " + ex);
				}
			}
		}

		System.out.println(failures.size() + " failures happened during "
				+ times + " calls");
		for (Throwable failure : failures) {
			System.err.println(failures.indexOf(failure) + ": "
					+ failure.getMessage());
		}
		System.out.println(failures.size() + " failures happened during "
				+ times + " calls");
	}
}
