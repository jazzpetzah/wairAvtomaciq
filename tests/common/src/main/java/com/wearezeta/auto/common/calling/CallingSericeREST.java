package com.wearezeta.auto.common.calling;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.log.ZetaLogger;

final class CallingSericeREST {
	private static final Logger log = ZetaLogger.getLog(CallingSericeREST.class
			.getSimpleName());

	private static final String URL_PROTOCOL = "http://";

	public static String getApiRoot() {
		try {
			return String
					.format("%s%s:%s",
							URL_PROTOCOL,
							CommonUtils
									.getDefaultCallingServiceHostFromConfig(CallingSericeREST.class),
							CommonUtils
									.getDefaultCallingServicePortFromConfig(CallingSericeREST.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static Client client = Client.create();

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws CallingServiceException {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new CallingServiceException(
					String.format(
							"Backend request failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	private static String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws CallingServiceException {
		final ClientResponse response = webResource.post(ClientResponse.class,
				entity);
		final String responseString = response.getEntity(String.class);
		log.debug("HTTP POST request.\nInput data: " + entity.toString()
				+ "\nResponse: " + responseString);
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseString;
	}

	private static String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws CallingServiceException {
		ClientResponse response = webResource.put(ClientResponse.class, entity);
		log.debug("HTTP PUT request.\nInput data: " + entity.toString());
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		try {
			final String responseString = response.getEntity(String.class);
			log.debug("Response: " + responseString);
			return responseString;
		} catch (UniformInterfaceException e) {
			log.debug("Response: <EMPTY>");
			return "";
		}
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws CallingServiceException {
		ClientResponse response = webResource.get(ClientResponse.class);
		final String responseString = response.getEntity(String.class);
		log.debug("HTTP GET request.\nResponse: " + responseString);
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseString;
	}

	private static Builder buildDefaultRequest(String restAction, String accept) {
		final String dstUrl = String.format("%s/%s", getApiRoot(), restAction);
		log.debug(String.format("Request to %s...", dstUrl));
		return client.resource(dstUrl).accept(accept)
				.type(MediaType.APPLICATION_JSON);
	}

	private static Builder buildDefaultRequest(String restAction) {
		return buildDefaultRequest(restAction, MediaType.WILDCARD);
	}

	public static JSONObject makeCall(String email, String password,
			String conversationId, String backend,
			CallingServiceBackend callBackend) throws CallingServiceException {
		Builder webResource = buildDefaultRequest("api/call");
		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("conversationId", conversationId);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend.toString());
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject getCallStatus(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/status", id));
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void muteCall(String id) throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/mute", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void unmuteCall(String id) throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/unmute", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void stopCall(String id) throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/stop", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static JSONObject makeWaitingInstance(String email, String password,
			String backend, CallingServiceBackend callBackend)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest("api/waitingInstance");
		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend.toString());
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject getWaitingInstanceStatus(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/status", id));
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void muteWaitingInstance(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/mute", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void acceptNextIncomingCall(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/accept", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void unmuteWaitingInstance(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/unmute", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void stopWaitingInstance(String id)
			throws CallingServiceException {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/stop", id));
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

}
