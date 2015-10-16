package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.ActorMessage.TerminateRemotes$;
import com.waz.provision.CoordinatorActor;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DevicePool implements IDevicePool {

    //TODO make configurable
    public static final FiniteDuration ACTOR_DURATION = new FiniteDuration(30000, TimeUnit.MILLISECONDS);

    private ActorRef coordinatorActorRef;

    private final String PROCESS_PREFIX = "Remote_process_";
    private final String DEVICE_PREFIX = "Remote_device_";

    //TODO make configurable
    private final int NUM_PROCESSES = 1;
    //TODO have a MAX_DEVICES limit
    private final int NUM_DEVICES;

    //TODO that caching stuff nick was talking about
    private List<RemoteProcess> processCache;
    private List<Device> deviceCache;

    public DevicePool(int numDevices) {
        if (numDevices < 1) {
            throw new IllegalArgumentException("There should be at least one device created in the DevicePool");
        }

        //TODO consider having a default number of remotes for every test?
        NUM_DEVICES = numDevices;

        processCache = new ArrayList<>(NUM_PROCESSES);
        deviceCache = new ArrayList<>(NUM_DEVICES);

        coordinatorActorRef = setUpCoordinatorActor();

        spawnProcesses();
        spawnDevices();
    }

    private ActorRef setUpCoordinatorActor() {
        Config config = ConfigFactory.load("actor_coordinator");
        ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        ActorRef actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        return actorRef;
    }

    private void spawnProcesses() {
        for (int i = 0; i < NUM_PROCESSES; i++) {
            String processName = PROCESS_PREFIX + i;
            processCache.add(new RemoteProcess(processName, coordinatorActorRef, ACTOR_DURATION));
        }
    }

    /**
     * Devices are spread evenly among available processes.
     */
    private void spawnDevices() {
        for (int i = 0; i < NUM_DEVICES; i++) {
            int processIndex = i % NUM_PROCESSES;
            RemoteProcess parentProcess = processCache.get(processIndex);
            String deviceName = DEVICE_PREFIX + i;
            deviceCache.add(new Device(deviceName, parentProcess, ACTOR_DURATION));
        }
    }

    public Boolean hasFreeDevices() {
        return getFreeDevices().size() > 0;
    }

    public Device getNextFreeDevice() {
        //TODO will need to do some sync shit to make sure all the devices are ready at this point in time...
        if (!hasFreeDevices()) {
            throw new RuntimeException("There are no free Devices left");
        }
        return getFreeDevices().get(0);
    }

    public List<Device> getDevicesWithLoggedInUser(ClientUser user) {
        if (deviceCache == null) {
            throw new IllegalStateException("The device pool has been closed down and cannot be used any more.");
        }
        return deviceCache.stream().filter(device -> device.isLoggedInUser(user)).collect(Collectors.toList());
    }

    /**
     * @return all devices that do not have a logged in user
     */
    private List<Device> getFreeDevices() {
        if (deviceCache == null) {
            throw new IllegalStateException("The device pool has been closed down and cannot be used any more.");
        }
        //Some fancy shit IntelliJ did for me :)
        return deviceCache.stream().filter(device -> !device.hasLoggedInUser()).collect(Collectors.toList());
    }

    public void killAllDevices() {
        coordinatorActorRef.tell(TerminateRemotes$.MODULE$, null);
        deviceCache = null;
        processCache = null;
    }

}
