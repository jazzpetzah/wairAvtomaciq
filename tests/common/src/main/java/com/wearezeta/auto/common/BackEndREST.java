package com.wearezeta.auto.common;

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
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.image_send.*;

import java.awt.image.BufferedImage;

public class BackEndREST {

	private static final Logger log = ZetaLogger.getLog(BackEndREST.class.getSimpleName());

	ClientConfig config = new DefaultClientConfig();
	static Client client = Client.create();

	public static void autoTestSendRequest(ClientUser user, ClientUser contact)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		user = loginByUser(user);
		user = getUserInfo(user);
		sendConnectRequest(user, contact, contact.getName(), "Hello!!!");
	}

	public static void autoTestAcceptAllRequest(ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		user = loginByUser(user);
		acceptAllConnections(user);
	}

	public static void sendDialogMessage(ClientUser fromUser,
			ClientUser toUser, String message) throws Exception {
		fromUser = loginByUser(fromUser);
		String id = getConversationWithSingleUser(fromUser, toUser);
		sendConversationMessage(fromUser, id, message);
	}

	public static void sendDialogMessageByChatName(ClientUser fromUser,
			String chatName, String message) throws Exception {
		fromUser = loginByUser(fromUser);
		String id = getConversationByName(fromUser, chatName);
		sendConversationMessage(fromUser, id, message);
	}

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
								Arrays.toString(acceptableResponseCodes)));
			}
		}
	}

	private static String httpPost(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource
				.post(ClientResponse.class, entity);
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static String httpPut(Builder webResource, Object entity,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.put(ClientResponse.class, entity);
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(String.class);
	}

	private static Object httpGet(Builder webResource, Class<?> entityClass,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
		VerifyRequestResult(response.getStatus(), acceptableResponseCodes);
		return response.getEntity(entityClass);
	}

	private static String httpGet(Builder webResource,
			int[] acceptableResponseCodes) throws BackendRequestException {
		ClientResponse response = webResource.get(ClientResponse.class);
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
		// TODO: Replace with smart logger
		for (String line : lines) {
			log.debug(line);
		}
	}

	public static ClientUser loginByUser(ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		Builder webResource = buildDefaultRequest("login",
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		final String input = String.format(
				"{\"email\":\"%s\",\"password\":\"%s\",\"label\":\"\"}",
				user.getEmail(), user.getPassword());
		final String output = httpPost(webResource, input, new int[] { HttpStatus.SC_OK });
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

	public static ClientUser getUserInfo(ClientUser user)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		Builder webResource = buildDefaultRequestWithAuth("self",
				MediaType.APPLICATION_JSON, user);
		final String output = httpGet(webResource,
				new int[] { HttpStatus.SC_OK });
		JSONObject jsonObj = new JSONObject(output);
		user.setName(jsonObj.getString("name"));
		user.setId(jsonObj.getString("id"));

		writeLog(new String[] {
				"Output from Server .... get User Info " + user.getEmail(),
				output + "\n" });

		return user;
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
			changeConnectRequestStatus(user, to, "accepted");
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
		log.debug("Input data: " + input);
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
		log.debug("Request for 'User Activation' (key: " + key + "; code: " + code + ")");
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
			String chatName, String imagePath, InputStream src) throws Throwable {
		
		userFrom = loginByUser(userFrom);
		sendPicture(userFrom, imagePath,
				getConversationByName(userFrom, chatName), src);
	}

	public static void createGroupConversation(ClientUser user,
			List<ClientUser> contacts, String conversationName)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
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

		writeLog(new String[] { "Output for 'Create Group Chat'" + "\n\t" + output });
	}

	private static void sendPicture(ClientUser userFrom, String imagePath,
			String convId, InputStream src) throws Throwable {
		
		String imageMimeType = "";
		if (null == src) {
			InputStream is = new BufferedInputStream(new FileInputStream(imagePath));
			imageMimeType = URLConnection.guessContentTypeFromStream(is);
			is.close();
		}
		else {
			imageMimeType = URLConnection.guessContentTypeFromStream(src);
			
			if (imageMimeType == null) {
				//hardcode as there is no good way to check it when reading a resource from jar
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
		}
		else {
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

	private static String getConversationByName(ClientUser fromUser,
			String conversationName) throws Exception {
		String conversationId = null;
		JSONArray jsonArray = getConversations(fromUser);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			conversationId = conversation.getString("id");
			String name = conversation.getString("name");
			if (name.equals("null")) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0)).getString("id");
					String contactName = BackEndREST.getUserNameByID(id, fromUser);
					if (contactName.equals(conversationName)) 
					{
						return conversationId;
					}
				}

			}
			if (name.equals(conversationName)) {
				return conversationId;
			}
			else {
				conversationId = "";
			}
		}
		return conversationId;
	}

	private static void sendConversationMessage(ClientUser user, String convId,
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
	
	public static String [] getConversationsAsStringArray(ClientUser user) throws Exception {
		JSONArray jsonArray = getConversations(user);
		ArrayList<String> result = new ArrayList<String>(); 
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			String name = conversation.getString("name");
			if (name.equals("null")) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0)).getString("id");
					String contactName = BackEndREST.getUserNameByID(id, user);
					result.add(contactName);
				}
				else {
					String contactName = "";
					for (int j = 0; j < otherArray.length(); j++) {
						String id = ((JSONObject) otherArray.get(j)).getString("id");
						contactName += BackEndREST.getUserNameByID(id, user);
						if (j < otherArray.length() - 1) {
							contactName += ", ";
						}
					}
					result.add(contactName);
				}
			}
			else {
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

	public static URI getBaseURI() throws IllegalArgumentException,
			UriBuilderException, IOException {
		return UriBuilder.fromUri(
				CommonUtils.getDefaultBackEndUrlFromConfig(CommonUtils.class))
				.build();
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
		return getPictureAssetFromConversation(fromUser, toUser, Integer.MAX_VALUE);
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
					if (currentIndex == index) return result;
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
