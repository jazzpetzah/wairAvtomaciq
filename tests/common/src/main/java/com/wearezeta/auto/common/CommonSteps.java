package com.wearezeta.auto.common;

import com.waz.api.Message;
import com.waz.model.MessageId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.backend.*;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.MessageReactionType;
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
    public static final int DEFAULT_WAIT_UNTIL_TIMEOUT_SECONDS = 10;

    private static final int BACKEND_USER_SYNC_TIMEOUT = 180; // seconds

    //increased timeout to make it stable on jenkins
    private static final int BACKEND_SUGGESTIONS_SYNC_TIMEOUT = 240; // seconds

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

    public void UserRemovesAnotherUserFromGroupConversation(String userWhoRemovesAlias, String userToRemoveAlias, String chatName)
            throws Exception {
        ClientUser userWhoRemoves = usrMgr.findUserByNameOrNameAlias(userWhoRemovesAlias);
        ClientUser userToRemove = usrMgr.findUserByNameOrNameAlias(userToRemoveAlias);

        chatName = usrMgr.replaceAliasesOccurences(chatName, ClientUsersManager.FindBy.NAME_ALIAS);
        BackendAPIWrappers.removeUserFromGroupConversation(userWhoRemoves, userToRemove, chatName);
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

    public void ThereIsAKnownUser(String name, String email, String password) throws Exception {
        ClientUser user = new ClientUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        usrMgr.appendCustomUser(user);
    }

    public void ThereAreNUsers(Platform currentPlatform, int count) throws Exception {
        usrMgr.createUsersOnBackend(count, RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreXAdditionalUsers(Platform currentPlatform, int count) throws Exception {
        usrMgr.createAndAppendUsers(count, RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count, String myNameAlias) throws Exception {
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

    public void ArchiveConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserArchiveConversation(ownerUser, dstConvoName, null, false);
    }

    public void ArchiveConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserArchiveConversation(ownerUser, dstConvoName, null, true);
    }

    public void GenerateNewLoginCode(String asUser) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(asUser);
        BackendAPIWrappers.generateNewLoginCode(user);
    }

    public void MuteConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserMutesConversation(ownerUser, dstConvoName, null, false);
    }

    public void MuteConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserMutesConversation(ownerUser, dstConvoName, null, true);
    }

    public void UnarchiveConversationWithUser(String ownerUser, String dstConvoName) throws Exception {
        UserUnarchiveConversation(ownerUser, dstConvoName, null, false);
    }

    public void UnarchiveConversationWithGroup(String ownerUser, String dstConvoName) throws Exception {
        UserUnarchiveConversation(ownerUser, dstConvoName, null, true);
    }

    public void ChangeGroupChatName(String asUserNameAliases, String conversationToRename, String newConversationName)
            throws Exception {
        ClientUser asUser = usrMgr.findUserByNameOrNameAlias(asUserNameAliases);
        final String conversationIDToRename = BackendAPIWrappers.getConversationIdByName(asUser,
                conversationToRename);
        BackendAPIWrappers.changeGroupChatName(asUser, conversationIDToRename, newConversationName);
    }

    public void UnregisterPushToken(String pushToken) throws Exception {
        ClientUser asUser = usrMgr.getSelfUser().orElseThrow(() -> new IllegalStateException("No self user assigned"));
        BackendAPIWrappers.unregisterPushToken(asUser, pushToken);
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

    public void UserIsTypingInConversation(String typingFromUserNameAlias, String dstConversationName) throws Exception {
        final ClientUser typingFromUser = usrMgr.findUserByNameOrNameAlias(typingFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(typingFromUser, dstConversationName);
        seBridge.typing(typingFromUser, convId);
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
        //default is local delete, rather than delete everywhere
        UserDeleteMessage(msgFromuserNameAlias, dstConversationName, messageId, deviceName, isGroup, false);
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, MessageId messageId,
                                  String deviceName, boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromuserNameAlias);
        if (!isGroup) {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        if (isDeleteEverywhere) {
            seBridge.deleteMessageEverywhere(user, dstConvId, messageId, deviceName);
        } else {
            seBridge.deleteMessage(user, dstConvId, messageId, deviceName);
        }
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        //default is delete local, rather than delete eveywhere
        UserDeleteLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, isGroup, false);
    }

    public void UserLikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, MessageReactionType.LIKE);
    }

    public void UserUnlikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, MessageReactionType.UNLIKE);
    }

    private void userReactLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        MessageReactionType reactionType) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);

        seBridge.reactMessage(user, dstConvId, getFilteredLastMessageId(messageInfos), reactionType, deviceName);
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);

        if (isDeleteEverywhere) {
            seBridge.deleteMessageEverywhere(user, dstConvId, getFilteredLastMessageId(messageInfos), deviceName);
        } else {
            seBridge.deleteMessage(user, dstConvId, getFilteredLastMessageId(messageInfos), deviceName);
        }
    }

    public void UserXVerifiesRecentMessageType(String msgFromUserNameAlias, String dstConversationName,
                                               String deviceName, String expectedType) throws Exception {
        expectedType = expectedType.toUpperCase();
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        final ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);
        // TODO: Handle the situation with zero length of messageInfos
        final String actualType = messageInfos[messageInfos.length - 1].tpe().toString().toUpperCase();
        Assert.assertEquals(String.format("The type of the recent conversation message '%s' is not equal to the "
                + "expected type '%s'", actualType, expectedType), actualType, expectedType);
    }

    public void UserUpdateLatestMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                        String deviceName, boolean isGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);

        seBridge.updateMessage(user, getFilteredLastMessageId(messageInfos), newMessage, deviceName);
    }

    public void UserUpdateSecondLastMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                            String deviceName, boolean isGroup) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = usrMgr.replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        ActorMessage.MessageInfo[] messageInfos = seBridge.getConversationMessages(user, dstConvId, deviceName);

        seBridge.updateMessage(user, getFilteredSecondLastMessageId(messageInfos), newMessage, deviceName);
    }

    /**
     * Note: if there is no message in conversation, it will return Optional.empty()
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
            return Optional.ofNullable(getFilteredLastMessageId(messageInfos).str());
        }
        return Optional.empty();
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

    public void UserSentLocationToConversation(String msgFromUserNameAlias, String deviceName, String dstConversationName,
                                               float longitude,
                                               float latitude, String locationName, int zoom, boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);

        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            SEBridge.getInstance().sendLocation(msgFromUser, deviceName, msgToUser.getId(), longitude, latitude, locationName,
                    zoom);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            SEBridge.getInstance().sendLocation(msgFromUser, deviceName, dstConvId, longitude, latitude, locationName, zoom);
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
            if (deviceName == null) {
                SEBridge.getInstance().muteConversation(msgFromUser, msgToUser.getId());
            } else {
                SEBridge.getInstance().muteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().muteConversation(msgFromUser, dstConvId);
            } else {
                SEBridge.getInstance().muteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnmutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().unmuteConversation(msgFromUser, msgToUser.getId());
            } else {
                SEBridge.getInstance().unmuteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().unmuteConversation(msgFromUser, dstConvId);
            } else {
                SEBridge.getInstance().unmuteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserArchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser fromUser = usrMgr.findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().archiveConversation(fromUser, dstUser.getId());
            } else {
                SEBridge.getInstance().archiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().archiveConversation(fromUser, dstConvId);
            } else {
                SEBridge.getInstance().archiveConversation(fromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnarchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                          boolean isGroup) throws Exception {
        ClientUser fromUser = usrMgr.findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().unarchiveConversation(fromUser, dstUser.getId());
            } else {
                SEBridge.getInstance().unarchiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                SEBridge.getInstance().unarchiveConversation(fromUser, dstConvId);
            } else {
                SEBridge.getInstance().unarchiveConversation(fromUser, dstConvId, deviceName);
            }
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

    public void WaitUntilContactIsSuggestedInSearchResult(String searchByNameAlias,
                                                          String contactAlias) throws Exception {
        String query = usrMgr.replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        BackendAPIWrappers.waitUntilSuggestionFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query,
                1, true, BACKEND_SUGGESTIONS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByEmail(String searchByNameAlias, String contactAlias) throws Exception {
        final ClientUser userAs = usrMgr.findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getEmail();
        BackendAPIWrappers.waitUntilContactsFound(usrMgr.findUserByNameOrNameAlias(searchByNameAlias), query, 1, true,
                BACKEND_USER_SYNC_TIMEOUT);
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

    /**
     * Upload fake addressbook to Backend
     *
     * @param userAsNameAlias the user who upload the addressbook
     * @param contacts        could be a list of phone numbers (+49.....) or emails , seperated by comma
     * @throws Exception
     */
    public void UserXHasContactsInAddressBook(String userAsNameAlias, String contacts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String contact : splitAliases(contacts)) {
            if (contact.startsWith("+")) {
                sb.append(contact);
            } else {
                sb.append(usrMgr.replaceAliasesOccurences(contact, FindBy.EMAIL_ALIAS));
            }
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
                try {
                    BackendAPIWrappers.removeOtrClient(usr, c);
                } catch (BackendRequestException e) {
                    if (e.getReturnCode() == 404) {
                        // To avoid multithreading issues
                        e.printStackTrace();
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    public String GetInvitationUrl(String user) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(user);
        return InvitationLinkGenerator.getInvitationUrl(usr.getId());
    }

    public void UserSharesLocationTo(String senderAlias, String dstConversationName, boolean isGroup, String deviceName)
            throws Exception {
        final ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(senderAlias);
        final String dstConvId = isGroup
                ? BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName)
                : usrMgr.findUserByNameOrNameAlias(dstConversationName).getId();
        SEBridge.getInstance().shareDefaultLocation(msgFromUser, dstConvId, deviceName);
    }

    public void UserSwitchToEphmeraMode(String senderAlias, String dstConversationName, long expirationMilliseconds,
                                        boolean isGroup, String deviceName) throws Exception {
        final ClientUser msgFromUser = usrMgr.findUserByNameOrNameAlias(senderAlias);
        final String dstConvId = isGroup
                ? BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName)
                : usrMgr.findUserByNameOrNameAlias(dstConversationName).getId();
        SEBridge.getInstance().setEphemeraMode(msgFromUser, dstConvId, expirationMilliseconds, deviceName);
    }

    public void UserResetsPassword(String nameAlias, String newPassword) throws Exception {
        final ClientUser usr = usrMgr.findUserByNameOrNameAlias(nameAlias);
        BackendAPIWrappers.changeUserPassword(usr, usr.getPassword(), newPassword);
    }

    private Map<String, Optional<String>> recentMessageIds = new HashMap<>();

    private String generateConversationKey(String userFrom, String dstName, String deviceName) {
        return String.format("%s:%s:%s", usrMgr.replaceAliasesOccurences(userFrom, ClientUsersManager.FindBy.NAME_ALIAS),
                usrMgr.replaceAliasesOccurences(dstName, ClientUsersManager.FindBy.NAME_ALIAS), deviceName);
    }

    public void UserXRemembersLastMessage(String userNameAlias, boolean isGroup, String dstNameAlias, String deviceName)
            throws Exception {
        recentMessageIds.put(generateConversationKey(userNameAlias, dstNameAlias, deviceName),
                UserGetRecentMessageId(userNameAlias, dstNameAlias, deviceName, isGroup));
    }

    private Optional<String> getUserXLastMessageId(String rememberedMessage, String userNameAlias, boolean isGroup,
                                                   String dstNameAlias, String deviceName, int durationSeconds)
            throws Exception {
        return CommonUtils.waitUntil(durationSeconds,
                CommonSteps.DEFAULT_WAIT_UNTIL_INTERVAL_MILLISECONDS,
                () -> {
                    Optional<String> messageId = UserGetRecentMessageId(userNameAlias,
                            dstNameAlias, deviceName, isGroup);

                    String actualMessage = messageId.orElse("");
                    // Try to wait for a different a message id
                    if (actualMessage.equals(rememberedMessage)) {
                        throw new IllegalStateException(
                                String.format("The recent remembered message id %s and the current message id %s"
                                        + " should be different", rememberedMessage, actualMessage));
                    } else {
                        return actualMessage;
                    }
                });
    }

    public void UserXFoundLastMessageChanged(String userNameAlias, boolean isGroup, String dstNameAlias,
                                             String deviceName, int durationSeconds) throws Exception {
        final String convoKey = generateConversationKey(userNameAlias, dstNameAlias, deviceName);
        if (!recentMessageIds.containsKey(convoKey)) {
            throw new IllegalStateException("You should remember the recent message before you check it");
        }
        final String rememberedMessageId = recentMessageIds.get(convoKey).orElse("");
        Optional<String> actualMessageId = getUserXLastMessageId(rememberedMessageId, userNameAlias, isGroup, dstNameAlias, deviceName, durationSeconds);
        Assert.assertTrue(String.format("Actual message Id should not equal to '%s'", rememberedMessageId),
                actualMessageId.isPresent());
    }

    public void UserXFoundLastMessageNotChanged(String userNameAlias, boolean isGroup, String dstNameAlias,
                                             String deviceName, int durationSeconds) throws Exception {
        final String convoKey = generateConversationKey(userNameAlias, dstNameAlias, deviceName);
        if (!recentMessageIds.containsKey(convoKey)) {
            throw new IllegalStateException("You should remember the recent message before you check it");
        }
        final String rememberedMessageId = recentMessageIds.get(convoKey).orElse("");
        Optional<String> actualMessageId = getUserXLastMessageId(rememberedMessageId, userNameAlias, isGroup,
                dstNameAlias, deviceName, durationSeconds);
        Assert.assertTrue(String.format("Actual message Id should equal to '%s'", rememberedMessageId),
                !actualMessageId.isPresent());
    }

    private MessageId getFilteredLastMessageId(ActorMessage.MessageInfo[] messageInfos) throws Exception {
        for (int i = messageInfos.length - 1; i >= 0; i--) {
            if (!messageInfos[i].tpe().equals(Message.Type.UNKNOWN)) {
                return messageInfos[i].id();
            }
        }
        throw new Exception("Could not find any valid message");
    }

    private MessageId getFilteredSecondLastMessageId(ActorMessage.MessageInfo[] messageInfos) throws Exception {
        MessageId latestMessage = null;
        for (int i = messageInfos.length - 1; i >= 0; i--) {
            if (!messageInfos[i].tpe().equals(Message.Type.UNKNOWN)) {
                if (latestMessage == null) {
                    latestMessage = messageInfos[i].id();
                } else {
                    return messageInfos[i].id();
                }
            }
        }
        throw new Exception("Could not find any valid message");
    }
}
