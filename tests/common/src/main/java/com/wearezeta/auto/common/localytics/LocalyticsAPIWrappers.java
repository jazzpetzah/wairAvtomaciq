package com.wearezeta.auto.common.localytics;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public final class LocalyticsAPIWrappers {
	private LocalyticsToken token;

	public LocalyticsAPIWrappers(String apiKey, String apiSecret) {
		this.token = new LocalyticsToken(apiKey, apiSecret);
	}

	private static JSONArray stringsArrayToJSONArray(String[] arrIn) {
		JSONArray result = new JSONArray();
		for (String item : arrIn) {
			result.put(item);
		}
		return result;
	}

	private static String dateToLocalyticsShortTimestamp(Date dt) {
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(dt);
	}

	public int getNumberOfEventOccurencesPerPeriod(String appId,
			String eventName, Date periodStart, Date periodEnd)
			throws Exception {
		JSONObject queryData = new JSONObject();
		queryData
				.put("app_id", stringsArrayToJSONArray(new String[] { appId }));
		queryData.put("metrics",
				stringsArrayToJSONArray(new String[] { "occurrences" }));
		// https://api.localytics.com/docs#dimensions
		queryData.put("dimensions",
				stringsArrayToJSONArray(new String[] { "event_name" }));
		JSONObject conditionsData = new JSONObject();
		JSONArray dayPeriodDefinition = stringsArrayToJSONArray(new String[] {
				"between", dateToLocalyticsShortTimestamp(periodStart),
				dateToLocalyticsShortTimestamp(periodEnd) });
		conditionsData.put("day", dayPeriodDefinition);
		queryData.put("conditions", conditionsData);

		final JSONObject result = LocalyticsRestAPI.query(token, queryData);
		if (result.has("results")) {
			JSONArray results = result.getJSONArray("results");
			int numberOfOccurences = 0;
			for (int idx = 0; idx < results.length(); idx++) {
				if (results.getJSONObject(idx).getString("event_name")
						.equals(eventName)) {
					numberOfOccurences += results.getJSONObject(idx).getInt(
							"occurrences");
				}
			}
			return numberOfOccurences;
		} else {
			throw new RuntimeException("No resusts were returned!");
		}
	}

	public int getNumberOfEventAttributeOccurencesPerPeriod(String appId,
			String eventName, String attributeName, Date periodStart,
			Date periodEnd) throws Exception {
		JSONObject queryData = new JSONObject();
		queryData
				.put("app_id", stringsArrayToJSONArray(new String[] { appId }));
		// https://api.localytics.com/docs#query-attribute-explorer
		final String caninicalMetricsName = String.format("sum(n:%s)",
				attributeName);
		queryData.put("metrics",
				stringsArrayToJSONArray(new String[] { caninicalMetricsName }));
		queryData.put("dimensions",
				stringsArrayToJSONArray(new String[] { "day" }));
		JSONObject conditionsData = new JSONObject();
		conditionsData.put("event_name", eventName);
		JSONArray dayPeriodDefinition = stringsArrayToJSONArray(new String[] {
				"between", dateToLocalyticsShortTimestamp(periodStart),
				dateToLocalyticsShortTimestamp(periodEnd) });
		conditionsData.put("day", dayPeriodDefinition);
		queryData.put("conditions", conditionsData);

		final JSONObject result = LocalyticsRestAPI.query(token, queryData);
		if (result.has("results")) {
			JSONArray results = result.getJSONArray("results");
			int numberOfOccurences = 0;
			for (int idx = 0; idx < results.length(); idx++) {
				if (results.getJSONObject(idx).has(caninicalMetricsName)) {
					numberOfOccurences += results.getJSONObject(idx).getInt(
							caninicalMetricsName);
				}
			}
			return numberOfOccurences;
		} else {
			throw new RuntimeException("No resusts were returned!");
		}
	}
}
