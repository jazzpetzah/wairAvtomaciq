package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;

/**
 *
 */
public interface IRemoteEntity {

    String name();

    ActorRef ref();

    boolean isConnected();

}
