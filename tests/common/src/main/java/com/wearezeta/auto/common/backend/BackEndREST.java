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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.image_send.*;
import com.wearezeta.auto.user_management.ClientUser;

import java.awt.image.BufferedImage;

public class BackEndREST {
	private static final Logger log = ZetaLogger.getLog(BackEndREST.class
			.getSimpleName());

	private static String backendUrl = "not set";

	private static final int LOGIN_TOO_FREQUENT_ERROR = 429;

	static {
		log.setLevel(Level.DEBUG);
	}

	private static Client client = Client.create();

	private static void VerifyRequestResult(int currentResponseCode,
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
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.put(ClientResponse.class, entity);
		log.debug("HTTP PUT request(Input data: " + entity + ", Response: "
				+ response.toString() + ")");
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Object httpGet(Builder webResource, Class<?> entityClass,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		log.debug("HTTP GET request(Response: " + response.toString() + ")");
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(entityClass);
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		log.debug("HTTP GET request(Response: " + response.toString() + ")");
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Builder buildDefaultRequest(String restAction, String accept)
			throws IllegalArgumentException, UriBuilderException, IOException {
		return client
				.resource(String.format("%s/%s", getBaseURI(), restAction))
				.accept(accept);
	}

	private static Builder buildDefaultRequestWithAuth(String restAction,
			String accept, ClientUser user) throws IllegalArgumentException,
			UriBuilderException, IOException {
		return client
				.resource(String.format("%s/%s", getBaseURI(), restAction))
				.accept(accept)
				.header(HttpHeaders.AUTHORIZATION,
						String.format("%s %s", user.getTokenType(),
								user.getAccessToken()));
	}

	private synchronized static void writeLog(String[] lines) {
		for (String line : lines) {
			log.debug(line);
		}
	}

	// ! Mutates user instance
	public static ClientUser loginByUser(ClientUser user) throws Exception {
		if (user.getAccessToken() != null) {
			try {
				getUserNameByID(user.getId(), user);
				return user;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Builder webResource = buildDefaultRequest("login",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"email\":\"%s\",\"password\":\"%s\",\"label\":\"\"}",
				user.getEmail(), user.getPassword());
		int tryNum = 1;
		String output = "";
		int toWait = 1;
		while (tryNum < 5) {
			try {
				output = httpPost(webResource, input,
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
		JSONObject jsonObj = new JSONObject(output);
		user.setAccessToken(jsonObj.getString("access_token"));
		user.setTokenType(jsonObj.getString("token_type"));

		writeLog(new String[] {
				"Output from Server ....  login By User " + user.getEmail(),
				output + "\n" });

		return user;
	}

	public static String getUserNameByID(String id, ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		Builder webResource = buildDefaultRequestWithAuth("users/" + id,
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		JSONObject jsonObj = new JSONObject(output);

		return jsonObj.getString("name");
	}

	public static ClientUser getUserInfo(ClientUser user) throws Exception {
		ClientUser result = user.clone();
		Builder webResource = buildDefaultRequestWithAuth("self",
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		JSONObject jsonObj = new JSONObject(output);
		result.setName(jsonObj.getString("name"));
		result.setId(jsonObj.getString("id"));

		writeLog(new String[] {
				"Output from Server .... get User Info " + user.getEmail(),
				output + "\n" });

		return result;
	}

	public static void sendConnectRequest(ClientUser user, ClientUser contact,
			String connectName, String message)
			throws IllegalArgumentException, UriBuilderException, IOException,
			BackendRequestException {
		Builder webResource = buildDefaultRequestWithAuth("self/connections",
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"user\": \"%s\",\"name\": \"%s\",\"message\": \"%s\"}",
				contact.getId(), connectName, message);
		final String output = httpPost(webResource, input, new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_CREATED });

		writeLog(new String[] {
				"Output from Server .... send Connect Request by"
						+ user.getEmail(), output + "\n" });
	}

	public static void acceptAllConnections(ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			BackendRequestException, JSONException {
		Builder webResource = buildDefaultRequestWithAuth("self/connections",
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });

		writeLog(new String[] {
				"Output from Server ....  accept All Connections "
						+ user.getEmail(), output + "\n" });

		JSONArray newJArray = new JSONArray(output);
		for (int i = 0; i < newJArray.length(); i++) {
			String to = ((JSONObject) newJArray.get(i)).getString("to");
			String status = ((JSONObject) newJArray.get(i)).getString("status");
			if (!status.equals("accepted")) {
				changeConnectRequestStatus(user, to, "accepted");
			}
		}
	}

	public static void ignoreAllConnections(ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			BackendRequestException, JSONException {
		Builder webResource = buildDefaultRequestWithAuth("self/connections",
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });

		writeLog(new String[] {
				"Output from Server ....  ignore All Connections "
						+ user.getEmail(), output + "\n" });

		JSONArray newJArray = new JSONArray(output);
		for (int i = 0; i < newJArray.length(); i++) {
			String to = ((JSONObject) newJArray.get(i)).getString("to");
			changeConnectRequestStatus(user, to, "ignored");
		}
	}

	public static void changeConnectRequestStatus(ClientUser user,
			String connectionId, String newStatus)
			throws BackendRequestException, IllegalArgumentException,
			UriBuilderException, IOException {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("self/connections/%s", connectionId),
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String input = String.format("{\"status\": \"%s\"}", newStatus);
		final String output = httpPut(webResource, input,
				new int[] { HttpStatus.SC_OK });

		writeLog(new String[] {
				"Output from Server ....  change Connect Request Status "
						+ user.getEmail(), output + "\n" });
	}

	public static void registerNewUser(String email, String userName,
			String password) throws IllegalArgumentException,
			UriBuilderException, IOException, JSONException,
			BackendRequestException {
		log.debug("Request for 'Register New User'");
		Builder webResource = buildDefaultRequest("register",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"email\": \"%s\",\"name\": \"%s\",\"password\": \"%s\"}",
				email, userName, password);
		final String output = httpPost(webResource, input,
				new int[] { HttpStatus.SC_CREATED });
		JSONObject jsonObj = new JSONObject(output);
		jsonObj.getString("id");

		writeLog(new String[] { "Output for 'Register New User' - " + email
				+ "\n\t" + output });
	}

	public static void activateNewUser(String key, String code)
			throws BackendRequestException, IllegalArgumentException,
			UriBuilderException, IOException {
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

	public static void sendPictureToSingleUserConversation(ClientUser userFrom,
			ClientUser userTo, String imagePath) throws Throwable {

		userFrom = loginByUser(userFrom);
		sendPicture(userFrom, imagePath,
				getConversationWithSingleUser(userFrom, userTo), null);
	}

	public static void sendPictureToChatByName(ClientUser userFrom,
			String chatName, String imagePath, InputStream src)
			throws Exception {
		userFrom = loginByUser(userFrom);
		sendPicture(userFrom, imagePath,
				getConversationByName(userFrom, chatName), src);
	}

	public static void createGroupConversation(ClientUser user,
			List<ClientUser> contacts, String conversationName)
			throws Exception {
		user = loginByUser(user);
		user = getUserInfo(user);
		List<String> quotedContacts = new ArrayList<String>();
		for (ClientUser contact : contacts) {
			ClientUser newUser = contact;
			newUser = loginByUser(newUser);
			newUser = getUserInfo(newUser);
			quotedContacts.add(String.format("\"%s\"", newUser.getId()));
		}
		final String input = String.format(
				"{\"users\": [ %s ],\"name\": \"%s\" }",
				StringUtils.join(quotedContacts, ", "), conversationName);
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String output = httpPost(webResource, input,
				new int[] { HttpStatus.SC_CREATED });

		writeLog(new String[] { "Output for 'Create Group Chat'" + "\n\t"
				+ output });
	}

	private static void sendPicture(ClientUser userFrom, String imagePath,
			String convId, InputStream src) throws Exception {
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
		for (AssetRequest request : reqBuilder.getRequests()) {
			Builder webResource = buildDefaultRequestWithAuth(
					request.getEndpoint(), MediaType.APPLICATION_JSON, userFrom)
					.type(request.getContentType())
					.header("Content-Disposition",
							request.getContentDisposition())
					.header("Content-Length", request.getContentLength());
			String output = httpPost(webResource, request.getPayload(),
					new int[] { HttpStatus.SC_CREATED });
			writeLog(new String[] { "Output from Server ....", output + "\n" });
		}
	}

	public static String getConversationWithSingleUser(ClientUser fromUser,
			ClientUser toUser) throws Exception {
		String conversationId = null;
		JSONArray jsonArray = getConversations(fromUser);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			conversationId = conversation.getString("id");
			conversation = (JSONObject) conversation.get("members");
			JSONArray otherArray = (JSONArray) conversation.get("others");
			if (otherArray.length() == 1) {
				String id = ((JSONObject) otherArray.get(0)).getString("id");
				if (id.equals(toUser.getId())) {
					return conversationId;
				}
			}
		}
		return conversationId;
	}

	public static String getConversationByName(ClientUser fromUser,
			String conversationName) throws Exception {
		String conversationId = null;
		JSONArray jsonArray = getConversations(fromUser);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			conversationId = conversation.getString("id");
			String name = conversation.getString("name");
			name = name.replaceAll("\uFFFC", "").trim();

			if (name.equals("null") || name.equals(fromUser.getName())) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0))
							.getString("id");
					String contactName = BackEndREST.getUserNameByID(id,
							fromUser);
					if (contactName.equals(conversationName)) {
						return conversationId;
					}
				}

			}
			if (name.equals(conversationName)) {
				return conversationId;
			} else {
				conversationId = "";
			}
		}
		return conversationId;
	}

	public static void sendConversationMessage(ClientUser user, String convId,
			String message) throws BackendRequestException,
			IllegalArgumentException, UriBuilderException, IOException {
		String nonce = CommonUtils.generateGUID();

		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/messages", convId),
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"content\": \"%s\", \"nonce\": \"%s\"}", message, nonce);
		final String output = httpPost(webResource, input,
				new int[] { HttpStatus.SC_CREATED });

		writeLog(new String[] { "Output from Server ....\n\t" + output });
	}

	public static String sendConversationPing(ClientUser user, String convId)
			throws IllegalArgumentException, UriBuilderException, IOException,
			BackendRequestException, JSONException {
		String nonce = CommonUtils.generateGUID();

		Builder webResource = buildDefaultRequestWithAuth(
				"conversations/" + convId + "/knock",
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String input = String.format("{\"nonce\": \"%s\"}", nonce);
		final String output = httpPost(webResource, input, new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		final JSONObject jsonObj = new JSONObject(output);
		String eventId = jsonObj.getString("id");
		writeLog(new String[] { "Output from Server .... " + output + "\n" });
		return eventId;
	}

	public static void sendConvertsationHotPing(ClientUser user,
			String convId, String refId) throws IllegalArgumentException,
			UriBuilderException, IOException, BackendRequestException {
		String nonce = CommonUtils.generateGUID();

		Builder webResource = buildDefaultRequestWithAuth(
				"conversations/" + convId + "/hot-knock",
				MediaType.APPLICATION_JSON, user).type(
				MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"nonce\": \"%s\", \"ref\": \"%s\"}", nonce, refId);
		final String output = httpPost(webResource, input, new int[] {
				HttpStatus.SC_OK, HttpStatus.SC_CREATED });
		writeLog(new String[] { "Output from Server .... " + output + "\n" });
	}

	public static String[] getConversationsAsStringArray(ClientUser user)
			throws Exception {
		JSONArray jsonArray = getConversations(user);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			String name = conversation.getString("name");
			if (name.equals("null") || name.equals(user.getName())) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0))
							.getString("id");
					String contactName = BackEndREST.getUserNameByID(id, user);
					result.add(contactName);
				} else {
					String contactName = "";
					for (int j = 0; j < otherArray.length(); j++) {
						String id = ((JSONObject) otherArray.get(j))
								.getString("id");
						contactName += BackEndREST.getUserNameByID(id, user);
						if (j < otherArray.length() - 1) {
							contactName += ", ";
						}
					}
					result.add(contactName);
				}
			} else {
				result.add(name);
			}

		}
		return (String[]) result.toArray(new String[0]);
	}

	private static JSONArray getConversations(ClientUser user) throws Exception {
		Builder webResource = buildDefaultRequestWithAuth("conversations",
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		final JSONObject jsonObj = new JSONObject(output);

		writeLog(new String[] { "Output for 'Get Conversations' \n\t" + output });

		return (JSONArray) jsonObj.get("conversations");
	}

	public static void setDefaultBackendURL(String Url) {
		backendUrl = Url;
	}

	public static URI getBaseURI() throws IllegalArgumentException,
			UriBuilderException, IOException {
		String backend = backendUrl.equals("not set") ? CommonUtils
				.getDefaultBackEndUrlFromConfig(CommonUtils.class) : backendUrl;

		return UriBuilder.fromUri(backend).build();
	}

	private static JSONArray getEventsfromConversation(String convID,
			ClientUser user) throws JSONException, BackendRequestException,
			IllegalArgumentException, UriBuilderException, IOException {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("conversations/%s/events", convID),
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		final JSONObject jsonObj = new JSONObject(output);

		writeLog(new String[] {
				"Output from Server .... get Events of Conversation ",
				output + "\n" });

		return (JSONArray) jsonObj.get("events");
	}

	private static BufferedImage getAssetsDownload(String convID,
			String assetID, ClientUser user) throws BackendRequestException,
			IllegalArgumentException, UriBuilderException, IOException {
		Builder webResource = buildDefaultRequestWithAuth(
				String.format("assets/%s/?conv_id=%s", assetID, convID),
				MediaType.MEDIA_TYPE_WILDCARD, user);
		final BufferedImage assetDownload = (BufferedImage) httpGet(
				webResource, BufferedImage.class,
				new int[] { HttpStatus.SC_OK });

		writeLog(new String[] {
				"Output from Server .... get Asset Download URL ",
				"ASSET PIC: " + assetDownload + "\n" });

		return assetDownload;
	}

	public static BufferedImage getPictureAssetFromConversation(
			ClientUser fromUser, ClientUser toUser) throws Exception {
		return getPictureAssetFromConversation(fromUser, toUser, 0);
	}

	public static BufferedImage getLastPictureAssetFromConversation(
			ClientUser fromUser, ClientUser toUser) throws Exception {
		return getPictureAssetFromConversation(fromUser, toUser,
				Integer.MAX_VALUE);
	}

	public static BufferedImage getPictureAssetFromConversation(
			ClientUser fromUser, ClientUser toUser, int index) throws Exception {
		BufferedImage result = null;
		int currentIndex = 0;
		String convID = getConversationWithSingleUser(fromUser, toUser);

		JSONArray eventsOfConv = getEventsfromConversation(convID, fromUser);
		for (int i = 0; i < eventsOfConv.length(); i++) {
			JSONObject event = (JSONObject) eventsOfConv.get(i);
			String type = event.getString("type");
			if (type.equals("conversation.asset-add")) {
				JSONObject data = (JSONObject) event.get("data");
				JSONObject info = (JSONObject) data.get("info");
				String tag = info.getString("tag");
				if (tag.equals("medium")) {
					result = getAssetsDownload(convID, (String) data.get("id"),
							fromUser);
					if (currentIndex == index)
						return result;
					currentIndex++;
				}
			}
		}

		if (Integer.MAX_VALUE == index) {
			return result;
		} else {
			// TODO: Raise exception
			return null;
		}
	}
}
