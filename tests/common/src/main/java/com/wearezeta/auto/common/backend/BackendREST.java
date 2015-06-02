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
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.onboarding.AddressBook;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.image_send.*;

import java.awt.image.BufferedImage;

// Backend API calls should be invoked indirectly via API Wrappers class
final class BackendREST {
	private static final Logger log = ZetaLogger.getLog(BackendREST.class
			.getSimpleName());

	private static final int MAX_REQUEST_RETRY_COUNT = 3;

	private static final CommonRESTHandlers restHandlers = new CommonRESTHandlers(
			BackendREST::verifyRequestResult, MAX_REQUEST_RETRY_COUNT);

	private static String backendUrl = null;
	private static Client client = Client.create();
	static {
		log.setLevel(Level.DEBUG);
	}

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
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject login(PhoneNumber phoneNumber, String code)
			throws Exception {
		Builder webResource = buildDefaultRequest("login",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		requestBody.put("code", code);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getUserInfoByID(String id, AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("users/" + id,
				MediaType.APPLICATION_JSON, token);
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getUserInfo(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self",
				MediaType.APPLICATION_JSON, token);
		final String output = restHandlers.httpGet(webResource,
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
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_CREATED });
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
		final String output = restHandlers.httpGet(webResource,
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
		restHandlers.httpPut(webResource, requestBody.toString(), new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_NO_CONTENT });
	}

	public static void updateSelfEmail(AuthToken token, String newEmail)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/email",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", newEmail);
		restHandlers.httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_ACCEPTED });
	}

	public static void updateSelfPassword(AuthToken token, String oldPassword,
			String newPassword) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/password",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		if (oldPassword != null) {
			requestBody.put("old_password", oldPassword);
		}
		requestBody.put("new_password", newPassword);
		restHandlers.httpPut(webResource, requestBody.toString(), new int[] {
				HttpStatus.SC_ACCEPTED, HttpStatus.SC_OK });
	}

	public static void detachSelfEmail(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/email",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		restHandlers.httpDelete(webResource, new int[] { HttpStatus.SC_OK });
	}

	public static void updateSelfPhoneNumber(AuthToken token,
			PhoneNumber phoneNumber) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/phone",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		restHandlers.httpPut(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_ACCEPTED });
	}

	public static void detachSelfPhoneNumber(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("self/phone",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		restHandlers.httpDelete(webResource, new int[] { HttpStatus.SC_OK });
	}

	public static JSONObject registerNewUser(String email, String userName,
			String password) throws Exception {
		Builder webResource = buildDefaultRequest("register",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("name", userName);
		requestBody.put("password", password);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject registerNewUser(PhoneNumber phoneNumber,
			String userName, String activationCode) throws Exception {
		Builder webResource = buildDefaultRequest("register",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		requestBody.put("name", userName);
		requestBody.put("phone_code", activationCode);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static void activateNewUser(String key, String code)
			throws Exception {
		Builder webResource = buildDefaultRequest(
				String.format("activate?code=%s&key=%s", code, key),
				MediaType.APPLICATION_JSON);
		restHandlers.httpGet(webResource, new int[] { HttpStatus.SC_OK });
	}

	public static void bookPhoneNumber(PhoneNumber phoneNumber)
			throws Exception {
		Builder webResource = buildDefaultRequest("activate/send",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		restHandlers.httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
	}

	// Don't share these values with anyone!!!
	// Otherwise backend guys will find your cunning ass
	private static final String BASIC_AUTH_HEADER_VALUE_EDGE = "Basic d2lyZS1lZGdlOiQyXVxTbihGYD8rUlkiLkM=";
	private static final String BASIC_AUTH_HEADER_VALUE_STAGING = "Basic d2lyZS1zdGFnaW5nOnRqNGEzbl1BQzpFcn5yJTQ=";

	private static String getAuthValue() throws Exception {
		String authValue = null;
		final String host = getBaseURI().getHost();
		if (host.toLowerCase().contains("edge")) {
			authValue = BASIC_AUTH_HEADER_VALUE_EDGE;
		} else if (host.toLowerCase().contains("staging")) {
			authValue = BASIC_AUTH_HEADER_VALUE_STAGING;
		} else {
			throw new RuntimeException(String.format("Unknown backend host %s",
					host));
		}
		return authValue;
	}

	public static JSONObject getActivationDataViaBackdoor(
			PhoneNumber phoneNumber) throws Exception {
		Builder webResource = buildDefaultRequest(
				String.format("i/users/activation-code?phone=%s",
						URLEncoder.encode(phoneNumber.toString(), "utf-8")),
				MediaType.APPLICATION_JSON).header("Authorization",
				getAuthValue());
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getActivationDataViaBackdoor(String email)
			throws Exception {
		Builder webResource = buildDefaultRequest(
				String.format("i/users/activation-code?email=%s",
						URLEncoder.encode(email, "utf-8")),
				MediaType.APPLICATION_JSON).header("Authorization",
				getAuthValue());
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void activateNewUser(PhoneNumber phoneNumber, String code,
			boolean isDryRun) throws Exception {
		Builder webResource = buildDefaultRequest("activate",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		requestBody.put("code", code);
		requestBody.put("dryrun", isDryRun);
		restHandlers.httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
	}

	public static void activateNewUser(String email, String code,
			boolean isDryRun) throws Exception {
		Builder webResource = buildDefaultRequest("activate",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", email);
		requestBody.put("code", code);
		requestBody.put("dryrun", isDryRun);
		restHandlers.httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
	}

	public static void generateLoginCode(PhoneNumber phoneNumber)
			throws Exception {
		Builder webResource = buildDefaultRequest("login/send",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", phoneNumber.toString());
		restHandlers.httpPost(webResource, requestBody.toString(),
				new int[] { HttpStatus.SC_OK });
	}

	public static JSONObject getLoginCodeViaBackdoor(PhoneNumber phoneNumber)
			throws Exception {
		Builder webResource = buildDefaultRequest(
				String.format("i/users/login-code?phone=%s",
						URLEncoder.encode(phoneNumber.toString(), "utf-8")),
				MediaType.APPLICATION_JSON).header("Authorization",
				getAuthValue());
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
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
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_CREATED });
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
			final String output = restHandlers.httpPost(webResource,
					request.getPayload(), new int[] { HttpStatus.SC_CREATED });
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
		restHandlers.httpPost(webResource, requestBody.toString(),
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
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_CREATED });
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
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_CREATED });
		return new JSONObject(output);
	}

	public static JSONObject getConversationsInfo(AuthToken token)
			throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, token);
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getEventsFromConversation(AuthToken token,
			String convId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/events", convId),
				MediaType.APPLICATION_JSON, token);
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getLastEventIDs(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/last-events"),
				MediaType.APPLICATION_JSON, token);
		final String output = restHandlers.httpGet(webResource,
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
		final BufferedImage assetDownload = (BufferedImage) restHandlers
				.httpGet(webResource, BufferedImage.class,
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
		restHandlers.httpPut(webResource, requestBody.toString(),
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
		restHandlers.httpPut(webResource, requestBody.toString(), new int[] {
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
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject addContactsToGroupConvo(AuthToken token,
			List<String> contactsIds, String conversationId) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/members", conversationId),
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		JSONObject requestBody = new JSONObject();
		JSONArray userIds = new JSONArray();
		for (String userId : contactsIds) {
			userIds.put(userId);
		}
		requestBody.put("users", userIds);
		final String output = restHandlers.httpPost(webResource,
				requestBody.toString(), new int[] { HttpStatus.SC_OK,
						HttpStatus.SC_NO_CONTENT });
		return new JSONObject(output);
	}

	public static JSONObject uploadAddressBook(AuthToken token,
			AddressBook addressBook) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("onboarding/v3",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = restHandlers.httpPost(webResource, addressBook
				.asJSONObject().toString(), new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static JSONObject getSuggestions(AuthToken token) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("search/suggestions",
				MediaType.APPLICATION_JSON, token).type(
				MediaType.APPLICATION_JSON);
		final String output = restHandlers.httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		return new JSONObject(output);
	}

	public static void setDefaultBackendURL(String url) {
		backendUrl = url;
	}

	public static URI getBaseURI() throws Exception {
		String backend = (backendUrl == null) ? CommonUtils
				.getDefaultBackEndUrlFromConfig(CommonUtils.class) : backendUrl;

		return UriBuilder.fromUri(backend).build();
	}
}
