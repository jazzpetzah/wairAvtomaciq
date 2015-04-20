package com.wearezeta.auto.ios;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.ios.pages.IncomingCallPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallingSteps {
	private final CommonCallingSteps commonCallingSteps = CommonCallingSteps
			.getInstance();

	/**
	 * Make call to a specific user. You may instantiate more than one incoming
	 * call from single caller by calling this step multiple times
	 * 
	 * @step. (.*) calls (.*) using (\\w+)$
	 * 
	 * @param userFromNameAlias
	 *            caller name/alias
	 * @param userToNameAlias
	 *            destination name/alias
	 * @param callBackend
	 *            call backend. Available values: 'autocall', 'webdriver'
	 * @throws Exception
	 */
	@When("(.*) calls (.*) using (\\w+)$")
	public void UserXCallsToUserYUsingCallBackend(String userFromNameAlias,
			String userToNameAlias, String callBackend) throws Exception {
		commonCallingSteps.UserXCallsToUserYUsingCallBackend(userFromNameAlias,
				userToNameAlias, callBackend);
		PagesCollection.callPage = new IncomingCallPage(PagesCollection.loginPage.getDriver(), 
				PagesCollection.loginPage.getWait());
	}

	/**
	 * Stop all outgoing calls on the caller side (for all call backends)
	 * 
	 * @step. (.*) stops? all calls to (.*)
	 * 
	 * @param userFromNameAlias
	 *            caller name/alias
	 * @param userToNameAlias
	 *            destination name/alias
	 * @throws Exception
	 */
	@When("(.*) stops? all calls to (.*)")
	public void UserXStopsCallsToUserY(String userFromNameAlias,
			String userToNameAlias) throws Exception {
		commonCallingSteps.UserXStopsCallsToUserY(userFromNameAlias,
				userToNameAlias);
	}

	/**
	 * Verify whether call status is changed to one of the expected values after
	 * N seconds timeout
	 * 
	 * @step. (.*) verifies that call status to (.*) is changed to (.*) in
	 *        (\\d+) seconds?$
	 * 
	 * @param userFromNameAlias
	 *            caller name/alias
	 * @param userToNameAlias
	 *            destination name/alias
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. Available
	 *            values: "starting", "waiting", "active", "active_muted",
	 *            "stopping", "inactive"
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed.
	 * @throws Exception
	 */
	@Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String userFromNameAlias,
			String userToNameAlias, String expectedStatuses, int timeoutSeconds)
			throws Exception {
		commonCallingSteps.UserXVerifesCallStatusToUserY(userFromNameAlias,
				userToNameAlias, expectedStatuses, timeoutSeconds);
	}

	/**
	 * Verify whether waiting instance status is changed to one of the expected
	 * values after N seconds timeout
	 * 
	 * @step. (.*) verifies that waiting instance status is changed to (.*) in
	 *        (\\d+) seconds?$
	 * 
	 * @param userFromNameAlias
	 *            caller name/alias
	 * @param expectedStatuses
	 *            comma-separated list of expected call statuses. Available
	 *            values: "starting", "waiting", "active", "active_muted",
	 *            "stopping", "inactive"
	 * @param timeoutSeconds
	 *            number of seconds to wait until call status is changed. For
	 *            waiting instances this timeout mostly depends on calling
	 *            server hardware resources and it is recommended to set its
	 *            value to 60-90 seconds or even more to keep your tests stable
	 * @throws Exception
	 */
	@Then("(.*) verifies that waiting instance status is changed to (.*) in (\\d+) seconds?$")
	public void UserXVerifesCallStatusToUserY(String userFromNameAlias,
			String expectedStatuses, int timeoutSeconds) throws Exception {
		commonCallingSteps.UserXVerifesWaitingInstanceStatus(userFromNameAlias,
				expectedStatuses, timeoutSeconds);
	}

	/**
	 * Execute waiting instance as 'userAsNameAlias' user on calling server
	 * using 'callingServiceBackend' tool
	 * 
	 * @step. (.*) starts? waiting instance using (\\w+)$
	 * 
	 * @param userAsNameAlias
	 *            call receiver's name/alias
	 * @param callingServiceBackend
	 *            available values: 'blender', 'webdriver'
	 * @throws Exception
	 */
	@When("(.*) starts? waiting instance using (\\w+)$")
	public void UserXStartsWaitingInstance(String userAsNameAlias,
			String callingServiceBackend) throws Exception {
		commonCallingSteps.UserXStartsWaitingInstance(userAsNameAlias,
				callingServiceBackend);
	}

	/**
	 * Automatically accept the next incoming call for the particular user as
	 * soon as it appears in UI. Waiting instance should be already created for
	 * this particular user
	 * 
	 * @step. (.*) accepts? next incoming call automatically$
	 * 
	 * @param userAsNameAlias
	 *            call receiver's name/alias
	 * @throws Exception
	 */
	@When("(.*) accepts? next incoming call automatically$")
	public void UserXAcceptsNextIncomingCallAutomatically(String userAsNameAlias)
			throws Exception {
		commonCallingSteps
				.UserXAcceptsNextIncomingCallAutomatically(userAsNameAlias);
	}

	/**
	 * Close all waiting instances (and incoming calls) for the particular user
	 * 
	 * @step. (.*) stops? all waiting instances$
	 * 
	 * @param userAsNameAlias
	 *            user name/alias
	 * @throws Exception
	 */
	@When("(.*) stops? all waiting instances$")
	public void UserXStopsIncomingCalls(String userAsNameAlias)
			throws Exception {
		commonCallingSteps.UserXStopsIncomingCalls(userAsNameAlias);
	}
}
