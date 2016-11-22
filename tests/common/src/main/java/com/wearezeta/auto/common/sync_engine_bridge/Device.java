package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;

import akka.actor.PoisonPill;
import com.waz.api.EphemeralExpiration;
import com.waz.model.MessageId;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import org.apache.commons.lang3.StringUtils;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import static akka.pattern.Patterns.gracefulStop;

class Device extends RemoteEntity implements IDevice {

    private Optional<ClientUser> loggedInUser = Optional.empty();
    private Optional<String> id = Optional.empty();
    private Optional<String> fingerprint = Optional.empty();
    private IRemoteProcess hostProcess = null;
    private final ActorRef coordinatorActorRef;

    public Device(IRemoteProcess hostProcess, String deviceName, ActorRef coordinatorActorRef,
                  FiniteDuration actorTimeout) {
        super(deviceName, actorTimeout);
        this.hostProcess = hostProcess;
        this.coordinatorActorRef = coordinatorActorRef;
        respawn();
    }

    private <T> T retryOnTimeoutFailure(FunctionalInterfaces.ISupplierWithException<T> r) throws Exception {
        try {
            return r.call();
        } catch (TimeoutException e) {
            // recreate process and retry
            respawn();
            if (hasLoggedInUser()) {
                logInWithUser(this.loggedInUser.get());
            }
            return r.call();
        }
    }

    private boolean spawnOnHostProcess() {
        try {
            final Object resp = askActor(this.hostProcess.ref(), new ActorMessage.SpawnRemoteDevice(null, this.name()));
            if (resp instanceof ActorRef) {
                ActorRef deviceRef = (ActorRef) resp;
                this.setRef(deviceRef);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void respawn() {
        if (this.ref() != null) {
            try {
                Future<Boolean> stopped = gracefulStop(this.ref(), Duration.create(5, TimeUnit.SECONDS),
                        PoisonPill.getInstance());
                Await.result(stopped, Duration.create(6, TimeUnit.SECONDS));
                // the actor has been stopped
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.setRef(null);
        }

        if (!spawnOnHostProcess()) {
            try {
                this.hostProcess.restart();
                if (spawnOnHostProcess()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new IllegalStateException(String.format(
                    "There was an error establishing a connection with a new device: "
                            + "%s on process: %s. Please check the log file %s for more details.",
                    this.name(), this.hostProcess.name(), this.hostProcess.getLogPath()));
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
    public Optional<ClientUser> getLoggedInUser() {
        return this.loggedInUser;
    }

    @Override
    public void logInWithUser(ClientUser user) throws Exception {
        final Object resp = retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.Login(user.getEmail(), user.getPassword()))
        );
        if (resp instanceof ActorMessage.Successful$) {
            this.loggedInUser = Optional.of(user);
            // Wait until prekeys are generated asynchronously for this client
            Thread.sleep(2000);
        } else {
            throw new IllegalStateException(String.format(
                    "User '%s' has failed to log in into device '%s'. Please check the log file %s for more details.",
                    user.getName(), this.name(), this.hostProcess.getLogPath()));
        }
    }

    @Override
    public void setLabel(String label) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SetDeviceLabel(label))
        );
    }

    @Override
    public void sendMessage(String convId, String message) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SendText(new RConvId(convId), message))
        );
    }

    @Override
    public void sendImage(String convId, String path) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SendImage(new RConvId(convId), path))
        );
    }

    @Override
    public void sendGiphy(String convId, String searchQuery) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SendGiphy(new RConvId(convId), searchQuery))
        );
    }

    @Override
    public void sendPing(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.Knock(new RConvId(convId)))
        );
    }

    @Override
    public void typing(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.Typing(new RConvId(convId)))
        );
    }

    @Override
    public void clearConversation(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.ClearConversation(new RConvId(convId)))
        );
    }

    @Override
    public void muteConversation(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.MuteConv(new RConvId(convId)))
        );
    }

    @Override
    public void unmuteConversation(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.UnmuteConv(new RConvId(convId)))
        );
    }

    @Override
    public void archiveConversation(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.ArchiveConv(new RConvId(convId)))
        );
    }

    @Override
    public void unarchiveConversation(String convId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.UnarchiveConv(new RConvId(convId)))
        );
    }

    @Override
    public void sendFile(String convId, String path, String mime) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SendFile(new RConvId(convId), path, mime))
        );
    }

    @Override
    public void deleteMessage(String convId, MessageId messageId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.DeleteMessage(new RConvId(convId), messageId))
        );
    }

    @Override
    public void deleteMessageEveryWhere(String convId, MessageId messageId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.RecallMessage(new RConvId(convId), messageId))
        );
    }

    @Override
    public void updateMessage(MessageId messageId, String newMessage) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.UpdateText(messageId, newMessage))
        );
    }

    @Override
    public ActorMessage.MessageInfo[] getConversationMessages(String convId) throws Exception {
        try {
            final ActorMessage.ConvMessages convMessages = retryOnTimeoutFailure(
                    () -> (ActorMessage.ConvMessages) askActor(this.ref(),
                            new ActorMessage.GetMessages(new RConvId(convId)))
            );
            return convMessages.msgs();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return new ActorMessage.MessageInfo[]{};
    }

    @Override
    public void reactMessage(String convId, MessageId messageId, MessageReactionType action) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(),
                        new ActorMessage.SetMessageReaction(new RConvId(convId), messageId, action.getAction()))
        );
    }

    @Override
    public void shareLocation(String convId, float lon, float lat, String address, int zoom) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SendLocation(new RConvId(convId), lon, lat, address,
                        zoom))
        );
    }

    @Override
    public void shareLocation(String convId) throws Exception {
        shareLocation(convId, Constants.DEFAULT_GMAP_LON, Constants.DEFAULT_GMAP_LAT, Constants.DEFAULT_GMAP_ADDRESS,
                Constants.DEFAULT_GMAP_ZOOM_LEVEL);
    }

    @Override
    public void setEphemeralMode(String convId, long expirationMilliseconds) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.SetEphemeral(new RConvId(convId),
                        EphemeralExpiration.getForMillis(expirationMilliseconds)))
        );
    }

    @Override
    public void markEphemeralRead(String convId, MessageId messageId) throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), new ActorMessage.MarkEphemeralRead(new RConvId(convId), messageId))
        );
    }

    @Override
    public String getId() throws Exception {
        if (!this.id.isPresent()) {
            final Object resp = retryOnTimeoutFailure(
                    () -> askActor(this.ref(), new ActorMessage.GetDeviceId())
            );
            if (resp instanceof ActorMessage.Successful) {
                // FIXME: This padding should happen on SE side. Please remove this workaround when it's fixed
                id = Optional.of(
                        StringUtils.leftPad(((ActorMessage.Successful) resp).response(), 16, "0")
                );
                return id.get();
            }
            throw new IllegalStateException(String.format(
                    "Could not get ID of '%s' device. Please check the log file %s for more details.",
                    this.name(), this.hostProcess.getLogPath()));
        } else {
            return this.id.get();
        }
    }

    @Override
    public String getFingerprint() throws Exception {
        if (!this.fingerprint.isPresent()) {
            final Object resp = retryOnTimeoutFailure(
                    () -> askActor(this.ref(), new ActorMessage.GetDeviceFingerPrint())
            );
            if (resp instanceof ActorMessage.Successful) {
                fingerprint = Optional.of(((ActorMessage.Successful) resp).response());
                return fingerprint.get();
            }
            throw new IllegalStateException(String.format(
                    "Could not get fingerprint of '%s' device. Please check the log file %s for more details.",
                    this.name(), this.hostProcess.getLogPath()));
        } else {
            return this.fingerprint.get();
        }
    }

    @Override
    public void destroy() {
        if (this.coordinatorActorRef != null && this.hostProcess != null) {
            this.ref().tell(PoisonPill.getInstance(), null);
            this.hostProcess = null;
        }
    }

    @Override
    public void setAssetToV3() throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), ActorMessage.SetAssetToV3$.MODULE$)
        );
    }

    @Override
    public void setAssetToV2() throws Exception {
        retryOnTimeoutFailure(
                () -> askActor(this.ref(), ActorMessage.SetAssetToV2$.MODULE$)
        );
    }
}
