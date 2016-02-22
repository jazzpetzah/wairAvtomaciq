package com.wearezeta.auto.android_tablet.steps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();

    {
        commonCallingSteps.setAutocallVersion("2.1");
    }

    /**
     * Make call to a specific user. You may instantiate more than one incoming
     * call from single caller by calling this step multiple times
     *
     * @param callers          comma-separated list of caller names/aliases
     * @param conversationName destination conversation name
     * @param callBackend      call backend. Available values: 'autocall', 'chrome',
     *                         'firefox'
     * @throws Exception
     * @step. (.*) calls? (.*) using (\\w+)$
     */
    @When("^(.*) calls? (.*) using (\\w+)$")
    public void UsersCallToUserYUsingCallBackend(String callers,
                                                 String conversationName, String callBackend) throws Exception {
        for (String caller : splitAliases(callers)) {
            commonCallingSteps.callToConversation(caller, conversationName, callBackend);
        }
    }

    /**
     * Stop call on the caller side
     *
     * @param callers          comma-separated list of caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (.*) stops? all calls to (.*)
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
     * @param callers          comma-separated list of caller name/alias
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in
     * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UsersVerifyCallStatusToUserY(String callers,
                                             String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        for (String caller : splitAliases(callers)) {
            commonCallingSteps.verifyCallingStatus(caller, conversationName, expectedStatuses, timeoutSeconds);
        }
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected
     * values after N seconds timeout
     *
     * @param callees          comma-separated list of callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:y|ies) that waiting instance status is changed to
     * (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UsersVerifyCallStatusToUserY(String callees,
                                             String expectedStatuses, int timeoutSeconds) throws Exception {
        commonCallingSteps.verifyAcceptingCallStatus(splitAliases(callees), expectedStatuses, timeoutSeconds);
    }

    /**
     * Execute waiting instance as 'userAsNameAlias' user on calling server
     * using 'callingServiceBackend' tool
     *
     * @param callees               callee name/alias. Can be comma-separated list of names
     * @param callingServiceBackend available values: 'blender', 'chrome', * 'firefox'
     * @throws Exception
     * @step. (.*) starts? waiting instance using (\\w+)$
     */
    @When("(.*) starts? waiting instance using (\\w+)$")
    public void UsersStartWaitingInstance(String callees,
                                          String callingServiceBackend) throws Exception {
        commonCallingSteps.startWaitingInstances(splitAliases(callees), callingServiceBackend);
    }

    /**
     * Automatically accept the next incoming call for the particular users as
     * soon as it appears in UI. Waiting instance should be already created for
     * this particular user
     *
     * @param callees callee names/aliases, one ore more comma-separated names
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming call automatically$")
    public void UsersAcceptNextIncomingCallAutomatically(String callees) throws Exception {
        commonCallingSteps.acceptNextCall(splitAliases(callees));
    }

    /**
     * Close all waiting instances (and incoming calls) for the particular users
     *
     * @param callees comma-separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) stops? all waiting instances$
     */
    @When("(.*) stops? all waiting instances$")
    public void UsersStopIncomingCalls(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            commonCallingSteps.stopWaitingCall(callee);
        }
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
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows) throws Exception {
        for (String callee : splitAliases(callees)) {
            assertThat(commonCallingSteps.getFlows(callee), hasSize(numberOfFlows));
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
                assertThat("incoming bytes", flow.getBytesIn(), greaterThan(0L));
                assertThat("outgoing bytes", flow.getBytesOut(), greaterThan(0L));
            }
        }
    }
}
