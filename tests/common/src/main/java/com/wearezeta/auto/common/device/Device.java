package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage.Login;
import com.waz.provision.ActorMessage.SendText;
import com.waz.provision.ActorMessage.SpawnRemoteDevice;
import com.waz.provision.ActorMessage.Successful$;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.duration.FiniteDuration;

public class Device extends RemoteEntity implements IDevice {

    private ClientUser loggedInUser = null;

    public Device(String deviceName, IRemoteProcess process, FiniteDuration actorTimeout) {
        super(actorTimeout);
        name = deviceName;
        //TODO check that this doesn't become a bottleneck with lots of Devices...
        if (!process.isConnected()) {
            throw new IllegalStateException("The process used to spawn this device has no established connection");
        }
        spawnDevice(process);
    }

    private void spawnDevice(IRemoteProcess process) {
        //TODO check that process is established
        ActorRef processActorRef = process.ref();
        Object resp = askActor(processActorRef, new SpawnRemoteDevice(null, name));

        if (resp instanceof ActorRef) {
            ref = (ActorRef) resp;
        } else {
            //TODO come up with a better exception?
            throw new IllegalStateException("There was an error establishing a connection with a new device: " + name +
                    " on process: " + process.name() + ". The response was: " + resp);
        }
    }

    public boolean hasLoggedInUser() {
        return loggedInUser != null;
    }

    public boolean isLoggedInUser(ClientUser user) {
        return loggedInUser == user;
    }

    public void logInWithUser(ClientUser user) {
        if (user == null) {
            throw new NullPointerException("Client user cannot be null");
        }

        Object resp = askActor(ref, new Login(user.getEmail(), user.getPassword()));

        if (resp instanceof Successful$) {
            System.out.println("Login to: " + name + " successful");
            loggedInUser = user;
        } else {
            //TODO some sort of error handling here.
        }

    }

    @Override
    public void sendMessage(String convId, String message) {
        tellActor(ref, new SendText(new RConvId(convId), message));
    }

}
