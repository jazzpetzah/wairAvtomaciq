package com.wearezeta.auto.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.backend.ConnectionStatus;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.OSXAddressBookHelpers;

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
		ClientUser userFrom = usrMgr
				.findUserByNameOrNameAlias(userFromNameAlias);
		for (String userToNameAlias : splitAliases(usersToNameAliases)) {
			ClientUser userTo = usrMgr
					.findUserByNameOrNameAlias(userToNameAlias);
			BackendAPIWrappers.sendConnectRequest(userFrom, userTo,
					CONNECTION_NAME + userTo.getName(), CONNECTION_MESSAGE);
		}
	}

	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		ClientUser chatOwner = usrMgr
				.findUserByNameOrNameAlias(chatOwnerNameAlias);
		List<ClientUser> participants = new ArrayList<ClientUser>();
		for (String participantNameAlias : splitAliases(otherParticipantsNameAlises)) {
			participants.add(usrMgr
					.findUserByNameOrNameAlias(participantNameAlias));
		}
		BackendAPIWrappers.createGroupConversation(chatOwner, participants,
				chatName);
	}

	private static final String OTHER_USERS_ALIAS = "all other";

	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		ClientUser usrFrom = usrMgr
				.findUserByNameOrNameAlias(userFromNameAlias);
		if (usersToNameAliases.toLowerCase().contains(OTHER_USERS_ALIAS)) {
			List<ClientUser> otherUsers = usrMgr.getCreatedUsers();
			otherUsers.remove(usrFrom);
			for (ClientUser usrTo : otherUsers) {
				BackendAPIWrappers.autoTestSendRequest(usrFrom, usrTo);
				BackendAPIWrappers.autoTestAcceptAllRequest(usrTo);
			}
		} else {
			for (String userToName : splitAliases(usersToNameAliases)) {
				ClientUser usrTo = usrMgr.findUserByNameOrNameAlias(userToName);
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
		usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
	}

	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		BackendAPIWrappers.ignoreAllConnections(userTo);
	}

	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * 1000);
	}

	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		ClientUser blockAsUser = usrMgr
				.findUserByNameOrNameAlias(blockAsUserNameAlias);
		ClientUser userToBlock = usrMgr
				.findUserByNameOrNameAlias(userToBlockNameAlias);
		try {
			BackendAPIWrappers.sendConnectRequest(blockAsUser, userToBlock,
					"connect", CommonSteps.CONNECTION_MESSAGE);
		} catch (BackendRequestException e) {
			// Ignore silently
		}
		BackendAPIWrappers.changeConnectRequestStatus(blockAsUser,
				userToBlock.getId(), ConnectionStatus.Blocked);
	}

	public void UnblockContact(String unblockAsUserNameAlias,
			String userToUnblockNameAlias) throws Exception {
		ClientUser unblockAsUser = usrMgr
				.findUserByNameOrNameAlias(unblockAsUserNameAlias);
		ClientUser userToUnblock = usrMgr
				.findUserByNameOrNameAlias(userToUnblockNameAlias);
		try {
			BackendAPIWrappers.sendConnectRequest(unblockAsUser, userToUnblock,
					"connect", CommonSteps.CONNECTION_MESSAGE);
		} catch (BackendRequestException e) {
			// Ignore silently
		}
		BackendAPIWrappers.changeConnectRequestStatus(unblockAsUser,
				userToUnblock.getId(), ConnectionStatus.Accepted);
	}

	public void ArchiveConversationWithUser(String usersToNameAliases,
			String archiveConversationWithUser) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
		ClientUser archivedUser = usrMgr
				.findUserByNameOrNameAlias(archiveConversationWithUser);
		BackendAPIWrappers.archiveUserConv(user, archivedUser);
	}

	public void UnarchiveConversationWithUser(String usersToNameAliases,
			String archiveConversationWithUser) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
		ClientUser archivedUser = usrMgr
				.findUserByNameOrNameAlias(archiveConversationWithUser);
		BackendAPIWrappers.unarchiveUserConv(user, archivedUser);
	}

	public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		BackendAPIWrappers.acceptAllConnections(userTo);
	}

	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser pingFromUser = usrMgr
				.findUserByNameOrNameAlias(pingFromUserNameAlias);
		dstConversationName = usrMgr.replaceAliasesOccurences(
				dstConversationName, FindBy.NAME_ALIAS);
		pingId = BackendAPIWrappers.sendPingToConversation(pingFromUser,
				dstConversationName);
		Thread.sleep(1000);
	}

	public void UserSentMessageToUser(String msgFromUserNameAlias,
			String dstUserNameAlias, String message) throws Exception {
		ClientUser msgFromUser = usrMgr
				.findUserByNameOrNameAlias(msgFromUserNameAlias);
		ClientUser msgToUser = usrMgr
				.findUserByNameOrNameAlias(dstUserNameAlias);
		BackendAPIWrappers.sendDialogMessage(msgFromUser, msgToUser, message);
	}

	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		ClientUser hotPingFromUser = usrMgr
				.findUserByNameOrNameAlias(hotPingFromUserNameAlias);
		dstConversationName = usrMgr.replaceAliasesOccurences(
				dstConversationName, FindBy.NAME_ALIAS);
		BackendAPIWrappers.sendHotPingToConversation(hotPingFromUser,
				dstConversationName, pingId);
		Thread.sleep(1000);
	}

	public void UserSendsImageToConversation(String imageSenderUserNameAlias,
			String imagePath, String dstConversationName, Boolean isGroup)
			throws Exception {
		ClientUser imageSender = usrMgr
				.findUserByNameOrNameAlias(imageSenderUserNameAlias);
		if (!isGroup) {
			ClientUser imageReceiver = usrMgr
					.findUserByNameOrNameAlias(dstConversationName);
			BackendAPIWrappers.sendPictureToSingleUserConversation(imageSender,
					imageReceiver, imagePath);
		} else {
			dstConversationName = usrMgr.replaceAliasesOccurences(
					dstConversationName, FindBy.NAME_ALIAS);
			BackendAPIWrappers.sendPictureToChatByName(imageSender,
					dstConversationName, imagePath);
		}
	}

	public void AddContactsUsersToMacContacts() throws Exception {
		OSXAddressBookHelpers.addUsersToContacts(usrMgr.getCreatedUsers());
	}

	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		OSXAddressBookHelpers.removeUsersFromContacts(usrMgr.getCreatedUsers());
	}

	public void IChangeUserAvatarPicture(String userNameAlias,
			String picturePath) throws Exception {
		if (new File(picturePath).exists()) {
			BackendAPIWrappers.updateUserPicture(
					usrMgr.findUserByNameOrNameAlias(userNameAlias),
					picturePath);
		} else {
			throw new NotImplementedException("Please implement loading pictures from resources");
			// TODO: extract picture from resources
		}
	}

	public void IChangeUserName(String userNameAlias, String newName)
			throws Exception {
		BackendAPIWrappers.updateUserName(
				usrMgr.findUserByNameOrNameAlias(userNameAlias), newName);
	}

	public void IChangeUserAccentColor(String userNameAlias, String colorName)
			throws Exception {
		BackendAPIWrappers.updateUserAccentColor(
				usrMgr.findUserByNameOrNameAlias(userNameAlias),
				AccentColor.getByName(colorName));
	}

	public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
			throws Exception {
		usrMgr.appendSharedUsers(namePrefix, count);
	}

	public void UserXIsMe(String nameAlias) throws Exception {
		usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(nameAlias));
	}

	public void BlockTcpConnectionForApp(String appName) throws IOException {
		CommonUtils.blockTcpForAppName(appName);
	}

	public void EnableTcpConnectionForApp(String appName) throws IOException {
		CommonUtils.enableTcpForAppName(appName);
	}

	public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
			String contactAlias, int timeout) throws Exception {
		String query = usrMgr.replaceAliasesOccurences(contactAlias,
				FindBy.NAME_ALIAS);
		query = usrMgr.replaceAliasesOccurences(contactAlias,
				FindBy.EMAIL_ALIAS);
		BackendAPIWrappers.waitUntilContactsFound(
				usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query, 1,
				true, timeout);
	}
}
