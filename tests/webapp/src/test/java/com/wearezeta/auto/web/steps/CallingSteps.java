package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.calling2.v1.exception.CallingServiceInstanceException;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.management.InstanceNotFoundException;
import org.apache.log4j.Logger;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CallingSteps {

    private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class.getName());
    private static final int FLOW_UPDATE_WAIT_MS = 5000;

    private final TestContext context;

    public CallingSteps() {
        this.context = new TestContext();
    }

    public CallingSteps(TestContext context) {
        this.context = context;
    }

    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().startVideoCallToConversation(context.getUserManager().splitAliases(callerNames),
                conversationName);
    }

    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().callToConversation(context.getUserManager().splitAliases(callerNames),
                conversationName);
    }

    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName)
            throws Exception {
        if (outgoingCall == null) {
            context.getCallingManager().stopIncomingCall(context.getUserManager().splitAliases(instanceUsers));
        } else {
            context.getCallingManager().stopOutgoingCall(context.getUserManager().splitAliases(instanceUsers), conversationName);
        }
    }

    @When("^(.*) declines? calls? from conversation (.*)$")
    public void UserXDeclinesCallFromConversationY(String calleeNames, String conversationName) throws Exception {
        context.getCallingManager().declineIncomingCallToConversation(
                context.getUserManager().splitAliases(calleeNames), conversationName);
    }

    @Then("(.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
            String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        context.getCallingManager().verifyCallingStatus(context.getUserManager().splitAliases(callers), conversationName,
                expectedStatuses, timeoutSeconds);
    }

    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
            String expectedStatuses, int timeoutSeconds) throws Exception {
        context.getCallingManager().verifyAcceptingCallStatus(context.getUserManager().splitAliases(callees),
                expectedStatuses, timeoutSeconds);
    }

    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees,
            String callingServiceBackend) throws Exception {
        context.startPinging();
        context.getCallingManager().startInstances(context.getUserManager().splitAliases(callees), callingServiceBackend,
                String.format("%s_%s", "Webapp", WebAppExecutionContext.getBrowser()), context.getTestname());
        context.stopPinging();
    }

    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video)
            throws Exception {
        if (video == null) {
            context.getCallingManager().acceptNextCall(context.getUserManager().splitAliases(callees));
        } else {
            context.getCallingManager().acceptNextVideoCall(context.getUserManager().splitAliases(callees));
        }

    }

    @Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
            throws Exception {
        for (String callee : context.getUserManager().splitAliases(callees)) {
            final List<Flow> flows = context.getCallingManager().getFlows(callee);
            LOG.info("flows: \n" + flows);
            assertThat("# of flows don't match " + numberOfFlows, flows, hasSize(numberOfFlows));
        }
    }

    @Then("(.*) verif(?:ies|y) that all audio flows have greater than 0 bytes$")
    public void UserXVerifesAllAudioFlowBytesGreaterZero(String callees) throws Exception {
        for (String callee : context.getUserManager().splitAliases(callees)) {
            List<Flow> flows = context.getCallingManager().getFlows(callee);
            for (Flow flow : flows) {
                LOG.info("flows: \n" + flows);
                assertThat("incoming audio bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(0L));
                assertThat("outgoing audio bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    @Then("(.*) verif(?:ies|y) that all video flows have greater than 0 bytes$")
    public void UserXVerifesAllVideoFlowBytesGreaterZero(String callees) throws Exception {
        for (String callee : context.getUserManager().splitAliases(callees)) {
            List<Flow> flows = context.getCallingManager().getFlows(callee);
            for (Flow flow : flows) {
                LOG.info("flows: \n" + flows);
                assertThat("incoming video bytes: \n" + flow, flow.getTelemetry().getStats().getVideo().getBytesReceived(),
                        greaterThan(0L));
                assertThat("outgoing video bytes: \n" + flow, flow.getTelemetry().getStats().getVideo().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    @Then("(.*) verif(?:ies|y) to( not)? get video data from (.*)$")
    public void UserXVerifesToGetVideoDataFromY(String callees, String not, String caller) throws Exception {
        context.startPinging();
        ClientUser sender = context.getUserManager().findUserByNameOrNameAlias(caller);
        List<String> splitAliases = context.getUserManager().splitAliases(callees);
        Map<String, Flow> oldFlows = new HashMap<>();
        Map<String, Flow> newFlows = new HashMap<>();

        for (String callee : splitAliases) {
            oldFlows.putAll(getFlows(callee));
            if (oldFlows.isEmpty()) {
                throw new Exception(String.format("Could not get flows from callee %s with caller %s", callee, sender.getId()));
            }
        }
        LOG.info("old flows: \n" + oldFlows);
        Thread.sleep(FLOW_UPDATE_WAIT_MS);
        for (String callee : splitAliases) {
            newFlows.putAll(getFlows(callee));
        }
        for (Map.Entry<String, Flow> newFlowEntry : newFlows.entrySet()) {
            final Flow newFlow = newFlowEntry.getValue();
            final Flow oldFlow = oldFlows.get(newFlowEntry.getKey());
            if (not == null) {
                assertThat(
                        "There is no video data flowing: \noldFlow: " + oldFlow.getTelemetry().getStats().getVideo() + "\n\nnewFlow: " + newFlow.
                        getTelemetry().getStats().getVideo(),
                        newFlow.getTelemetry().getStats().getVideo().getBytesReceived(),
                        greaterThan(oldFlow.getTelemetry().getStats().getVideo().getBytesReceived()));
            } else {
                assertThat(
                        "There is video data flowing: \noldFlow: " + oldFlow.getTelemetry().getStats().getVideo() + "\n\nnewFlow: " + newFlow.
                        getTelemetry().getStats().getVideo(),
                        newFlow.getTelemetry().getStats().getVideo().getBytesReceived(),
                        equalTo(oldFlow.getTelemetry().getStats().getVideo().getBytesReceived()));
            }
        }
        context.stopPinging();
    }

    @Then("(.*) verif(?:ies|y) to( not)? get audio data from (.*)$")
    public void UserXVerifesToGetAudioDataFromY(String callees, String not, String caller) throws Exception {
        context.startPinging();
        ClientUser sender = context.getUserManager().findUserByNameOrNameAlias(caller);
        List<String> splitAliases = context.getUserManager().splitAliases(callees);
        Map<String, Flow> oldFlows = new HashMap<>();
        Map<String, Flow> newFlows = new HashMap<>();

        for (String callee : splitAliases) {
            oldFlows.putAll(getFlows(callee));
            if (oldFlows.isEmpty()) {
                throw new Exception(String.format("Could not get flows from callee %s with caller %s", callee, sender.getId()));
            }
        }
        LOG.info("old flows: \n" + oldFlows);
        Thread.sleep(FLOW_UPDATE_WAIT_MS);
        for (String callee : splitAliases) {
            newFlows.putAll(getFlows(callee));
        }
        LOG.info("new flows: \n" + newFlows);
        for (Map.Entry<String, Flow> newFlowEntry : newFlows.entrySet()) {
            final Flow newFlow = newFlowEntry.getValue();
            final Flow oldFlow = oldFlows.get(newFlowEntry.getKey());
            if (not == null) {
                assertThat(
                        "There is no audio data flowing: \noldFlow: " + oldFlow.getTelemetry().getStats().getAudio() + "\n\nnewFlow:" + newFlow.
                        getTelemetry().getStats().getAudio(),
                        newFlow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(oldFlow.getTelemetry().getStats().getAudio().getBytesReceived()));
            } else {
                assertThat(
                        "There is audio data flowing: \noldFlow: " + oldFlow.getTelemetry().getStats().getAudio() + "\n\nnewFlow:" + newFlow.
                        getTelemetry().getStats().getAudio(),
                        newFlow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        equalTo(oldFlow.getTelemetry().getStats().getAudio().getBytesReceived()));
            }
        }
        context.stopPinging();
    }

    private Map<String, Flow> getFlows(String callee) throws CallingServiceInstanceException, NoSuchUserException,
            InstanceNotFoundException {
        return context.getCallingManager().getFlows(callee).stream()
                .collect(Collectors.toMap(f -> String.format("%s_%s", callee, f.getMeta().getRemoteUserId()), f -> f));
    }

    @Then("(.*) verif(?:ies|y) that call to conversation (.*) was successful$")
    public void UserXVerifesOutgoingCallWasSuccessful(String callers, String conversation) throws Exception {
        for (Call call : context.getCallingManager().getOutgoingCall(context.getUserManager().splitAliases(callers),
                conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    @Then("(.*) verif(?:ies|y) that incoming call was successful$")
    public void UserXVerifesIncomingCallWasSuccessful(String callees) throws Exception {
        for (Call call : context.getCallingManager().getIncomingCall(context.getUserManager().splitAliases(callees))) {
            assertNotNull("There are no metrics available for this incoming call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Throwable {
        final int flowWaitTime = 3;
        final List<String> calleeList = context.getUserManager().splitAliases(callees);
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
            context.getCallingManager().switchVideoOn(context.getUserManager().splitAliases(callees));
        } else {
            context.getCallingManager().switchVideoOff(context.getUserManager().splitAliases(callees));
        }
    }

    @When("(.*) (maximises|minimises) video call")
    public void UserXResizesVideo(String callees, String toggle) throws Exception {
        if (toggle.equals("maximises")) {
            context.getCallingManager().maximiseVideoCall(context.getUserManager().splitAliases(callees));
        } else {
            context.getCallingManager().minimiseVideoCall(context.getUserManager().splitAliases(callees));
        }
    }
}
