package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();

    {
        commonCallingSteps.setAutocallVersion("2.1");
    }

    /**
     * Make call to a specific user. You may instantiate more than one incoming
     * call from single caller by calling this step multiple times
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     * @param callBackend      call backend. Available values: 'autocall', 'chrome',
     *                         'firefox'
     * @throws Exception
     * @step. (.*) calls (.*) using (\\w+)$
     */
    @When("(.*) calls (.*) using (\\w+)$")
    public void UserXCallsToUserYUsingCallBackend(String caller,
                                                  String conversationName, String callBackend) throws Exception {
        commonCallingSteps.callToConversation(caller, conversationName, callBackend);
    }

    /**
     * Stop call on the caller side
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (.*) stops? all calls to (.*)
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
     * @param caller           caller name/alias
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in
     * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String caller,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        commonCallingSteps.verifyCallingStatus(caller, conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected
     * values after N seconds timeout
     *
     * @param callees          comma separated list of callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that waiting instance status is changed to
     * (.*) in * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        commonCallingSteps.verifyAcceptingCallStatus(splitAliases(callees), expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify that the instance has X active flows
     *
     * @param callees       comma separated list of callee names/aliases
     * @param numberOfFlows expected number of flows
     * @throws Exception
     * @step. (.*) verif(?:ies|y) to have (\\d+) flows?$
     */
    @Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
            throws Exception {
        for (String callee : splitAliases(callees)) {
            int actualFlowNumber = commonCallingSteps.getFlows(callee).size();
            Assert.assertTrue(String.format("Expected flows number is : %s but actual flows number was : %s", numberOfFlows, actualFlowNumber), actualFlowNumber == numberOfFlows);
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes
     * running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            for (Flow flow : commonCallingSteps.getFlows(callee)) {
                Assert.assertTrue("There is no incoming bytes", flow.getBytesIn() > 0L);
                Assert.assertTrue("There is no outgoing bytes", flow.getBytesOut() > 0L);
            }
        }
    }

    /**
     * Execute waiting instance as 'userAsNameAlias' user on calling server
     * using 'callingServiceBackend' tool
     *
     * @param callees               comma separated callee name/alias
     * @param callingServiceBackend available values: 'blender', 'chrome', * 'firefox'
     * @throws Exception
     * @step. (.*) starts? waiting instance using (\\w+)$
     */
    @When("(.*) starts? waiting instance using (\\w+)$")
    public void UserXStartsWaitingInstance(String callees,
                                           String callingServiceBackend) throws Exception {
        commonCallingSteps.startWaitingInstances(splitAliases(callees), callingServiceBackend);
    }

    /**
     * Automatically accept the next incoming call for the particular user as
     * soon as it appears in UI. Waiting instance should be already created for
     * this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees) throws Exception {
        commonCallingSteps.acceptNextCall(splitAliases(callees));
    }

    /**
     * Automatically accept the next incoming video call for the particular user
     * as soon as it appears in UI. Waiting instance should be already created
     * for this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming video call automatically$
     */
    @When("(.*) accepts? next incoming video call automatically$")
    public void UserXAcceptsNextIncomingVideoCallAutomatically(String callees) throws Exception {
        commonCallingSteps.acceptNextVideoCall(splitAliases(callees));
    }

    /**
     * Make a video call to a specific user.
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     * @param callBackend      call backend. Available values: 'autocall', 'chrome',
     *                         'firefox'
     * @throws Exception
     * @step. (.*) starts a video call to (.*) using (.*)$
     */
    @When("(.*) starts a video call to (.*) using (.*)$")
    public void UserXStartVideoCallsToUserYUsingCallBackend(String caller,
                                                            String conversationName, String callBackend) throws Exception {
        commonCallingSteps.startVideoCallToConversation(caller, conversationName, callBackend);
    }
}
