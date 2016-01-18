package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import com.wearezeta.auto.common.log.ZetaLogger;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CallingSteps {

	private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class
			.getName());

	private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2
			.getInstance();

	/**
	 * Make call to a specific user. You may instantiate more than one incoming
	 * call from single caller by calling this step multiple times
	 *
	 * @step. (.*) calls (.*) using (.*)$
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
	@When("(.*) calls (.*) using (.*)$")
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
	 * @step. (.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in
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
	@Then("(.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
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
	 * @step. (.*) verif(?:ies|y) that waiting instance status is changed to
	 *        (.*) in * (\\d+) seconds?$
	 *
	 * @param callees
	 *            comma separated list of callee names/aliases
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. See
	 *            com.wearezeta.auto.common.calling2.v1.model.CallStatus for
	 *            more details
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed
	 * @throws Exception
	 */
	@Then("(.*) verif(?:ies|y) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String callees,
			String expectedStatuses, int timeoutSeconds) throws Exception {
		commonCallingSteps.verifyAcceptingCallStatus(splitAliases(callees),
				expectedStatuses, timeoutSeconds);
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

	/**
	 * Execute waiting instance as 'userAsNameAlias' user on calling server
	 * using 'callingServiceBackend' tool
	 *
	 * @step. (.*) starts? waiting instance using (.*)$
	 *
	 * @param callees
	 *            comma separated callee name/alias
	 * @param callingServiceBackend
	 *            available values: 'blender', 'chrome', * 'firefox'
	 * @throws Exception
	 */
	@When("(.*) starts? waiting instance using (.*)$")
	public void UserXStartsWaitingInstance(String callees,
			String callingServiceBackend) throws Exception {
		commonCallingSteps.startWaitingInstances(splitAliases(callees),
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
	 *            comma separated list of callee names/aliases
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
	 * Executes consecutive calls without logging out etc.
	 *
	 * @step. ^I call (\\d+) times with (.*)
	 *
	 * @param times
	 *            number of consecutive calls
	 * @param callees
	 *            participants which will wait for a call
	 * @throws java.lang.Throwable
	 */
	@Then("^I call (\\d+) times with (.*)")
	public void ICallXTimes(int times, String callees) throws Throwable {
		final String MYSELF = "Myself";
		final int flowWaitTime = 3;
		final List<String> calleeList = splitAliases(callees);
		final CommonSteps commonSteps = CommonSteps.getInstance();
		final ConversationPageSteps convSteps = new ConversationPageSteps();
		final CommonCallingSteps2 commonCalling = CommonCallingSteps2
				.getInstance();
		final WarningPageSteps warningSteps = new WarningPageSteps();
		final Map<Integer, Throwable> failures = new HashMap<>();
		for (int i = 0; i < times; i++) {
			LOG.info("\n\nSTARTING CALL " + i);
			try {
				for (String callee : calleeList) {
					UserXAcceptsNextIncomingCallAutomatically(callee);
				}
				LOG.info("All instances are waiting");
				try {
					try {
						warningSteps.ISeeAnotherCallWarningModal("not");
					} catch (Throwable e) {
						warningSteps
								.IClickButtonInAnotherCallWarningModal("End Call");
						LOG.error(e.getMessage());
					}
					convSteps.ICallUser();
					for (String callee : calleeList) {
						UserXVerifesCallStatusToUserY(callee, "active", 60);
					}
					commonSteps.WaitForTime(flowWaitTime);
					for (String callee : calleeList) {
						UserXVerifesHavingXFlows(callee, calleeList.size());
						UserXVerifesHavingXFlows(callee);
					}
					LOG.info("All instances are active");
					convSteps.IWaitForCallingBar(MYSELF);
					LOG.info("Callingbar is visible");
					convSteps.IEndTheCall();
					LOG.info("Terminated call");
					convSteps.IDoNotCallingBar();
					LOG.info("Calling bar is not visible anymore");
					LOG.info("CALL " + i + " SUCCESSFUL");
				} catch (Throwable e) {
					LOG.info("CALL " + i + " FAILED");
					failures.put(i, e);
					try {
						convSteps.IEndTheCall();
						convSteps.IDoNotCallingBar();
					} catch (Throwable ex) {
						LOG.error("Cannot stop call " + i + " " + ex);
					}
				}
				for (String callee : calleeList) {
					commonCalling.stopWaitingCall(callee);
				}
				LOG.info("All instances are stopped");
			} catch (Throwable e) {
				LOG.error("Can not stop waiting call " + i + " " + e);
				try {
					convSteps.IEndTheCall();
					convSteps.IDoNotCallingBar();
				} catch (Throwable ex) {
					LOG.error("Can not stop call " + i + " " + ex);
				}
			}
		}

		LOG.info(failures.size() + " failures happened during " + times
				+ " calls");
		failures.forEach((Integer i, Throwable t) -> {
			LOG.error(i + ": " + t.getMessage());
		});

		for (Map.Entry<Integer, Throwable> entrySet : failures.entrySet()) {
			// will just throw the first exception to indicate failed calls in
			// test results
			throw entrySet.getValue();
		}
		LOG.info(failures.size() + " failures happened during " + times
				+ " calls");
	}
}
