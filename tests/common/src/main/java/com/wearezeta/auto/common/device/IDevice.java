package com.wearezeta.auto.common.device;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

/**
 *
 */
public interface IDevice extends IRemoteEntity {

    boolean hasLoggedInUser();

    boolean isLoggedInUser(ClientUser user);

    void logInWithUser(ClientUser user);

    void sendMessage(String convId, String message);

}
