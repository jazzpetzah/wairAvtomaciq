package com.wearezeta.auto.common.device;

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
