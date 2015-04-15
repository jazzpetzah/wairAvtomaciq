package com.wearezeta.auto.common.calling;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendRequestException;
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
									.getDefaultCallingServiceHostFromConfig(CallingUtil.class),
							CommonUtils
									.getDefaultCallingServicePortFromConfig(CallingUtil.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static Client client = Client.create();

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

	private static String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.put(ClientResponse.class, entity);
		log.debug("HTTP PUT request(Input data: " + entity + ", Response: "
				+ response.toString() + ")");
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		log.debug("HTTP GET request(Response: " + response.toString() + ")");
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Builder buildDefaultRequest(String restAction, String accept) {
		return client
				.resource(String.format("%s/%s", getApiRoot(), restAction))
				.accept(accept).type(MediaType.APPLICATION_JSON);
	}

	private synchronized static void writeLog(String[] lines) {
		for (String line : lines) {
			log.debug(line);
		}
	}

	public static JSONObject makeCall(String email, String password,
			String conversationId, String backend,
			CallingServiceBackend callBackend) throws BackendRequestException {
		Builder webResource = buildDefaultRequest("api/call",
				MediaType.APPLICATION_JSON);

		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("conversationId", conversationId);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend.toString());

		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output from Server .... makeCall ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject getCallStatus(String id)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/call/%s/status", id),
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... getCallStatus ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static void muteCall(String id) throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/call/%s/mute", id),
				MediaType.APPLICATION_JSON);
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK });
	}

	public static void unmuteCall(String id) throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/call/%s/unmute", id),
				MediaType.APPLICATION_JSON);
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK });
	}

	public static void stopCall(String id) throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/call/%s/stop", id),
				MediaType.APPLICATION_JSON);
		httpGet(webResource, new int[] { HttpStatus.SC_OK });
	}

	public static JSONObject makeWaitingInstance(String email, String password,
			String backend, CallingServiceBackend callBackend)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest("api/waitingInstance",
				MediaType.APPLICATION_JSON);

		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend.toString());

		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output from Server .... makeWaitingInstance ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject getWaitingInstanceStatus(String id)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/waitingInstance/%s/status", id),
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] {
				"Output from Server .... getWaitingInstancetatus ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static void muteWaitingInstance(String id)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/waitingInstance/%s/mute", id),
				MediaType.APPLICATION_JSON);
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK });
	}

	public static void unmuteWaitingInstance(String id)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/waitingInstance/%s/unmute", id),
				MediaType.APPLICATION_JSON);
		httpPut(webResource, "", new int[] { HttpStatus.SC_OK });
	}

	public static void stopWaitingInstance(String id)
			throws BackendRequestException {
		Builder webResource = buildDefaultRequest(
				String.format("api/waitingInstance/%s/stop", id),
				MediaType.APPLICATION_JSON);
		httpGet(webResource, new int[] { HttpStatus.SC_OK });
	}

}
