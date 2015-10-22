package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.pattern.Patterns;

import com.waz.provision.ActorMessage;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

abstract class RemoteEntity implements IRemoteEntity {

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
		Object resp = null;
		try {
			resp = askActor(ref, new ActorMessage.Echo("test", "test"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (resp instanceof ActorMessage.Echo
				&& ((ActorMessage.Echo) resp).msg().equals("test")) {
			return true;
		}
		return false;
	}

	/**
	 * The method is synchronous
	 * 
	 * @param actorRef
	 * @param message
	 * @return
	 * @throws Exception
	 */
	protected Object askActor(ActorRef actorRef, ActorMessage message)
			throws Exception {
		Future<Object> future = Patterns.ask(actorRef, message,
				actorTimeout.toMillis());
		return Await.result(future, actorTimeout);
	}

	/**
	 * The method is asynchronous
	 * 
	 * @param actorRef
	 * @param message
	 */
	protected void tellActor(ActorRef actorRef, ActorMessage message) {
		actorRef.tell(message, null);
	}
}
