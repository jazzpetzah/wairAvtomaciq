package com.wearezeta.auto.common.backend;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.email.EmailHeaders;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.image_send.AssetData;
import com.wearezeta.auto.image_send.ImageAssetData;
import com.wearezeta.auto.image_send.ImageAssetProcessor;
import com.wearezeta.auto.image_send.ImageAssetRequestBuilder;

// Almost all methods of this class mutate ClientUser
// argument by performing automatic login (set id and session token attributes)
public final class BackendAPIWrappers {
	public static final int ACTIVATION_TIMEOUT = 120; // seconds

	private static final Logger log = ZetaLogger
			.getLog(BackendAPIWrappers.class.getSimpleName());

	public static ClientUser createUser(ClientUser user) throws Exception {
		IMAPSMailbox mbox = IMAPSMailbox.createDefaultInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", user.getEmail());
		MBoxChangesListener listener = mbox.startMboxListener(expectedHeaders);
		BackendREST.registerNewUser(user.getEmail(), user.getName(),
				user.getPassword());
		activateRegisteredUser(listener);
		user.setUserState(UserState.Created);
		return user;
	}

	public static void activateRegisteredUser(MBoxChangesListener listener)
			throws Exception {
		EmailHeaders registrationInfo = IMAPSMailbox.getFilteredMessageHeaders(
				listener, ACTIVATION_TIMEOUT);
		BackendREST.activateNewUser(registrationInfo.getXZetaKey(),
				registrationInfo.getXZetaCode());
		log.debug(String.format("User %s is activated",
				registrationInfo.getLastUserEmail()));
	}

	public static void createContactLinks(ClientUser userFrom,
			List<ClientUser> usersTo) throws Exception {
		userFrom = tryLoginByUser(userFrom);
		for (ClientUser userTo : usersTo) {
			autoTestSendRequest(userFrom, userTo);
			autoTestAcceptAllRequest(userTo);
		}
	}

	public static void autoTestSendRequest(ClientUser userFrom,
			ClientUser userTo) throws Exception {
		userFrom = tryLoginByUser(userFrom);
		userTo = tryLoginByUser(userTo);
		sendConnectRequest(userFrom, userTo, userTo.getName(),
				CommonSteps.CONNECTION_MESSAGE);
		userFrom.setUserState(UserState.RequestSend);
	}

	public static ClientUser autoTestAcceptAllRequest(ClientUser userTo)
			throws Exception {
		userTo = tryLoginByUser(userTo);
		acceptAllConnections(userTo);
		userTo.setUserState(UserState.AllContactsConnected);
		return userTo;
	}

	public static void sendDialogMessage(ClientUser fromUser,
			ClientUser toUser, String message) throws Exception {
		fromUser = tryLoginByUser(fromUser);
		String id = getConversationWithSingleUser(fromUser, toUser);
		sendConversationMessage(fromUser, id, message);
	}

	public static void sendDialogMessageByChatName(ClientUser fromUser,
			String toChat, String message) throws Exception {
		fromUser = tryLoginByUser(fromUser);
		String id = getConversationIdByName(fromUser, toChat);
		sendConversationMessage(fromUser, id, message);
	}

	public static String sendPingToConversation(ClientUser fromUser,
			String toChat) throws Exception {
		fromUser = tryLoginByUser(fromUser);
		String id = getConversationIdByName(fromUser, toChat);
		return sendConversationPing(fromUser, id);
	}

	public static void sendHotPingToConversation(ClientUser fromUser,
			String toChat, String id) throws Exception {
		fromUser = tryLoginByUser(fromUser);
		String conv_id = getConversationIdByName(fromUser, toChat);
		sendConvertsationHotPing(fromUser, conv_id, id);
	}

	private static AuthToken generateAuthToken(ClientUser user) {
		return new AuthToken(user.getTokenType(), user.getAccessToken());
	}

	public static void sendPictureToSingleUserConversation(ClientUser userFrom,
			ClientUser userTo, String imagePath) throws Exception {
		FileInputStream fis = new FileInputStream(imagePath);
		try {
			sendPictureToSingleUserConversation(userFrom, userTo, fis);
		} finally {
			fis.close();
		}
	}

	public static void sendPictureToSingleUserConversation(ClientUser userFrom,
			ClientUser userTo, InputStream image) throws Exception {
		userFrom = tryLoginByUser(userFrom);

		byte[] srcImageAsByteArray = IOUtils.toByteArray(image);
		BackendREST.sendPicture(generateAuthToken(userFrom),
				getConversationWithSingleUser(userFrom, userTo),
				srcImageAsByteArray, guessMimeType(image));
	}

	private static String guessMimeType(InputStream stream) throws IOException {
		String imageMimeType = URLConnection.guessContentTypeFromStream(stream);
		if (imageMimeType == null) {
			// hardcode as there is no good way to check it when reading a
			// resource from jar
			imageMimeType = "image/jpeg";
		}
		return imageMimeType;
	}

	public static void sendPictureToChatByName(ClientUser userFrom,
			String chatName, String imagePath) throws Exception {
		FileInputStream fis = new FileInputStream(imagePath);
		try {
			sendPictureToChatByName(userFrom, chatName, imagePath);
		} finally {
			fis.close();
		}
	}

	public static void sendPictureToChatByName(ClientUser userFrom,
			String chatName, InputStream src) throws Exception {
		userFrom = tryLoginByUser(userFrom);
		byte[] srcImageAsByteArray = IOUtils.toByteArray(src);
		BackendREST.sendPicture(generateAuthToken(userFrom),
				getConversationIdByName(userFrom, chatName),
				srcImageAsByteArray, guessMimeType(src));
	}

	private static String getConversationIdByName(ClientUser ownerUser,
			String conversationName) throws Exception {
		JSONArray jsonArray = getConversations(ownerUser);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			final String conversationId = conversation.getString("id");
			String name = "null";
			if (conversation.get("name") instanceof String) {
				name = conversation.getString("name");
			}
			name = name.replaceAll("\uFFFC", "").trim();
			if (name.equals("null") || name.equals(ownerUser.getName())) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0))
							.getString("id");
					String contactName = getUserNameByID(id, ownerUser);
					if (contactName.equals(conversationName)) {
						return conversationId;
					}
				}
			}
			if (name.equals(conversationName)) {
				return conversationId;
			}
		}
		throw new NoSuchElementException(String.format(
				"Conversation '%s' does not exist for user '%s'",
				conversationName, ownerUser.getName()));
	}

	public static String[] getConversationsAsStringArray(ClientUser user)
			throws Exception {
		tryLoginByUser(user);
		JSONArray jsonArray = getConversations(user);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject conversation = (JSONObject) jsonArray.get(i);
			String name = "null";
			if (conversation.get("name") instanceof String) {
				name = conversation.getString("name");
			}
			if (name.equals("null") || name.equals(user.getName())) {
				conversation = (JSONObject) conversation.get("members");
				JSONArray otherArray = (JSONArray) conversation.get("others");
				if (otherArray.length() == 1) {
					String id = ((JSONObject) otherArray.get(0))
							.getString("id");
					String contactName = getUserNameByID(id, user);
					result.add(contactName);
				} else {
					String contactName = "";
					for (int j = 0; j < otherArray.length(); j++) {
						String id = ((JSONObject) otherArray.get(j))
								.getString("id");
						contactName += getUserNameByID(id, user);
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

	public static BufferedImage getPictureAssetFromConversation(
			ClientUser fromUser, ClientUser toUser) throws Exception {
		return getPictureAssetFromConversation(fromUser, toUser, 0);
	}

	public static BufferedImage getLastPictureAssetFromConversation(
			ClientUser fromUser, ClientUser toUser) throws Exception {
		return getPictureAssetFromConversation(fromUser, toUser,
				Integer.MAX_VALUE);
	}

	private static BufferedImage getPictureAssetFromConversation(
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
					result = BackendREST.getAssetsDownload(
							generateAuthToken(fromUser), convID,
							(String) data.get("id"));
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

	private static String getConversationWithSingleUser(ClientUser fromUser,
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

	private static ClientUser tryLoginByUser(ClientUser user) throws Exception {
		if (user.getAccessToken() != null) {
			try {
				BackendREST.getUserInfo(generateAuthToken(user));
				return user;
			} catch (BackendRequestException e) {
				// Ignore silently
			}
		}

		final JSONObject loggedUserInfo = BackendREST.login(user.getEmail(),
				user.getPassword());
		user.setAccessToken(loggedUserInfo.getString("access_token"));
		user.setTokenType(loggedUserInfo.getString("token_type"));
		final JSONObject additionalUserInfo = BackendREST
				.getUserInfo(generateAuthToken(user));
		user.setId(additionalUserInfo.getString("id"));
		return user;
	}

	private static String getUserNameByID(String id, ClientUser user)
			throws Exception {
		tryLoginByUser(user);
		final JSONObject userInfo = BackendREST.getUserInfoByID(id,
				generateAuthToken(user));
		return userInfo.getString("name");
	}

	public static void sendConnectRequest(ClientUser user, ClientUser contact,
			String connectName, String message) throws Exception {
		tryLoginByUser(user);
		tryLoginByUser(contact);
		BackendREST.sendConnectRequest(generateAuthToken(user),
				contact.getId(), connectName, message);
	}

	public static void acceptAllConnections(ClientUser user) throws Exception {
		tryLoginByUser(user);
		JSONArray connections = BackendREST.getConnectionsInfo(
				generateAuthToken(user), null, null);
		for (int i = 0; i < connections.length(); i++) {
			String to = ((JSONObject) connections.get(i)).getString("to");
			String status = ((JSONObject) connections.get(i))
					.getString("status");
			if (status.equals(ConnectionStatus.Pending.toString())) {
				changeConnectRequestStatus(user, to, ConnectionStatus.Accepted);
			}
		}
	}

	public static void ignoreAllConnections(ClientUser user) throws Exception {
		tryLoginByUser(user);
		JSONArray connections = BackendREST.getConnectionsInfo(
				generateAuthToken(user), null, null);
		for (int i = 0; i < connections.length(); i++) {
			String to = ((JSONObject) connections.get(i)).getString("to");
			String status = ((JSONObject) connections.get(i))
					.getString("status");
			if (status.equals(ConnectionStatus.Pending.toString())) {
				changeConnectRequestStatus(user, to, ConnectionStatus.Ignored);
			}
		}
	}

	public static void changeConnectRequestStatus(ClientUser user,
			String connectionId, ConnectionStatus newStatus) throws Exception {
		tryLoginByUser(user);
		BackendREST.changeConnectRequestStatus(generateAuthToken(user),
				connectionId, newStatus);
	}

	public static void createGroupConversation(ClientUser user,
			List<ClientUser> contacts, String conversationName)
			throws Exception {
		user = tryLoginByUser(user);
		List<String> ids = new ArrayList<String>();
		for (ClientUser contact : contacts) {
			tryLoginByUser(contact);
			ids.add(contact.getId());
		}
		BackendREST.createGroupConversation(generateAuthToken(user), ids,
				conversationName);
	}

	public static void sendConversationMessage(ClientUser userFrom,
			String convId, String message) throws Exception {
		tryLoginByUser(userFrom);
		BackendREST.sendConversationMessage(generateAuthToken(userFrom),
				convId, message);
	}

	public static String sendConversationPing(ClientUser userFrom, String convId)
			throws Exception {
		tryLoginByUser(userFrom);
		JSONObject response = BackendREST.sendConversationPing(
				generateAuthToken(userFrom), convId);
		return response.getString("id");
	}

	public static void sendConvertsationHotPing(ClientUser userFrom,
			String convId, String refId) throws Exception {
		tryLoginByUser(userFrom);
		BackendREST.sendConvertsationHotPing(generateAuthToken(userFrom),
				convId, refId);
	}

	private static JSONArray getConversations(ClientUser user) throws Exception {
		JSONObject conversationsInfo = BackendREST
				.getConversationsInfo(generateAuthToken(user));
		return conversationsInfo.getJSONArray("conversations");
	}

	private static JSONArray getEventsfromConversation(String convId,
			ClientUser user) throws Exception {
		tryLoginByUser(user);
		return BackendREST.getEventsFromConversation(generateAuthToken(user),
				convId).getJSONArray("events");
	}

	public static BufferedImage getAssetsDownload(String convId,
			String assetId, ClientUser user) throws Exception {
		tryLoginByUser(user);
		return BackendREST.getAssetsDownload(generateAuthToken(user), convId,
				assetId);
	}

	public static void updateUserPicture(ClientUser user, InputStream picture)
			throws Exception {
		tryLoginByUser(user);
		final String convId = user.getId();
		final byte[] srcImageAsByteArray = IOUtils.toByteArray(picture);

		ImageAssetData srcImgData = new ImageAssetData(convId,
				srcImageAsByteArray, guessMimeType(picture));
		srcImgData.setIsPublic(true);
		ImageAssetProcessor imgProcessor = new ImageAssetProcessor(srcImgData);
		ImageAssetRequestBuilder reqBuilder = new ImageAssetRequestBuilder(
				imgProcessor);
		Map<JSONObject, AssetData> sentPictures = BackendREST.sendPicture(
				generateAuthToken(user), convId, reqBuilder);
		Map<String, AssetData> processedAssets = new LinkedHashMap<String, AssetData>();
		for (Map.Entry<JSONObject, AssetData> entry : sentPictures.entrySet()) {
			final String postedImageId = entry.getKey().getJSONObject("data")
					.getString("id");
			processedAssets.put(postedImageId, entry.getValue());
		}
		BackendREST.updateSelfInfo(generateAuthToken(user), null,
				processedAssets, null);
	}

	public static void updateUserPicture(ClientUser user, String picturePath)
			throws Exception {
		final InputStream fis = new FileInputStream(picturePath);
		try {
			updateUserPicture(user, fis);
		} finally {
			fis.close();
		}
	}

	public static void updateUserName(ClientUser user, String newName)
			throws Exception {
		tryLoginByUser(user);
		BackendREST
				.updateSelfInfo(generateAuthToken(user), null, null, newName);
		user.setName(newName);
	}

	public static void updateUserAccentColor(ClientUser user, AccentColor color)
			throws Exception {
		tryLoginByUser(user);
		BackendREST.updateSelfInfo(generateAuthToken(user), color.getId(),
				null, null);
		user.setAccentColor(color);
	}
}
