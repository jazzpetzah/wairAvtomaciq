package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.SpawnRemoteDevice;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

public class Device extends RemoteEntity implements IDevice {

    private ClientUser loggedInUser = null;

    public Device(String deviceName, IRemoteProcess process, FiniteDuration actorTimeout) {
        super(actorTimeout);
        name = deviceName;
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
        FiniteDuration duration = DevicePool.ACTOR_DURATION;

        Future<Object> future = Patterns.ask(ref, new ActorMessage.Login(user.getEmail(), user.getPassword()), duration.toMillis());

//        Object resp = Await.result(future, duration);
//        System.out.println("Response type: " + resp.getClass());
//        if (resp instanceof ActorMessage.Successful$) {
//            System.out.println("Login successful");
//            registeredDevices.put(user.getName(), deviceActor);
//            unregisteredDevices.remove(nextDeviceKey);
//            Thread.sleep(3000); //just to allow the actor time to sign in
//        } else {
//            System.out.println("Login not successful, killing remote");
//        }
        loggedInUser = user;
    }
}
