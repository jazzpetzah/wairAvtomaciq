package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.CoordinatorActor;
import com.waz.provision.ActorMessage.ReleaseRemotes$;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.List;

public class UserDevicePool {

    private ActorRef coordinatorActorRef;
    private RemoteProcess hostProcess;
    private static final int MAX_DEVICES = 1100;
    private volatile int deviceCount = 0;
    private Map<ClientUser, CopyOnWriteArrayList<IDevice>> userDevices = new ConcurrentHashMap<>();
    private static final FiniteDuration ACTOR_DURATION = new FiniteDuration(30000, TimeUnit.MILLISECONDS);
    private static final Logger LOG = ZetaLogger.getLog(UserDevicePool.class.getSimpleName());

    public UserDevicePool(String backendType, boolean otrOnly) {
        final Config config = ConfigFactory.load("actor_coordinator");
        final ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        this.coordinatorActorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        this.hostProcess = new RemoteProcess(UUID.randomUUID().toString()
                .substring(0, 8), this.coordinatorActorRef, ACTOR_DURATION, backendType, otrOnly);
    }

    public synchronized IDevice addDevice(ClientUser user) {
        return addDevice(user, "Device_" + System.currentTimeMillis());
    }

    public synchronized IDevice addDevice(ClientUser user, String deviceName) {
        LOG.info("Add new device for user " + user.getName() + " with device name " + deviceName);
        if (deviceCount >= MAX_DEVICES) {
            throw new IllegalStateException(String.format(
                    "Cannot create more than %s devices per one process instance", MAX_DEVICES));
        }
        CopyOnWriteArrayList<IDevice> devices = userDevices.get(user);
        if (devices == null) {
            devices = new CopyOnWriteArrayList<>();
        }
        final Device result = new Device(deviceName, this.hostProcess, ACTOR_DURATION);
        devices.add(result);
        deviceCount++;
        userDevices.put(user, devices);
        return result;
    }

    public List<IDevice> getDevices(ClientUser user) {
        if (!userDevices.containsKey(user)) {
            throw new IllegalArgumentException(String.format(
                    "Could not find device list for user %s", user.getName()));
        }
        return userDevices.get(user);
    }

    public IDevice getDevice(ClientUser user, String deviceName) {
        if (userDevices.containsKey(user)) {
            for (IDevice device : userDevices.get(user)) {
                if (deviceName.equals(device.name())) {
                    return device;
                }
            }
            throw new IllegalArgumentException(String.format(
                    "Could not find device %s for user %s", deviceName,
                    user.getName()));
        }
        throw new IllegalArgumentException(String.format(
                "Could not find device list for user %s", user.getName()));
    }

    public IDevice getRandomDevice(ClientUser user) {
        if (userDevices.containsKey(user)) {
            // chosen by fair dice roll: https://xkcd.com/221/
            return userDevices.get(user).get(0);
        }
        throw new IllegalArgumentException(String.format(
                "Could not find device list for user %s", user.getName()));
    }

    public IDevice getOrAddRandomDevice(ClientUser user) {
        if (userDevices.containsKey(user)) {
            // chosen by fair dice roll: https://xkcd.com/221/
            return userDevices.get(user).get(0);
        }
        return addDevice(user);
    }

    public synchronized void shutdown() {
        if (this.userDevices != null && this.coordinatorActorRef != null && this.hostProcess != null) {
            LOG.info(String.format("Shutting down device pool @ process named '%s'...", this.hostProcess.name()));
            coordinatorActorRef.tell(ReleaseRemotes$.MODULE$, null);
            this.coordinatorActorRef = null;
            this.userDevices = null;
            this.hostProcess = null;
            this.deviceCount = 0;
        } else {
            LOG.error("Trying to shut down the device pool for the second time! Skipping...");
        }
    }

    public int size() {
        return deviceCount;
    }
}
