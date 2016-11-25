package com.wearezeta.auto.common.sync_engine_bridge;

import com.waz.model.MessageId;
import com.waz.model.UserId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.Optional;

public interface IDevice extends IRemoteEntity {

    boolean hasLoggedInUser();

    boolean isLoggedInUser(ClientUser user);

    Optional<ClientUser> getLoggedInUser();

    void logInWithUser(ClientUser user) throws Exception;

    void sendMessage(String convId, String message) throws Exception;

    void sendImage(String convId, String path) throws Exception;

    void sendPing(String convId) throws Exception;

    void typing(String convId) throws Exception;

    void clearConversation(String convId) throws Exception;

    void muteConversation(String convId) throws Exception;

    void unmuteConversation(String convId) throws Exception;

    void archiveConversation(String convId) throws Exception;

    void unarchiveConversation(String convId) throws Exception;

    // TODO: void sendAsset(String convId, byte[] data, String mime, String filename) throws Exception;

    void sendFile(String convId, String path, String mime) throws Exception;

    void deleteMessage(String convId, MessageId messageId) throws Exception;

    void deleteMessageEveryWhere(String convId, MessageId messageId) throws Exception;

    void updateMessage(MessageId messageId, String newMessage) throws Exception;

    ActorMessage.MessageInfo[] getConversationMessages(String convId) throws Exception;

    void reactMessage(String convId, MessageId messageId, MessageReactionType reactionType) throws Exception;

    void shareLocation(String convId, float lon, float lat, String address, int zoom) throws Exception;

    void shareLocation(String convId) throws Exception;

    void setLabel(String label) throws Exception;

    void setEphemeralMode(String convId, long expirationMilliseconds) throws Exception;

    void markEphemeralRead(String convId, MessageId messageId) throws Exception;

    String getId() throws Exception;

    String getFingerprint() throws Exception;

    void destroy();

    void setAssetToV3() throws Exception;

    void setAssetToV2() throws Exception;

    void sendGiphy(String convId, String searchQuery) throws Exception;

    void cancelConnection(UserId userId) throws Exception;
}
