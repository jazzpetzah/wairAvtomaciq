package com.wearezeta.auto.common.email.handlers;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.json.JSONArray;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

final class RESTMBoxAPI {
	private static final Logger log = ZetaLogger.getLog(RESTMBoxAPI.class
			.getSimpleName());

	private static final String URL_PROTOCOL = "http://";

	public static String getApiRoot() {
		try {
			return String
					.format("%s%s:%s",
							URL_PROTOCOL,
							CommonUtils
									.getDefaultEmailListenerServiceHostFromConfig(RESTMBoxAPI.class),
							CommonUtils
									.getDefaultEmailListenerServicePortFromConfig(RESTMBoxAPI.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws RESTMBoxException {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new RESTMBoxException(
					String.format(
							"Backend request failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	private static final int MAX_RESPONSE_LENGTH = 80;

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws RESTMBoxException {
		ClientResponse response = webResource.get(ClientResponse.class);
		final String responseString = response.getEntity(String.class);
		if (responseString != null
				&& responseString.length() > MAX_RESPONSE_LENGTH) {
			log.debug("HTTP GET request.\nResponse: "
					+ responseString.substring(0, MAX_RESPONSE_LENGTH) + "...");
		} else {
			log.debug("HTTP GET request.\nResponse: " + responseString);
		}
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseString;
	}

	private static Builder buildDefaultRequest(String restAction,
			int timeoutMilliseconds) {
		final String dstUrl = String.format("%s/%s", getApiRoot(), restAction);
		log.debug(String.format("Request to %s...", dstUrl));
		final Client client = Client.create();
		client.setReadTimeout(timeoutMilliseconds);
		return client.resource(dstUrl).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
	}

	public static JSONArray getRecentEmailsForUser(String email, int minCount,
			int maxCount, int timeoutMilliseconds) throws RESTMBoxException {
		Builder webResource = buildDefaultRequest(String.format(
				"recent_emails/%s/%s/%s", email, maxCount, minCount),
				timeoutMilliseconds);
		try {
			final String output = httpGet(webResource,
					new int[] { HttpStatus.SC_OK });
			return new JSONArray(output);
		} catch (ReadTimeoutException e) {
			e.printStackTrace();
			return new JSONArray();
		}
	}
}
