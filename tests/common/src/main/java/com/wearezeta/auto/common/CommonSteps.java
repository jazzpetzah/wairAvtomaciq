package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.List;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.backend.ConnectionStatus;
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
		for (String userToNameAlias : splitAliases(usersToNameAliases)) {
			ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
			BackendAPIWrappers.autoTestSendRequest(userFrom, userTo);
		}
	}

	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		ClientUser chatOwner = usrMgr.findUserByNameAlias(chatOwnerNameAlias);
		List<ClientUser> participants = new ArrayList<ClientUser>();
		for (String participantNameAlias : splitAliases(otherParticipantsNameAlises)) {
			participants.add(usrMgr.findUserByNameAlias(participantNameAlias));
		}
		BackendAPIWrappers.createGroupConversation(chatOwner, participants,
				chatName);
	}

	private static final String OTHER_USERS_ALIAS = "all other";

	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		ClientUser usrFrom = usrMgr.findUserByNameAlias(userFromNameAlias);
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
		BackendAPIWrappers.ignoreAllConnections(userTo);
	}

	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * 1000);
	}

	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		ClientUser blockAsUser = usrMgr
				.findUserByNameAlias(blockAsUserNameAlias);
		ClientUser userToBlock = usrMgr
				.findUserByNameAlias(userToBlockNameAlias);
		try {
			BackendAPIWrappers.autoTestSendRequest(blockAsUser, userToBlock);
		} catch (BackendRequestException e) {
			// Ignore silently
		}
		BackendAPIWrappers.changeConnectRequestStatus(blockAsUser,
				userToBlock.getId(), ConnectionStatus.Blocked);
	}

	public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
		BackendAPIWrappers.acceptAllConnections(userTo);
	}

	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser pingFromUser = usrMgr
				.findUserByNameAlias(pingFromUserNameAlias);
		pingId = BackendAPIWrappers.sendPingToConversation(pingFromUser,
				dstConversationName);
		Thread.sleep(1000);
	}

	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser hotPingFromUser = usrMgr
				.findUserByNameAlias(hotPingFromUserNameAlias);
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

	public void IHaveConnectionRequest(String userToNameAlias) throws Exception {
		ClientUser userTo = usrMgr.findUserByNameAlias(userToNameAlias);
		BackendAPIWrappers.autoTestSendRequest(userTo,
				usrMgr.getSelfUserOrThrowError(), CommonSteps.CONNECTION_NAME
						+ userTo.getName(), CommonSteps.CONNECTION_MESSAGE);
	}
}
