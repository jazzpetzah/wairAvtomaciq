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

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.CallKitOverlayPage;
import com.wearezeta.auto.ios.pages.CallingOverlayPage;
import com.wearezeta.auto.ios.pages.ConversationViewPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

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
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);
        final Map<Integer, Exception> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            LOG.info("\n\nSTARTING CALL " + i);
            try {
                pagesCollection.getPage(ConversationViewPage.class).tapAudioCallButton();
                LOG.info("Pressing Audio button to start call");

                commonCallingSteps.acceptNextCall(calleeList);
                commonCallingSteps.verifyAcceptingCallStatus(calleeList, "active", 20);
                LOG.info("All instances are active");

                pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelVisible();
                LOG.info("Calling overlay is visible");

                commonSteps.WaitForTime(callDurationMinutes * 60);

                pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName("Leave");

                commonCallingSteps.verifyAcceptingCallStatus(calleeList, "destroyed", 20);
                LOG.info("All instances are destroyed");

                pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
                LOG.info("Calling overlay is NOT visible");
                LOG.info("CALL " + i + " SUCCESSFUL");
                commonSteps.WaitForTime(timeBetweenCall);

            } catch (InterruptedException t) {
                LOG.info("CALL " + i + " FAILED");
                LOG.error("Can not stop waiting call " + i + " " + t);
                try {
                    pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName("Leave");
                    pagesCollection.getPage(ConversationViewPage.class).isAudioCallButtonOnToolbarVisible();
                } catch (InterruptedException ex) {
                    LOG.error("Can not stop call " + i + " " + ex);
                }
                failures.put(i, t);
            }

        }

        LOG.info(failures.size() + " failures happened during " + times
                + " calls");
        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(i + ": " + t.getMessage());
        });

        for (Map.Entry<Integer, Exception> entrySet : failures.entrySet()) {
            // will just throw the first exception to indicate failed calls in
            // test results
            throw entrySet.getValue();
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
     * @throws Exception
     * @step. ^(.*) calls me (\d+) times for (\d+) minutes with (.*)$
     */
    @Then("^(\\w+) calls to (\\w+) (\\d+) times? for (\\d+) minutes?$")
    public void IReceiveCallsXTimes(String callees, String conversationName, int times, int callDurationMinutes)
            throws Exception {

        LOG.info("Call setup_time and estab_time array needs to be emptied");
        arrayCallSetupTime.clear();
        arrayCallEstabTime.clear();

        final Map<Integer, Exception> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            receiveOneCallInConsecutiveLoop(callees, i, conversationName, callDurationMinutes, failures);
        }

        int sumCallSetupTime = arrayCallSetupTime.stream().reduce(0, Integer::sum);

        int sumCallEstabTime = arrayCallEstabTime.stream().reduce(0, Integer::sum);

        int avgCallSetupTime = 0;
        int avgCallEstabTime = 0;
        int successfulCallsCount = times - failures.size();
        if (successfulCallsCount > 0) {
           avgCallSetupTime = sumCallSetupTime / successfulCallsCount;
           avgCallEstabTime = sumCallEstabTime / successfulCallsCount;
        }

        createCallingReport(times, failures, avgCallSetupTime, avgCallEstabTime);

        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(i + ": " + t.getMessage());
        });

        failures.forEach((k, v) -> {
            Throwables.propagate(v);
        });
    }

    // this file is needed to report the call stats via jenkins into a read only chat
    private static final String CALL_STATS_FILENAME = "multi_call_result.txt";

    private void createCallingReport(int timesOfCalls, final Map<Integer, Exception> failures, int avgCallSetupTime,
                                     int avgCallEstabTime)
            throws Exception {
        String message = String.format("%s/%s calls succeeded, average Call setup_time: %s ms , average Call estab_time:" +
                " %s ms", timesOfCalls - failures.size(), timesOfCalls, avgCallSetupTime, avgCallEstabTime);
        LOG.info(message);

        Files.write(Paths.get(CommonUtils.getBuildPathFromConfig(CallingSteps.class), CALL_STATS_FILENAME),
                message.getBytes());
    }

    private void receiveOneCallInConsecutiveLoop(String callees, int numberOfCall, String conversationName,
                                                 int callDurationMinutes, final Map<Integer, Exception> failures)
            throws Exception {
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);

        LOG.info("\n\nSTARTING CALL " + numberOfCall);
        try {
            pagesCollection.getCommonPage().pressHomeButton();
            LOG.info("Put app into background");

            commonCallingSteps.callToConversation(calleeList, conversationName);

            pagesCollection.getPage(CallKitOverlayPage.class).isVisible("Audio");
            LOG.info("Audio Call Kit overlay is visible");

            pagesCollection.getPage(CallKitOverlayPage.class).tapButton("Accept");

            if (numberOfCall == 0) {
                pagesCollection.getCommonPage().acceptAlert();
            }

            commonCallingSteps.verifyCallingStatus(calleeList, conversationName, "active", 20);
            LOG.info("All instances are active");

            commonSteps.WaitForTime(callDurationMinutes * 60);

            pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName("Leave");
            pagesCollection.getPage(CallingOverlayPage.class).isCallStatusLabelInvisible();
            LOG.info("Calling overlay is NOT visible");

            for (String callee : calleeList) {
                UserXVerifesCallWasSuccessful(callee, conversationName);
                for (Call call : commonCallingSteps.getOutgoingCall(calleeList, conversationName)) {
                    arrayCallSetupTime.add(((int) call.getMetrics().getSetupTime()));
                    arrayCallEstabTime.add(((int) call.getMetrics().getEstabTime()));
                }
            }

            LOG.info("CALL " + numberOfCall + " SUCCESSFUL");
            commonSteps.WaitForTime(timeBetweenCall);

        } catch (Exception t) {
            LOG.info("CALL " + numberOfCall + " FAILED");

            try {
                pagesCollection.getPage(CallingOverlayPage.class).tapButtonByName("Leave");
            } catch (Exception ex) {
                LOG.error("Can not stop call " + numberOfCall + " " + ex);
                try {
                    pagesCollection.getPage(CallKitOverlayPage.class).tapButton("Decline");
                } catch (Exception exe) {
                    LOG.error("Can not stop call kit " + numberOfCall + " " + exe);
                }
            }
            failures.put(numberOfCall, t);
        }
    }
}
