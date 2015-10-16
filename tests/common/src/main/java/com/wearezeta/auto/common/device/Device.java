package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.SpawnRemoteDevice;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

public class Device {

    private String deviceName;

    private ActorRef deviceActorRef;

    private ClientUser loggedInUser = null;

    public Device(String deviceName, RemoteProcess process) {
        this.deviceName = deviceName;
        spawnDevice(process);
    }

    private void spawnDevice(RemoteProcess process) {
        ActorRef processActorRef = process.ref();
        Object resp = DevicePool.askActor(processActorRef, new SpawnRemoteDevice(null, deviceName));

        if (resp instanceof ActorRef) {
            deviceActorRef = (ActorRef) resp;
        } else {
            //TODO come up with a better exception?
            throw new IllegalStateException("There was an error establishing a connection with a new device: " + deviceName +
                    " on process: " + process.name() + ". The response was: " + resp);
        }
    }

    public ActorRef ref() {
        //TODO quick check to make sure the ref is still working
        return deviceActorRef;
    }

    public boolean hasLoggedInUser() {
        return loggedInUser != null;
    }

    public boolean isLoggedInUser(ClientUser user) {
        return loggedInUser == user;
    }

    public void logInWithUser(ClientUser user) {
        FiniteDuration duration = DevicePool.ACTOR_DURATION;
        loggedInUser = user;
        //TODO the actual login stuff

        Future<Object> future = Patterns.ask(deviceActorRef, new ActorMessage.Login(user.getEmail(), user.getPassword()), duration.toMillis());

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
    }
}
