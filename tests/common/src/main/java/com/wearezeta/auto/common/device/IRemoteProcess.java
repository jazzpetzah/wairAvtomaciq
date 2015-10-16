package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;

/**
 *
 */
public interface IRemoteProcess {
    String name();

    ActorRef ref();

    boolean isConnected();
}
