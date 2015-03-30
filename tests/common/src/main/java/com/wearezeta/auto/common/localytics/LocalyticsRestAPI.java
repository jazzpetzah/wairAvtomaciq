package com.wearezeta.auto.common.localytics;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;

/**
 * https://api.localytics.com/docs
 * 
 */
final class LocalyticsRestAPI {

	private static final Logger log = ZetaLogger.getLog(LocalyticsRestAPI.class
			.getSimpleName());

	private static final String BASE_URL = "https://api.localytics.com/v1";
	private static final String MEDIA_TYPE_LOCALYTICS_JSON = "application/vnd.localytics.v1+hal+json";
	private static final String QUERY_ENDPOINT = "query";

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws BackendRequestException {
		if (acceptableResponseCodes.length > 0) {
			boolean isResponseCodeAcceptable = false;
			for (int code : acceptableResponseCodes) {
				if (code == currentResponseCode) {
					isResponseCodeAcceptable = true;
					break;
				}
			}
			if (!isResponseCodeAcceptable) {
				throw new BackendRequestException(
						String.format(
								"Backend request failed. Request return code is: %d. Expected codes are: %s",
								currentResponseCode,
								Arrays.toString(acceptableResponseCodes)),
						currentResponseCode);
			}
		}
	}

	private static String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		Object lock = new Object();
		ClientResponse response;
		synchronized (lock) {
			response = webResource.post(ClientResponse.class, entity);
		}
		log.debug("HTTP POST request(Input data: " + entity + ", Response: "
				+ response.toString() + ")");
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Builder buildDefaultRequest(String restAction,
			LocalyticsToken token) throws Exception {
		final Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(token.getKey(), token
				.getSecret()));
		return client.resource(String.format("%s/%s", BASE_URL, restAction))
				.type(MediaType.APPLICATION_JSON)
				.accept(MEDIA_TYPE_LOCALYTICS_JSON);
	}

	private synchronized static void writeLog(String[] lines) {
		for (String line : lines) {
			log.debug(line);
		}
	}

	public static JSONObject query(LocalyticsToken token, JSONObject requestBody)
			throws Exception {
		Builder webResource = buildDefaultRequest(QUERY_ENDPOINT, token);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Query output from server ....", output });
		return new JSONObject(output);
	}
}
