package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.TerminateRemotes$;
import com.waz.provision.CoordinatorActor;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DevicePool {

    public static final FiniteDuration ACTOR_DURATION = new FiniteDuration(30000, TimeUnit.MILLISECONDS);

    private ActorRef coordinatorActorRef;

    private final String PROCESS_PREFIX = "Remote_process_";
    private final String DEVICE_PREFIX = "Remote_device_";

    private final int NUM_PROCESSES = 1;
    //TODO have a MAX_DEVICES limit
    private final int NUM_DEVICES;

    //TODO that caching stuff nick was talking about
    private List<RemoteProcess> processCache;
    private List<Device> deviceCache;

    public DevicePool(int numDevices) {
        //TODO consider having a default number of remotes for every test?
        NUM_DEVICES = numDevices;

        //TODO think of a better way to keep track of all these devices...
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
            processCache.add(new RemoteProcess(processName, coordinatorActorRef));
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
            deviceCache.add(new Device(deviceName, parentProcess));
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
        return deviceCache.stream().filter(device -> device.isLoggedInUser(user)).collect(Collectors.toList());
    }

    /**
     * @return all devices that do not have a logged in user
     */
    private List<Device> getFreeDevices() {
        //Some fancy shit IntelliJ did for me :)
        return deviceCache.stream().filter(device -> !device.hasLoggedInUser()).collect(Collectors.toList());
    }

    public void killAllDevices() {
        coordinatorActorRef.tell(TerminateRemotes$.MODULE$, null);
        deviceCache = null;
        processCache = null;
    }

    //TODO think of a tidier way to refactor this out.
    public static Object askActor(ActorRef actorRef, ActorMessage message) {
        Future<Object> future = Patterns.ask(actorRef, message, ACTOR_DURATION.toMillis());
        Object resp = null;
        try {
            resp = Await.result(future, ACTOR_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
    }


}
