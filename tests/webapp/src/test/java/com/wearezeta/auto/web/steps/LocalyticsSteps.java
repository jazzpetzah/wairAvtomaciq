package com.wearezeta.auto.web.steps;

import java.util.List;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;

import com.wearezeta.auto.web.common.WebAppExecutionContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class LocalyticsSteps {

    private static final Logger log = ZetaLogger.getLog(LocalyticsSteps.class.getSimpleName());

    private final TestContext context;

    private int rememberedEvents = -1;

    public LocalyticsSteps(TestContext context) {
        this.context = context;
    }

    @When("^I enable localytics via URL parameter$")
    public void IEnableLocalytics() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            String currentUrl = context.getDriver().getCurrentUrl();
            if (currentUrl.contains("?")) {
                context.getDriver().get(currentUrl + "&localytics");
            } else {
                context.getDriver().get(currentUrl + "?localytics");
            }
        }
    }

    @Then("^I( do not)? see localytics event (.*) without attributes$")
    public void ISeeLocalyticsEvent(String doNot, String event) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            List<String> localyticsEvents = this.getLoggedEvents("localytics");
            if (doNot == null) {
                assertThat("Did not find localytics event " + event + " in browser console", localyticsEvents,
                        hasItem("Localytics event '" + event + "' without attributes\""));
            } else {
                assertThat("Found localytics event " + event + " in browser console", localyticsEvents,
                        not(hasItem("Localytics event '" + event + "' without attributes\"")));
            }
        }
    }

    @Then("^I( do not)? see localytics event (.*) with attributes (.*)$")
    public void ISeeLocalyticsEvent(String doNot, String event, String attributes) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            List<String> localyticsEvents = this.getLoggedEvents("localytics");
            if (doNot == null) {
                assertThat("Did not find localytics event " + event + " in browser console", localyticsEvents,
                        hasItem("Localytics event '" + event + "' with attributes: " + attributes));
            } else {
                assertThat("Found localytics event " + event + " in browser console", localyticsEvents,
                        not(hasItem("Localytics event '" + event + "' with attributes: " + attributes)));
            }
        }
    }

    @When("^I remember number of (localytics|raygun) events$")
    public void IRememberLoggedEvents(String eventType) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            rememberedEvents = getLoggedEvents(eventType).size();
        }
    }

    @Then("^There are( no)? added (localytics|raygun) events$")
    public void ThereAreAddedEvents(String no, String eventType) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            if (rememberedEvents == -1) {
                throw new Exception("Please use step to remember " + eventType + " events before this step");
            }
            int newEvents = this.getLoggedEvents(eventType).size();
            if (no == null) {
                assertThat("No new " + eventType + " Events happened", newEvents, not(equalTo(rememberedEvents)));
            } else {
                assertThat("New " + eventType + " Events happened", newEvents, equalTo(rememberedEvents));
            }
        }
    }

    private List<String> getLoggedEvents(String eventType) throws Exception {
        String event;
        if (eventType.equals("localytics")) {
            event = "Localytics event";
        } else {
            event = "Raygun";
        }
        List<String> loggedEvents = context.getLogManager().getBrowserLog().stream()
                .filter((entry) -> entry.getMessage().contains(event))
                .map((entry) -> entry.getMessage().substring(entry.getMessage().lastIndexOf("|") + 2))
                .collect(Collectors.toList());
        loggedEvents.forEach((localyticsEvent) -> {
            log.info("Found event: " + localyticsEvent);
        });
        return loggedEvents;
    }
}
