package com.wearezeta.auto.common.localytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public final class CommonLocalyticsSteps {

	public static final Logger log = ZetaLogger
			.getLog(CommonLocalyticsSteps.class.getSimpleName());

	private Map<String, Integer> eventsCountSnapshot = new HashMap<String, Integer>();
	private Map<String, Date> eventsSnapshotDates = new HashMap<String, Date>();
	private LocalyticsAPIWrappers localyticsApiWrappers;
	private String appId;

	public CommonLocalyticsSteps(String appId,
			LocalyticsAPIWrappers localyticsApiWrappers) {
		this.localyticsApiWrappers = localyticsApiWrappers;
		this.appId = appId;
	}

	private static List<String> splitEvents(String events) {
		return new ArrayList<String>(Arrays.asList(events.split(","))).stream()
				.map(x -> x.trim()).collect(Collectors.toList());
	}

	private static final int MILLISECONDS_IN_TWO_DAYS = 3600 * 24 * 2 * 1000;

	public void ITakeSnapshotOfXEventCount(String events) throws Exception {
		List<String> eventsList = splitEvents(events);
		final Date twoDaysBefore = new Date(new Date().getTime()
				- MILLISECONDS_IN_TWO_DAYS);
		for (String eventName : eventsList) {
			final Date currentTimestamp = new Date();
			eventsSnapshotDates.put(eventName, twoDaysBefore);
			eventsCountSnapshot.put(eventName, localyticsApiWrappers
					.getNumberOfEventOccurencesPerPeriod(appId, eventName,
							twoDaysBefore, currentTimestamp));
			log.debug(String.format("Taken snapshot for '%s' event at %s",
					events, currentTimestamp.toString()));
		}
	}

	private static final int SECONDS_INTERVAL = 5;

	public void IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
			String events, long secondsTimeout) throws Exception {
		List<String> eventsList = splitEvents(events);
		for (String eventName : eventsList) {
			log.debug(String.format("Verifying stats for event '%s'...",
					eventName));
			if (!eventsCountSnapshot.containsKey(eventName)) {
				throw new RuntimeException(String.format(
						"'%s' event has to be snapshotted before", eventName));
			}
			final int snapshottedValue = eventsCountSnapshot.get(eventName);
			log.debug(String
					.format("\tSnapshotted value: %s", snapshottedValue));
			int currentValue = -1;
			long milliSecondsStarted = new Date().getTime();
			do {
				currentValue = localyticsApiWrappers
						.getNumberOfEventOccurencesPerPeriod(appId, eventName,
								eventsSnapshotDates.get(eventName), new Date());
				if (currentValue > snapshottedValue) {
					break;
				}
				Thread.sleep(SECONDS_INTERVAL * 1000);
			} while (new Date().getTime() - milliSecondsStarted < secondsTimeout * 1000);
			log.debug(String.format("\tCurrent value: %s", currentValue));
			if (currentValue <= snapshottedValue) {
				throw new AssertionError(
						String.format(
								"'%s' events count has not been changed within %s second(s) timeout",
								eventName, secondsTimeout));
			}
		}
	}
}
