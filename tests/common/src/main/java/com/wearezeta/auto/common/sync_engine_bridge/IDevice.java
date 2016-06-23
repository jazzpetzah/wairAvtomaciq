package com.wearezeta.auto.common.sync_engine_bridge;

import com.waz.model.MessageId;
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

    void clearConversation(String convId) throws Exception;

    void muteConversation(String convId) throws Exception;

    void unmuteConversation(String convId) throws Exception;

	// TODO: void sendAsset(String convId, byte[] data, String mime, String filename) throws Exception;

	void sendFile(String convId, String path, String mime) throws Exception;

    void sendLocation(String convId, float longitude, float latitude, String name, int zoom)
            throws Exception;

    void deleteMessage(String convId, MessageId messageId) throws Exception;

    ActorMessage.MessageInfo[] getConversationMessages(String convId) throws Exception;

    void setLabel(String label) throws Exception;

    String getId() throws Exception;

    String getFingerprint() throws Exception;

    void destroy();
}
