package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.wearezeta.auto.common.ZetaFormatter;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class CallingSteps {
    /**
     * Make audio or video call(s) to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) start(?:s|ing) a video call to (.*)$
     */
    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .startVideoCallToConversation(
                        AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callerNames), conversationName);
    }

    /**
     * Make voice calls to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) calls (.*)$
     */
    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .callToConversation(
                        AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callerNames), conversationName);
    }

    /**
     * Stop call on the other side
     *
     * @param instanceUsers    comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) stops? calls( to (.*))$
     */
    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName)
            throws Exception {
        if (outgoingCall == null) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopIncomingCall(AndroidTabletTestContextHolder.getInstance().getTestContext()
                            .getUsersManager().splitAliases(instanceUsers));
        } else {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .stopOutgoingCall(
                            AndroidTabletTestContextHolder.getInstance().getTestContext()
                                    .getUsersManager().splitAliases(instanceUsers), conversationName);
        }
    }

    /**
     * Verify whether call status is changed to one of the expected values after N seconds timeout
     *
     * @param callers          callers names/aliases
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .verifyCallingStatus(
                        AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callers),
                        conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected values after N seconds timeout
     *
     * @param callees          comma separated callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .verifyAcceptingCallStatus(
                        AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callees), expectedStatuses, timeoutSeconds);
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server using 'callingServiceBackend' tool
     *
     * @param callees               callee name/alias
     * @param callingServiceBackend available values: 'autocall', 'chrome', 'firefox', 'zcall'
     * @throws Exception
     * @step. (.*) starts? instances? using (.*)$
     */
    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees,
                                    String callingServiceBackend) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .startInstances(
                        AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callees),
                callingServiceBackend, "Android_Tablet", ZetaFormatter.getScenario());
    }

    /**
     * Automatically accept the next incoming audio call or for the particular user as soon as it appears in UI. Waiting
     * instance should be already created for this particular user
     *
     * @param callees callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video)
            throws Exception {
        if (video == null) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .acceptNextCall(AndroidTabletTestContextHolder.getInstance().getTestContext()
                            .getUsersManager().splitAliases(callees));
        } else {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .acceptNextVideoCall(AndroidTabletTestContextHolder.getInstance().getTestContext()
                            .getUsersManager().splitAliases(callees));
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
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
            throws Exception {
        for (String callee : AndroidTabletTestContextHolder.getInstance().getTestContext()
                .getUsersManager().splitAliases(callees)) {
            assertThat(AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                            .getFlows(callee), hasSize(numberOfFlows));
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : AndroidTabletTestContextHolder.getInstance().getTestContext()
                .getUsersManager().splitAliases(callees)) {
            for (Flow flow : AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                    .getFlows(callee)) {
                assertThat("incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(0L));
                assertThat("outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    /**
     * Verify that each call of the instances was successful
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call to conversation (.*) was successful$
     */
    @Then("(.*) verif(?:ies|y) that call to conversation (.*) was successful$")
    public void UserXVerifesCallWasSuccessful(String callees, String conversation) throws Exception {
        for (Call call : AndroidTabletTestContextHolder.getInstance().getTestContext().getCallingManager()
                .getOutgoingCall(AndroidTabletTestContextHolder.getInstance().getTestContext()
                                .getUsersManager().splitAliases(callees), conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }
}
