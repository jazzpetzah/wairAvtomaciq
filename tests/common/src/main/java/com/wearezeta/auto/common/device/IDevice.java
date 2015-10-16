package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

/**
 *
 */
public interface IDevice {
    ActorRef ref();

    boolean hasLoggedInUser();

    boolean isLoggedInUser(ClientUser user);

    void logInWithUser(ClientUser user);

}
