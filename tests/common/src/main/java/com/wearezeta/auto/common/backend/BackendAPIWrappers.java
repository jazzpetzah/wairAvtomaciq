package com.wearezeta.auto.common.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.email.EmailHeaders;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.UserState;

public final class BackendAPIWrappers {
	public static final int ACTIVATION_TIMEOUT = 120; // seconds

	private static final Logger log = ZetaLogger
			.getLog(BackendAPIWrappers.class.getSimpleName());

	// ! Mutates user instance
	public static ClientUser createUser(ClientUser user) throws Exception {
		IMAPSMailbox mbox = IMAPSMailbox.createDefaultInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", user.getEmail());
		MBoxChangesListener listener = mbox.startMboxListener(expectedHeaders);
		BackEndREST.registerNewUser(user.getEmail(), user.getName(),
				user.getPassword());
		activateRegisteredUser(listener);
		user.setUserState(UserState.Created);
		return user;
	}

	public static void activateRegisteredUser(MBoxChangesListener listener)
			throws Exception {
		EmailHeaders registrationInfo = IMAPSMailbox.getFilteredMessageHeaders(
				listener, ACTIVATION_TIMEOUT);
		BackEndREST.activateNewUser(registrationInfo.getXZetaKey(),
				registrationInfo.getXZetaCode());
		log.debug(String.format("User %s is activated",
				registrationInfo.getLastUserEmail()));
	}

	

	// ! Mutates all user instances
	public static void createContactLinks(ClientUser userFrom,
			List<ClientUser> usersTo) throws Exception {
		userFrom = BackEndREST.loginByUser(userFrom);
		userFrom.setId(BackEndREST.getUserInfo(userFrom).getId());
		for (ClientUser userTo : usersTo) {
			autoTestSendRequest(userFrom, userTo);
			autoTestAcceptAllRequest(userTo);
		}
	}

	// ! Mutates user instance
	public static void autoTestSendRequest(ClientUser userFrom, ClientUser userTo)
			throws Exception {
		userFrom = BackEndREST.loginByUser(userFrom);
		userFrom = BackEndREST.getUserInfo(userFrom);
		BackEndREST.sendConnectRequest(userFrom, userTo, userTo.getName(),
				"Hello!!!");
		userFrom.setUserState(UserState.RequestSend);
	}

	// ! Mutates user instance
	public static ClientUser autoTestAcceptAllRequest(ClientUser userTo)
			throws Exception {
		userTo = BackEndREST.loginByUser(userTo);
		BackEndREST.acceptAllConnections(userTo);
		userTo.setUserState(UserState.AllContactsConnected);
		return userTo;
	}

	// ! Mutates user instance
	public static void sendDialogMessage(ClientUser fromUser,
			ClientUser toUser, String message) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationWithSingleUser(fromUser, toUser);
		BackEndREST.sendConversationMessage(fromUser, id, message);
	}

	// ! Mutates user instance
	public static void sendDialogMessageByChatName(ClientUser fromUser,
			String toChat, String message) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationByName(fromUser, toChat);
		BackEndREST.sendConversationMessage(fromUser, id, message);
	}

	// ! Mutates user instance
	public static String sendPingToConversation(ClientUser fromUser,
			String toChat) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationByName(fromUser, toChat);
		return BackEndREST.sendConversationPing(fromUser, id);
	}

	// ! Mutates user instance
	public static void sendHotPingToConversation(ClientUser fromUser,
			String toChat, String id) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String conv_id = BackEndREST.getConversationByName(fromUser, toChat);
		BackEndREST.sendConvertsationHotPing(fromUser, conv_id, id);
	}
}
