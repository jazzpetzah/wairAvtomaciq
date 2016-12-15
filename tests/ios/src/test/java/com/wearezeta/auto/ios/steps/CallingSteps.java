package com.wearezeta.auto.ios.steps;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;

import com.wearezeta.auto.common.ZetaFormatter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.wearezeta.auto.common.calling2.v1.model.Metrics;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.CallKitOverlayPage;
import com.wearezeta.auto.ios.pages.CallingOverlayPage;
import com.wearezeta.auto.ios.pages.ConversationViewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private final CommonSteps commonSteps = CommonSteps.getInstance();

    /**
     * Make call to a specific user. You may instantiate more than one incoming
     * call from single caller by calling this step multiple times
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (\w+) calls (\w+)$
     */
    @When("(\\w+) calls (\\w+)$")
    public void UserXCallsToUserYUsingCallBackend(String caller, String conversationName) throws Exception {
        commonCallingSteps.callToConversation(commonCallingSteps.getUsersManager().splitAliases(caller), conversationName);
    }

    /**
     * Stop outgoing or incoming call (audio and video) to the other side
     *
     * @param instanceUsers    comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) stops? (incoming call from|outgoing call to) (.*)
     */
    @When("^(.*) stops? (incoming call from|outgoing call to) (.*)")
    public void UserXStopsIncomingOutgoingCallsToUserY(String instanceUsers, String typeOfCall, String conversationName)
            throws Exception {
        if (typeOfCall.equals("incoming call from")) {
            commonCallingSteps.stopIncomingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers));
        } else {
            commonCallingSteps.stopOutgoingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers), conversationName);
        }
    }


    /**
     * Verify whether call status is changed to one of the expected values after
     * N seconds timeout
     *
     * @param callers          caller names/aliases
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
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        commonCallingSteps.verifyCallingStatus(commonCallingSteps.getUsersManager().splitAliases(callers),
                conversationName, expectedStatuses, timeoutSeconds);
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
        commonCallingSteps.verifyAcceptingCallStatus(commonCallingSteps.getUsersManager().splitAliases(callees),
                expectedStatuses, timeoutSeconds);
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
        for (String callee : commonCallingSteps.getUsersManager().splitAliases(callees)) {
            int actualFlowNumber = commonCallingSteps.getFlows(callee).size();
            Assert.assertTrue(String.format("Expected flows number is : %s but actual flows number was : %s",
                    numberOfFlows, actualFlowNumber), actualFlowNumber == numberOfFlows);
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
        for (String callee : commonCallingSteps.getUsersManager().splitAliases(callees)) {
            for (Flow flow : commonCallingSteps.getFlows(callee)) {
                Assert.assertTrue("There is no incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived() > 0L);
                Assert.assertTrue("There is no outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent() > 0L);
            }
        }
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server
     * using 'callingServiceBackend' tool
     *
     * @param callees               comma separated callee name/alias
     * @param callingServiceBackend available values: 'blender', 'chrome', * 'firefox'
     * @throws Exception
     * @step. (.*) starts? instance using (.*)$
     */
    @When("(.*) starts? instance using (.*)$")
    public void UserXStartsInstance(String callees, String callingServiceBackend) throws Exception {
        commonCallingSteps.startInstances(commonCallingSteps.getUsersManager().splitAliases(callees),
                callingServiceBackend, "iOS", ZetaFormatter.getScenario());
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
        commonCallingSteps.acceptNextCall(commonCallingSteps.getUsersManager().splitAliases(callees));
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
        commonCallingSteps.acceptNextVideoCall(commonCallingSteps.getUsersManager().splitAliases(callees));
    }

    /**
     * Make a video call to a specific user.
     *
     * @param callers          caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. (.*) starts? a video call to (.*)$
     */
    @When("(.*) starts? a video call to (.*)$")
    public void UserXStartVideoCallsToUserYUsingCallBackend(String callers, String conversationName) throws Exception {
        commonCallingSteps.startVideoCallToConversation(commonCallingSteps.getUsersManager().splitAliases(callers),
                conversationName);
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
        for (Call call : commonCallingSteps.getOutgoingCall(commonCallingSteps.getUsersManager().splitAliases(callees),
                conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class
            .getName());

    /**
     * Executes consecutive calls without logging out etc.
     *
     * @param callDurationMinutes
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @throws java.lang.Throwable
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     */
    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Exception {
        final Map<Integer, Exception> failures = new HashMap<>();
        final List<Integer> callsWithoutMetricsData = new ArrayList<>();
        final List<Integer> callsWithoutByteFlowData = new ArrayList<>();
        arrayCallSetupTime.clear();
        arrayCallEstabTime.clear();
        for (int i = 0; i < times; i++) {
            makeSingleCall(callees, i, callDurationMinutes, failures, callsWithoutMetricsData, callsWithoutByteFlowData);
        }

        int avgCallSetupTime = calculateCallStatistics(arrayCallSetupTime, times,failures,callsWithoutMetricsData);
        int avgCallEstabTime = calculateCallStatistics(arrayCallEstabTime, times,failures,callsWithoutMetricsData);
        createCallingReport(times, failures, callsWithoutMetricsData, callsWithoutByteFlowData, avgCallSetupTime, avgCallEstabTime, CallLoopType.Make);

        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(String.format("Failure %s: %s",i,t.getMessage()));
        });

        if (!failures.isEmpty()) {
            Assert.fail(formatFailuresList(failures));
        }
    }

    private static final String CALL_STATUS_DESTROYED = "destroyed";
    private static final String CALL_STATUS_ACTIVE = "active";

    private void makeSingleCall(String callees, int callIndex, int callDurationMinutes,
                                final Map<Integer, Exception> failures, List<Integer> callsWithoutMetricsData,
                                List<Integer> callsWithoutByteFlowData) throws Exception {
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);

        LOG.info("\n\nSTARTING CALL " + callIndex);
        try {
            pagesCollection.getPage(ConversationViewPage.class).tapAudioCallButton();
            LOG.info("Pressing Audio button to start call");

            if (callIndex == 0) {
                pagesCollection.getCommonPage().acceptAlert();
            }

            commonCallingSteps.acceptNextCall(calleeList);
            commonCallingSteps.verifyAcceptingCallStatus(calleeList, CALL_STATUS_ACTIVE, 20);
            LOG.info("All instances are active");

            boolean isVisible = pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelVisible();
            if (!isVisible) {
                throw new CallIterationError(callIndex, "Calling overlay should be visible");
            }
            LOG.info("Calling overlay is visible");

            commonSteps.WaitForTime(callDurationMinutes * 60);
            pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            boolean isInvisible = pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
            if (!isInvisible) {
                throw new CallIterationError(callIndex, "Calling overlay should be invisible");
            }
            LOG.info("Calling overlay is NOT visible");
            commonCallingSteps.verifyAcceptingCallStatus(calleeList, CALL_STATUS_DESTROYED, 20);
            LOG.info("All instances are destroyed");

            for (Call call : commonCallingSteps.getIncomingCall(calleeList)) {
                final Metrics metrics = call.getMetrics();
                if (metrics == null) {
                    LOG.info("Could not get metrics for this call.");
                    callsWithoutMetricsData.add(callIndex);
                } else {
                    final int avgRateU = (int) metrics.getAvgRateU();
                    final int avgRateD = (int) metrics.getAvgRateD();
                    if (avgRateU == -1 || avgRateD == -1) {
                        callsWithoutByteFlowData.add(callIndex);
                    }
                    arrayCallSetupTime.add(((int) metrics.getSetupTime()));
                    arrayCallEstabTime.add(((int) metrics.getEstabTime()));
                }
            }
            LOG.info(String.format("CALL %s SUCCESSFUL", callIndex));
            commonSteps.WaitForTime(timeBetweenCall);

        } catch (InterruptedException ie) {
            Throwables.propagate(ie);
        } catch (Exception t) {
            LOG.info(String.format("CALL %s FAILED", callIndex));
            LOG.error(String.format("Can not stop waiting call %s because %s", callIndex, t.getMessage()));
            try {
                pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            } catch (Exception ex) {
                LOG.error(String.format("Can not stop call kit %s because %s", callIndex, ex));
                try {
                    boolean callButtonVisible = pagesCollection.getPage(ConversationViewPage.class).isAudioCallButtonOnToolbarVisible();
                    if (!callButtonVisible) {
                        throw new CallIterationError(callIndex, "Calling button is not visible");
                    }
                } catch (Exception exe) {
                    LOG.error(String.format("Can not start a new call because %s", ex));
                }
            }
            failures.put(callIndex, t);
        }
    }

    //save the setup time and estab time for every call to calculate the average time
    private List<Integer> arrayCallSetupTime = new ArrayList<>();
    private List<Integer> arrayCallEstabTime = new ArrayList<>();

    /**
     * Receive consecutive calls without logging out etc.
     *
     * @param callDurationMinutes time of the call
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @param conversationName    user to be called
     * @param appState            if App is in BG or FG
     * @throws Exception
     * @step. ^(\w+) calls to (\w+) in (Background|Foreground) (\d+) times? for (\d+) minutes?$
     */
    @Then("^(\\w+) calls to (\\w+) in (Background|Foreground) (\\d+) times? for (\\d+) minutes?$")
    public void IReceiveCallsXTimes(String callees, String conversationName, String appState, int times, int callDurationMinutes)
            throws Exception {
        LOG.info("Call setup_time and estab_time array needs to be emptied");
        arrayCallSetupTime.clear();
        arrayCallEstabTime.clear();

        final Map<Integer, Exception> failures = new HashMap<>();
        final List<Integer> callsWithoutMetricsData = new ArrayList<>();
        final List<Integer> callsWithoutByteFlowData = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            receiveSingleCall(callees, i, appState, conversationName, callDurationMinutes, failures, callsWithoutMetricsData, callsWithoutByteFlowData);
        }

        int avgCallSetupTime = calculateCallStatistics(arrayCallSetupTime, times,failures,callsWithoutMetricsData);
        int avgCallEstabTime = calculateCallStatistics(arrayCallEstabTime, times,failures,callsWithoutMetricsData);
        createCallingReport(times, failures, callsWithoutMetricsData, callsWithoutByteFlowData, avgCallSetupTime, avgCallEstabTime, CallLoopType.Receive);

        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(String.format("Failure %s: %s",i,t.getMessage()));
        });
        if (!failures.isEmpty()) {
            Assert.fail(formatFailuresList(failures));
        }
    }

    private int calculateCallStatistics(List<Integer> arrayCallStatisticTime, int numberOfCalls,
                                        final Map<Integer, Exception> failures,
                                        List<Integer> callsWithoutMetricsData){
        int sumCallStatisticTime = arrayCallStatisticTime.stream().reduce(0, Integer::sum);
        int avgCallStatistic = 0;
        int successfulCallsCount = numberOfCalls - failures.size() - callsWithoutMetricsData.size();
        if (successfulCallsCount > 0) {
            avgCallStatistic = sumCallStatisticTime / successfulCallsCount;
        }
        return avgCallStatistic;
    }

    public enum CallLoopType {
        Receive("RECEIVE"), Make("MAKE");

        private final String name;
        CallLoopType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // this file is needed to report the call stats via jenkins into a read only chat
    private static final String CALL_STATS_FILENAME = "multi_call_result.txt";

    private void createCallingReport(int timesOfCalls, final Map<Integer, Exception> failures,
                                     final List<Integer> callsWithoutMetricsData, final List<Integer> callsWithoutByteFlowData,
                                     int avgCallSetupTime, int avgCallEstabTime, CallLoopType typeOfCall)
            throws Exception {
        String message = String.format("%s CALL LOOP! %s/%s calls succeeded. Got no metrics data from %s calls." +
                        " Average calculated from %s calls. average Call setup_time: %s ms , average Call estab_time: %s ms." +
                        "%s/%s calls had no byte flow during the call.",
                typeOfCall, timesOfCalls - failures.size(), timesOfCalls, callsWithoutMetricsData.size(),
                timesOfCalls - failures.size() - callsWithoutMetricsData.size(), avgCallSetupTime, avgCallEstabTime,
                callsWithoutByteFlowData.size(), timesOfCalls);
        LOG.info(message);

        final String reportContent = String.format("%s=%s", "MULTI_CALL_RESULT", message);
        Files.write(Paths.get(CommonUtils.getBuildPathFromConfig(CallingSteps.class), CALL_STATS_FILENAME),
                reportContent.getBytes());
    }

    private final class CallIterationError extends Exception {
        public CallIterationError(int numberOfCall, String message) {
            super(String.format("Call %s failed with %s", numberOfCall, message));
        }
    }

    private static final String SEPARATOR = "\n--------------------------------------\n";

    private String formatFailuresList(Map<Integer, Exception> failures) {
        final StringBuilder builder = new StringBuilder();
        failures.forEach((callIndex, failureException) -> {
            String header = String.format("Failure summary for call # %s\n", callIndex);
            builder.append(header);
            builder.append(failureException.getMessage()).append("\n");
            if (!(failureException instanceof CallIterationError)) {
                builder.append(ExceptionUtils.getStackTrace(failureException));
            }
            builder.append(SEPARATOR);
        });
        return builder.toString();
    }

    private static final String CALLINGOVERLAY_LEAVE_BUTTON = "Leave";
    private static final String CALLKIT_DECLINE_BUTTON = "Decline";
    private static final String CALLKIT_ACCEPT_BUTTON = "Accept";

    private void receiveSingleCall(String callees, int callIndex, String appState, String conversationName,
                                   int callDurationMinutes, final Map<Integer, Exception> failures,
                                   List<Integer> callsWithoutMetricsData,
                                   List<Integer> callsWithoutByteFlowData) throws Exception {
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);

        LOG.info("\n\nSTARTING CALL " + callIndex);
        try {
            if (appState.equals("Background")) {
                pagesCollection.getCommonPage().pressHomeButton();
                LOG.info("Put app into background");
            }
            commonCallingSteps.callToConversation(calleeList, conversationName);

            boolean isVisible = pagesCollection.getPage(CallKitOverlayPage.class).isVisible("Audio");
            if (!isVisible) {
                throw new CallIterationError(callIndex, "Audio Call Kit overlay should be visible");
            }
            LOG.info("Audio Call Kit overlay is visible");
            pagesCollection.getPage(CallKitOverlayPage.class).tapButton(CALLKIT_ACCEPT_BUTTON);
            if (callIndex == 0) {
                pagesCollection.getCommonPage().acceptAlert();
            }

            commonCallingSteps.verifyCallingStatus(calleeList, conversationName, CALL_STATUS_ACTIVE, 20);
            LOG.info("All instances are active");

            LOG.info("Calling for 1 minute");
            commonSteps.WaitForTime(callDurationMinutes * 60);

            pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            boolean isNotVisible = pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
            if (!isNotVisible) {
                throw new CallIterationError(callIndex, "Audio Call overlay should be invisible");
            }
            LOG.info("Calling overlay is NOT visible");

            for (Call call : commonCallingSteps.getOutgoingCall(calleeList, conversationName)) {
                final Metrics metrics = call.getMetrics();
                if (metrics == null) {
                    LOG.info("Could not get metrics for this call.");
                    callsWithoutMetricsData.add(callIndex);
                    continue;
                }
                final int avgRateU = (int) metrics.getAvgRateU();
                final int avgRateD = (int) metrics.getAvgRateD();
                if (avgRateU == -1 || avgRateD == -1) {
                    callsWithoutByteFlowData.add(callIndex);
                }
                arrayCallSetupTime.add(((int) metrics.getSetupTime()));
                arrayCallEstabTime.add(((int) metrics.getEstabTime()));
            }

            LOG.info(String.format("CALL %s SUCCESSFUL", callIndex));
            commonSteps.WaitForTime(timeBetweenCall);
        } catch (InterruptedException ie) {
            Throwables.propagate(ie);
        } catch (Exception t) {
            LOG.info(String.format("CALL %s FAILED", callIndex));
            try {
                pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName(CALLINGOVERLAY_LEAVE_BUTTON);
            } catch (Exception ex) {
                LOG.error(String.format("Can not stop call %s because %s", callIndex, ex));
                try {
                    pagesCollection.getPage(CallKitOverlayPage.class).tapButton(CALLKIT_DECLINE_BUTTON);
                } catch (Exception exe) {
                    LOG.error(String.format("Can not stop call kit %s because %s", callIndex, exe));
                    try {
                        pagesCollection.getCommonPage().pressHomeButton();
                        LOG.info("Put app into background");
                    } catch (Exception morexe) {
                        LOG.error(String.format("Call %s failed because %s", callIndex, morexe));
                    }
                }
            }
            failures.put(callIndex, t);
        }
    }
}