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

import org.apache.commons.lang3.ArrayUtils;
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
		if (!ArrayUtils.contains(acceptableResponseCodes, currentResponseCode)) {
			throw new BackendRequestException(
					String.format(
							"Backend request failed. Request return code is: %d. Expected codes are: %s",
							currentResponseCode,
							Arrays.toString(acceptableResponseCodes)),
					currentResponseCode);
		}
	}

	private static final String EMPTY_LOG_RECORD = "<EMPTY>";

	private static String formatEntity(Object entity) {
		String result = "<" + entity.getClass().getSimpleName() + ">";
		if (entity instanceof String) {
			if (((String) entity).length() == 0) {
				result = EMPTY_LOG_RECORD;
			} else {
				result = entity.toString();
			}
		}
		return result;
	}

	private static String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		Object lock = new Object();
		ClientResponse response;
		synchronized (lock) {
			response = webResource.post(ClientResponse.class, entity);
		}
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(
				"POST REQUEST\n >>> Input data: %s\n >>> Response: %s",
				formatEntity(entity), (responseStr.length() > 0) ? responseStr
						: EMPTY_LOG_RECORD));
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseStr;
	}

	private static String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.put(ClientResponse.class, entity);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format(
				"PUT REQUEST\n >>> Input data: %s\n >>> Response: %s",
				formatEntity(entity), (responseStr.length() > 0) ? responseStr
						: EMPTY_LOG_RECORD));
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseStr;
	}

	private static Object httpGet(Builder webResource, Class<?> entityClass,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		Object responseObj = null;
		try {
			responseObj = response.getEntity(entityClass);
		} catch (UniformInterfaceException e) {
			// Do nothing
		}
		log.debug(String.format("GET REQUEST\n >>> Response Object: <%s>",
				entityClass.getName()));
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseObj;
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		String responseStr;
		try {
			responseStr = response.getEntity(String.class);
		} catch (UniformInterfaceException e) {
			responseStr = "";
		}
		log.debug(String.format("GET REQUEST\n >>> Response: %s",
				(responseStr.length() > 0) ? responseStr : EMPTY_LOG_RECORD));
		verifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return responseStr;
	}

	private static Builder buildDefaultRequest(String restAction, String accept)
			throws Exception {
		final String dstUrl = String.format("%s/%s", getBaseURI(), restAction);
		log.debug(String.format("Making request to %s...", dstUrl));
		return client.resource(dstUrl).accept(accept);
	}

	private static Builder buildDefaultRequestWithAuth(String restAction,
			String accept, AuthToken token) throws Exception {
		final String dstUrl = String.format("%s/%s", getBaseURI(), restAction);
		log.debug(String.format("Making request to %s...", dstUrl));
		return client
				.resource(dstUrl)
				.accept(accept)
				.header(HttpHeaders.AUTHORIZATION,
						String.format("%s %s", token.getType(),
								token.getValueOrThrowError()));
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
		return new JSONObject(output);
	}

	public static JSONObject getUserInfoByID(String id, AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("users/" + id,
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getUserInfo(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self",
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
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
		httpPut(webResource, requestBody.toString(), new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT });
	}

	public static JSONObject registerNewUser(String email, String userName,
			String password) throws Exception {
		Builder webResource = buildDefaultRequest("register",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("name", userName);
		requestBody.put("password", password);
		final String output = httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static void activateNewUser(String key, String code)
			throws Exception {
		Builder webResource = buildDefaultRequest(
				String.format("activate?code=%s&key=%s", code, key),
				MediaType.APPLICATION_JSON);
		httpGet(webResource, new int[] { HttpStatus.SC_OK });
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
		httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_CREATED });
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
		return new JSONObject(output);
	}

	public static JSONObject getConversationsInfo(AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getEventsFromConversation(AuthToken token,
			String convId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/events", convId),
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getLastEventIDs(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/last-events"),
				MediaType.APPLICATION_JSON, token);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
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
		httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
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
		httpPut(webResource, requestBody.toString(), new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_CREATED });
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
		return new JSONObject(output);
	}

	public static JSONObject uploadAddressBook(AuthToken token,
			AddressBook addressBook) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("onboarding/v3",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpPost(webResource, addressBook.asJSONObject()
				.toString(), new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getSuggestions(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("search/suggestions",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
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
