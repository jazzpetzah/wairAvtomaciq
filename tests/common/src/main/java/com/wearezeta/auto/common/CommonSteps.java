package com.wearezeta.auto.common;

import com.google.common.collect.Lists;
import com.wearezeta.auto.common.backend.*;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.test_context.TestContext;
import com.wearezeta.auto.common.wire_actors.ActorsRESTWrapper;
import com.wearezeta.auto.common.wire_actors.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static javax.xml.bind.DatatypeConverter.parseDateTime;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public final class CommonSteps {

    private static final Logger log = ZetaLogger.getLog(CommonSteps.class.getSimpleName());

    public static final String CONNECTION_NAME = "CONNECT TO ";
    public static final String CONNECTION_MESSAGE = "Hello!";
    public static final long DEFAULT_WAIT_UNTIL_INTERVAL_MILLISECONDS = 1000;
    public static final int DEFAULT_WAIT_UNTIL_TIMEOUT_SECONDS = 10;

    private static final int BACKEND_USER_SYNC_TIMEOUT = 180; // seconds

    //increased timeout to make it stable on jenkins
    private static final int BACKEND_SUGGESTIONS_SYNC_TIMEOUT = 240; // seconds
    private static final int BACKEND_COMMON_CONTACTS_SYNC_TIMEOUT = 240; // seconds

    private Supplier<TestContext> testContextSupplier;

    private ClientUsersManager getUserManager() {
        return this.testContextSupplier.get().getUserManager();
    }

    private SEBridge getDeviceManager() {
        return this.testContextSupplier.get().getDeviceManager();
    }

    public CommonSteps(Supplier<TestContext> testContextSupplier) {
        this.testContextSupplier = testContextSupplier;
    }

    public void ConnectionRequestIsSentTo(String userFromNameAlias,
                                          String usersToNameAliases) throws Exception {
        ClientUser userFrom = getUserManager().findUserByNameOrNameAlias(userFromNameAlias);
        for (String userToNameAlias : getUserManager().splitAliases(usersToNameAliases)) {
            ClientUser userTo = getUserManager().findUserByNameOrNameAlias(userToNameAlias);
            BackendAPIWrappers.sendConnectRequest(userFrom, userTo,
                    CONNECTION_NAME + userTo.getName(), CONNECTION_MESSAGE);
        }
    }

    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises) throws Exception {
        ClientUser chatOwner = getUserManager().findUserByNameOrNameAlias(chatOwnerNameAlias);
        final List<ClientUser> participants = new ArrayList<>();
        for (String participantNameAlias : getUserManager().splitAliases(otherParticipantsNameAlises)) {
            participants.add(getUserManager().findUserByNameOrNameAlias(participantNameAlias));
        }
        BackendAPIWrappers.createGroupConversation(chatOwner, participants, chatName);
        // Set nameAlias for the group
        // Required for group calling tests
        ClientUser groupUser = new ClientUser();
        groupUser.setName(chatName);
        groupUser.addNameAlias(chatName);
        getUserManager().appendCustomUser(groupUser);
    }

    public void UserRemovesAnotherUserFromGroupConversation(String userWhoRemovesAlias,
                                                            String userToRemoveAlias,
                                                            String chatName) throws Exception {
        ClientUser userWhoRemoves = getUserManager().findUserByNameOrNameAlias(userWhoRemovesAlias);
        ClientUser userToRemove = getUserManager().findUserByNameOrNameAlias(userToRemoveAlias);

        chatName = getUserManager().replaceAliasesOccurences(chatName, ClientUsersManager.FindBy.NAME_ALIAS);
        BackendAPIWrappers.removeUserFromGroupConversation(userWhoRemoves, userToRemove, chatName);
    }

    public void UserIsConnectedTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        final ClientUser asUser = getUserManager().findUserByNameOrNameAlias(userFromNameAlias);
        for (String userToName : getUserManager().splitAliases(usersToNameAliases)) {
            final ClientUser usrTo = getUserManager().findUserByNameOrNameAlias(userToName);
            BackendAPIWrappers.sendConnectionRequest(usrTo, asUser);
            BackendAPIWrappers.acceptIncomingConnectionRequest(asUser, usrTo);
        }
    }

    public void ThereIsAKnownUser(String name, String email, String password) throws Exception {
        ClientUser user = new ClientUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        getUserManager().appendCustomUser(user);
    }

    public void ThereAreNUsers(Platform currentPlatform, int count) throws Exception {
        getUserManager().createUsersOnBackend(count, RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreXAdditionalUsers(Platform currentPlatform, int count) throws Exception {
        getUserManager().createAndAppendUsers(count, RegistrationStrategy.getRegistrationStrategyForPlatform(currentPlatform));
    }

    public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count, String myNameAlias) throws Exception {
        getUserManager().createUsersOnBackend(count, RegistrationStrategy
                .getRegistrationStrategyForPlatform(currentPlatform));
        getUserManager().setSelfUser(getUserManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeRegOnlyByMail(int count, String myNameAlias) throws Exception {
        getUserManager().createUsersOnBackend(count, RegistrationStrategy.ByEmailOnly);
        getUserManager().setSelfUser(getUserManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(int count, String myNameAlias) throws Exception {
        getUserManager().createUsersOnBackend(count, RegistrationStrategy.ByPhoneNumberOnly);
        getUserManager().setSelfUser(getUserManager().findUserByNameOrNameAlias(myNameAlias));
    }

    public void IgnoreAllIncomingConnectRequest(String userToNameAlias) throws Exception {
        ClientUser userTo = getUserManager().findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.ignoreAllIncomingConnections(userTo);
    }

    public void CancelAllOutgoingConnectRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = getUserManager().findUserByNameOrNameAlias(userToNameAlias);
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
        ClientUser blockAsUser = getUserManager().findUserByNameOrNameAlias(blockAsUserNameAlias);
        ClientUser userToBlock = getUserManager().findUserByNameOrNameAlias(userToBlockNameAlias);
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
        ClientUser unblockAsUser = getUserManager().findUserByNameOrNameAlias(unblockAsUserNameAlias);
        ClientUser userToUnblock = getUserManager().findUserByNameOrNameAlias(userToUnblockNameAlias);
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
        ClientUser user = getUserManager().findUserByNameOrNameAlias(asUser);
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
        ClientUser asUser = getUserManager().findUserByNameOrNameAlias(asUserNameAliases);
        final String conversationIDToRename = BackendAPIWrappers.getConversationIdByName(asUser,
                conversationToRename);
        BackendAPIWrappers.changeGroupChatName(asUser, conversationIDToRename, newConversationName);
    }

    public void UnregisterPushToken(String pushToken) throws Exception {
        ClientUser asUser = getUserManager().getSelfUser().orElseThrow(() -> new IllegalStateException("No self user assigned"));
        BackendAPIWrappers.unregisterPushToken(asUser, pushToken);
    }

    public void AcceptAllIncomingConnectionRequests(String userToNameAlias) throws Exception {
        ClientUser userTo = getUserManager().findUserByNameOrNameAlias(userToNameAlias);
        BackendAPIWrappers.acceptAllIncomingConnectionRequests(userTo);
    }

    public void UserAddsRemoteDeviceToAccount(String userNameAlias,
                                              String deviceName, String label) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        getDeviceManager().addRemoteDeviceToAccount(user, deviceName, label);
    }

    public void UserPingedConversationOtr(String pingFromUserNameAlias,
                                          String dstConversationName) throws Exception {
        final ClientUser pingFromUser = getUserManager().findUserByNameOrNameAlias(pingFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(pingFromUser, dstConversationName);
        getDeviceManager().sendPing(pingFromUser, convId);
    }

    public void UserSendsGiphy(String sendingFromUserNameAlias, String dstConversationName, String searchQuery,
                               String deviceName, boolean isGroup) throws Exception {
        final ClientUser userFrom = getUserManager().findUserByNameOrNameAlias(sendingFromUserNameAlias);
        String dstConvId;
        if (isGroup) {
            dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);
        } else {
            dstConvId = getUserManager().findUserByNameOrNameAlias(dstConversationName).getId();
        }
        getDeviceManager().sendGiphy(userFrom, dstConvId, searchQuery, deviceName);
    }

    public void UserIsTypingInConversation(String typingFromUserNameAlias, String dstConversationName) throws Exception {
        final ClientUser typingFromUser = getUserManager().findUserByNameOrNameAlias(typingFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String convId = BackendAPIWrappers.getConversationIdByName(typingFromUser, dstConversationName);
        getDeviceManager().typing(typingFromUser, convId);
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, String messageId,
                                  String deviceName, boolean isGroup) throws Exception {
        //default is local delete, rather than delete everywhere
        UserDeleteMessage(msgFromuserNameAlias, dstConversationName, messageId, deviceName, isGroup, false);
    }

    public void UserDeleteMessage(String msgFromuserNameAlias, String dstConversationName, String messageId,
                                  String deviceName, boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromuserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        if (isDeleteEverywhere) {
            getDeviceManager().deleteMessageEverywhere(user, dstConvId, messageId, deviceName);
        } else {
            getDeviceManager().deleteMessage(user, dstConvId, messageId, deviceName);
        }
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        //default is delete local, rather than delete eveywhere
        UserDeleteLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName, isGroup, false);
    }

    public void UserReadEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String messageId,
                                         String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        getDeviceManager().markEphemeralRead(user, dstConvId, messageId, deviceName);
    }

    public void UserReadLastEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                             boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);
        getDeviceManager().markEphemeralRead(user, dstConvId, getFilteredLastMessageId(messageInfos), deviceName);
    }

    public void UserReadSecondLastEphemeralMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                                   boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);
        getDeviceManager().markEphemeralRead(user, dstConvId, getFilteredSecondLastMessageId(messageInfos), deviceName);
    }

    public void UserLikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName,
                ActorsRESTWrapper.MessageReaction.LIKE);
    }

    public void UserUnlikeLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName)
            throws Exception {
        userReactLatestMessage(msgFromUserNameAlias, dstConversationName, deviceName,
                ActorsRESTWrapper.MessageReaction.UNLIKE);
    }

    private void userReactLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        ActorsRESTWrapper.MessageReaction reactionType) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);

        getDeviceManager().reactMessage(user, dstConvId, getFilteredLastMessageId(messageInfos), reactionType, deviceName);
    }

    public void UserDeleteLatestMessage(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup, boolean isDeleteEverywhere) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);

        if (isDeleteEverywhere) {
            getDeviceManager().deleteMessageEverywhere(user, dstConvId, getFilteredLastMessageId(messageInfos), deviceName);
        } else {
            getDeviceManager().deleteMessage(user, dstConvId, getFilteredLastMessageId(messageInfos), deviceName);
        }
    }

    public void UserXVerifiesRecentMessageType(String msgFromUserNameAlias, String dstConversationName,
                                               String deviceName, String expectedType) throws Exception {
        expectedType = expectedType.toUpperCase();
        final ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        final String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        final List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);
        // TODO: Handle the situation with zero length of messageInfos
        final String actualType = messageInfos.get(messageInfos.size() - 1).getType().toString().toUpperCase();
        Assert.assertEquals(String.format("The type of the recent conversation message '%s' is not equal to the "
                + "expected type '%s'", actualType, expectedType), actualType, expectedType);
    }

    public void UserUpdateLatestMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                        String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);

        getDeviceManager().updateMessage(user, getFilteredLastMessageId(messageInfos), newMessage, deviceName);
    }

    public void UserUpdateSecondLastMessage(String msgFromUserNameAlias, String dstConversationName, String newMessage,
                                            String deviceName, boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);

        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);

        getDeviceManager().updateMessage(user, getFilteredSecondLastMessageId(messageInfos), newMessage, deviceName);
    }

    public void UserUpdateMessageById(String msgFromUserNameAlias, String messageId,
                                      String newMessage, String deviceName) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        getDeviceManager().updateMessage(user, messageId, newMessage, deviceName);
    }

    /**
     * Note: if there is no message in conversation, it will return Optional.empty()
     */
    public Optional<String> UserGetRecentMessageId(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                                   boolean isGroup) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        }
        String dstConvId = BackendAPIWrappers.getConversationIdByName(user, dstConversationName);
        List<ActorsRESTWrapper.MessageInfo> messageInfos = getDeviceManager().getConversationMessages(user, dstConvId, deviceName);
        if (!messageInfos.isEmpty()) {
            return Optional.ofNullable(getFilteredLastMessageId(messageInfos));
        }
        return Optional.empty();
    }

    public void UserSentMessageToUser(String msgFromUserNameAlias,
                                      String dstUserNameAlias, String message) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstUserNameAlias);
        BackendAPIWrappers.sendDialogMessage(msgFromUser, msgToUser, message);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
                                         String dstUserNameAlias, String message, String deviceName) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstUserNameAlias);
        getDeviceManager().sendConversationMessage(msgFromUser, msgToUser.getId(), message, deviceName);
    }

    public void UserSentOtrMessageToUser(String msgFromUserNameAlias,
                                         String dstUserNameAlias, String message) throws Exception {
        UserSentOtrMessageToUser(msgFromUserNameAlias, dstUserNameAlias, message, null);
    }

    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String dstConversationName, String message) throws Exception {
        ClientUser userFrom = getUserManager().findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        BackendAPIWrappers.sendDialogMessageByChatName(userFrom, dstConversationName, message);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
                                                 String dstConversationName, String message, String deviceName)
            throws Exception {
        ClientUser userFrom = getUserManager().findUserByNameOrNameAlias(userFromNameAlias);
        dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
        String dstConvId = BackendAPIWrappers.getConversationIdByName(userFrom, dstConversationName);
        getDeviceManager().sendConversationMessage(userFrom, dstConvId, message, deviceName);
    }

    public void UserSentOtrMessageToConversation(String userFromNameAlias,
                                                 String dstConversationName, String message) throws Exception {
        UserSentOtrMessageToConversation(userFromNameAlias, dstConversationName, message, null);
    }

    public void UserSentImageToConversation(String imageSenderUserNameAlias,
                                            String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = getUserManager().findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversation(imageSender, imageReceiver, imagePath);
        } else {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByName(imageSender, dstConversationName, imagePath);
        }
    }

    public void UserSentImageToConversationOtr(String imageSenderUserNameAlias,
                                               String imagePath, String dstConversationName, boolean isGroup)
            throws Exception {
        ClientUser imageSender = getUserManager()
                .findUserByNameOrNameAlias(imageSenderUserNameAlias);
        if (!isGroup) {
            ClientUser imageReceiver = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            BackendAPIWrappers.sendPictureToSingleUserConversationOtr(imageSender, imageReceiver, imagePath, getDeviceManager());
        } else {
            dstConversationName = getUserManager().replaceAliasesOccurences(dstConversationName, FindBy.NAME_ALIAS);
            BackendAPIWrappers.sendPictureToChatByNameOtr(imageSender, dstConversationName, imagePath, getDeviceManager());
        }
    }

    public void UserSentFileToConversation(String msgFromUserNameAlias, String dstConversationName, String path,
                                           String mime, String deviceName, boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("Please make sure the file %s exists and is accessible",
                    path));
        }
        if (!isGroup) {
            ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            getDeviceManager().sendFile(msgFromUser, msgToUser.getId(), path, mime, deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDeviceManager().sendFile(msgFromUser, dstConvId, path, mime, deviceName);
        }
    }

    public void UserSentLocationToConversation(String msgFromUserNameAlias, String deviceName, String dstConversationName,
                                               float longitude,
                                               float latitude, String locationName, int zoom, boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);

        if (!isGroup) {
            ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            getDeviceManager().sendLocation(msgFromUser, deviceName, msgToUser.getId(), longitude, latitude, locationName,
                    zoom);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDeviceManager().sendLocation(msgFromUser, deviceName, dstConvId, longitude, latitude, locationName, zoom);
        }
    }

    public void UserClearsConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                       boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            getDeviceManager().clearConversation(msgFromUser, msgToUser.getId(), deviceName);
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            getDeviceManager().clearConversation(msgFromUser, dstConvId, deviceName);
        }
    }

    public void UserMutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                      boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDeviceManager().muteConversation(msgFromUser, msgToUser.getId());
            } else {
                getDeviceManager().muteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                getDeviceManager().muteConversation(msgFromUser, dstConvId);
            } else {
                getDeviceManager().muteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnmutesConversation(String msgFromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (!isGroup) {
            ClientUser msgToUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDeviceManager().unmuteConversation(msgFromUser, msgToUser.getId());
            } else {
                getDeviceManager().unmuteConversation(msgFromUser, msgToUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
            if (deviceName == null) {
                getDeviceManager().unmuteConversation(msgFromUser, dstConvId);
            } else {
                getDeviceManager().unmuteConversation(msgFromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserArchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                        boolean isGroup) throws Exception {
        ClientUser fromUser = getUserManager().findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDeviceManager().archiveConversation(fromUser, dstUser.getId());
            } else {
                getDeviceManager().archiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                getDeviceManager().archiveConversation(fromUser, dstConvId);
            } else {
                getDeviceManager().archiveConversation(fromUser, dstConvId, deviceName);
            }
        }
    }

    public void UserUnarchiveConversation(String fromUserNameAlias, String dstConversationName, String deviceName,
                                          boolean isGroup) throws Exception {
        ClientUser fromUser = getUserManager().findUserByNameOrNameAlias(fromUserNameAlias);
        if (!isGroup) {
            ClientUser dstUser = getUserManager().findUserByNameOrNameAlias(dstConversationName);
            if (deviceName == null) {
                getDeviceManager().unarchiveConversation(fromUser, dstUser.getId());
            } else {
                getDeviceManager().unarchiveConversation(fromUser, dstUser.getId(), deviceName);
            }
        } else {
            String dstConvId = BackendAPIWrappers.getConversationIdByName(fromUser, dstConversationName);
            if (deviceName == null) {
                getDeviceManager().unarchiveConversation(fromUser, dstConvId);
            } else {
                getDeviceManager().unarchiveConversation(fromUser, dstConvId, deviceName);
            }
        }
    }

    public void IChangeUserAvatarPicture(String userNameAlias, String picturePath) throws Exception {
        final ClientUser dstUser = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        if (new File(picturePath).exists()) {
            BackendAPIWrappers.updateUserPicture(dstUser, picturePath);
        } else {
            throw new IOException(String.format("The picture '%s' is not accessible", picturePath));
        }
    }

    public void IChangeUserAvatarPicture(String userNameAlias, String picturePath,
                                         ActorsRESTWrapper.AssetsVersion protocol) throws Exception {
        final ClientUser dstUser = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        if (new File(picturePath).exists()) {
            switch (protocol) {
                case V2:
                    BackendAPIWrappers.updateUserPictureV2(dstUser, picturePath);
                    break;
                case V3:
                    BackendAPIWrappers.updateUserPictureV3(dstUser, picturePath);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown protocol '%s'", protocol.name()));
            }
        } else {
            throw new IOException(String.format("The picture '%s' is not accessible", picturePath));
        }
    }

    public void UserDeletesAvatarPicture(String userNameAlias) throws Exception {
        final ClientUser dstUser = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        BackendAPIWrappers.removeUserPicture(dstUser);
    }

    public void IChangeName(String userNameAlias, String newName) throws Exception {
        BackendAPIWrappers.updateName(getUserManager().findUserByNameOrNameAlias(userNameAlias), newName);
    }

    public void IChangeUniqueUsername(String userNameAlias, String name) throws Exception {
        name = getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        BackendAPIWrappers.updateUniqueUsername(getUserManager().findUserByNameOrNameAlias(userNameAlias), name);
    }

    public void UsersSetUniqueUsername(String userNameAliases) throws Exception {
        for (String userNameAlias : getUserManager().splitAliases(userNameAliases)) {
            final ClientUser user = getUserManager().findUserByNameOrNameAlias(userNameAlias);
            BackendAPIWrappers.updateUniqueUsername(getUserManager().findUserByNameOrNameAlias(userNameAlias), user.getUniqueUsername()
                    .toLowerCase());
        }
    }

    public void IChangeUserAccentColor(String userNameAlias, String colorName) throws Exception {
        BackendAPIWrappers.updateUserAccentColor(getUserManager().findUserByNameOrNameAlias(userNameAlias),
                AccentColor.getByName(colorName));
    }

    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix) throws Exception {
        getUserManager().appendSharedUsers(namePrefix, count);
    }

    public void UserXIsMe(String nameAlias) throws Exception {
        getUserManager().setSelfUser(getUserManager().findUserByNameOrNameAlias(nameAlias));
    }

    public void WaitUntilContactIsNotFoundInSearch(String searchByNameAlias,
                                                   String contactAlias, int timeoutSeconds) throws Exception {
        String query = getUserManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = getUserManager().replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        query = getUserManager().replaceAliasesOccurences(query, FindBy.UNIQUE_USERNAME_ALIAS);
        BackendAPIWrappers.waitUntilContactNotFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                timeoutSeconds);
    }

    public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
                                                String contactAlias) throws Exception {
        String query = getUserManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        query = getUserManager().replaceAliasesOccurences(query, FindBy.EMAIL_ALIAS);
        query = getUserManager().replaceAliasesOccurences(query, FindBy.UNIQUE_USERNAME_ALIAS);
        BackendAPIWrappers.waitUntilContactsFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                1, true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilCommonContactsIsGenerated(String searchByNameAlias, String contactAlias) throws Exception {
        WaitUntilCommonContactsIsGenerated(searchByNameAlias, contactAlias, 1);
    }

    public void WaitUntilCommonContactsIsGenerated(String searchByNameAlias, String contactAlias,
                                                   int expectCountOfCommonContacts) throws Exception {
        ClientUser searchByUser = getUserManager().findUserBy(searchByNameAlias, FindBy.NAME_ALIAS);
        ClientUser destUser = getUserManager().findUserBy(contactAlias, FindBy.NAME_ALIAS);
        BackendAPIWrappers.waitUntilCommonContactsFound(searchByUser, destUser, expectCountOfCommonContacts,
                true, BACKEND_COMMON_CONTACTS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsSuggestedInSearchResult(String searchByNameAlias,
                                                          String contactAlias) throws Exception {
        String query = getUserManager().replaceAliasesOccurences(contactAlias, FindBy.NAME_ALIAS);
        BackendAPIWrappers.waitUntilSuggestionFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), query,
                1, true, BACKEND_SUGGESTIONS_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByEmail(String searchByNameAlias, String contactAlias) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getEmail();
        BackendAPIWrappers.waitUntilContactsFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), query, 1, true,
                BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilContactIsFoundInSearchByUniqueUsername(String searchByNameAlias, String contactAlias) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(contactAlias);
        String query = userAs.getUniqueUsername();
        BackendAPIWrappers.waitUntilContactsFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), query, 1, true,
                BACKEND_USER_SYNC_TIMEOUT);
    }

    public void WaitUntilTopPeopleContactsIsFoundInSearch(String searchByNameAlias, int size) throws Exception {
        BackendAPIWrappers.waitUntilTopPeopleContactsFound(getUserManager().findUserByNameOrNameAlias(searchByNameAlias), size,
                size, true, BACKEND_USER_SYNC_TIMEOUT);
    }

    public void UserXAddedContactsToGroupChat(String userAsNameAlias,
                                              String contactsToAddNameAliases, String chatName) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userAsNameAlias);
        List<ClientUser> contactsToAdd = new ArrayList<>();
        for (String contactNameAlias : getUserManager().splitAliases(contactsToAddNameAliases)) {
            contactsToAdd.add(getUserManager().findUserByNameOrNameAlias(contactNameAlias));
        }
        BackendAPIWrappers.addContactsToGroupConversation(userAs, contactsToAdd, chatName);
    }

    public void UserXRemoveContactFromGroupChat(String userAsNameAlias,
                                                String contactToRemoveNameAlias, String chatName) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userAsNameAlias);
        final ClientUser userToRemove = getUserManager().findUserByNameOrNameAlias(contactToRemoveNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userToRemove, chatName);
    }

    public void UserXLeavesGroupChat(String userNameAlias, String chatName) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userNameAlias);

        BackendAPIWrappers.removeUserFromGroupConversation(userAs, userAs, chatName);

    }

    private Map<String, String> profilePictureSnapshotsMap = new HashMap<>();
    private Map<String, String> profilePictureV3SnapshotsMap = new HashMap<>();
    private Map<String, String> profilePictureV3PreviewSnapshotsMap = new HashMap<>();

    public void UserXTakesSnapshotOfProfilePicture(String userNameAlias) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        String email = userAs.getEmail();
        profilePictureSnapshotsMap.put(email, BackendAPIWrappers.getUserPictureHash(userAs));
        profilePictureV3SnapshotsMap.put(email,
                BackendAPIWrappers.getUserAssetKey(userAs,
                        BackendAPIWrappers.PROFILE_PICTURE_JSON_ATTRIBUTE));
        profilePictureV3PreviewSnapshotsMap.put(email,
                BackendAPIWrappers.getUserAssetKey(userAs,
                        BackendAPIWrappers.PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE));
    }

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias, int secondsTimeout) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        String email = userAs.getEmail();
        String previousHash, previousCompleteKey, previousPreviewKey;
        if (profilePictureSnapshotsMap.containsKey(email)
                && profilePictureV3SnapshotsMap.containsKey(email)
                && profilePictureV3PreviewSnapshotsMap.containsKey(email)) {
            previousHash = profilePictureSnapshotsMap.get(email);
            previousCompleteKey = profilePictureV3SnapshotsMap.get(email);
            previousPreviewKey = profilePictureV3PreviewSnapshotsMap.get(email);
        } else {
            throw new RuntimeException(String.format(
                    "Please take user picture snapshot for user '%s' first",
                    userAs.getEmail()));
        }
        long millisecondsStarted = System.currentTimeMillis();
        String actualHash, actualCompleteKey, actualPreviewKey;
        do {
            actualHash = BackendAPIWrappers.getUserPictureHash(userAs);
            actualCompleteKey = BackendAPIWrappers.getUserAssetKey(userAs,
                    BackendAPIWrappers.PROFILE_PICTURE_JSON_ATTRIBUTE);
            actualPreviewKey = BackendAPIWrappers.getUserAssetKey(userAs,
                    BackendAPIWrappers.PROFILE_PREVIEW_PICTURE_JSON_ATTRIBUTE);
            if (!actualHash.equals(previousHash)
                    && !actualCompleteKey.equals(previousCompleteKey)
                    && !actualPreviewKey.equals(previousPreviewKey)) {
                break;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000);
        assertThat("User profile picture is not different (V2)", actualHash, not(equalTo(previousHash)));
        assertThat("User big profile picture is not different (V3)", actualCompleteKey, not(equalTo(previousCompleteKey)));
        assertThat("User small profile picture is not different (V3)", actualPreviewKey, not(equalTo(previousPreviewKey)));
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
        for (String contact : getUserManager().splitAliases(contacts)) {
            if (contact.startsWith("+")) {
                sb.append(contact);
            } else {
                sb.append(getUserManager().replaceAliasesOccurences(contact, FindBy.EMAIL_ALIAS));
            }
            sb.append(ClientUsersManager.ALIASES_SEPARATOR);
        }
        this.UserXHasEmailsInAddressBook(userAsNameAlias, sb.toString());
    }

    public void UserXHasEmailsInAddressBook(String userAsNameAlias,
                                            String emails) throws Exception {
        final ClientUser userAs = getUserManager().findUserByNameOrNameAlias(userAsNameAlias);
        BackendAPIWrappers.uploadAddressBookWithContacts(userAs, getUserManager().splitAliases(emails));
    }

    public void UserXSendsPersonalInvitationWithMessageToUserWithMail(
            String sender, String toMail, String message) throws Exception {
        ClientUser user = getUserManager().findUserByNameOrNameAlias(sender);
        ClientUser invitee = getUserManager().findUserByEmailOrEmailAlias(toMail);
        BackendAPIWrappers.sendPersonalInvitation(user, invitee.getEmail(), invitee.getName(), message);
    }

    public void IAddUserToTheListOfTestCaseUsers(String nameAlias) throws Exception {
        ClientUser userToAdd = getUserManager().findUserByNameOrNameAlias(nameAlias);
        getUserManager().appendCustomUser(userToAdd);
    }

    public void UserRemovesAllRegisteredOtrClients(String forUser) throws Exception {
        final ClientUser usr = getUserManager().findUserByNameOrNameAlias(forUser);
        final List<OtrClient> allOtrClients = BackendAPIWrappers.getOtrClients(usr);
        for (OtrClient c : allOtrClients) {
            BackendAPIWrappers.removeOtrClient(usr, c);
        }
    }

    public List<String> GetDevicesIDsForUser(String name) throws Exception {
        final ClientUser usr = getUserManager().findUserByNameOrNameAlias(name);
        return getDeviceManager().getDeviceIds(usr);
    }

    public void UserKeepsXOtrClients(String userAs, int clientsCountToKeep) throws Exception {
        final ClientUser usr = getUserManager().findUserByNameOrNameAlias(userAs);
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
        final ClientUser usr = getUserManager().findUserByNameOrNameAlias(user);
        return InvitationLinkGenerator.getInvitationUrl(usr.getId());
    }

    public void UserSharesLocationTo(String senderAlias, String dstConversationName, boolean isGroup, String deviceName)
            throws Exception {
        final ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(senderAlias);
        final String dstConvId = isGroup
                ? BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName)
                : getUserManager().findUserByNameOrNameAlias(dstConversationName).getId();
        getDeviceManager().shareDefaultLocation(msgFromUser, dstConvId, deviceName);
    }

    public void UserSwitchesToEphemeralMode(String senderAlias, String dstConversationName,
                                            long expirationMilliseconds, boolean isGroup, String deviceName)
            throws Exception {
        final ClientUser msgFromUser = getUserManager().findUserByNameOrNameAlias(senderAlias);
        String dstConvId;
        if (isGroup) {
            dstConvId = BackendAPIWrappers.getConversationIdByName(msgFromUser, dstConversationName);
        } else {
            dstConvId = getUserManager().findUserByNameOrNameAlias(dstConversationName).getId();
        }
        getDeviceManager().setEphemeralMode(msgFromUser, dstConvId,
                Timedelta.fromMilliSeconds(expirationMilliseconds), deviceName);
    }

    public void UserSetAssetMode(String actorUserNameAlias, ActorsRESTWrapper.AssetsVersion asset, String deviceName)
            throws Exception {
        final ClientUser actorUser = getUserManager().findUserByNameOrNameAlias(actorUserNameAlias);
        switch (asset) {
            case V3:
                getDeviceManager().setAssetToV3(actorUser, deviceName);
                break;
            case V2:
                getDeviceManager().setAssetToV2(actorUser, deviceName);
                break;
            default:
                throw new IllegalArgumentException("Only support AssetProtocol V2 and V3");
        }
    }

    public void UserCancelConnection(String userNameAlias, String canceldUserNameAlias, String deviceName)
            throws Exception {
        final ClientUser user = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        final ClientUser canceledUser = getUserManager().findUserByNameOrNameAlias(canceldUserNameAlias);
        getDeviceManager().cancelConnection(user, canceledUser, deviceName);
    }

    public String GetUserUnqiueUsername(String userNameAlias, String deviceName) throws Exception {
        final ClientUser user = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        return getDeviceManager().getUniqueUsername(user, deviceName);
    }

    public void UpdateUniqueUsername(String userNameAlias, String uniqueUserName, String deviceName) throws Exception {
        final ClientUser user = getUserManager().findUserByNameOrNameAlias(userNameAlias);
        getDeviceManager().updateUniqueUsername(user, uniqueUserName, deviceName);
    }

    public void UserResetsPassword(String nameAlias, String newPassword) throws Exception {
        final ClientUser usr = getUserManager().findUserByNameOrNameAlias(nameAlias);
        BackendAPIWrappers.changeUserPassword(usr, usr.getPassword(), newPassword);
    }

    private Map<String, Optional<String>> recentMessageIds = new HashMap<>();

    private String generateConversationKey(String userFrom, String dstName, String deviceName) {
        return String.format("%s:%s:%s", getUserManager().replaceAliasesOccurences(userFrom, ClientUsersManager.FindBy.NAME_ALIAS),
                getUserManager().replaceAliasesOccurences(dstName, ClientUsersManager.FindBy.NAME_ALIAS), deviceName);
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

    private String getFilteredLastMessageId(List<ActorsRESTWrapper.MessageInfo> messageInfos) throws Exception {
        for (ActorsRESTWrapper.MessageInfo messageInfo : Lists.reverse(messageInfos)) {
            if (messageInfo.getType() != ActorsRESTWrapper.MessageType.UNKNOWN) {
                return messageInfo.getId();
            }
        }
        throw new RuntimeException("Could not find any valid message");
    }

    private String getFilteredSecondLastMessageId(List<ActorsRESTWrapper.MessageInfo> messageInfos) throws Exception {
        ActorsRESTWrapper.MessageInfo latestMessage = null;
        for (ActorsRESTWrapper.MessageInfo messageInfo : Lists.reverse(messageInfos)) {
            if (messageInfo.getType() != ActorsRESTWrapper.MessageType.UNKNOWN) {
                if (latestMessage == null) {
                    latestMessage = messageInfo;
                } else {
                    return messageInfo.getId();
                }
            }
        }
        throw new RuntimeException("Could not find any valid message");
    }

    public void uploadSelfContact(String userAliases) throws Exception {
        for (String alias : getUserManager().splitAliases(userAliases)) {
            final ClientUser selfUser = getUserManager().findUserByNameOrNameAlias(alias);
            BackendAPIWrappers.uploadSelfContact(selfUser);
        }
    }
}
