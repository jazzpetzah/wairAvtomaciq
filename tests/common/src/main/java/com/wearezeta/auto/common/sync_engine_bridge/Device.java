package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Optional;

import jersey.repackaged.com.google.common.base.Throwables;
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
		spawnDevice();
	}

	private void spawnDevice() {
		final ActorRef processActorRef = this.hostProcess.ref();
		try {
			final Object resp = askActor(processActorRef,
					new SpawnRemoteDevice(null, this.name));
			if (resp instanceof ActorRef) {
				ActorRef deviceRef = (ActorRef) resp;
				this.ref = deviceRef;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new IllegalStateException(
				String.format(
						"There was an error establishing a connection with a new device: "
								+ "%s on process: %s. Please check the log file %s for more details.",
						this.name(), this.hostProcess.name(),
						this.hostProcess.getLogPath()));
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
		try {
			final Object resp = askActor(ref,
					new Login(user.getEmail(), user.getPassword()));
			if (resp instanceof Successful$) {
				loggedInUser = Optional.of(user);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException(
				String.format(
						"User '%s' has failed to log in into device '%s'. Please check the log file %s for more details.",
						user.getName(), this.name(),
						this.hostProcess.getLogPath()));
	}

	@Override
	public void sendMessage(String convId, String message) {
		try {
			askActor(ref, new SendText(new RConvId(convId), message));
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public void logout() {
		// TODO: Auto-generated method stub
	}
}
