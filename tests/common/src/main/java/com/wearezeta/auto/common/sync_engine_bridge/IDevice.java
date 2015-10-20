package com.wearezeta.auto.common.sync_engine_bridge;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

public interface IDevice extends IRemoteEntity {
	boolean hasLoggedInUser();

	boolean isLoggedInUser(ClientUser user);

	void logInWithUser(ClientUser user);

	void sendMessage(String convId, String message);
	
	void logout();
}
