package com.wearezeta.auto.android.steps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertThat;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();

    {
        commonCallingSteps.setAutocallVersion("2.1");
    }

    /**
     * Make audio or video call(s) to one specific conversation.
     *
     * @step. ^(.*) start(?:s|ing) a video call to (.*)$
     *
     * @param callerNames caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        commonCallingSteps.startVideoCallToConversation(splitAliases(callerNames), conversationName);
    }

    /**
     * Make voice calls to one specific conversation.
     *
     * @step. ^(.*) calls (.*)$
     *
     * @param callerNames caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        commonCallingSteps.callToConversation(splitAliases(callerNames), conversationName);
    }

    /**
     * Stop call on the other side
     *
     * @step. ^(.*) stops? calls( to (.*))$
     *
     * @param instanceUsers comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName)
            throws Exception {
        if (outgoingCall == null) {
            commonCallingSteps.stopIncomingCall(splitAliases(instanceUsers));
        } else {
            commonCallingSteps.stopOutgoingCall(splitAliases(instanceUsers), conversationName);
        }
    }

    /**
     * Verify whether call status is changed to one of the expected values after N seconds timeout
     *
     * @step. (.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$
     *
     * @param caller callers names/aliases
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     * com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds number of seconds to wait until call status is changed
     * @throws Exception
     */
    @Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
            String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        commonCallingSteps.verifyCallingStatus(splitAliases(callers), conversationName,
                expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected values after N seconds timeout
     *
     * @step. (.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$
     *
     * @param callees comma separated callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     * com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds number of seconds to wait until call status is changed
     * @throws Exception
     */
    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
            String expectedStatuses, int timeoutSeconds) throws Exception {
        commonCallingSteps.verifyAcceptingCallStatus(splitAliases(callees),
                expectedStatuses, timeoutSeconds);
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server using 'callingServiceBackend' tool
     *
     * @step. (.*) starts? instances? using (.*)$
     *
     * @param callees callee name/alias
     * @param callingServiceBackend available values: 'autocall', 'chrome', 'firefox', 'zcall'
     * @throws Exception
     */
    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees,
            String callingServiceBackend) throws Exception {
        commonCallingSteps.startInstances(splitAliases(callees),
                callingServiceBackend);
    }

    /**
     * Automatically accept the next incoming audio call or for the particular user as soon as it appears in UI. Waiting
     * instance should be already created for this particular user
     *
     * @step. (.*) accepts? next incoming call automatically$
     *
     * @param callees callee names/aliases
     * @throws Exception
     */
    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video)
            throws Exception {
        if (video == null) {
            commonCallingSteps.acceptNextCall(splitAliases(callees));
        } else {
            commonCallingSteps.acceptNextVideoCall(splitAliases(callees));
        }

    }

    /**
     * Verify that the instance has X active flows
     *
     * @step. (.*) verif(?:ies|y) to have (\\d+) flows?$
     * @param callees comma separated list of callee names/aliases
     * @param numberOfFlows expected number of flows
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
     * Verify that each flow of the instance had incoming and outgoing bytes running over the line
     *
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     *
     * @param callees comma separated list of callee names/aliases
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
