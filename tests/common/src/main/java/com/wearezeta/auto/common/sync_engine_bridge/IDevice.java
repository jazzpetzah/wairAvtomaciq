package com.wearezeta.auto.common.sync_engine_bridge;

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

    void setLabel(String label) throws Exception;

    String getId() throws Exception;

    String getFingerprint() throws Exception;

	void destroy() throws Exception;
}
