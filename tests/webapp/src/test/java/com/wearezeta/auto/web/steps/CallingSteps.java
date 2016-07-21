package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CallingSteps {

    private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class
            .getName());

    private final TestContext context;
    
    public CallingSteps() {
        this.context = new TestContext();
    }

    public CallingSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Make video call(s) to one specific conversation.
     *
     * @step. ^(.*) start(?:s|ing) a video call to (.*)$
     *
     * @param callerNames caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().startVideoCallToConversation(splitAliases(callerNames), conversationName);
    }

    /**
     * Make calls to one specific conversation.
     *
     * @step. ^(.*) calls (.*)$
     *
     * @param callerNames caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().callToConversation(splitAliases(callerNames), conversationName);
    }

    /**
     * Stop call (audio and video) on the other side
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
            context.getCallingManager().stopIncomingCall(splitAliases(instanceUsers));
        } else {
            context.getCallingManager().stopOutgoingCall(splitAliases(instanceUsers), conversationName);
        }
    }

    /**
     * Verify whether call status is changed to one of the expected values after N seconds timeout
     *
     * @step. (.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$
     *
     * @param callers callers names/aliases
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     * com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds number of seconds to wait until call status is changed
     * @throws Exception
     */
    @Then("(.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
            String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        context.getCallingManager().verifyCallingStatus(splitAliases(callers), conversationName,
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
        context.getCallingManager().verifyAcceptingCallStatus(splitAliases(callees),
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
        context.startPinging();
        context.getCallingManager().startInstances(splitAliases(callees), callingServiceBackend);
        context.stopPinging();
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
            context.getCallingManager().acceptNextCall(splitAliases(callees));
        } else {
            context.getCallingManager().acceptNextVideoCall(splitAliases(callees));
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
            final List<Flow> flows = context.getCallingManager().getFlows(callee);
            LOG.info("flows: \n"+ flows);
            assertThat("# of flows don't match "+numberOfFlows , flows, hasSize(numberOfFlows));
        }
    }
    
    /**
     * Verify that each audio flow of the instance had incoming and outgoing bytes running over the line
     *
     * @step. (.*) verif(?:ies|y) that all audio flows have greater than 0 bytes$
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     */
    @Then("(.*) verif(?:ies|y) that all audio flows have greater than 0 bytes$")
    public void UserXVerifesAllAudioFlowBytesGreaterZero(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            List<Flow> flows = context.getCallingManager().getFlows(callee);
            for (Flow flow : flows) {
                LOG.info("flows: \n"+flows);
                assertThat("incoming audio bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesReceived(), greaterThan(0L));
                assertThat("outgoing audio bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesSent(), greaterThan(0L));
            }
        }
    }
    
    /**
     * Verify that each video flow of the instance had incoming and outgoing bytes running over the line
     *
     * @step. (.*) verif(?:ies|y) that all video flows have greater than 0 bytes$
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     */
    @Then("(.*) verif(?:ies|y) that all video flows have greater than 0 bytes$")
    public void UserXVerifesAllVideoFlowBytesGreaterZero(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            List<Flow> flows = context.getCallingManager().getFlows(callee);
            for (Flow flow : flows) {
                LOG.info("flows: \n"+flows);
                assertThat("incoming video bytes: \n" + flow, flow.getTelemetry().getStats().getVideo().getBytesReceived(), greaterThan(0L));
                assertThat("outgoing video bytes: \n" + flow, flow.getTelemetry().getStats().getVideo().getBytesSent(), greaterThan(0L));
            }
        }
    }

    /**
     * Verify that each call of the instances was successful
     *
     * @step. (.*) verif(?:ies|y) that call to conversation (.*) was successful$
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     */
    @Then("(.*) verif(?:ies|y) that call to conversation (.*) was successful$")
    public void UserXVerifesCallWasSuccessful(String callees, String conversation) throws Exception {
        for (Call call : context.getCallingManager().getOutgoingCall(splitAliases(callees), conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    /**
     * Executes consecutive calls without logging out etc.
     *
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     *
     * @param callDurationMinutes
     * @param times number of consecutive calls
     * @param callees participants which will wait for a call
     * @throws java.lang.Throwable
     */
    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Throwable {
        final int flowWaitTime = 3;
        final List<String> calleeList = splitAliases(callees);
        final ConversationPageSteps convSteps = new ConversationPageSteps(context);
        final CommonCallingSteps2 commonCalling = context.getCallingManager();
        final WarningPageSteps warningSteps = new WarningPageSteps(context);
        final CallPageSteps callPageSteps = new CallPageSteps(context);
        final Map<Integer, Throwable> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            LOG.info("\n\nSTARTING CALL " + i);
            try {
                for (String callee : calleeList) {
                    UserXAcceptsNextIncomingCallAutomatically(callee, null);
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
                    Thread.sleep(flowWaitTime * 1000);
                    int totalFlowChecks = callDurationMinutes * 4;
                    Map<String, Flow> flows = new HashMap<>();
                    LOG.info(totalFlowChecks + " checks");
                    for (int j = totalFlowChecks; j > 0; j--) {
                        LOG.info("checking flows   " + j);
                        for (String callee : calleeList) {
                            getCheckAndCompareFlows(flows, callee, flowWaitTime);
                        }
                        LOG.info("All instances are active");
                        callPageSteps.ISeeCallControlsForConversation(null, null, callees);
                        long flowCheckInterval = (callDurationMinutes * 60 * 1000)
                                / totalFlowChecks;
                        LOG.info("Waiting for " + flowCheckInterval + "ms ...");
                        Thread.sleep(flowCheckInterval);
                        LOG.info("!");
                    }

                    LOG.info("All instances are active");
                    callPageSteps.ISeeCallControlsForConversation(null, null, callees);
                    LOG.info("Callingbar is visible");
                    callPageSteps.IClickEndCallButton(callees);
                    LOG.info("Terminated call");
                    callPageSteps.ISeeCallControlsForConversation("not", null, callees);
                    LOG.info("Calling bar is not visible anymore");
                    LOG.info("CALL " + i + " SUCCESSFUL");
                } catch (Throwable e) {
                    LOG.info("CALL " + i + " FAILED");
                    failures.put(i, e);
                    try {
                        callPageSteps.IClickEndCallButton(callees);
                        callPageSteps.ISeeCallControlsForConversation("not", null, callees);
                    } catch (Throwable ex) {
                        LOG.error("Cannot stop call " + i + " " + ex);
                    }
                }
                commonCalling.stopIncomingCall(calleeList);
                LOG.info("All instances are stopped");
            } catch (Throwable e) {
                LOG.error("Can not stop waiting call " + i + " " + e);
                try {
                    callPageSteps.IClickEndCallButton(callees);
                    callPageSteps.ISeeCallControlsForConversation("not", null, callees);
                } catch (Throwable ex) {
                    LOG.error("Can not stop call " + i + " " + ex);
                }
            }
            LOG.info("Waiting end...");
            Thread.sleep(10000);
            LOG.info("!");
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

    private void getCheckAndCompareFlows(Map<String, Flow> flowMap,
            String callee, int participantSize) throws Exception {
        UserXVerifesHavingXFlows(callee, participantSize);
        for (Flow flow : context.getCallingManager().getFlows(callee)) {
            Flow oldFlow = flowMap.get(callee + flow.getMeta().getRemoteUserId());
            if (oldFlow != null) {
                assertThat("incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(oldFlow.getTelemetry().getStats().getAudio().getBytesReceived()));
                assertThat("outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(oldFlow.getTelemetry().getStats().getAudio().getBytesSent()));
            }
            flowMap.put(callee + flow.getMeta().getRemoteUserId(), flow);
        }
    }

    @When("(.*) switch(?:es) video (off|on)$")
    public void UserXSwitchesVideo(String callees, String toggle) throws Exception {
        if (toggle.equals("on")) {
            context.getCallingManager().switchVideoOn(splitAliases(callees));
        } else {
            context.getCallingManager().switchVideoOff(splitAliases(callees));
        }
    }
}
