package com.wearezeta.auto.ios.steps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import com.wearezeta.auto.common.CommonCallingSteps2;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallingSteps {

	private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2
			.getInstance();

	/**
	 * Make call to a specific user. You may instantiate more than one incoming
	 * call from single caller by calling this step multiple times
	 *
	 * @step. (.*) calls? (.*) using (\\w+)$
	 *
	 * @param callers
	 *            comma-separated list of caller names/aliases
	 * @param conversationName
	 *            destination conversation name
	 * @param callBackend
	 *            call backend. Available values: 'autocall', 'chrome',
	 *            'firefox'
	 * @throws Exception
	 */
	@When("^(.*) calls? (.*) using (\\w+)$")
	public void UsersCallToUserYUsingCallBackend(String callers,
			String conversationName, String callBackend) throws Exception {
		for (String caller : splitAliases(callers)) {
			commonCallingSteps.callToConversation(caller, conversationName,
					callBackend);
		}
	}

	/**
	 * Stop call on the caller side
	 *
	 * @step. (.*) stops? all calls to (.*)
	 *
	 * @param callers
	 *            comma-separated list of caller names/aliases
	 * @param conversationName
	 *            destination conversation name
	 * @throws Exception
	 */
	@When("(.*) stops? all calls to (.*)")
	public void UsersStopCallsToUserY(String callers, String conversationName)
			throws Exception {
		for (String caller : splitAliases(callers)) {
			commonCallingSteps.stopCall(caller, conversationName);
		}
	}

	/**
	 * Verify whether call status is changed to one of the expected values after
	 * N seconds timeout
	 *
	 * @step. (.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in
	 *        (\\d+) seconds?$
	 *
	 * @param callers
	 *            comma-separated list of caller name/alias
	 * @param conversationName
	 *            destination conversation
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. See
	 *            com.wearezeta.auto.common.calling2.v1.model.CallStatus
	 *            for more details
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
	public void UsersVerifyCallStatusToUserY(String callers,
			String conversationName, String expectedStatuses, int timeoutSeconds)
			throws Exception {
		for (String caller : splitAliases(callers)) {
			commonCallingSteps.verifyCallingStatus(caller, conversationName,
					expectedStatuses, timeoutSeconds);
		}
	}

	/**
	 * Verify whether waiting instance status is changed to one of the expected
	 * values after N seconds timeout
	 *
	 * @step. (.*) verif(?:y|ies) that waiting instance status is changed to
	 *        (.*) in (\\d+) seconds?$
	 *
	 * @param callees
	 *            comma-separated list of callee names/aliases
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. See
	 *            com.wearezeta.auto.common.calling2.v1.model.CallStatus
	 *            for more details
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
	public void UsersVerifyCallStatusToUserY(String callees,
			String expectedStatuses, int timeoutSeconds) throws Exception {
		for (String callee : splitAliases(callees)) {
			commonCallingSteps.verifyAcceptingCallStatus(callee,
					expectedStatuses, timeoutSeconds);
		}
	}

	/**
	 * Execute waiting instance as 'userAsNameAlias' user on calling server
	 * using 'callingServiceBackend' tool
	 *
	 * @step. (.*) starts? waiting instance using (\\w+)$
	 *
	 * @param callees
	 *            callee name/alias. Can be comma-separated list of names
	 * @param callingServiceBackend
	 *            available values: 'blender', 'chrome', * 'firefox'
	 * @throws Exception
	 */
	@When("(.*) starts? waiting instance using (\\w+)$")
	public void UsersStartWaitingInstance(String callees,
			String callingServiceBackend) throws Exception {
		commonCallingSteps.startWaitingInstances(splitAliases(callees),
				callingServiceBackend);
	}

	/**
	 * Automatically accept the next incoming call for the particular users as
	 * soon as it appears in UI. Waiting instance should be already created for
	 * this particular user
	 *
	 * @step. (.*) accepts? next incoming call automatically$
	 *
	 * @param callees
	 *            callee names/aliases, one ore more comma-separated names
	 * @throws Exception
	 */
	@When("(.*) accepts? next incoming call automatically$")
	public void UsersAcceptNextIncomingCallAutomatically(String callees)
			throws Exception {
		for (String callee : splitAliases(callees)) {
			commonCallingSteps.acceptNextCall(callee);
		}
	}

	/**
	 * Close all waiting instances (and incoming calls) for the particular users
	 *
	 * @step. (.*) stops? all waiting instances$
	 *
	 * @param callees
	 *            comma-separated list of callee names/aliases
	 * @throws Exception
	 */
	@When("(.*) stops? all waiting instances$")
	public void UsersStopIncomingCalls(String callees) throws Exception {
		for (String callee : splitAliases(callees)) {
			commonCallingSteps.stopWaitingCall(callee);
		}
	}
}
