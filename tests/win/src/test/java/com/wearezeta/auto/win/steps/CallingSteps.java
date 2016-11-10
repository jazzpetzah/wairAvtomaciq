package com.wearezeta.auto.win.steps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CallingSteps {

    private final WrapperTestContext context;

    public CallingSteps() {
        this.context = new WrapperTestContext();
    }

    public CallingSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().startVideoCallToConversation(splitAliases(callerNames), conversationName);
    }

    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        context.getCallingManager().callToConversation(splitAliases(callerNames), conversationName);
    }

    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName) throws Exception {
        if (outgoingCall == null) {
            context.getCallingManager().stopIncomingCall(splitAliases(instanceUsers));
        } else {
            context.getCallingManager().stopOutgoingCall(splitAliases(instanceUsers), conversationName);
        }
    }

    @Then("(.*) verif(?:y|ies) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers, String conversationName, String expectedStatuses,
            int timeoutSeconds) throws Exception {
        context.getCallingManager().verifyCallingStatus(splitAliases(callers), conversationName,
                expectedStatuses, timeoutSeconds);
    }

    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees, String expectedStatuses, int timeoutSeconds) throws Exception {
        context.getCallingManager().verifyAcceptingCallStatus(splitAliases(callees), expectedStatuses, timeoutSeconds);
    }

    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees, String callingServiceBackend) throws Exception {
        context.getCallingManager().startInstances(splitAliases(callees), callingServiceBackend, "Win_Wrapper", ZetaFormatter.
                getScenario());
    }

    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video) throws Exception {
        if (video == null) {
            context.getCallingManager().acceptNextCall(splitAliases(callees));
        } else {
            context.getCallingManager().acceptNextVideoCall(splitAliases(callees));
        }
    }

    @Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows) throws Exception {
        for (String callee : splitAliases(callees)) {
            final List<Flow> flows = context.getCallingManager().getFlows(callee);
            assertThat("existing flows: \n" + flows, flows, hasSize(numberOfFlows));
        }
    }

    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : splitAliases(callees)) {
            for (Flow flow : context.getCallingManager().getFlows(callee)) {
                assertThat("incoming bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesReceived(),
                        greaterThan(0L));
                assertThat("outgoing bytes: \n" + flow, flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    @When("(.*) (maximises|minimises) video call")
    public void UserXResizesVideo(String callees, String toggle) throws Exception {
        if (toggle.equals("maximises")) {
            context.getCallingManager().maximiseVideoCall(splitAliases(callees));
        } else {
            context.getCallingManager().minimiseVideoCall(splitAliases(callees));
        }
    }

}
