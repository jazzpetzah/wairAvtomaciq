package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.List;

import com.wearezeta.auto.common.backend.BackEndREST;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.OSXAddressBookHelpers;
import com.wearezeta.auto.user_management.ClientUsersManager;

public final class CommonSteps {
	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";

	private String pingId = null;

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public ClientUsersManager getUserManager() {
		return this.usrMgr;
	}

	private static CommonSteps instance = null;

	public static CommonSteps getInstance() {
		if (instance == null) {
			instance = new CommonSteps();
		}
		return instance;
	}

	private CommonSteps() {
	}

	public static final String ALIASES_SEPARATOR = ",";

	public static List<String> splitAliases(String aliases) {
		List<String> result = new ArrayList<String>();
		String[] splittedAliases = aliases.split(ALIASES_SEPARATOR);
		for (String splittedAlias : splittedAliases) {
			result.add(splittedAlias.trim());
		}
		return result;
	}

	public void ConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		ClientUser userFrom = usrMgr.findUserByNameAlias(userFromNameAlias);
		userFrom = BackEndREST.loginByUser(userFrom);
		for (String userToNameAlias : splitAliases(usersToNameAliases)) {
			ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
			userTo = BackEndREST.loginByUser(userTo);
			BackEndREST.sendConnectRequest(userFrom, userTo, CONNECTION_NAME
					+ userTo.getName(), CONNECTION_MESSAGE);
		}
	}

	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		ClientUser chatOwner = usrMgr.findUserByNameAlias(chatOwnerNameAlias);
		chatOwner = BackEndREST.loginByUser(chatOwner);
		List<ClientUser> participants = new ArrayList<ClientUser>();
		for (String participantNameAlias : splitAliases(otherParticipantsNameAlises)) {
			participants.add(usrMgr.findUserByNameAlias(participantNameAlias));
		}
		BackEndREST.createGroupConversation(chatOwner, participants, chatName);
	}

	private static final String OTHER_USERS_ALIAS = "all other";

	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		ClientUser usrFrom = usrMgr.findUserByNameAlias(userFromNameAlias);
		usrFrom = BackEndREST.loginByUser(usrFrom);
		if (usersToNameAliases.toLowerCase().contains(OTHER_USERS_ALIAS)) {
			List<ClientUser> otherUsers = usrMgr.getCreatedUsers();
			otherUsers.remove(usrFrom);
			for (ClientUser usrTo : otherUsers) {
				BackendAPIWrappers.autoTestSendRequest(usrFrom, usrTo);
				BackendAPIWrappers.autoTestAcceptAllRequest(usrTo);
			}
		} else {
			for (String userToName : splitAliases(usersToNameAliases)) {
				ClientUser usrTo = usrMgr.findUserByNameAlias(userToName);
				BackendAPIWrappers.autoTestSendRequest(usrFrom, usrTo);
				BackendAPIWrappers.autoTestAcceptAllRequest(usrTo);
			}
		}
	}

	public void ThereAreNUsers(int count) throws Exception {
		usrMgr.createUsersOnBackend(count);
	}

	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		usrMgr.createUsersOnBackend(count);
		usrMgr.setSelfUser(usrMgr.findUserByNameAlias(myNameAlias));
	}

	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
		userTo = BackEndREST.loginByUser(userTo);
		BackEndREST.ignoreAllConnections(userTo);
	}

	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * 1000);
	}

	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		ClientUser blockAsUser = usrMgr
				.findUserByNameAlias(blockAsUserNameAlias);
		blockAsUser = BackEndREST.loginByUser(blockAsUser);
		ClientUser userToBlock = usrMgr
				.findUserByNameAlias(userToBlockNameAlias);
		userToBlock = BackEndREST.loginByUser(userToBlock);
		try {
			BackEndREST.sendConnectRequest(blockAsUser, userToBlock, "connect",
					"Hello");
			Thread.sleep(1000);
		} catch (BackendRequestException e) {
			// Ignore silently
		}
		BackEndREST.changeConnectRequestStatus(blockAsUser,
				userToBlock.getId(), "blocked");
	}

	public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
		userTo = BackEndREST.loginByUser(userTo);
		BackEndREST.acceptAllConnections(userTo);
	}

	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser pingFromUser = usrMgr
				.findUserByNameAlias(pingFromUserNameAlias);
		pingFromUser = BackEndREST.loginByUser(pingFromUser);
		pingId = BackendAPIWrappers.sendPingToConversation(pingFromUser,
				dstConversationName);
		Thread.sleep(1000);
	}

	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser hotPingFromUser = usrMgr
				.findUserByNameAlias(hotPingFromUserNameAlias);
		hotPingFromUser = BackEndREST.loginByUser(hotPingFromUser);
		BackendAPIWrappers.sendHotPingToConversation(hotPingFromUser,
				dstConversationName, pingId);
		Thread.sleep(1000);
	}

	public void AddContactsUsersToMacContacts() throws Exception {
		OSXAddressBookHelpers.addUsersToContacts(usrMgr.getCreatedUsers());
	}

	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		OSXAddressBookHelpers.removeUsersFromContacts(usrMgr.getCreatedUsers());
	}
}
