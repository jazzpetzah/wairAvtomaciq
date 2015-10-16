package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.waz.provision.ActorMessage;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

/**
 *
 */
public abstract class RemoteEntity implements IRemoteEntity {

    protected FiniteDuration actorTimeout;

    protected String name;

    protected ActorRef ref;

    public RemoteEntity(FiniteDuration actorTimeout) {
        this.actorTimeout = actorTimeout;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ActorRef ref() {
        return ref;
    }

    @Override
    public boolean isConnected() {
        Object resp = askActor(ref, new ActorMessage.Echo("test", "test"));
        if (resp instanceof ActorMessage.Echo && ((ActorMessage.Echo) resp).msg().equals("test")) {
            return true;
        }
        return false;
    }

    public Object askActor(ActorRef actorRef, ActorMessage message) {
        Future<Object> future = Patterns.ask(actorRef, message, actorTimeout.toMillis());
        Object resp = null;

        try {
            resp = Await.result(future, actorTimeout);
        } catch (Exception e) {
            System.out.println("No response from actor: " + actorRef + " received. Message was: " + message);
            e.printStackTrace();
        }
        return resp;
    }
}
