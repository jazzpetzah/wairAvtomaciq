package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Optional;

import akka.actor.ActorRef;

import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import scala.concurrent.duration.FiniteDuration;

class Device extends RemoteEntity implements IDevice {

    private Optional<ClientUser> loggedInUser = Optional.empty();
    private Optional<String> id = Optional.empty();
    private Optional<String> fingerprint = Optional.empty();
    private IRemoteProcess hostProcess;

    public Device(String deviceName, IRemoteProcess process, FiniteDuration actorTimeout) {
        super(actorTimeout);
        this.setName(deviceName);
        this.hostProcess = process;
        spawn();
    }

    private void spawn() {
        final ActorRef processActorRef = this.hostProcess.ref();
        try {
            final Object resp = askActor(processActorRef,
                    new ActorMessage.SpawnRemoteDevice(null, this.name()));
            if (resp instanceof ActorRef) {
                ActorRef deviceRef = (ActorRef) resp;
                this.setRef(deviceRef);
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
    public void logInWithUser(ClientUser user) throws Exception {
        final Object resp = askActor(this.ref(),
                new ActorMessage.Login(user.getEmail(), user.getPassword()));
        if (resp instanceof ActorMessage.Successful$) {
            this.loggedInUser = Optional.of(user);
        } else {
            throw new RuntimeException(
                    String.format(
                            "User '%s' has failed to log in into device '%s'. Please check the log file %s for more details.",
                            user.getName(), this.name(), this.hostProcess.getLogPath()));
        }
    }

    @Override
    public void setLabel(String label) throws Exception {
        askActor(this.ref(), new ActorMessage.SetDeviceLabel(label));
    }

    @Override
    public void sendMessage(String convId, String message) throws Exception {
        askActor(this.ref(), new ActorMessage.SendText(new RConvId(convId), message));
    }

    @Override
    public void logout() {
        // TODO: Auto-generated method stub
    }

    private static final long IMAGE_SENDING_TIMEOUT = 90 * 1000; // milliseconds

    @Override
    public void sendImage(String convId, String path) throws Exception {
        askActor(this.ref(), new ActorMessage.SendImage(new RConvId(convId), path), IMAGE_SENDING_TIMEOUT);
    }

    private static final long PING_SENDING_TIMEOUT = 60 * 1000; // milliseconds

    @Override
    public void sendPing(String convId) throws Exception {
        askActor(this.ref(), new ActorMessage.Knock(new RConvId(convId)), PING_SENDING_TIMEOUT);
    }

    @Override
    public String getId() throws Exception {
        if (!this.id.isPresent()) {
            final Object resp = askActor(this.ref(), new ActorMessage.GetDeviceId());
            if (resp instanceof ActorMessage.Successful) {
                id = Optional.of(((ActorMessage.Successful) resp).response());
                return id.get();
            } else {
                throw new RuntimeException(
                        String.format(
                                "Could not get ID of device. Please check the log file %s for more details.", this.name(), this.hostProcess.getLogPath()));
            }
        } else {
            return this.id.get();
        }
    }

    @Override
    public String getFingerprint() throws Exception {
        if (!this.fingerprint.isPresent()) {
            final Object resp = askActor(this.ref(), new ActorMessage.GetDeviceFingerPrint());
            if (resp instanceof ActorMessage.Successful) {
                fingerprint = Optional.of(((ActorMessage.Successful) resp).response());
                return fingerprint.get();
            } else {
                throw new RuntimeException(
                        String.format(
                                "Could not get fingerprint of device. Please check the log file %s for more details.", this.name(), this.hostProcess.getLogPath()));
            }
        } else {
            return this.fingerprint.get();
        }
    }

}
