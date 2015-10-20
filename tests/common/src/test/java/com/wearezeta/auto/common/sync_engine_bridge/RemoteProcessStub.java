package com.wearezeta.auto.common.sync_engine_bridge;

import com.wearezeta.auto.common.sync_engine_bridge.IRemoteProcess;

import akka.actor.ActorPath;
import akka.actor.ActorRef;

/**
 *
 */
public class RemoteProcessStub implements IRemoteProcess {

	private String name = "RemoteProcessStub";

	@Override
	public String name() {
		return name;
	}

	@Override
	public ActorRef ref() {
		return new ActorRef() {
			private static final long serialVersionUID = 7096351005874225823L;

			@Override
			public boolean isTerminated() {
				return true;
			}

			@Override
			public ActorPath path() {
				return null;
			}
		};
	}

	@Override
	public boolean isConnected() {
		return false;
	}
}
