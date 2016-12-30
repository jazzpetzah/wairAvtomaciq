package com.wearezeta.auto.common.wire_actors;

import akka.actor.ActorRef;

interface IRemoteEntity {

    String name();

    ActorRef ref();

    boolean isConnected();
}
