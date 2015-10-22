package com.wearezeta.auto.android.steps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import static org.junit.Assert.assertThat;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

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
	@When("^(.*) calls (.*) using (\\w+)$")
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
	 * @param callers
	 *            comma separated list of caller names/aliases
	 * @param conversationName
	 *            destination conversation name
	 * @throws Exception
	 */
	@When("(.*) stops? all calls to (.*)")
	public void UserXStopsCallsToUserY(String callers, String conversationName)
			throws Exception {
		for (String caller : splitAliases(callers)) {
			commonCallingSteps.stopCall(caller, conversationName);
		}
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
	 *            comma-separated list of expected call statuses. See
	 *            com.wearezeta.auto.common.calling2.v1.model.CallStatus for
	 *            more details
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
	 * @step. (.*) verif(?:y|ies) that waiting instance status is changed to
	 *        (.*) in (\\d+) seconds?$
	 *
	 * @param callees
	 *            comma separated callee names/aliases
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. See
	 *            com.wearezeta.auto.common.calling2.v1.model.CallStatus for
	 *            more details
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String callees,
			String expectedStatuses, int timeoutSeconds) throws Exception {
		commonCallingSteps.verifyAcceptingCallStatus(splitAliases(callees),
				expectedStatuses, timeoutSeconds);
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
	 * @param callees
	 *            callee names/aliases
	 * @throws Exception
	 */
	@When("(.*) accepts? next incoming call automatically$")
	public void UserXAcceptsNextIncomingCallAutomatically(String callees)
			throws Exception {
		commonCallingSteps.acceptNextCall(splitAliases(callees));
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
	
	/**
	 * Verify that the instance has X active flows
	 * 
	 * @step. (.*) verif(?:ies|y) to have (\\d+) flows?$
	 * @param callees
	 *            comma separated list of callee names/aliases
	 * @param numberOfFlows
	 *            expected number of flows
	 * @throws Exception
	 */
	@Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
	public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
			throws Exception {
		for (String callee : splitAliases(callees)) {
			assertThat(commonCallingSteps.getFlows(callee),
					hasSize(numberOfFlows));
		}
	}

	/**
	 * Verify that each flow of the instance had incoming and outgoing bytes
	 * running over the line
	 *
	 * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
	 * 
	 * @param callees
	 *            comma separated list of callee names/aliases
	 * @throws Exception
	 */
	@Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
	public void UserXVerifesHavingXFlows(String callees) throws Exception {
		for (String callee : splitAliases(callees)) {
			for (Flow flow : commonCallingSteps.getFlows(callee)) {
				assertThat("incoming bytes", flow.getBytesIn(), greaterThan(0L));
				assertThat("outgoing bytes", flow.getBytesOut(),
						greaterThan(0L));
			}
		}
	}
}
