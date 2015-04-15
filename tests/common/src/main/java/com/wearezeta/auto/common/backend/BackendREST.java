package com.wearezeta.auto.common.backend;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.onboarding.AddressBook;
import com.wearezeta.auto.image_send.*;

import java.awt.image.BufferedImage;

// Backend API calls should be invoked indirectly via API Wrappers class
final class BackendREST {
	private static final Logger log = ZetaLogger.getLog(BackendREST.class
			.getSimpleName());

	private static String backendUrl = "not set";

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
			throws Exception {
		return client
				.resource(String.format("%s/%s", getBaseURI(), restAction))
				.accept(accept);
	}

	private static Builder buildDefaultRequestWithAuth(String restAction,
			String accept, AuthToken token) throws Exception {
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
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
		;
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
		String output = "<NO CONTENT>";
		try {
			output = httpPut(webResource, requestBody.toString(), new int[] {
					HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT });
		} catch (UniformInterfaceException e) {
		}
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
		JSONArray ids = new JSONArray(contactIds.toArray(new String[0]));
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

	public static Map<JSONObject, AssetData> sendPicture(AuthToken token,
			String convId, ImageAssetRequestBuilder reqBuilder)
			throws Exception {
		Map<JSONObject, AssetData> result = new LinkedHashMap<JSONObject, AssetData>();
		for (AssetRequest request : reqBuilder.getRequests()) {
			Builder webResource = buildDefaultRequestWithAuth(
					request.getEndpoint(), MediaType.APPLICATION_JSON, token)
					.type(request.getContentType())
					.header("Content-Disposition",
							request.getContentDisposition())
					.header("Content-Length", request.getContentLength());
			final String output = httpPost(webResource, request.getPayload(),
					new int[] { HttpStatus.SC_CREATED });
			writeLog(new String[] { "Output from Server ....", output + "\n" });
			final JSONObject jsonOutput = new JSONObject(output);
			result.put(jsonOutput, request.getAssetDataObject());
		}
		return result;
	}

	public static Map<JSONObject, AssetData> sendPicture(AuthToken token,
			String convId, byte[] srcImageAsByteArray, String imageMimeType)
			throws Exception {
		ImageAssetData srcImgData = new ImageAssetData(convId,
				srcImageAsByteArray, imageMimeType);
		srcImgData.setIsPublic(true);
		ImageAssetProcessor imgProcessor = new ConvoImageProcessor(srcImgData);
		ImageAssetRequestBuilder reqBuilder = new ImageAssetRequestBuilder(
				imgProcessor);
		return sendPicture(token, convId, reqBuilder);
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

	public static JSONObject getLastEventIDs(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/last-events"),
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... get Last Event ID's ",
				output + "\n" });
		return new JSONObject(output);
	}

	public static String getLastEventFromConversation(AuthToken token,
			String convId) throws Exception {
		JSONArray convsWithLastIds = BackendREST.getLastEventIDs(token)
				.getJSONArray("conversations");
		for (int i = 0; i < convsWithLastIds.length(); i++) {
			if (convsWithLastIds.getJSONObject(i).getString("id")
					.equals(convId)) {
				return convsWithLastIds.getJSONObject(i).getString("event");
			}
		}
		throw new IOException("Invalid conversation ID");
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

	private static JSONArray generateRequestForSelfPicture(
			Map<String, AssetData> publishedPictureAssets) {
		JSONArray result = new JSONArray();
		for (Map.Entry<String, AssetData> entry : publishedPictureAssets
				.entrySet()) {
			final JSONObject pictureItem = new JSONObject();
			final String publishedPictureId = entry.getKey();
			final ImageAssetData pictureAssetData = (ImageAssetData) entry
					.getValue();
			pictureItem.put("content_type", pictureAssetData.getMimeType());
			pictureItem.put("content_length",
					pictureAssetData.getImageData().length);
			pictureItem.put("id", publishedPictureId);

			JSONObject additionalInfo = new JSONObject();
			additionalInfo.put("correlation_id",
					pictureAssetData.getCorrelationId());
			additionalInfo.put("height", pictureAssetData.getHeight());
			additionalInfo.put("nonce", pictureAssetData.getNonce());
			additionalInfo.put("original_height",
					pictureAssetData.getOriginalHeight());
			additionalInfo.put("original_width",
					pictureAssetData.getOriginalWidth());
			additionalInfo.put("public", pictureAssetData.getIsPublic());
			additionalInfo.put("tag", pictureAssetData.getTag());
			additionalInfo.put("width", pictureAssetData.getWidth());
			pictureItem.put("info", additionalInfo);

			result.put(pictureItem);
		}
		return result;
	}

	public static void updateSelfInfo(AuthToken token, Integer accentId,
			Map<String, AssetData> publishedPictureAssets, String name)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("self"), MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		if (accentId != null) {
			requestBody.put("accent_id", accentId.intValue());
		}
		if (publishedPictureAssets != null) {
			requestBody.put("picture",
					generateRequestForSelfPicture(publishedPictureAssets));
		}
		if (name != null) {
			requestBody.put("name", name);
		}
		final String output = httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... Update Self Info ",
				output });
	}

	public static void updateConvSelfInfo(AuthToken token, String convId,
			String lastRead, Boolean muted, Boolean archived) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/self", convId),
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		if (lastRead != null) {
			requestBody.put("last_read", lastRead);
		}
		if (muted != null) {
			requestBody.put("muted", (boolean) muted);
		}
		if (archived != null) {
			if (archived) {
				requestBody
						.put("archived", BackendREST
								.getLastEventFromConversation(token, convId));
			} else {
				requestBody.put("archived", "false");
			}
		}
		final String output = httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output from Server .... Update conversation self info "
				+ output + "\n" });
	}

	public static JSONObject searchForContacts(AuthToken token, String query)
			throws Exception {
		// Changed this to make it look the same as in webapp
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("search/contacts?q=%s&size=30&l=3&d=1",
						URLEncoder.encode(query, "utf-8")),
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... Search for contacts "
				+ output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject addContactsToGroupConvo(AuthToken token,
			List<String> contactsIds, String conversationId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/members",
						URLEncoder.encode(conversationId, "utf-8")),
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		JSONArray userIds = new JSONArray();
		for (String userId : contactsIds) {
			userIds.put(userId);
		}
		requestBody.put("users", userIds);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT });
		writeLog(new String[] { "Output from Server .... Add contacts to group convo "
				+ output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject uploadAddressBook(AuthToken token,
			AddressBook addressBook) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("onboarding/v3",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpPost(webResource, addressBook.asJSONObject()
				.toString(), new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... Upload Address Book v3 "
				+ output + "\n" });
		return new JSONObject(output);
	}

	public static JSONObject getSuggestions(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("search/suggestions",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		writeLog(new String[] { "Output from Server .... Get Suggestions "
				+ output + "\n" });
		return new JSONObject(output);
	}

	public static void setDefaultBackendURL(String url) {
		backendUrl = url;
	}

	public static URI getBaseURI() throws Exception {
		String backend = backendUrl.equals("not set") ? CommonUtils
				.getDefaultBackEndUrlFromConfig(CommonUtils.class) : backendUrl;

		return UriBuilder.fromUri(backend).build();
	}
}
