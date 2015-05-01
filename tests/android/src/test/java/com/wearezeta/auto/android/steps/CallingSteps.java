package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonCallingSteps;

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
		PagesCollection.callingOverlayPage = new CallingOverlayPage(
				PagesCollection.loginPage.getLazyDriver());
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
