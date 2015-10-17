package com.wearezeta.auto.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import org.apache.log4j.Logger;

import javax.ws.rs.NotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will steadily replace CommonSteps, taking any regular functionality that is needed (like registering users)
 * but replacing methods that need to be encrypted. I think the easiest way will be to have both classes coexist
 * and steadily transfer to the two over.
 *
 * For now, I'll prove the concept with the Android project
 */
public final class CommonStepsOtr {
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

    private static CommonStepsOtr instance = null;

    public synchronized static CommonStepsOtr getInstance() {
        if (instance == null) {
            instance = new CommonStepsOtr();
        }
        return instance;
    }

    private CommonStepsOtr() {
    }

    public static final String ALIASES_SEPARATOR = ",";

    public static List<String> splitAliases(String aliases) {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ConnectionRequestIsSentTo(String userFromNameAlias,
                                          String usersToNameAliases) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    private static final String OTHER_USERS_ALIAS = "all other";

    public void UserIsConnectedTo(String userFromNameAlias,
                                  String usersToNameAliases) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ThereAreNUsers(Platform currentPlatform, int count)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ThereAreNUsersWhereXIsMe(Platform currentPlatform, int count,
                                         String myNameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ThereAreNUsersWhereXIsMeRegOnlyByMail(Platform currentPlatform, int count,
                                                      String myNameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(
            Platform currentPlatform, int count, String myNameAlias)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    private static final int DRIVER_PING_INTERVAL_SECONDS = 60;

    /**
     * Wait for time in seconds
     *
     * @throws Exception
     */
    public void WaitForTime(int seconds) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    /**
     * Wait for time in seconds
     *
     * @throws Exception
     */
    public void WaitForTime(double seconds) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UnblockContact(String unblockAsUserNameAlias,
                               String userToUnblockNameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ArchiveConversationWithUser(String usersToNameAliases,
                                            String archiveConversationWithUser) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ArchiveConversationWithGroup(String aUser,
                                             String archiveConversationWithGroup) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void GenerateNewLoginCode(String asUser) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void MuteConversationWithUser(String usersToNameAliases,
                                         String muteConversationWithUser) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void MuteConversationWithGroup(String usersToNameAliases,
                                          String muteConversationWithGroup) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UnarchiveConversationWithUser(String usersToNameAliases,
                                              String archiveConversationWithUser) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UnarchiveConversationWithGroup(String aUser,
                                               String archiveConversationWithGroup) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserPingedConversation(String pingFromUserNameAlias,
                                       String dstConversationName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserSentMessageToUser(String msgFromUserNameAlias,
                                      String dstUserNameAlias, String message) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserHotPingedConversation(String hotPingFromUserNameAlias,
                                          String dstConversationName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String dstConversationName, String message) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserSendsImageToConversation(String imageSenderUserNameAlias,
                                             String imagePath, String dstConversationName, Boolean isGroup)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void IChangeUserAvatarPicture(String userNameAlias,
                                         String picturePath) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void IChangeUserName(String userNameAlias, String newName)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void IChangeUserAccentColor(String userNameAlias, String colorName)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXIsMe(String nameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void BlockTcpConnectionForApp(String appName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void EnableTcpConnectionForApp(String appName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void WaitUntilSuggestionFound(String userAsNameAlias)
            throws NoSuchUserException, Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void WaitUntilContactIsNotFoundInSearch(String searchByNameAlias,
                                                   String contactAlias, int timeoutSeconds) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void WaitUntilContactIsFoundInSearch(String searchByNameAlias,
                                                String contactAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void WaitUntilContactBlockStateInSearch(String searchByNameAlias,
                                                   String contactAlias, boolean expectedState) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXAddedContactsToGroupChat(String userAsNameAlias,
                                              String contactsToAddNameAliases, String chatName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXRemoveContactFromGroupChat(String userAsNameAlias,
                                                String contactToRemoveNameAlias, String chatName) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXLeavesGroupChat(String userNameAlias, String chatName)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");

    }

    private Map<String, String> profilePictureSnapshotsMap = new HashMap<String, String>();

    public void UserXTakesSnapshotOfProfilePicture(String userNameAlias)
            throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias, int secondsTimeout) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    private static final int PICTURE_CHANGE_TIMEOUT = 15; // seconds

    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXHasContactsInAddressBook(String userAsNameAlias,
                                              String contacts) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }

    public void UserXHasEmailsInAddressBook(String userAsNameAlias,
                                            String emails) throws Exception {
        throw new NotSupportedException("Method has not been replaced yet");
    }
}
