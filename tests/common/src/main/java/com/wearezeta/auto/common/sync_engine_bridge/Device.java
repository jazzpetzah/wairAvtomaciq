package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Optional;

import akka.actor.ActorRef;

import com.waz.model.RConvId;
import com.waz.provision.ActorMessage.Login;
import com.waz.provision.ActorMessage.SendText;
import com.waz.provision.ActorMessage.SpawnRemoteDevice;
import com.waz.provision.ActorMessage.Successful$;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import scala.concurrent.duration.FiniteDuration;

class Device extends RemoteEntity implements IDevice {

	private Optional<ClientUser> loggedInUser = Optional.empty();
	private IRemoteProcess hostProcess;

	public Device(String deviceName, IRemoteProcess process,
			FiniteDuration actorTimeout) {
		super(actorTimeout);
		this.name = deviceName;
		this.hostProcess = process;
		// TODO check that this doesn't become a bottleneck with lots of
		// Devices...
		if (!process.isConnected()) {
			throw new IllegalStateException(
					"The process used to spawn this device has no established connection");
		}
		spawnDevice();
	}

	private void spawnDevice() {
		// TODO check that process is established
		ActorRef processActorRef = this.hostProcess.ref();
		Object resp = askActor(processActorRef, new SpawnRemoteDevice(null,
				name));
		if (resp instanceof ActorRef) {
			ActorRef deviceRef = (ActorRef) resp;
			this.ref = deviceRef;
		} else {
			throw new IllegalStateException(
					"There was an error establishing a connection with a new device: "
							+ name + " on process: " + this.hostProcess.name()
							+ ". The response was: " + resp);
		}
	}

	@Override
	public boolean hasLoggedInUser() {
		return this.loggedInUser.isPresent();
	}

	@Override
	public boolean isLoggedInUser(ClientUser user) {
		return this.loggedInUser.orElseGet(() -> null) == user;
	}

	@Override
	public void logInWithUser(ClientUser user) {
		Object resp = askActor(ref,
				new Login(user.getEmail(), user.getPassword()));
		if (resp instanceof Successful$) {
			System.out.println("Login to: " + name + " successful");
			loggedInUser = Optional.of(user);
		} else {
			throw new RuntimeException(String.format(
					"User '%s' has failed to log in into device '%s'",
					user.getName(), this.name()));
		}
	}

	@Override
	public void sendMessage(String convId, String message) {
		tellActor(ref, new SendText(new RConvId(convId), message));
	}

	@Override
	public void logout() {
		// TODO: Auto-generated method stub
	}
}
