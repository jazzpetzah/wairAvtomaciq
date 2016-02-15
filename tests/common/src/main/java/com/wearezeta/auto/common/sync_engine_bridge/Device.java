package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;

import akka.actor.PoisonPill;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import scala.concurrent.duration.FiniteDuration;

class Device extends RemoteEntity implements IDevice {

    private Optional<ClientUser> loggedInUser = Optional.empty();
    private Optional<String> id = Optional.empty();
    private Optional<String> fingerprint = Optional.empty();
    private IRemoteProcess hostProcess;
    private ActorRef coordinatorActorRef;

    public Device(IRemoteProcess hostProcess, String deviceName, ActorRef coordinatorActorRef,
                  FiniteDuration actorTimeout) {
        super(null, deviceName, actorTimeout);
        this.hostProcess = hostProcess;
        this.coordinatorActorRef = coordinatorActorRef;
        respawn();
    }

    private void respawn() {
        final ActorRef processActorRef = this.hostProcess.ref();
        try {
            final Object resp = askActor(processActorRef, new ActorMessage.SpawnRemoteDevice(null, this.name()));
            if (resp instanceof ActorRef) {
                ActorRef deviceRef = (ActorRef) resp;
                this.setRef(deviceRef);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalStateException(String.format(
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
    public Optional<ClientUser> getLoggedInUser() {
        return this.loggedInUser;
    }

    @Override
    public void logInWithUser(ClientUser user) throws Exception {
        Object resp;
        try {
            resp = askActor(this.ref(), new ActorMessage.Login(user.getEmail(), user.getPassword()));
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            resp = askActor(this.ref(), new ActorMessage.Login(user.getEmail(), user.getPassword()));
        }
        if (resp instanceof ActorMessage.Successful$) {
            this.loggedInUser = Optional.of(user);
        } else {
            throw new RuntimeException(String.format(
                    "User '%s' has failed to log in into device '%s'. Please check the log file %s for more details.",
                    user.getName(), this.name(), this.hostProcess.getLogPath()));
        }
    }

    @Override
    public void setLabel(String label) throws Exception {
        try {
            askActor(this.ref(), new ActorMessage.SetDeviceLabel(label));
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            if (hasLoggedInUser()) {
                logInWithUser(this.loggedInUser.get());
            }
            askActor(this.ref(), new ActorMessage.SetDeviceLabel(label));
        }
    }

    @Override
    public void sendMessage(String convId, String message) throws Exception {
        try {
            askActor(this.ref(), new ActorMessage.SendText(new RConvId(convId), message));
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            if (hasLoggedInUser()) {
                logInWithUser(this.loggedInUser.get());
            }
            askActor(this.ref(), new ActorMessage.SendText(new RConvId(convId), message));
        }
    }

    @Override
    public void sendImage(String convId, String path) throws Exception {
        try {
            askActor(this.ref(), new ActorMessage.SendImage(new RConvId(convId), path));
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            if (hasLoggedInUser()) {
                logInWithUser(this.loggedInUser.get());
            }
            askActor(this.ref(), new ActorMessage.SendImage(new RConvId(convId), path));
        }
    }

    @Override
    public void sendPing(String convId) throws Exception {
        try {
            askActor(this.ref(), new ActorMessage.Knock(new RConvId(convId)));
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            if (hasLoggedInUser()) {
                logInWithUser(this.loggedInUser.get());
            }
            askActor(this.ref(), new ActorMessage.Knock(new RConvId(convId)));
        }
    }

    @Override
    public String getId() throws Exception {
        if (!this.id.isPresent()) {
            Object resp;
            try {
                resp = askActor(this.ref(), new ActorMessage.GetDeviceId());
            } catch (TimeoutException e) {
                // recreate process and retry
                respawn();
                if (hasLoggedInUser()) {
                    logInWithUser(this.loggedInUser.get());
                }
                resp = askActor(this.ref(), new ActorMessage.GetDeviceId());
            }
            if (resp instanceof ActorMessage.Successful) {
                id = Optional.of(((ActorMessage.Successful) resp).response());
                return id.get();
            } else {
                throw new RuntimeException(String.format(
                        "Could not get ID of '%s' device. Please check the log file %s for more details.",
                        this.name(), this.hostProcess.getLogPath()));
            }
        } else {
            return this.id.get();
        }
    }

    @Override
    public String getFingerprint() throws Exception {
        if (!this.fingerprint.isPresent()) {
            Object resp;
            try {
                resp = askActor(this.ref(), new ActorMessage.GetDeviceFingerPrint());
            } catch (TimeoutException e) {
                // recreate process and retry
                respawn();
                if (hasLoggedInUser()) {
                    logInWithUser(this.loggedInUser.get());
                }
                resp = askActor(this.ref(), new ActorMessage.GetDeviceFingerPrint());
            }
            if (resp instanceof ActorMessage.Successful) {
                fingerprint = Optional.of(((ActorMessage.Successful) resp).response());
                return fingerprint.get();
            } else {
                throw new RuntimeException(String.format(
                        "Could not get fingerprint of '%s' device. Please check the log file %s for more details.",
                        this.name(), this.hostProcess.getLogPath()));
            }
        } else {
            return this.fingerprint.get();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.coordinatorActorRef != null && this.hostProcess != null) {
            this.ref().tell(PoisonPill.getInstance(), null);
            this.coordinatorActorRef = null;
            this.hostProcess = null;
        }
    }
}
