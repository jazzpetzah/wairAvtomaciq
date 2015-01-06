package com.wearezeta.auto.common.backend;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.AuthToken.AuthTokenIsNotSetException;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.image_send.*;

import java.awt.image.BufferedImage;

// Backend API calls should be invoked indirectly via API Wrappers class
final class BackendREST {
	private static final Logger log = ZetaLogger.getLog(BackendREST.class
			.getSimpleName());

	private static String backendUrl = "not set";

	private static final int LOGIN_TOO_FREQUENT_ERROR = 429;

	static {
		log.setLevel(Level.DEBUG);
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

	private static Object httpGet(Builder webResource, Class<?> entityClass,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		log.debug("HTTP GET request(Response: " + response.toString() + ")");
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(entityClass);
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		log.debug("HTTP GET request(Response: " + response.toString() + ")");
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Builder buildDefaultRequest(String restAction, String accept)
			throws IllegalArgumentException, UriBuilderException, IOException {
		return client
				.resource(String.format("%s/%s", getBaseURI(), restAction))
				.accept(accept);
	}

	private static Builder buildDefaultRequestWithAuth(String restAction,
			String accept, AuthToken token) throws IllegalArgumentException,
			UriBuilderException, IOException, AuthTokenIsNotSetException {
		return client
				.resource(String.format("%s/%s", getBaseURI(), restAction))
				.accept(accept)
				.header(HttpHeaders.AUTHORIZATION,
						String.format("%s %s", token.getType(),
								token.getValueOrThrowError()));
	}

	private synchronized static void writeLog(String[] lines) {
		for (String line : lines) {
			log.debug(line);
		}
	}

	public static JSONObject login(String email, String password)
			throws Exception {
		Builder webResource = buildDefaultRequest("login",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("password", password);
		requestBody.put("label", "");
		int tryNum = 1;
		String output = "";
		int toWait = 1;
		while (tryNum < 5) {
			try {
				output = httpPost(webResource, requestBody.toString(),
						new int[] { HttpStatus.SC_OK });
				break;
			} catch (BackendRequestException e) {
				if (e.getReturnCode() == LOGIN_TOO_FREQUENT_ERROR) {
					log.debug(String
							.format("Login request for login number #%d failed. Retrying...",
									tryNum));
					tryNum++;
					Thread.sleep(toWait * 1000);
					toWait *= 2;
				} else {
					throw e;
				}
			}
		}
		writeLog(new String[] {
				"Output from Server ....  login By User " + email,
				output + "\n" });

		return new JSONObject(output);
	}

	public static JSONObject getUserInfoByID(String id, AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("users/" + id,
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... get User Info By Id ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject getUserInfo(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self",
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... get User Info ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject sendConnectRequest(AuthToken fromToken,
			String toId, String connectName, String message) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/connections",
				MediaType.APPLICATION_JSON, fromToken).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("user", toId);
		requestBody.put("name", connectName);
		requestBody.put("message", message);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] {
				"Output from Server .... send Connect Request ", output + "\n" });
		return new JSONObject(output);
	}

	// If size == null or start == null then default values will be used instead
	// max size value is limited to 100
	// default size value is 100
	public static JSONArray getConnectionsInfo(AuthToken token, Integer size,
			String start) throws Exception {
		String requestUri = "self/connections";
		if (size != null && start != null) {
			requestUri = String.format("%s?start=%s&size=%s", requestUri,
					start, size.intValue());
		} else if (size != null) {
			requestUri = String.format("%s?size=%d", requestUri,
					size.intValue());
		} else if (start != null) {
			requestUri = String.format("%s?start=%s", requestUri, start);
		}
		Builder webResource = buildDefaultRequestWithAuth(requestUri,
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] {
				"Output from Server ....  get connection info ", output + "\n" });
		return new JSONArray(output);
	}

	public static void changeConnectRequestStatus(AuthToken token,
			String connectionId, ConnectionStatus newStatus) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("self/connections/%s", connectionId),
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("status", newStatus.toString());
		final String output = httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] {
				"Output from Server ....  change Connect Request Status ",
				output + "\n" });
	}

	public static JSONObject registerNewUser(String email, String userName,
			String password) throws Exception {
		log.debug("Request for 'Register New User'");
		Builder webResource = buildDefaultRequest("register",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("name", userName);
		requestBody.put("password", password);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output for 'Register New User' - " + email
				+ "\n\t" + output });
		return new JSONObject(output);
	}

	public static void activateNewUser(String key, String code)
			throws Exception {
		log.debug("Request for 'User Activation' (key: " + key + "; code: "
				+ code + ")");
		Builder webResource = buildDefaultRequest(
				String.format("activate?code=%s&key=%s", code, key),
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output for 'User Activation'\n\t"
				+ (output.trim().isEmpty() ? "<EMPTY OUTPUT>" : output) });
	}

	public static JSONObject createGroupConversation(AuthToken token,
			List<String> contactIds, String conversationName) throws Exception {
		JSONArray ids = new JSONArray(contactIds);
		JSONObject requestBody = new JSONObject();
		requestBody.put("users", ids);
		requestBody.put("name", conversationName);
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output for 'Create Group Chat'" + "\n\t"
				+ output });
		return new JSONObject(output);
	}

	public static List<JSONObject> sendPicture(AuthToken token,
			String imagePath, String convId, InputStream src) throws Exception {
		String imageMimeType = "";
		if (null == src) {
			InputStream is = new BufferedInputStream(new FileInputStream(
					imagePath));
			imageMimeType = URLConnection.guessContentTypeFromStream(is);
			is.close();
		} else {
			imageMimeType = URLConnection.guessContentTypeFromStream(src);

			if (imageMimeType == null) {
				// hardcode as there is no good way to check it when reading a
				// resource from jar
				imageMimeType = "image/jpeg";
			}
		}
		byte[] srcImageAsByteArray;

		if (null == src) {
			FileInputStream fis = new FileInputStream(imagePath);

			try {
				srcImageAsByteArray = IOUtils.toByteArray(fis);
			} finally {
				fis.close();
			}
		} else {
			srcImageAsByteArray = IOUtils.toByteArray(src);
		}

		ImageAssetData srcImgData = new ImageAssetData(convId,
				srcImageAsByteArray, imageMimeType);
		srcImgData.setIsPublic(true);
		ImageAssetProcessor imgProcessor = new ImageAssetProcessor(srcImgData);
		ImageAssetRequestBuilder reqBuilder = new ImageAssetRequestBuilder(
				imgProcessor);
		List<JSONObject> result = new ArrayList<JSONObject>();
		for (AssetRequest request : reqBuilder.getRequests()) {
			Builder webResource = buildDefaultRequestWithAuth(
					request.getEndpoint(), MediaType.APPLICATION_JSON, token)
					.type(request.getContentType())
					.header("Content-Disposition",
							request.getContentDisposition())
					.header("Content-Length", request.getContentLength());
			String output = httpPost(webResource, request.getPayload(),
					new int[] { HttpStatus.SC_CREATED });
			writeLog(new String[] { "Output from Server ....", output + "\n" });
			result.add(new JSONObject(output));
		}
		return result;
	}

	public static void sendConversationMessage(AuthToken userFromToken,
			String convId, String message) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/messages", convId),
				MediaType.APPLICATION_JSON, userFromToken).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("content", message);
		requestBody.put("nonce", CommonUtils.generateGUID());
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output from Server ....\n\t" + output });
	}

	public static JSONObject sendConversationPing(AuthToken userFromToken,
			String convId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				"conversations/" + convId + "/knock",
				MediaType.APPLICATION_JSON, userFromToken).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("nonce", CommonUtils.generateGUID());
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Ping output from Server .... " + output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject sendConvertsationHotPing(AuthToken userFromToken,
			String convId, String refId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				"conversations/" + convId + "/hot-knock",
				MediaType.APPLICATION_JSON, userFromToken).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("ref", refId);
		requestBody.put("nonce", CommonUtils.generateGUID());
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Hot Ping output from Server .... " + output
				+ "\n" });
		return new JSONObject(output);
	}

	public static JSONObject getConversationsInfo(AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output for 'Get Conversations' \n\t" + output });
		return new JSONObject(output);
	}

	public static JSONObject getEventsFromConversation(AuthToken token,
			String convId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/events", convId),
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] {
				"Output from Server .... get Events of Conversation ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static BufferedImage getAssetsDownload(AuthToken token,
			String convId, String assetId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("assets/%s/?conv_id=%s", assetId, convId),
				MediaType.MEDIA_TYPE_WILDCARD, token);
		final BufferedImage assetDownload = (BufferedImage) httpGet(
				webResource, BufferedImage.class,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] {
				"Output from Server .... get Asset Download URL ",
				"ASSET PIC: " + assetDownload + "\n" });
		return assetDownload;
	}

	public static void setDefaultBackendURL(String url) {
		backendUrl = url;
	}

	public static URI getBaseURI() throws IllegalArgumentException,
			UriBuilderException, IOException {
		String backend = backendUrl.equals("not set") ? CommonUtils
				.getDefaultBackEndUrlFromConfig(CommonUtils.class) : backendUrl;

		return UriBuilder.fromUri(backend).build();
	}
}
