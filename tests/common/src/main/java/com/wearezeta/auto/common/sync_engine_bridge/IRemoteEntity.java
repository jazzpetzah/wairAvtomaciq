package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;

interface IRemoteEntity {

	String name();

	ActorRef ref();

	boolean isConnected();

}
