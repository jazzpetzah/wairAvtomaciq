package com.wearezeta.auto.common.localytics;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * https://api.localytics.com/docs
 * 
 */
final class LocalyticsRestAPI {
	private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
			LocalyticsRestAPI::verifyRequestResult);

	private static final String BASE_URL = "https://api.localytics.com/v1";
	private static final String MEDIA_TYPE_LOCALYTICS_JSON = MediaType.APPLICATION_JSON; // "application/vnd.localytics.v1+hal+json";
	private static final String QUERY_ENDPOINT = "query";

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws LocalyticsRESTError {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new LocalyticsRESTError(
					String.format(
							"Localytics API request failed. Return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	private static Builder buildDefaultRequest(String restAction,
			LocalyticsToken token) throws Exception {
		final Client client = ClientBuilder.newClient();
		client.register(HttpAuthenticationFeature.basic(token.getKey(),
				token.getSecret()));
		return client.target(String.format("%s/%s", BASE_URL, restAction))
				.request().accept(MEDIA_TYPE_LOCALYTICS_JSON);
	}

	public static JSONObject query(LocalyticsToken token, JSONObject requestBody)
			throws Exception {
		Builder webResource = buildDefaultRequest(QUERY_ENDPOINT, token);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}
}
