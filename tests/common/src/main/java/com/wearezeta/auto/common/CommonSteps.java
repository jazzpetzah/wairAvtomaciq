package com.wearezeta.auto.common;

import com.waz.model.MessageId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.backend.*;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.*;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static javax.xml.bind.DatatypeConverter.parseDateTime;

import org.apache.log4j.Logger;

public final class CommonSteps {

    private static final Logger log = ZetaLogger.getLog(CommonSteps.class.getSimpleName());

    public static final String CONNECTION_NAME = "CONNECT TO ";
    public static final String CONNECTION_MESSAGE = "Hello!";
    public static final long DEFAULT_WAIT_UNTIL_INTERVAL_MILLISECONDS = 1000;

    private static final int BACKEND_USER_SYNC_TIMEOUT = 180; // seconds
    private static final int BACKEND_SUGGESTIONS_SYNC_TIMEOUT = 90; // seconds

    private String pingId = null;

    private final ClientUsersManager usrMgr;

    private final SEBridge seBridge;

    public ClientUsersManager getUserManager() {
        return this.usrMgr;
    }

    private static CommonSteps instance = null;

    public synchronized static CommonSteps getInstance() {
        if (instance == null) {
            instance = new CommonSteps(ClientUsersManager.getInstance(), SEBridge.getInstance());
        }
        return instance;
    }

    /**
     * We break the singleton pattern here and make the constructor public to have multiple instances of this class for parallel
     * test executions. This means this class is not suitable as singleton and it should be changed to a non-singleton class. In
     * order to stay downward compatible we chose to just change the constructor.
     *
     * @return
     */
    public CommonSteps(ClientUsersManager usrMgr, SEBridge seBridge) {
        this.usrMgr = usrMgr;
        this.seBridge = seBridge;
    }

    public static final String ALIASES_SEPARATOR = ",";

    public static List<String> splitAliases(String aliases) {
        List<String> result = new ArrayList<>();
        String[] splittedAliases = aliases.split(ALIASES_SEPARATOR);
        for (String splittedAlias : splittedAliases) {
            result.add(splittedAlias.trim());
        }
        return result;
    }

    public void ConnectionRequestIsSentTo(String userFromNameAlias,
            String usersToNameAliases) throws Exception {
        ClientUser userFrom = usrMgr.findUserByNameOrNameAlias(userFromNameAlias);
        for (String userToNameAlias : splitAliases(usersToNameAliases)) {
            ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
            BackendAPIWrappers.sendConnectRequest(userFrom, userTo,
                    CONNECTION_NAME + userTo.getName(), CONNECTION_MESSAGE);
        }
    }

    private static final String OTHER_USERS_ALIAS = "all other";

    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
            String chatName, String otherParticipantsNameAlises) throws Exception {
        ClientUser chatOwner = usrMgr.findUserByNameOrNameAlias(chatOwnerNameAlias);
        List<ClientUser> participants = new ArrayList<>();

        if (otherParticipantsNameAlises.toLowerCase().contains(OTHER_USERS_ALIAS)) {
            participants = usrMgr.getCreatedUsers();
            participants.remove(chatOwner);
        } else {
            for (String participantNameAlias : splitAliases(otherParticipantsNameAlises)) {
                participants.add(usrMgr.findUserByNameOrNameAlias(participantNameAlias));
            }
        }
        BackendAPIWrappers.createGroupConversation(chatOwner, participants, chatName);
        // Set nameAlias for the group
        // Required for group calling tests
        ClientUser groupUser = new ClientUser();
        groupUser.setName(chatName);
        groupUser.addNameAlias(chatName);
        usrMgr.appendCustomUser(groupUser);
    }

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

    public void ThereIaAKnownUser(String name, String email, String password) throws Exception {
        ClientUser user = new ClientUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        usrMgr.appendCustomUser(user);
    }

    public void ThereAreNUsers(Platform currentPlatform, int count)
            throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count,
            String myNameAlias) throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy
                .getRegistrationStrategyForPlatform(currentPlatform));
        usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeRegOnlyByMail(int count, String myNameAlias) throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy.ByEmailOnly);
        usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(int count, String myNameAlias) throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy.ByPhoneNumberOnly);
        usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(myNameAlias));
    }

    public void IgnoreAllIncomingConnectRequest(String userToNameAlias) throws Exception {
        ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.ignoreAllConnections(userTo);
    }

    public void CancelAllOutgoingConnectRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.cancelAllOutgoingConnections(userTo);
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

    public void BlockContact(String blockAsUserNameAlias,
            String userToBlockNameAlias) throws Exception {
        ClientUser blockAsUser = usrMgr.findUserByNameOrNameAlias(blockAsUserNameAlias);
        ClientUser userToBlock = usrMgr.findUserByNameOrNameAlias(userToBlockNameAlias);
        try {
            BackendAPIWrappers.sendConnectRequest(blockAsUser, userToBlock,
                    "connect", CommonSteps.CONNECTION_MESSAGE);
        } catch (BackendRequestException e) {
            // Ignore silently
        }
        BackendAPIWrappers.changeConnectRequestStatus(blockAsUser, userToBlock.getId(), ConnectionStatus.Blocked);
    }

    public void UnblockContact(String unblockAsUserNameAlias,
            String userToUnblockNameAlias) throws Exception {
        ClientUser unblockAsUser = usrMgr.findUserByNameOrNameAlias(unblockAsUserNameAlias);
        ClientUser userToUnblock = usrMgr.findUserByNameOrNameAlias(userToUnblockNameAlias);
        try {
            BackendAPIWrappers.sendConnectRequest(unblockAsUser, userToUnblock, "connect",
                    CommonSteps.CONNECTION_MESSAGE);
        } catch (BackendRequestException e) {
            // Ignore silently
        }
        BackendAPIWrappers.changeConnectRequestStatus(unblockAsUser,
                userToUnblock.getId(), ConnectionStatus.Accepted);
    }

    public void ArchiveConversationWithUser(String usersToNameAliases,
            String archiveConversationWithUser) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
        ClientUser archivedUser = usrMgr.findUserByNameOrNameAlias(archiveConversationWithUser);
        BackendAPIWrappers.archiveUserConv(user, archivedUser);
    }

    public void ArchiveConversationWithGroup(String aUser,
            String archiveConversationWithGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(aUser);
        final String conversationIDToArchive = BackendAPIWrappers.getConversationIdByName(user,
                archiveConversationWithGroup);
        BackendAPIWrappers.archiveGroupConv(user, conversationIDToArchive);
    }

    public void GenerateNewLoginCode(String asUser) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(asUser);
        BackendAPIWrappers.generateNewLoginCode(user);
    }

    public void MuteConversationWithUser(String usersToNameAliases,
            String muteConversationWithUser) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
        ClientUser mutedUser = usrMgr.findUserByNameOrNameAlias(muteConversationWithUser);
        BackendAPIWrappers.updateConvMutedState(user, mutedUser, true);
    }

    public void MuteConversationWithGroup(String usersToNameAliases,
            String muteConversationWithGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
        BackendAPIWrappers.updateGroupConvMutedState(user, muteConversationWithGroup, true);
    }

    public void UnarchiveConversationWithUser(String usersToNameAliases,
            String archiveConversationWithUser) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(usersToNameAliases);
        ClientUser archivedUser = usrMgr.findUserByNameOrNameAlias(archiveConversationWithUser);
        BackendAPIWrappers.unarchiveUserConv(user, archivedUser);
    }

    public void UnarchiveConversationWithGroup(String aUser,
            String archiveConversationWithGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(aUser);
        final String conversationIDToArchive = BackendAPIWrappers.getConversationIdByName(user,
                archiveConversationWithGroup);
        BackendAPIWrappers.unarchiveGroupConv(user, conversationIDToArchive);
    }

    public void ChangeGroupChatName(String asUserNameAliases, String conversationToRename, String newConversationName)
            throws Exception {
        ClientUser asUser = usrMgr.findUserByNameOrNameAlias(asUserNameAliases);
        final String conversationIDToRename = BackendAPIWrappers.getConversationIdByName(asUser,
                conversationToRename);
        BackendAPIWrappers.changeGroupChatName(asUser, conversationIDToRename, newConversationName);
    }

    public void AcceptAllIncomingConnectionRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = usrMgr.findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.acceptAllConnections(userTo);
    }

    public void UserAddsRemoteDeviceToAccount(String userNameAlias,
            String deviceName, String label) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        seBridge.addRemoteDeviceToAccount(user, deviceName, label);
    }

    public void UserPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        ClientUser pingFromUser = usrMgr.findUserByNameOrNameAlias(pingFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        pingId = BackendAPIWrappers.sendPingToConversation(pingFromUser, dstConversationName);
        Thread.sleep(1000);
    }

    public void UserPingedConversationOtr(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        final ClientUser pingFromUser = usrMgr.findUserByNameOrNameAlias(pingFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(pingFromUser, dstConversationName);
        seBridge.sendPing(pingFromUser, convId);
    }

    public void UserHotPingedConversationOtr(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        final ClientUser pingFromUser = usrMgr.findUserByNameOrNameAlias(pingFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(pingFromUser, dstConversationName);
        for (int i = 0; i < 2; i++) {
            seBridge.sendPing(pingFromUser, convId);
            Thread.sleep(500);
        }
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, MessageId messageId,
                                  String deviceName, boolean isGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromuserNameAlias);
        if (!isGroup) {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        seBridge.deleteMessage(user, dstConvId, messageId, deviceName);
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if(!isGroup) {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);
        ActorMessage.MessageInfo lastMessage = messageInfos[messageInfos.length - 1];

        seBridge.deleteMessage(user, dstConvId, lastMessage.id(), deviceName);
    }

    /**
     * Note: if there is no message in conversation, it will return Optional.of("")
     *
     * @param msgFromUserNameAlias
     * @param dstConversationName
     * @param deviceName
     * @param isGroup
     * @return
     * @throws Exception
     */
    public Optional<String> UserGetRecentMessageId(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                                   boolean isGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);
        if (!ArrayUtils.isEmpty(messageInfos)) {
            return Optional.ofNullable(messageInfos[messageInfos.length - 1].id().str());
        }
        // Means there is no any message
        return Optional.of("");
    }

    public void UserSentMessageToUser(String msgFromUserNameAlias,
            String dstUserNameAlias, String message) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstUserNameAlias);
        BackendAPIWrappers.sendDialogMessage(msgFromUser, msgToUser, message);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
            String dstUserNameAlias, String message, String deviceName) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstUserNameAlias);
        seBridge.sendConversationMessage(msgFromUser, msgToUser.getId(), message, deviceName);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
            String dstUserNameAlias, String message) throws Exception {
        UserSentOtrMessageToUser(msgFromUserNameAlias, dstUserNameAlias, message, null);
    }

    public void UserHotPingedConversation(String hotPingFromUserNameAlias,
            String dstConversationName) throws Exception {
        ClientUser hotPingFromUser = usrMgr.findUserByNameOrNameAlias(hotPingFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        BackendAPIWrappers.sendHotPingToConversation(hotPingFromUser, dstConversationName, pingId);
        Thread.sleep(1000);
    }

    public void UserSentMessageToConversation(String userFromNameAlias,
            String dstConversationName, String message) throws Exception {
        ClientUser userFrom = usrMgr.findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        BackendAPIWrappers.sendDialogMessageByChatName(userFrom, dstConversationName, message);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
            String dstConversationName, String message, String deviceName)
            throws Exception {
        ClientUser userFrom = usrMgr.findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);
        seBridge.sendConversationMessage(userFrom, dstConvId, message, deviceName);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
            String dstConversationName, String message) throws Exception {
        UserSentOtrMessageToConversation(userFromNameAlias, dstConversationName, message, null);
    }

    public void UserSentImageToConversation(String imageSenderUserNameAlias,
            String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = usrMgr.findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversation(imageSender, imageReceiver, imagePath);
        } else {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByName(imageSender, dstConversationName, imagePath);
        }
    }

    public void UserSentImageToConversationOtr(String imageSenderUserNameAlias,
            String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = usrMgr
                .findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversationOtr(imageSender, imageReceiver, imagePath);
        } else {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByNameOtr(imageSender, dstConversationName, imagePath);
        }
    }

    public void UserSentFileToConversation(String msgFromUserNameAlias, String dstConversationName, String path,
            String mime, String deviceName, boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("Please make sure the file %s exists and is accessible",
                    path));
        }
        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            SEBridge.getInstance().sendFile(msgFromUser, msgToUser.getId(), path, mime, deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            SEBridge.getInstance().sendFile(msgFromUser, dstConvId, path, mime, deviceName);
        }
    }

    public void UserClearsConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
            boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            SEBridge.getInstance().clearConversation(msgFromUser, msgToUser.getId(), deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            SEBridge.getInstance().clearConversation(msgFromUser, dstConvId, deviceName);
        }
    }

    public void UserMutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
            boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            SEBridge.getInstance().muteConversation(msgFromUser, msgToUser.getId(), deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            SEBridge.getInstance().muteConversation(msgFromUser, dstConvId, deviceName);
        }
    }

    public void UserUnmutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
            boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            SEBridge.getInstance().unmuteConversation(msgFromUser, msgToUser.getId(), deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            SEBridge.getInstance().unmuteConversation(msgFromUser, dstConvId, deviceName);
        }
    }

    public void IChangeUserAvatarPicture(String userNameAlias,
            String picturePath) throws Exception {
        final ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        if (new File(picturePath).exists()) {
            BackendAPIWrappers.updateUserPicture(dstUser, picturePath);
        } else {
            throw new IOException(String.format("The picture '%s' is not accessible", picturePath));
        }
    }

    public void UserDeletesAvatarPicture(String userNameAlias) throws Exception {
        final ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        BackendAPIWrappers.updateUserPicture(dstUser, null);
    }

    public void IChangeUserName(String userNameAlias, String newName) throws Exception {
        BackendAPIWrappers.updateUserName(usrMgr.findUserByNameOrNameAlias(userNameAlias), newName);
    }

    public void IChangeUserAccentColor(String userNameAlias, String colorName) throws Exception {
        BackendAPIWrappers.updateUserAccentColor(usrMgr.findUserByNameOrNameAlias(userNameAlias),
                AccentColor.getByName(colorName));
    }

    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix) throws Exception {
        usrMgr.appendSharedUsers(namePrefix, count);
    }

    public void UserXIsMe(String nameAlias) throws Exception {
        usrMgr.setSelfUser(usrMgr.findUserByNameOrNameAlias(nameAlias));
    }

    public void WaitUntilSuggestionFound(String userAsNameAlias) throws Exception {
        BackendAPIWrappers.waitUntilSuggestionFound(usrMgr.findUserByNameOrNameAlias(userAsNameAlias),
                BACKEND_SUGGESTIONS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsNotFoundInSearch(String searchByNameAlias,
            String contactAlias, int timeoutSeconds) throws Exception {
        String query = usrMgr.replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = usrMgr.replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        BackendAPIWrappers.waitUntilContactNotFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query,
                timeoutSeconds);
    }

    public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
            String contactAlias) throws Exception {
        String query = usrMgr.replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = usrMgr.replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        BackendAPIWrappers.waitUntilContactsFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query, 1,
                true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByEmail(String searchByNameAlias, String contactAlias) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getEmail();
        BackendAPIWrappers.waitUntilContactsFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query, 1, true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilTopPeopleContactsIsFoundInSearch(String searchByNameAlias, int size) throws Exception {
        BackendAPIWrappers.waitUntilTopPeopleContactsFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), size,
                size, true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void UserXAddedContactsToGroupChat(String userAsNameAlias,
            String contactsToAddNameAliases, String chatName) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
        List<ClientUser> contactsToAdd = new ArrayList<>();
        for (String contactNameAlias : splitAliases(contactsToAddNameAliases)) {
            contactsToAdd.add(usrMgr.findUserByNameOrNameAlias(contactNameAlias));
        }
        BackendAPIWrappers.addContactsToGroupConversation(userAs, contactsToAdd, chatName);
    }

    public void UserXRemoveContactFromGroupChat(String userAsNameAlias,
            String contactToRemoveNameAlias, String chatName) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
        final ClientUser userToRemove = usrMgr.findUserByNameOrNameAlias(contactToRemoveNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userToRemove, chatName);
    }

    public void UserXLeavesGroupChat(String userNameAlias, String chatName) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userAs, chatName);

    }

    private Map<String, String> profilePictureSnapshotsMap = new HashMap<>();

    public void UserXTakesSnapshotOfProfilePicture(String userNameAlias) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        profilePictureSnapshotsMap.put(userAs.getEmail(), BackendAPIWrappers.getUserPictureHash(userAs));
    }

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias, int secondsTimeout) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userNameAlias);
        String previousHash;
        if (profilePictureSnapshotsMap.containsKey(userAs.getEmail())) {
            previousHash = profilePictureSnapshotsMap.get(userAs.getEmail());
        } else {
            throw new RuntimeException(String.format(
                    "Please take user picture snapshot for user '%s' first",
                    userAs.getEmail()));
        }
        long millisecondsStarted = System.currentTimeMillis();
        String actualHash;
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

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(String userNameAlias) throws Exception {
        UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias,
                PICTURE_CHANGE_TIMEOUT);
    }

    public void UserXHasContactsInAddressBook(String userAsNameAlias, String contacts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String contact : splitAliases(contacts)) {
            sb.append(usrMgr.findUserByNameOrNameAlias(contact).getEmail());
            sb.append(ALIASES_SEPARATOR);
        }
        this.UserXHasEmailsInAddressBook(userAsNameAlias, sb.toString());
    }

    public void UserXHasEmailsInAddressBook(String userAsNameAlias,
            String emails) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(userAsNameAlias);
        BackendAPIWrappers.uploadAddressBookWithContacts(userAs, splitAliases(emails));
    }

    public void UserXSendsPersonalInvitationWithMessageToUserWithMail(
            String sender, String toMail, String message) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(sender);
        ClientUser invitee = usrMgr.findUserByEmailOrEmailAlias(toMail);
        BackendAPIWrappers.sendPersonalInvitation(user, invitee.getEmail(), invitee.getName(), message);
    }

    public void IAddUserToTheListOfTestCaseUsers(String nameAlias) throws Exception {
        ClientUser userToAdd = usrMgr.findUserByNameOrNameAlias(nameAlias);
        userToAdd.setUserState(UserState.Created);
        usrMgr.appendCustomUser(userToAdd);
    }

    public void UserRemovesAllRegisteredOtrClients(String forUser) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(forUser);
        final List<OtrClient> allOtrClients = BackendAPIWrappers.getOtrClients(usr);
        for (OtrClient c : allOtrClients) {
            BackendAPIWrappers.removeOtrClient(usr, c);
        }
    }

    public List<String> GetDevicesIDsForUser(String name) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(name);
        return seBridge.getDeviceIds(usr);
    }

    public void UserKeepsXOtrClients(String userAs, int clientsCountToKeep) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(userAs);
        final List<OtrClient> allOtrClients = BackendAPIWrappers.getOtrClients(usr);
        final String defaultDateStr = "2016-01-01T12:00:00Z";
        // Newly registered clients coming first
        Collections.sort(allOtrClients, (c1, c2)
                -> parseDateTime(c2.getTime().orElse(defaultDateStr)).getTime().compareTo(
                parseDateTime(c1.getTime().orElse(defaultDateStr)).getTime()
        )
        );
        log.debug(String.format("Clients considered for removal %s", allOtrClients));
        if (allOtrClients.size() > clientsCountToKeep) {
            for (OtrClient c : allOtrClients.subList(clientsCountToKeep, allOtrClients.size())) {
                log.debug(String.format("Removing client with ID %s", c.getId()));
                BackendAPIWrappers.removeOtrClient(usr, c);
            }
        }
    }

    public String GetInvitationUrl(String user) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(user);
        return InvitationLinkGenerator.getInvitationUrl(usr.getId());
    }
}
