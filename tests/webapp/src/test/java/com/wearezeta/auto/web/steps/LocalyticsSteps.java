package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.localytics.CommonLocalyticsSteps;
import com.wearezeta.auto.common.localytics.LocalyticsAPIWrappers;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LocalyticsSteps {
	// ID for webapp staging
	private final static String APP_ID = "905792736c9f17c3464fd4e-60d90c82-d14a-11e4-af66-009c5fda0a25";
	// TODO: probably, these ids are the same for other platforms
	// if yes, then it's better to move this stuff to the common config file
	private final static String API_KEY = "5ecfc1339b7d3ece604da2c-449cfea8-b37b-11e4-acba-009c5fda0a25";
	private final static String API_SECRET = "720582f28b9c47b8f24e0a0-449d03c0-b37b-11e4-acba-009c5fda0a25";

	private CommonLocalyticsSteps commonSteps = new CommonLocalyticsSteps(
			APP_ID, new LocalyticsAPIWrappers(API_KEY, API_SECRET));

	@Given("^I take snapshot of (.*) event[s]* count$")
	public void ITakeSnapshotOfXEventCount(String events) throws Exception {
		commonSteps.ITakeSnapshotOfXEventCount(events);
	}

	@Given("^I take snapshot of (.*) attribute[s]* count$")
	public void ITakeSnapshotOfXAttributesCount(String attributes) throws Exception {
		commonSteps.ITakeSnapshotOfXAttributesCount(attributes);
	}
	
	@Then("^I verify the count of (.*) event[s]* ha[sve]+ been increased within (\\d+) second[s]*")
	public void IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
			String events, long secondsTimeout) throws Exception {
		commonSteps.IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
				events, secondsTimeout);
	}
	
	@Then("^I verify the count of (.*) attribute[s]* ha[sve]+ been increased within (\\d+) second[s]*")
	public void IVerifyTheCountOfXAttributesHasBeenIncreasedWithinYSeconds(
			String attributes, long secondsTimeout) throws Exception {
		commonSteps.IVerifyTheCountOfXAttributesHasBeenIncreasedWithinYSeconds(
				attributes, secondsTimeout);
	}
}
