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

    public LocalyticsSteps() {
        this.context = new TestContext();
    }

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

    @Then("^I( do not)? see localytics event (.*) with attributes (.*)$")
    public void ISeeLocalyticsEvent(String doNot, String event, String attributes) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            List<String> localyticsEvents = this.getLocalyticEvents();
            assertThat("Did not find any localytics events in browser console", not(localyticsEvents.isEmpty()));

            if (doNot == null) {
                assertThat("Did not find localytics event " + event + " in browser console", localyticsEvents,
                        hasItem("Localytics event '" + event + "' with attributes: " + attributes));
            } else {
                assertThat("Found localytics event " + event + " in browser console", localyticsEvents,
                        not(hasItem("Localytics event '" + event + "' with attributes: " + attributes)));
            }
        }
    }

    @When("^I remember number of localytics events$")
    public void IRememberLocalyticsEvents() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            rememberedEvents = getLocalyticEvents().size();
        }
    }

    @Then("^There are( no)? added localytics events$")
    public void ThereAreAddedEvents(String no) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            if (rememberedEvents == -1) {
                throw new Exception("Please use step to remember localytics events before this step");
            }
            int newEvents = this.getLocalyticEvents().size();
            if (no == null) {
                assertThat("No new Events happened", rememberedEvents, not(equalTo(newEvents)));
            } else {
                assertThat("New Events happened", rememberedEvents, equalTo(newEvents));
            }
        }
    }

    private List<String> getLocalyticEvents() throws Exception {
        List<String> localyticsEvents = context.getLogManager().getBrowserLog().stream()
                .filter((entry) -> entry.getMessage().contains("Localytics event"))
                .map((entry) -> entry.getMessage().substring(entry.getMessage().lastIndexOf("|") + 2))
                .collect(Collectors.toList());
        localyticsEvents.forEach((localyticsEvent) -> {
            log.info("Found event: " + localyticsEvent);
        });
        return localyticsEvents;
    }
}
