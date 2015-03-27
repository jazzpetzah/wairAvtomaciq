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
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
		JSONArray periodDefinition = stringsArrayToJSONArray(new String[] {
				"between", sdf.format(periodStart), sdf.format(periodEnd) });
		conditionsData.put("hour", periodDefinition);
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
				stringsArrayToJSONArray(new String[] { "hour" }));
		JSONObject conditionsData = new JSONObject();
		conditionsData.put("event_name", eventName);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
		JSONArray periodDefinition = stringsArrayToJSONArray(new String[] {
				"between", sdf.format(periodStart), sdf.format(periodEnd) });
		conditionsData.put("hour", periodDefinition);
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
