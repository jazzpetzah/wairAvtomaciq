package com.wearezeta.auto.common.backend;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.email.ActivationMessage;
import com.wearezeta.auto.common.email.InvitationMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.PasswordResetMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.image_send.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.onboarding.AddressBook;
import com.wearezeta.auto.common.onboarding.Card;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import com.wearezeta.auto.common.usrmgmt.UserState;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;

// Almost all methods of this class mutate ClientUser
// argument by performing automatic login (set id and session token attributes)
public final class BackendAPIWrappers {
    public static final int ACTIVATION_TIMEOUT = 120; // seconds
    private static final int INVITATION_RECEIVING_TIMEOUT = 60; // seconds

    private static final int REQUEST_TOO_FREQUENT_ERROR = 429;
    private static final int LOGIN_CODE_HAS_NOT_BEEN_USED_ERROR = 403;
    private static final int AUTH_FAILED_ERROR = 403;
    private static final int SERVER_SIDE_ERROR = 500;
    private static final int PHONE_NUMBER_ALREADY_REGISTERED_ERROR = 409;
    private static final int MAX_BACKEND_RETRIES = 5;

    private static final long MAX_MSG_DELIVERY_OFFSET = 10000; // milliseconds

    private static final Logger log = ZetaLogger
            .getLog(BackendAPIWrappers.class.getSimpleName());

    public static void setDefaultBackendURL(String url) {
        BackendREST.setDefaultBackendURL(url);
    }

    public static ClientUser updateUserAccentColor(ClientUser user)
            throws Exception {
        final JSONObject additionalUserInfo = BackendREST
                .getUserInfo(receiveAuthToken(user));
        user.setAccentColor(AccentColor.getById(additionalUserInfo
                .getInt("accent_id")));
        return user;
    }

    public static Future<String> initMessageListener(ClientUser forUser)
            throws Exception {
        IMAPSMailbox mbox = IMAPSMailbox.getInstance();
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER,
                forUser.getEmail());
        // The MAX_MSG_DELIVERY_OFFSET is necessary because of small
        // time difference between UTC and your local machine
        return mbox.getMessage(expectedHeaders, ACTIVATION_TIMEOUT,
                new Date().getTime() - MAX_MSG_DELIVERY_OFFSET);
    }

    /**
     * Creates a new user by sending the corresponding request to the backend
     *
     * @param user        ClientUser instance with initial user parameters
     *                    (name/email/password)
     * @param retryNumber set this to 1 if it is the first time you try to create this
     *                    particular user
     * @return Created ClientUser instance (with id property filled)
     * @throws Exception
     */
    public static ClientUser createUserViaBackdoor(ClientUser user,
                                                   int retryNumber, RegistrationStrategy strategy) throws Exception {
        String activationCode;
        switch (strategy) {
            case ByEmail:
                BackendREST.registerNewUser(user.getEmail(), user.getName(),
                        user.getPassword());
                activationCode = getActivationCodeForRegisteredEmail(user
                        .getEmail());
                activateRegisteredEmailByBackdoorCade(user.getEmail(),
                        activationCode, false);
                while (true) {
                    try {
                        attachUserPhoneNumber(user);
                        break;
                    } catch (BackendRequestException e) {
                        if (e.getReturnCode() == PHONE_NUMBER_ALREADY_REGISTERED_ERROR) {
                            user.setPhoneNumber(new PhoneNumber(
                                    PhoneNumber.WIRE_COUNTRY_PREFIX));
                        } else {
                            throw e;
                        }
                    }
                }
                break;
            case ByEmailOnly:
                BackendREST.registerNewUser(user.getEmail(), user.getName(),
                        user.getPassword());
                activationCode = getActivationCodeForRegisteredEmail(user
                        .getEmail());
                activateRegisteredEmailByBackdoorCade(user.getEmail(),
                        activationCode, false);
                break;
            case ByPhoneNumber:
            case ByPhoneNumberOnly:
                final int maxRetries = 5;
                int nTry = 0;
                while (true) {
                    try {
                        BackendREST.bookPhoneNumber(user.getPhoneNumber());
                        activationCode = getActivationCodeForBookedPhoneNumber(user
                                .getPhoneNumber());
                        activateRegisteredUserByPhoneNumber(user.getPhoneNumber(),
                                activationCode, true);
                        BackendREST.registerNewUser(user.getPhoneNumber(), user.getName(),
                                activationCode);
                        changeUserPassword(user, null, user.getPassword());
                        break;
                    } catch (BackendRequestException e) {
                        if ((e.getReturnCode() == PHONE_NUMBER_ALREADY_REGISTERED_ERROR ||
                                e.getReturnCode() == 403) && nTry < maxRetries) {
                            // Assign different phone number to this user
                            // The current has been most likely already already created
                            user.setPhoneNumber(new PhoneNumber(
                                    PhoneNumber.WIRE_COUNTRY_PREFIX));
                            nTry++;
                        } else {
                            throw new IllegalStateException(String.format(
                                    "User account with phone number '%s' cannot be created",
                                    user.getPhoneNumber().toString()), e);
                        }
                    }
                }
                if (strategy != RegistrationStrategy.ByPhoneNumberOnly) {
                    attachUserEmailUsingBackdoor(user);
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

    private static void activateRegisteredEmailByBackdoorCade(String email,
                                                              String code, boolean isDryRun) throws Exception {
        BackendREST.activateNewUser(email, code, isDryRun);
        log.debug(String.format("User '%s' is successfully activated", email));
    }

    private static String getActivationCodeForRegisteredEmail(String email)
            throws Exception {
        return BackendREST.getActivationDataViaBackdoor(email)
                .getString("code");
    }

    private static String getActivationCodeForBookedPhoneNumber(
            PhoneNumber phoneNumber) throws Exception {
        return BackendREST.getActivationDataViaBackdoor(phoneNumber).getString(
                "code");
    }

    public static void activateRegisteredUserByPhoneNumber(
            PhoneNumber phoneNumber, String activationCode, boolean isDryRun)
            throws Exception {
        BackendREST.activateNewUser(phoneNumber, activationCode, isDryRun);
        log.debug(String.format("User '%s' is successfully activated",
                phoneNumber.toString()));
    }

    private final static int MAX_ACTIVATION_CODE_GET_RETRIES = 6;
    private final static int BACKEND_ERROR_PHONE_NUMBER_NOT_BOOKED = 404;

    public static String getActivationCodeByPhoneNumber(PhoneNumber phoneNumber)
            throws Exception {
        int ntry = 1;
        BackendRequestException savedException = null;
        do {
            try {
                return getActivationCodeForBookedPhoneNumber(phoneNumber);
            } catch (BackendRequestException e) {
                if (e.getReturnCode() == BACKEND_ERROR_PHONE_NUMBER_NOT_BOOKED) {
                    // the number booking request has not been delivered to the
                    // backend yet
                    savedException = e;
                    log.debug(String
                            .format("The phone number '%s' seems to be not booked yet. Trying to get the activation code one more time (%d of %d)...",
                                    phoneNumber.toString(), ntry,
                                    MAX_ACTIVATION_CODE_GET_RETRIES));
                    Thread.sleep(2000 * ntry);
                } else {
                    throw e;
                }
            }
            ntry++;
        } while (ntry <= MAX_ACTIVATION_CODE_GET_RETRIES);
        throw savedException;
    }

    private final static int MAX_LOGIN_CODE_QUERIES = 6;

    public static String getLoginCodeByPhoneNumber(PhoneNumber phoneNumber)
            throws Exception {
        int ntry = 1;
        Exception savedException = null;
        do {
            try {
                return BackendREST.getLoginCodeViaBackdoor(phoneNumber)
                        .getString("code");
            } catch (BackendRequestException e) {
                log.error(String
                        .format("Failed to get login code for phone number '%s'. Retrying (%s of %s)...",
                                phoneNumber.toString(), ntry,
                                MAX_LOGIN_CODE_QUERIES));
                savedException = e;
                Thread.sleep(2000 * ntry);
            }
            ntry++;
        } while (ntry <= MAX_LOGIN_CODE_QUERIES);
        throw savedException;
    }

    public static void attachUserPhoneNumber(ClientUser user) throws Exception {
        BackendREST.updateSelfPhoneNumber(receiveAuthToken(user),
                user.getPhoneNumber());
        final String activationCode = getActivationCodeForBookedPhoneNumber(user
                .getPhoneNumber());
        activateRegisteredUserByPhoneNumber(user.getPhoneNumber(),
                activationCode, false);
    }

    /**
     * Change/set user password
     *
     * @param user
     * @param oldPassword set this to null if the user has no password set
     * @param newPassword
     * @throws Exception
     */
    private static void changeUserPassword(ClientUser user, String oldPassword,
                                           String newPassword) throws Exception {
        BackendREST.updateSelfPassword(receiveAuthToken(user), oldPassword,
                newPassword);
        user.setPassword(newPassword);
    }

    private static void attachUserEmailUsingBackdoor(ClientUser user)
            throws Exception {
        BackendREST.updateSelfEmail(receiveAuthToken(user), user.getEmail());
        final String activationCode = getActivationCodeForRegisteredEmail(user
                .getEmail());
        activateRegisteredEmailByBackdoorCade(user.getEmail(), activationCode,
                false);
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
        for (ClientUser userTo : usersTo) {
            autoTestSendRequest(userFrom, userTo);
            autoTestAcceptAllRequest(userTo);
        }
    }

    public static void autoTestSendRequest(ClientUser userFrom,
                                           ClientUser userTo) throws Exception {
        sendConnectRequest(userFrom, userTo, userTo.getName(),
                CommonSteps.CONNECTION_MESSAGE);
        userFrom.setUserState(UserState.RequestSend);
    }

    public static ClientUser autoTestAcceptAllRequest(ClientUser userTo)
            throws Exception {
        acceptAllConnections(userTo);
        userTo.setUserState(UserState.AllContactsConnected);
        return userTo;
    }

    public static void sendDialogMessage(ClientUser fromUser,
                                         ClientUser toUser, String message) throws Exception {
        final String convId = getConversationWithSingleUser(fromUser, toUser);
        sendConversationMessage(fromUser, convId, message);
    }

    public static void sendDialogMessageByChatName(ClientUser fromUser,
                                                   String toChat, String message) throws Exception {
        String id = getConversationIdByName(fromUser, toChat);
        sendConversationMessage(fromUser, id, message);
    }

    public static String sendPingToConversation(ClientUser fromUser,
                                                String toChat) throws Exception {
        String id = getConversationIdByName(fromUser, toChat);
        return sendConversationPing(fromUser, id);
    }

    public static void sendHotPingToConversation(ClientUser fromUser,
                                                 String toChat, String id) throws Exception {
        String conv_id = getConversationIdByName(fromUser, toChat);
        sendConvertsationHotPing(fromUser, conv_id, id);
    }

    private static AuthToken generateAuthToken(ClientUser user) {
        return new AuthToken(user.getTokenType(), user.getAccessToken());
    }

    private static AuthToken receiveAuthToken(ClientUser user) throws Exception {
        if (user.getAccessToken() == null) {
            user = tryLoginByUser(user);
        }
        return generateAuthToken(user);
    }

    public static void sendPictureToSingleUserConversation(ClientUser userFrom,
                                                           ClientUser userTo, String path) throws Exception {
        final String convId = getConversationWithSingleUser(userFrom, userTo);
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths.get(path));
        BackendREST.sendPicture(receiveAuthToken(userFrom), convId,
                srcImageAsByteArray, getImageMimeType(path));
    }

    public static void sendPictureToSingleUserConversationOtr(
            ClientUser userFrom, ClientUser userTo, String path)
            throws Exception {
        final String convId = getConversationWithSingleUser(userFrom, userTo);
        SEBridge.getInstance().sendImage(userFrom, convId, path);
    }

    private static String getImageMimeType(String path) {
        if (path.toLowerCase().endsWith(".png")) {
            return ImageAssetProcessor.MIME_TYPE_PNG;
        } else if (path.toLowerCase().endsWith(".jpg")
                || path.toLowerCase().endsWith(".jpeg")) {
            return ImageAssetProcessor.MIME_TYPE_JPEG;
        } else if (path.toLowerCase().endsWith(".gif")) {
            return ImageAssetProcessor.MIME_TYPE_GIF;
        } else {
            throw new RuntimeException(String.format(
                    "Cannot detect MIME type for the image by path %s", path));
        }
    }

    public static void sendPictureToChatById(ClientUser userFrom,
                                             String chatId, String path) throws Exception {
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths.get(path));
        BackendREST.sendPicture(receiveAuthToken(userFrom), chatId,
                srcImageAsByteArray, getImageMimeType(path));
    }

    public static void sendPictureToChatByName(ClientUser userFrom,
                                               String chatName, String path) throws Exception {
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths.get(path));
        BackendREST.sendPicture(receiveAuthToken(userFrom),
                getConversationIdByName(userFrom, chatName),
                srcImageAsByteArray, getImageMimeType(path));
    }

    public static void sendPictureToChatByNameOtr(ClientUser userFrom,
                                                  String chatName, String path) throws Exception {
        final String convId = getConversationIdByName(userFrom, chatName);
        SEBridge.getInstance().sendImage(userFrom, convId, path);
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
                            receiveAuthToken(fromUser), convID,
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
        toUser = tryLoginByUser(toUser);
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
        throw new RuntimeException(
                String.format(
                        "There is no conversation between users '%s' and '%s' on the backend",
                        fromUser.getName(), toUser.getName()));
    }

    public static void generateNewLoginCode(ClientUser user) throws Exception {
        BackendREST.generateLoginCode(user.getPhoneNumber());
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
                        try {
                            BackendREST
                                    .generateLoginCode(user.getPhoneNumber());
                        } catch (BackendRequestException e1) {
                            if (e1.getReturnCode() != LOGIN_CODE_HAS_NOT_BEEN_USED_ERROR) {
                                throw e1;
                            }
                        }
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
        final JSONObject userInfo = BackendREST.getUserInfoByID(id,
                receiveAuthToken(user));
        return userInfo.getString("name");
    }

    public static String getUserPictureHash(ClientUser user) throws Exception {
        final JSONObject userInfo = BackendREST
                .getUserInfo(receiveAuthToken(user));
        final String picture = userInfo.getJSONArray("picture").toString();
        return DigestUtils.sha256Hex(picture);
    }

    public static void sendConnectRequest(ClientUser user, ClientUser contact,
                                          String connectName, String message) throws Exception {
        tryLoginByUser(contact);
        BackendREST.sendConnectRequest(receiveAuthToken(user), contact.getId(),
                connectName, message);
    }

    private static JSONArray getAllConnections(ClientUser user)
            throws Exception {
        String startId = null;
        JSONObject connectionsInfo = null;
        final JSONArray result = new JSONArray();
        do {
            connectionsInfo = BackendREST.getConnectionsInfo(
                    receiveAuthToken(user), null, startId);
            final JSONArray connections = connectionsInfo
                    .getJSONArray("connections");
            for (int i = 0; i < connections.length(); i++) {
                result.put(connections.getJSONObject(i));
            }
            if (connections.length() > 0) {
                startId = connections.getJSONObject(connections.length() - 1)
                        .getString("to");
            }
        } while (connectionsInfo.getBoolean("has_more"));
        return result;
    }

    public static void acceptAllConnections(ClientUser user) throws Exception {
        final JSONArray connections = getAllConnections(user);
        for (int i = 0; i < connections.length(); i++) {
            String to = connections.getJSONObject(i).getString("to");
            String status = connections.getJSONObject(i).getString("status");
            if (status.equals(ConnectionStatus.Pending.toString())) {
                changeConnectRequestStatus(user, to, ConnectionStatus.Accepted);
            }
        }
    }

    public static void cancelAllOutgoingConnections(ClientUser user) throws Exception {
        final JSONArray connections = getAllConnections(user);
        for (int i = 0; i < connections.length(); i++) {
            String to = connections.getJSONObject(i).getString("to");
            String status = connections.getJSONObject(i).getString("status");
            if (status.equals(ConnectionStatus.Sent.toString())) {
                changeConnectRequestStatus(user, to, ConnectionStatus.Canceled);
            }
        }
    }

    public static void ignoreAllConnections(ClientUser user) throws Exception {
        final JSONArray connections = getAllConnections(user);
        for (int i = 0; i < connections.length(); i++) {
            String to = connections.getJSONObject(i).getString("to");
            String status = connections.getJSONObject(i).getString("status");
            if (status.equals(ConnectionStatus.Pending.toString())) {
                changeConnectRequestStatus(user, to, ConnectionStatus.Ignored);
            }
        }
    }

    public static void changeConnectRequestStatus(ClientUser user,
                                                  String connectionId, ConnectionStatus newStatus) throws Exception {
        BackendREST.changeConnectRequestStatus(receiveAuthToken(user),
                connectionId, newStatus);
    }

    public static void createGroupConversation(ClientUser user,
                                               List<ClientUser> contacts, String conversationName)
            throws Exception {
        List<String> ids = new ArrayList<String>();
        for (ClientUser contact : contacts) {
            tryLoginByUser(contact);
            ids.add(contact.getId());
        }
        BackendREST.createGroupConversation(receiveAuthToken(user), ids,
                conversationName);
    }

    public static void addContactsToGroupConversation(ClientUser asUser,
                                                      List<ClientUser> contacts, String conversationName)
            throws Exception {
        List<String> ids = new ArrayList<String>();
        for (ClientUser contact : contacts) {
            tryLoginByUser(contact);
            ids.add(contact.getId());
        }
        BackendREST.addContactsToGroupConvo(receiveAuthToken(asUser), ids,
                getConversationIdByName(asUser, conversationName));
    }

    public static void removeUserFromGroupConversation(ClientUser asUser,
                                                       ClientUser contact, String conversationName) throws Exception {
        contact = tryLoginByUser(contact);
        String contactId = contact.getId();
        BackendREST.removeContactFromGroupConvo(receiveAuthToken(asUser),
                contactId, getConversationIdByName(asUser, conversationName));
    }

    public static void sendConversationMessage(ClientUser userFrom,
                                               String convId, String message) throws Exception {
        BackendREST.sendConversationMessage(receiveAuthToken(userFrom), convId,
                message);
    }

    public static void sendConversationMessages(ClientUser userFrom,
                                                String convId, List<String> messages) throws Exception {
        for (String message : messages) {
            BackendREST.sendConversationMessage(receiveAuthToken(userFrom),
                    convId, message);
        }
    }

    public static void uploadAddressBookWithContacts(ClientUser user,
                                                     List<String> emailsToAdd) throws Exception {
        AddressBook addressBook = new AddressBook();
        for (String email : emailsToAdd) {
            Card card = new Card();
            card.addContact(email);
            addressBook.addCard(card);
        }
        BackendREST.uploadAddressBook(receiveAuthToken(user), addressBook);
    }

    public static void waitUntilSuggestionFound(ClientUser userFrom, int timeout)
            throws Exception {
        long startTimestamp = (new Date()).getTime();
        while ((new Date()).getTime() <= startTimestamp + timeout * 1000) {
            final JSONObject suggestions = BackendREST
                    .getSuggestions(receiveAuthToken(userFrom));
            log.debug("Suggestions: " + suggestions.toString());
            if (suggestions.getInt("returned") > 0) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format(
                "%s suggestions were not found within %s second(s) timeout",
                userFrom.getName(), timeout));
    }

    public static String sendConversationPing(ClientUser userFrom, String convId)
            throws Exception {
        JSONObject response = BackendREST.sendConversationPing(
                receiveAuthToken(userFrom), convId);
        return response.getString("id");
    }

    public static void sendConvertsationHotPing(ClientUser userFrom,
                                                String convId, String refId) throws Exception {
        BackendREST.sendConvertsationHotPing(receiveAuthToken(userFrom),
                convId, refId);
    }

    public static JSONArray getConversations(ClientUser user) throws Exception {
        final JSONArray result = new JSONArray();
        String startId = null;
        JSONObject conversationsInfo = null;
        do {
            int tryNum = 0;
            while (tryNum < MAX_BACKEND_RETRIES) {
                try {
                    conversationsInfo = BackendREST.getConversationsInfo(
                            receiveAuthToken(user), startId);
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
            final JSONArray response = conversationsInfo
                    .getJSONArray("conversations");
            for (int i = 0; i < response.length(); i++) {
                result.put(response.getJSONObject(i));
            }
            if (response.length() > 0) {
                startId = response.getJSONObject(response.length() - 1)
                        .getString("id");
            }
        } while (conversationsInfo.getBoolean("has_more"));
        return result;
    }

    private static JSONArray getEventsfromConversation(String convId,
                                                       ClientUser user) throws Exception {
        return BackendREST.getEventsFromConversation(receiveAuthToken(user),
                convId).getJSONArray("events");
    }

    public static BufferedImage getAssetsDownload(String convId,
                                                  String assetId, ClientUser user) throws Exception {
        return BackendREST.getAssetsDownload(receiveAuthToken(user), convId,
                assetId);
    }

    public static void updateUserPicture(ClientUser user, String picturePath)
            throws Exception {
        tryLoginByUser(user);
        final String convId = user.getId();
        final byte[] srcImageAsByteArray = Files.readAllBytes(Paths
                .get(picturePath));

        ImageAssetData srcImgData = new ImageAssetData(convId,
                srcImageAsByteArray, getImageMimeType(picturePath));
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

    public static void updateUserName(ClientUser user, String newName)
            throws Exception {
        BackendREST.updateSelfInfo(receiveAuthToken(user), null, null, newName);
        user.setName(newName);
    }

    public static void updateUserAccentColor(ClientUser user, AccentColor color)
            throws Exception {
        BackendREST.updateSelfInfo(receiveAuthToken(user), color.getId(), null,
                null);
        user.setAccentColor(color);
    }

    public static void updateConvLastReadState(ClientUser user, String convId,
                                               String lastReadEvent) throws Exception {
        // can be used to override last_read event or use
        // getLastEventFromConversation to set the last read to the last event
        BackendREST.updateConvSelfInfo(receiveAuthToken(user), convId,
                lastReadEvent, null, null);
    }

    public static void updateConvMutedState(ClientUser user,
                                            ClientUser mutedUser, boolean muted) throws Exception {
        final String convId = getConversationWithSingleUser(user, mutedUser);
        BackendREST.updateConvSelfInfo(receiveAuthToken(user), convId, null,
                muted, null);
    }

    public static void updateGroupConvMutedState(ClientUser user,
                                                 String groupConvName, boolean muted) throws Exception {
        BackendREST
                .updateConvSelfInfo(receiveAuthToken(user),
                        getConversationIdByName(user, groupConvName), null,
                        muted, null);
    }

    public static void archiveUserConv(ClientUser ownerUser,
                                       ClientUser archivedUser) throws Exception {
        final String convId = getConversationWithSingleUser(ownerUser,
                archivedUser);
        BackendREST.updateConvSelfInfo(receiveAuthToken(ownerUser), convId,
                null, null, true);
    }

    public static void archiveGroupConv(ClientUser selfUser,
                                        String conversationToArchive) throws Exception {
        BackendREST.updateConvSelfInfo(receiveAuthToken(selfUser),
                conversationToArchive, null, null, true);
    }

    public static void unarchiveUserConv(ClientUser ownerUser,
                                         ClientUser archivedUser) throws Exception {
        final String convId = getConversationWithSingleUser(ownerUser,
                archivedUser);
        BackendREST.updateConvSelfInfo(receiveAuthToken(ownerUser), convId,
                null, null, false);
    }

    public static void unarchiveGroupConv(ClientUser ownerUser,
                                          String conversationToUnarchive) throws Exception {
        tryLoginByUser(ownerUser);
        BackendREST.updateConvSelfInfo(generateAuthToken(ownerUser),
                conversationToUnarchive, null, null, false);
    }

    public static class NoContactsFoundException extends Exception {
        private static final long serialVersionUID = -7682778364420522320L;

        public NoContactsFoundException(String msg) {
            super(msg);
        }
    }

    public static void waitUntilContactsFound(ClientUser searchByUser,
                                              String query, int expectedCount, boolean orMore, int timeoutSeconds)
            throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount = -1;
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            final JSONObject searchResult = BackendREST.searchForContacts(
                    receiveAuthToken(searchByUser), query);
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
                        expectedCount, query, timeoutSeconds));
    }

    public static void waitUntilTopPeopleContactsFound(ClientUser searchByUser,
                                                       int size, int expectedCount, boolean orMore, int timeoutSeconds)
            throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount = -1;
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            final JSONObject searchResult = BackendREST
                    .searchForTopPeopleContacts(receiveAuthToken(searchByUser),
                            size);
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
                        expectedCount, size, timeoutSeconds));
    }

    public static void waitUntilContactBlockState(ClientUser searchByUser,
                                                  String query, boolean expectedState, int timeoutSeconds)
            throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000) {
            final JSONObject searchResult = BackendREST.searchForContacts(
                    receiveAuthToken(searchByUser), query);
            if (searchResult.has("documents")
                    && (searchResult.get("documents") instanceof JSONArray)) {
                final JSONObject doc = searchResult.getJSONArray("documents")
                        .getJSONObject(0);
                if (doc.getBoolean("blocked") == expectedState) {
                    return;
                }
            }
            Thread.sleep(1000);
        }
        throw new NoContactsFoundException(String.format(
                "%s contact was not blocked within %s second(s) timeout",
                query, timeoutSeconds));
    }

    public static void waitUntilContactNotFound(ClientUser searchByUser,
                                                String query, int timeoutSeconds) throws Exception {
        final long startTimestamp = System.currentTimeMillis();
        int currentCount = 0;
        do {
            final JSONObject searchResult = BackendREST.searchForContacts(
                    receiveAuthToken(searchByUser), query);
            if (searchResult.has("documents")
                    && (searchResult.get("documents") instanceof JSONArray)) {
                currentCount = searchResult.getJSONArray("documents").length();
            }
            if (currentCount <= 0) {
                return;
            }
            Thread.sleep(1000);
        } while (System.currentTimeMillis() - startTimestamp <= timeoutSeconds * 1000);
        throw new AssertionError(
                String.format(
                        "%s contact(s) '%s' are still found after %s second(s) timeout",
                        currentCount, query, timeoutSeconds));
    }

    public static void sendPersonalInvitation(ClientUser ownerUser,
                                              String toEmail, String toName, String message) throws Exception {
        tryLoginByUser(ownerUser);
        BackendREST.sendPersonalInvitation(generateAuthToken(ownerUser),
                toEmail, toName, message);
    }

    public static Optional<InvitationMessage> getInvitationMessage(String email)
            throws Exception {
        IMAPSMailbox mbox = IMAPSMailbox.getInstance();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
        try {
            final String msg = mbox.getMessage(expectedHeaders,
                    INVITATION_RECEIVING_TIMEOUT, 0).get();
            return Optional.of(new InvitationMessage(msg));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static List<OtrClient> getOtrClients(ClientUser forUser) throws Exception {
        tryLoginByUser(forUser);
        final List<OtrClient> result = new ArrayList<>();
        final JSONArray responseList = BackendREST.getClients(receiveAuthToken(forUser));
        for (int clientIdx = 0; clientIdx < responseList.length(); clientIdx++) {
            result.add(new OtrClient(responseList.getJSONObject(clientIdx)));
        }
        return result;
    }

    public static void removeOtrClient(ClientUser forUser, OtrClient otrClientInfo) throws Exception {
        tryLoginByUser(forUser);
        BackendREST.deleteClient(receiveAuthToken(forUser), forUser.getPassword(), otrClientInfo.getId());
    }
}
