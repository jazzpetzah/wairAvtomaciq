package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.ActorMessage.TerminateRemotes$;
import com.waz.provision.CoordinatorActor;

import scala.concurrent.duration.FiniteDuration;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

class DevicePool {
	private static final FiniteDuration ACTOR_DURATION = new FiniteDuration(
			30000, TimeUnit.MILLISECONDS);

	private ActorRef coordinatorActorRef;

	private final String DEVICE_PREFIX = "Remote_device";

	private static final int MAX_DEVICES = 1100;

	private RemoteProcess hostProcess;
	private final String processName = UUID.randomUUID().toString()
			.substring(0, 8);

	private List<Device> devices = new CopyOnWriteArrayList<>();

	public DevicePool() {
		final Config config = ConfigFactory.load("actor_coordinator");
		final ActorSystem system = ActorSystem.create("CoordinatorSystem",
				config);
		this.coordinatorActorRef = system.actorOf(
				Props.create(CoordinatorActor.class), "coordinatorActor");
		this.hostProcess = new RemoteProcess(this.processName,
				this.coordinatorActorRef, ACTOR_DURATION);
	}

	private synchronized String getNewDeviceName() {
		return String.format("%s_%s", DEVICE_PREFIX, this.devices.size() + 1);
	}

	public int getDevicesCount() {
		return this.devices.size();
	}

	public Device addDevice() {
		if (this.getDevicesCount() >= MAX_DEVICES) {
			throw new IllegalStateException(
					String.format(
							"Cannot create more than %s devices per one process instance",
							MAX_DEVICES));
		}
		final Device result = new Device(getNewDeviceName(), this.hostProcess,
				ACTOR_DURATION);
		this.devices.add(result);
		return result;
	}

	public synchronized void clear() {
		this.devices.clear();
	}

	public synchronized void shutdown() {
		coordinatorActorRef.tell(TerminateRemotes$.MODULE$, null);
		this.devices = null;
		this.coordinatorActorRef = null;
		this.hostProcess = null;
	}

}
