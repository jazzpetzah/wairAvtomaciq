package com.wearezeta.auto.common.backend;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import com.wearezeta.auto.common.usrmgmt.UserState;
import com.wearezeta.auto.image_send.AssetData;
import com.wearezeta.auto.image_send.ImageAssetData;
import com.wearezeta.auto.image_send.ImageAssetProcessor;
import com.wearezeta.auto.image_send.ImageAssetRequestBuilder;
import com.wearezeta.auto.image_send.SelfImageProcessor;

// Almost all methods of this class mutate ClientUser
// argument by performing automatic login (set id and session token attributes)
public final class BackendAPIWrappers {
	public static final int UI_ACTIVATION_TIMEOUT = 120; // seconds
	public static final int BACKEND_ACTIVATION_TIMEOUT = 90; // seconds

	private static final int REQUEST_TOO_FREQUENT_ERROR = 429;
	private static final int AUTH_FAILED_ERROR = 403;
	private static final int SERVER_SIDE_ERROR = 500;
	private static final int MAX_BACKEND_RETRIES = 5;

	private static final long MAX_MSG_DELIVERY_OFFSET = 10000; // milliseconds

	private static final Logger log = ZetaLogger
			.getLog(BackendAPIWrappers.class.getSimpleName());

	public static void setDefaultBackendURL(String url) {
		BackendREST.setDefaultBackendURL(url);
	}

	private static Future<String> initMessageListener(
			ClientUser userToActivate, int retryNumber) throws Exception {
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER,
				userToActivate.getEmail());
		if (retryNumber == 1) {
			return mbox.getMessage(expectedHeaders, BACKEND_ACTIVATION_TIMEOUT);
		} else {
			// The MAX_MSG_DELIVERY_OFFSET is necessary because of small
			// time
			// difference between
			// UTC and your local machine
			return mbox.getMessage(expectedHeaders, BACKEND_ACTIVATION_TIMEOUT,
					new Date().getTime() - MAX_MSG_DELIVERY_OFFSET);
		}
	}

	/**
	 * Creates a new user by sending the corresponding request to the backend
	 * 
	 * @param user
	 *            ClientUser instance with initial user parameters
	 *            (name/email/password)
	 * @param retryNumber
	 *            set this to 1 if it is the first time you try to create this
	 *            particular user
	 * @return Created ClientUser instance (with id property filled)
	 * @throws Exception
	 */
	public static ClientUser createUser(ClientUser user, int retryNumber,
			RegistrationStrategy strategy) throws Exception {
		switch (strategy) {
		case ByEmail:
			final Future<String> activationMessage = initMessageListener(user,
					retryNumber);
			BackendREST.registerNewUser(user.getEmail(), user.getName(),
					user.getPassword());
			activateRegisteredUserByEmail(activationMessage);
			attachUserPhoneNumber(user);
			break;
		case ByPhoneNumber:
			BackendREST.registerNewUser(user.getPhoneNumber(), user.getName());
			activateRegisteredUserByPhoneNumber(user.getPhoneNumber());
			changeUserPassword(user, null, user.getPassword());
			final int maxAttachRetries = 2;
			for (int tryNum = 1; tryNum <= maxAttachRetries; tryNum++) {
				try {
					log.debug(String
							.format("Trying to attach email address '%s' to the newly created user (retry %s of %s)...",
									user.getEmail(), tryNum, maxAttachRetries));
					attachUserEmail(user, tryNum);
					break;
				} catch (ExecutionException e) {
					if (tryNum >= maxAttachRetries) {
						throw e;
					}
				}
			}
			break;
		default:
			throw new RuntimeException(String.format(
					"Unknown registration strategy '%s'", strategy.name()));
		}
		user.setUserState(UserState.Created);
		return user;
	}

	public static void activateRegisteredUserByEmail(
			Future<String> activationMessage) throws Exception {
		final ActivationMessage registrationInfo = new ActivationMessage(
				activationMessage.get());
		final String key = registrationInfo.getXZetaKey();
		final String code = registrationInfo.getXZetaCode();
		log.debug(String
				.format("Received activation email message with key: %s, code: %s. Proceeding with activation...",
						key, code));
		BackendREST.activateNewUser(key, code);
		log.debug(String.format("User '%s' is successfully activated",
				registrationInfo.getDeliveredToEmail()));
	}

	public static void activateRegisteredUserByPhoneNumber(
			PhoneNumber phoneNumber) throws Exception {
		BackendREST.activateNewUser(phoneNumber, BackendREST
				.getActivationDataViaBackdoor(phoneNumber).getString("code"));
		log.debug(String.format("User '%s' is successfully activated",
				phoneNumber.toString()));
	}

	public static void attachUserPhoneNumber(ClientUser user) throws Exception {
		user = tryLoginByUser(user);
		BackendREST.updateSelfPhoneNumber(generateAuthToken(user),
				user.getPhoneNumber());
		activateRegisteredUserByPhoneNumber(user.getPhoneNumber());
	}

	/**
	 * Change/set user password
	 * 
	 * @param user
	 * @param oldPassword
	 *            set this to null if the user has no password set
	 * @param newPassword
	 * @throws Exception
	 */
	public static void changeUserPassword(ClientUser user, String oldPassword,
			String newPassword) throws Exception {
		user = tryLoginByUser(user);
		BackendREST.updateSelfPassword(generateAuthToken(user), oldPassword,
				newPassword);
		user.setPassword(newPassword);
	}

	public static void attachUserEmail(ClientUser user, int retryNumber)
			throws Exception {
		final Future<String> activationMessage = initMessageListener(user,
				retryNumber);
		user = tryLoginByUser(user);
		BackendREST.updateSelfEmail(generateAuthToken(user), user.getEmail());
		activateRegisteredUserByEmail(activationMessage);
	}

	public static String getUserActivationLink(Future<String> activationMessage)
			throws Exception {
		ActivationMessage registrationInfo = new ActivationMessage(
				activationMessage.get());
		return registrationInfo.extractActivationLink();
	}

	public static String getPasswordResetLink(
			Future<String> passwordResetMessage) throws Exception {
		PasswordResetMessage resetPassword = new PasswordResetMessage(
				passwordResetMessage.get());
		return resetPassword.extractPasswordResetLink();
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

	public static String getConversationIdByName(ClientUser ownerUser,
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

	public static ClientUser tryLoginByUser(ClientUser user) throws Exception {
		if (user.getAccessToken() != null) {
			try {
				BackendREST.getUserInfo(generateAuthToken(user));
				return user;
			} catch (BackendRequestException e) {
				// Ignore silently
			}
		}

		JSONObject loggedUserInfo = null;
		int tryNum = 0;
		while (tryNum < MAX_BACKEND_RETRIES) {
			try {
				try {
					loggedUserInfo = BackendREST.login(user.getEmail(),
							user.getPassword());
				} catch (BackendRequestException e) {
					// Retry in case the user has only phone number attached
					if (e.getReturnCode() == AUTH_FAILED_ERROR) {
						BackendREST.generateLoginCode(user.getPhoneNumber());
						final String code = BackendREST
								.getLoginCodeViaBackdoor(user.getPhoneNumber())
								.getString("code");
						loggedUserInfo = BackendREST.login(
								user.getPhoneNumber(), code);
					}
				}
				break;
			} catch (BackendRequestException e) {
				if (e.getReturnCode() == REQUEST_TOO_FREQUENT_ERROR) {
					log.debug(String.format(
							"Login request failed. Retrying (%d of %d)...",
							tryNum + 1, MAX_BACKEND_RETRIES));
					e.printStackTrace();
					tryNum++;
					if (tryNum >= MAX_BACKEND_RETRIES) {
						throw e;
					} else {
						Thread.sleep((tryNum + 1) * 2000);
					}
				} else {
					throw e;
				}
			}
		}

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

	public static String getUserPictureHash(ClientUser user) throws Exception {
		final JSONObject userInfo = BackendREST
				.getUserInfo(generateAuthToken(user));
		final String picture = userInfo.getJSONArray("picture").toString();
		return DigestUtils.sha256Hex(picture);
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

	public static void addContactsToGroupConversation(ClientUser asUser,
			List<ClientUser> contacts, String conversationName)
			throws Exception {
		asUser = tryLoginByUser(asUser);
		List<String> ids = new ArrayList<String>();
		for (ClientUser contact : contacts) {
			tryLoginByUser(contact);
			ids.add(contact.getId());
		}
		BackendREST.addContactsToGroupConvo(generateAuthToken(asUser), ids,
				getConversationIdByName(asUser, conversationName));
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
		JSONObject conversationsInfo = null;
		int tryNum = 0;
		while (tryNum < MAX_BACKEND_RETRIES) {
			try {
				conversationsInfo = BackendREST
						.getConversationsInfo(generateAuthToken(user));
				break;
			} catch (BackendRequestException e) {
				if (e.getReturnCode() == SERVER_SIDE_ERROR) {
					log.debug(String
							.format("Server side request failed. Retrying (%d of %d)...",
									tryNum + 1, MAX_BACKEND_RETRIES));
					e.printStackTrace();
					tryNum++;
					if (tryNum >= MAX_BACKEND_RETRIES) {
						throw e;
					} else {
						Thread.sleep((tryNum + 1) * 2000);
					}
				} else {
					throw e;
				}
			}
		}
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
		srcImgData.setCorrelationId(String.valueOf(UUID.randomUUID()));
		srcImgData.setNonce(srcImgData.getCorrelationId());
		ImageAssetProcessor imgProcessor = new SelfImageProcessor(srcImgData);
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

	public static void updateConvLastReadState(ClientUser user, String convId,
			String lastReadEvent) throws Exception {
		// can be used to override last_read event or use
		// getLastEventFromConversation to set the last read to the last event
		tryLoginByUser(user);
		BackendREST.updateConvSelfInfo(generateAuthToken(user), convId,
				lastReadEvent, null, null);
	}

	public static void updateConvMutedState(ClientUser user,
			ClientUser mutedUser, boolean muted) throws Exception {
		tryLoginByUser(user);
		BackendREST.updateConvSelfInfo(generateAuthToken(user),
				getConversationWithSingleUser(user, mutedUser), null, muted,
				null);
	}

	public static void archiveUserConv(ClientUser ownerUser,
			ClientUser archivedUser) throws Exception {
		tryLoginByUser(ownerUser);
		BackendREST.updateConvSelfInfo(generateAuthToken(ownerUser),
				getConversationWithSingleUser(ownerUser, archivedUser), null,
				null, true);
	}

	public static void archiveGroupConv(ClientUser selfUser,
			String conversationToArchive) throws Exception {
		tryLoginByUser(selfUser);
		BackendREST.updateConvSelfInfo(generateAuthToken(selfUser),
				conversationToArchive, null, null, true);
	}

	public static void unarchiveUserConv(ClientUser ownerUser,
			ClientUser archivedUser) throws Exception {
		tryLoginByUser(ownerUser);
		BackendREST.updateConvSelfInfo(generateAuthToken(ownerUser),
				getConversationWithSingleUser(ownerUser, archivedUser), null,
				null, false);
	}

	public static class NoContactsFoundException extends Exception {
		private static final long serialVersionUID = -7682778364420522320L;

		public NoContactsFoundException(String msg) {
			super(msg);
		}
	}

	public static void waitUntilContactsFound(ClientUser searchByUser,
			String query, int expectedCount, boolean orMore, int timeout)
			throws Exception {
		tryLoginByUser(searchByUser);
		long startTimestamp = (new Date()).getTime();
		int currentCount = -1;
		while ((new Date()).getTime() <= startTimestamp + timeout * 1000) {
			final JSONObject searchResult = BackendREST.searchForContacts(
					generateAuthToken(searchByUser), query);
			if (searchResult.has("documents")
					&& (searchResult.get("documents") instanceof JSONArray)) {
				currentCount = searchResult.getJSONArray("documents").length();
			} else {
				currentCount = 0;
			}
			if (currentCount == expectedCount
					|| (orMore && currentCount >= expectedCount)) {
				return;
			}
			Thread.sleep(1000);
		}
		throw new NoContactsFoundException(
				String.format(
						"%s contact(s) '%s' were not found within %s second(s) timeout",
						expectedCount, query, timeout));
	}
}
