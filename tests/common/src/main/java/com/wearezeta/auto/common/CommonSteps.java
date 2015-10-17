package com.wearezeta.auto.common;

import com.wearezeta.auto.common.backend.*;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommonSteps {
	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";
	private static final int BACKEND_USER_SYNC_TIMEOUT = 45; // seconds
	private static final int BACKEND_SUGGESTIONS_SYNC_TIMEOUT = 90; // seconds

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CommonSteps.class
			.getSimpleName());

	private String pingId = null;

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public ClientUsersManager getUserManager() {
		return this.usrMgr;
	}

	private static CommonSteps instance = null;

	public synchronized static CommonSteps getInstance() {
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
			String usersToNameAliases) throws Exception {
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
		// Set nameAlias for the group
		// Required for group calling tests
		ClientUser groupUser = new ClientUser();
		groupUser.setName(chatName);
		groupUser.addNameAlias(chatName);
		usrMgr.appendCustomUser(groupUser);
	}

	private static final String OTHER_USERS_ALIAS = "all other";

	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		ClientUser usrFrom = usrMgr
				.findUserByNameOrNameAlias(userFromNameAlias);
		if (usersToNameAliases.toLowerCase().contains(OTHER_USERS_ALIAS)) {
			List<ClientUser> otherUsers = usrMgr.getCreatedUsers();
			final int conversationsCount = BackendAPIWrappers.getConversations(
					usrFrom).length();
			if (conversationsCount >= otherUsers.size() * 0.9) {
				// Skip reconnect since this shortcut is used only for perf
				// tests
				return;
			}
			otherUsers.remove(usrFrom);
			for (ClientUser contact : otherUsers) {
				BackendAPIWrappers.autoTestSendRequest(contact, usrFrom);
			}
			BackendAPIWrappers.autoTestAcceptAllRequest(usrFrom);
		} else {
			for (String userToName : splitAliases(usersToNameAliases)) {
				ClientUser usrTo = usrMgr.findUserByNameOrNameAlias(userToName);
				BackendAPIWrappers.autoTestSendRequest(usrFrom, usrTo);
				BackendAPIWrappers.autoTestAcceptAllRequest(usrTo);
			}
		}
	}

	public void ThereAreNUsers(Platform currentPlatform, int count)
			throws Exception {
		usrMgr.createUsersOnBackend(count, RegistrationStrategy
				.getRegistrationStrategyForPlatform(currentPlatform));
	}

	public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count,
			String myNameAlias) throws Exception {
		usrMgr.createUsersOnBackend(count, RegistrationStrategy
				.getRegistrationStrategyForPlatform(currentPlatform));
		usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
	}

    public void ThereAreNUsersWhereXIsMeOtr(Platform currentPlatform, int count,
    String myNameAlias) throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy
                .getRegistrationStrategyForPlatform(currentPlatform));
        usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
        RemoteProcessIPC.startDevices(count - 1);
    }
	
	public void ThereAreNUsersWhereXIsMeRegOnlyByMail(Platform currentPlatform, int count,
			String myNameAlias) throws Exception {
		usrMgr.createUsersOnBackend(count, RegistrationStrategy.ByEmailOnly);
		usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
	}

	public void ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(
			Platform currentPlatform, int count, String myNameAlias)
			throws Exception {
		usrMgr.createUsersOnBackend(count,
				RegistrationStrategy.ByPhoneNumberOnly);
		usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
	}

	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
		BackendAPIWrappers.ignoreAllConnections(userTo);
	}

	private static final int DRIVER_PING_INTERVAL_SECONDS = 60;

	/**
	 * Wait for time in seconds
	 * 
	 * @throws Exception
	 */
	public void WaitForTime(int seconds) throws Exception {
		final Thread pingThread = new Thread() {
			public void run() {
				do {
					try {
						Thread.sleep(DRIVER_PING_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						return;
					}
					try {
						PlatformDrivers.getInstance().pingDrivers();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				} while (!isInterrupted());
			}
		};
		pingThread.start();
		try {
			Thread.sleep(seconds * 1000);
		} finally {
			pingThread.interrupt();
		}
	}

	/**
	 * Wait for time in seconds
	 * 
	 * @throws Exception
	 */
	public void WaitForTime(double seconds) throws Exception {
		final Thread pingThread = new Thread() {
			public void run() {
				do {
					try {
						Thread.sleep(DRIVER_PING_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						return;
					}
					try {
						PlatformDrivers.getInstance().pingDrivers();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				} while (!isInterrupted());
			}
		};
		pingThread.start();
		try {
			Thread.sleep((int) (seconds * 1000));
		} finally {
			pingThread.interrupt();
		}
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

	public void ArchiveConversationWithGroup(String aUser,
			String archiveConversationWithGroup) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(aUser);
		final String conversationIDToArchive = BackendAPIWrappers
				.getConversationIdByName(user, archiveConversationWithGroup);
		BackendAPIWrappers.archiveGroupConv(user, conversationIDToArchive);
	}

	public void GenerateNewLoginCode(String asUser) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(asUser);
		BackendAPIWrappers.generateNewLoginCode(user);
	}

	public void MuteConversationWithUser(String usersToNameAliases,
			String muteConversationWithUser) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
		ClientUser mutedUser = usrMgr
				.findUserByNameOrNameAlias(muteConversationWithUser);

		BackendAPIWrappers.updateConvMutedState(user, mutedUser, true);
	}

	public void MuteConversationWithGroup(String usersToNameAliases,
			String muteConversationWithGroup) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
		BackendAPIWrappers.updateGroupConvMutedState(user,
				muteConversationWithGroup, true);
	}

	public void UnarchiveConversationWithUser(String usersToNameAliases,
			String archiveConversationWithUser) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
		ClientUser archivedUser = usrMgr
				.findUserByNameOrNameAlias(archiveConversationWithUser);
		BackendAPIWrappers.unarchiveUserConv(user, archivedUser);
	}

	public void UnarchiveConversationWithGroup(String aUser,
			String archiveConversationWithGroup) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(aUser);
		final String conversationIDToArchive = BackendAPIWrappers
				.getConversationIdByName(user, archiveConversationWithGroup);
		BackendAPIWrappers.unarchiveGroupConv(user, conversationIDToArchive);
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

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
            String dstUserNameAlias, String message) throws Exception {
        ClientUser msgFromUser = usrMgr
                .findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = usrMgr
                .findUserByNameOrNameAlias(dstUserNameAlias);
        RemoteProcessIPC.loginToSingleRemoteProcess(msgFromUser);
        RemoteProcessIPC.sendConversationMessage(msgFromUser, msgToUser.getId(), message);
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

	public void UserSentMessageToConversation(String userFromNameAlias,
			String dstConversationName, String message) throws Exception {
		ClientUser userFrom = usrMgr
				.findUserByNameOrNameAlias(userFromNameAlias);
		dstConversationName = usrMgr.replaceAliasesOccurences(
				dstConversationName, FindBy.NAME_ALIAS);
		BackendAPIWrappers.sendDialogMessageByChatName(userFrom,
				dstConversationName, message);
	}

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
            String dstConversationName, String message) throws Exception {
        ClientUser userFrom = usrMgr
                .findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(
                dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);

        RemoteProcessIPC.loginToSingleRemoteProcess(userFrom);
        RemoteProcessIPC.sendConversationMessage(userFrom, dstConvId, message);
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

	public void IChangeUserAvatarPicture(String userNameAlias,
			String picturePath) throws Exception {
		final ClientUser dstUser = usrMgr
				.findUserByNameOrNameAlias(userNameAlias);
		if (new File(picturePath).exists()) {
			BackendAPIWrappers.updateUserPicture(dstUser, picturePath);
		} else {
			throw new IOException(String.format(
					"The picture '%s' is not accessible", picturePath));
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

	public void BlockTcpConnectionForApp(String appName) throws Exception {
		CommonUtils.blockTcpForAppName(appName);
	}

	public void EnableTcpConnectionForApp(String appName) throws Exception {
		CommonUtils.enableTcpForAppName(appName);
	}

	public void WaitUntilSuggestionFound(String userAsNameAlias)
			throws NoSuchUserException, Exception {
		BackendAPIWrappers.waitUntilSuggestionFound(
				usrMgr.findUserByNameOrNameAlias(userAsNameAlias),
				BACKEND_SUGGESTIONS_SYNC_TIMEOUT);
	}

	public void WaitUntilContactIsNotFoundInSearch(String searchByNameAlias,
			String contactAlias, int timeoutSeconds) throws Exception {
		String query = usrMgr.replaceAliasesOccurences(contactAlias,
				FindBy.NAME_ALIAS);
		query = usrMgr.replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
		BackendAPIWrappers.waitUntilContactNotFound(
				usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query,
				timeoutSeconds);
	}

	public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
			String contactAlias) throws Exception {
		String query = usrMgr.replaceAliasesOccurences(contactAlias,
				FindBy.NAME_ALIAS);
		query = usrMgr.replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
		BackendAPIWrappers.waitUntilContactsFound(
				usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query, 1,
				true, BACKEND_USER_SYNC_TIMEOUT);
	}

	public void WaitUntilContactBlockStateInSearch(String searchByNameAlias,
			String contactAlias, boolean expectedState) throws Exception {
		String query = usrMgr.replaceAliasesOccurences(contactAlias,
				FindBy.NAME_ALIAS);
		query = usrMgr.replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
		BackendAPIWrappers.waitUntilContactBlockState(
				usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query,
				expectedState, BACKEND_USER_SYNC_TIMEOUT);
	}

	public void UserXAddedContactsToGroupChat(String userAsNameAlias,
			String contactsToAddNameAliases, String chatName) throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userAsNameAlias);
		List<ClientUser> contactsToAdd = new ArrayList<ClientUser>();
		for (String contactNameAlias : splitAliases(contactsToAddNameAliases)) {
			contactsToAdd.add(usrMgr
					.findUserByNameOrNameAlias(contactNameAlias));
		}
		BackendAPIWrappers.addContactsToGroupConversation(userAs,
				contactsToAdd, chatName);
	}

	public void UserXRemoveContactFromGroupChat(String userAsNameAlias,
			String contactToRemoveNameAlias, String chatName) throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userAsNameAlias);
		final ClientUser userToRemove = usrMgr
				.findUserByNameOrNameAlias(contactToRemoveNameAlias);

		BackendAPIWrappers.removeUserFromGroupConversation(userAs,
				userToRemove, chatName);
	}

	public void UserXLeavesGroupChat(String userNameAlias, String chatName)
			throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userNameAlias);

		BackendAPIWrappers.removeUserFromGroupConversation(userAs, userAs,
				chatName);

	}

	private Map<String, String> profilePictureSnapshotsMap = new HashMap<String, String>();

	public void UserXTakesSnapshotOfProfilePicture(String userNameAlias)
			throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userNameAlias);
		profilePictureSnapshotsMap.put(userAs.getEmail(),
				BackendAPIWrappers.getUserPictureHash(userAs));
	}

	public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
			String userNameAlias, int secondsTimeout) throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userNameAlias);
		String previousHash = null;
		if (profilePictureSnapshotsMap.containsKey(userAs.getEmail())) {
			previousHash = profilePictureSnapshotsMap.get(userAs.getEmail());
		} else {
			throw new RuntimeException(String.format(
					"Please take user picture snapshot for user '%s' first",
					userAs.getEmail()));
		}
		long millisecondsStarted = System.currentTimeMillis();
		String actualHash = null;
		do {
			actualHash = BackendAPIWrappers.getUserPictureHash(userAs);
			if (!actualHash.equals(previousHash)) {
				break;
			}
			Thread.sleep(500);
		} while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
		Assert.assertFalse(
				String.format(
						"Actual and previous user pictures are equal, but expected to be different after %s second(s)",
						secondsTimeout), previousHash.equals(actualHash));
	}

	private static final int PICTURE_CHANGE_TIMEOUT = 15; // seconds

	public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
			String userNameAlias) throws Exception {
		UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias,
				PICTURE_CHANGE_TIMEOUT);
	}

	public void UserXHasContactsInAddressBook(String userAsNameAlias,
			String contacts) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (String contact : splitAliases(contacts)) {
			sb.append(usrMgr.findUserByNameOrNameAlias(contact).getEmail());
			sb.append(ALIASES_SEPARATOR);
		}
		this.UserXHasEmailsInAddressBook(userAsNameAlias, sb.toString());
	}

	public void UserXHasEmailsInAddressBook(String userAsNameAlias,
			String emails) throws Exception {
		final ClientUser userAs = usrMgr
				.findUserByNameOrNameAlias(userAsNameAlias);
		List<String> emailsToAdd = new ArrayList<String>();
		for (String email : splitAliases(emails)) {
			emailsToAdd.add(email);
		}
		BackendAPIWrappers.uploadAddressBookWithContacts(userAs, emailsToAdd);
	}
}
