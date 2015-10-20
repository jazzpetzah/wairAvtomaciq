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
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DevicePool implements IDevicePool {

    //TODO make configurable
    public static final FiniteDuration ACTOR_DURATION = new FiniteDuration(30000, TimeUnit.MILLISECONDS);

    private final int THREAD_LIMIT = 500;
    private ExecutorService executorService;
    private CompletionService<Device> completionService;

    private ActorRef coordinatorActorRef;

    private final String PROCESS_PREFIX = "Remote_process_";
    private final String DEVICE_PREFIX = "Remote_device_";

    //TODO make configurable, actually, is it even a good idea?
    private final int NUM_PROCESSES = 1;
    //TODO have a MAX_DEVICES limit
    private final int NUM_DEVICES;

    //TODO that caching stuff nick was talking about
    private List<RemoteProcess> processCache;

    private List<Future<Device>> spawningDevices;
    private List<Device> readyDevices;

    public DevicePool(int numDevices) {
        if (numDevices < 1) {
            throw new IllegalArgumentException("There should be at least one device created in the DevicePool");
        }

        //TODO consider having a default number of remotes for every test?
        NUM_DEVICES = numDevices;

        processCache = new ArrayList<>(NUM_PROCESSES);

        coordinatorActorRef = setUpCoordinatorActor();

        executorService = Executors.newFixedThreadPool(THREAD_LIMIT);
        completionService = new ExecutorCompletionService<>(executorService);
    }

    public void init() {
        processCache = spawnProcesses();
        spawningDevices = startSpawningDevices();
        readyDevices = collectReadyDevices();
    }

    private ActorRef setUpCoordinatorActor() {
        Config config = ConfigFactory.load("actor_coordinator");
        ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        ActorRef actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        return actorRef;
    }

    private List<RemoteProcess> spawnProcesses() {
        List<RemoteProcess> processes = new ArrayList<>();
        for (int i = 0; i < NUM_PROCESSES; i++) {
            String processName = PROCESS_PREFIX + i;
            processes.add(new RemoteProcess(processName, coordinatorActorRef, ACTOR_DURATION));
        }
        return processes;
    }

    /**
     * Devices are spread evenly among available processes.
     */
    private List<Future<Device>> startSpawningDevices() {
        List<Future<Device>> spawningDevices = new ArrayList<>();
        for (int i = 0; i < NUM_DEVICES; i++) {
            final int index = i;
            spawningDevices.add(completionService.submit(() -> {
                System.out.println("Creating device in executor");
                int processIndex = index % NUM_PROCESSES;
                RemoteProcess parentProcess = processCache.get(processIndex);
                String deviceName = DEVICE_PREFIX + index;
                return new Device(deviceName, parentProcess, ACTOR_DURATION);
            }));
        }
        return spawningDevices;
    }

    private List<Device> collectReadyDevices() {
        System.out.println("getAllDevices");
        List<Device> readyDevices = new ArrayList<>();

        while (readyDevices.size() < NUM_DEVICES) {
            try {
                Future<Device> deviceFuture = completionService.take();
                Device readyDevice = deviceFuture.get();
                spawningDevices.remove(deviceFuture);
                readyDevices.add(readyDevice);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                break;
            }
        }

        return readyDevices;
    }

    @Override
    public Boolean hasFreeDevices() {
        return getFreeDevices().size() > 0;
    }

    public Device getNextFreeDevice() {
        System.out.println("getNextFreeDevice");
        return getFreeDevices().get(0);
    }

    public List<Device> getDevicesWithLoggedInUser(ClientUser user) {
        List<Device> freeDevices = getFreeDevices();
        return freeDevices.stream().filter(device -> device.isLoggedInUser(user)).collect(Collectors.toList());
    }

    /**
     * @return all devices that do not have a logged in user
     */
    protected List<Device> getFreeDevices() {
        System.out.println("getFreeDevices");
        return readyDevices.stream().filter(device -> !device.hasLoggedInUser()).collect(Collectors.toList());
    }

    public void killAllDevices() {
        coordinatorActorRef.tell(TerminateRemotes$.MODULE$, null);
        processCache = null;
    }

}
