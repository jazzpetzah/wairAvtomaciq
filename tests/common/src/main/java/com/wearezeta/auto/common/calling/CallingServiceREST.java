package com.wearezeta.auto.common.calling;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.rest.RESTError;

final class CallingServiceREST {
	private static final Logger log = ZetaLogger
			.getLog(CallingServiceREST.class.getSimpleName());

	public static String getApiRoot() {
		try {
			return CommonUtils.getDefaultCallingServiceUrlFromConfig(CallingServiceREST.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
			CallingServiceREST::verifyRequestResult);

	/**
	 * Set configuration to accept our self-signed certificate for https
	 */
	public static ClientConfig configureClient() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, certs, new SecureRandom());
		} catch (java.security.GeneralSecurityException ex) {
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

		ClientConfig config = new DefaultClientConfig();
		try {
			config.getProperties().put(
					HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					new HTTPSProperties(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname,
								SSLSession session) {
							return true;
						}
					}, ctx));
		} catch (Exception e) {
		}
		return config;
	}

	private static Client client = Client.create(CallingServiceREST.configureClient());

	private static void verifyRequestResult(int currentResponseCode,
			int[] acceptableResponseCodes) throws CallingServiceException {
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new CallingServiceException(
					String.format(
							"The request to calling service failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
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
			CallingServiceBackend callBackend) throws RESTError {
		Builder webResource = buildDefaultRequest("api/call");
		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("conversationId", conversationId);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend.toString());
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject getCallStatus(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/status", id));
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void muteCall(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/mute", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void unmuteCall(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/unmute", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void stopCall(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/call/%s/stop", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static JSONObject makeWaitingInstance(String email, String password,
			String backend, CallingServiceBackend callBackend) throws RESTError {
		Builder webResource = buildDefaultRequest("api/waitingInstance");
		final JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("backend", backend);
		requestBody.put("callBackend", callBackend);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject getWaitingInstanceStatus(String id)
			throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/status", id));
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void muteWaitingInstance(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/mute", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void acceptNextIncomingCall(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/accept", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void unmuteWaitingInstance(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/unmute", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

	public static void stopWaitingInstance(String id) throws RESTError {
		Builder webResource = buildDefaultRequest(String.format(
				"api/waitingInstance/%s/stop", id));
		restHandlers.httpPut(webResource, "", new int[] { HttpStatus.SC_OK,
				HttpStatus.SC_NO_CONTENT });
	}

}
