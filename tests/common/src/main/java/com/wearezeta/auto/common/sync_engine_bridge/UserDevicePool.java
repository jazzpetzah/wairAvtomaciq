package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
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

public class UserDevicePool {

    private ActorRef coordinatorActorRef;
    private static final FiniteDuration ACTOR_DURATION = new FiniteDuration(60, TimeUnit.SECONDS);
    private static final Logger LOG = ZetaLogger.getLog(UserDevicePool.class.getSimpleName());
    private String backendType;
    private boolean otrOnly;

    private static int INITIAL_CACHE_SIZE = 3;

    static {
        try {
            INITIAL_CACHE_SIZE = CommonUtils.getCachedOtrDevicesCount(UserDevicePool.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int MAX_POOL_SIZE = 20;

    private static final int PROCESS_CREATION_TIMEOUT = 90; // seconds

    private Map<Future<IRemoteProcess>, Optional<Future<IDevice>>> cachedDevices = new ConcurrentHashMap<>();
    private Semaphore cachedDevicesGuard = new Semaphore(1);

    private void prefillCache() {
        int threadsCount = 2;
        final int cpuCount = Runtime.getRuntime().availableProcessors();
        if (cpuCount > 3) {
            threadsCount = cpuCount - 1;
        }
        final ExecutorService pool = Executors.newFixedThreadPool(threadsCount);
        for (int i = 0; i < INITIAL_CACHE_SIZE; i++) {
            pool.submit(() -> {
                try {
                    final Future<IRemoteProcess> p = Executors.newSingleThreadExecutor().submit(() ->
                            new RemoteProcess(CommonUtils.generateGUID().substring(0, 8),
                                    this.coordinatorActorRef, this.backendType, this.otrOnly));
                    cachedDevices.put(p, Optional.empty());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    private void resetCache() throws Exception {
        cachedDevicesGuard.acquire();
        try {
            for (Future<IRemoteProcess> p : cachedDevices.keySet()) {
                if (cachedDevices.get(p).isPresent()) {
                    try {
                        cachedDevices.get(p).get().get().destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        cachedDevices.put(p, Optional.empty());
                    }
                }
            }
        } finally {
            cachedDevicesGuard.release();
        }
    }

    private IDevice putDeviceInCache(ClientUser owner, String deviceName) throws Exception {
        Future<IRemoteProcess> targetProcess = null;
        Future<IDevice> targetDevice = null;
        cachedDevicesGuard.acquire();
        try {
            // Look for free entry in cache
            for (Future<IRemoteProcess> p : cachedDevices.keySet()) {
                if (!cachedDevices.get(p).isPresent()) {
                    targetProcess = p;
                    break;
                }
            }
            // All entries are busy, let's create a new one
            if (targetProcess == null) {
                if (cachedDevices.size() < MAX_POOL_SIZE) {
                    targetProcess = Executors.newSingleThreadExecutor().submit(() ->
                            new RemoteProcess(CommonUtils.generateGUID().substring(0, 8),
                                    this.coordinatorActorRef, this.backendType, this.otrOnly));
                } else {
                    throw new IllegalStateException(String.format(
                            "Cannot create more than %s devices. Make sure you've reset SE Bridge after the previous test",
                            MAX_POOL_SIZE));
                }
            }
            final Future<IRemoteProcess> keyProcess = targetProcess;
            targetDevice = Executors.newSingleThreadExecutor().submit(() ->
                    new Device(keyProcess.get(PROCESS_CREATION_TIMEOUT, TimeUnit.SECONDS),
                            deviceName, this.coordinatorActorRef, ACTOR_DURATION));
            cachedDevices.put(targetProcess, Optional.of(targetDevice));
        } finally {
            cachedDevicesGuard.release();
        }

        final IDevice result = targetDevice.get();
        result.logInWithUser(owner);
        return result;
    }

    private List<IDevice> selectUserDevices(ClientUser forUser) throws Exception {
        final List<IDevice> result = new ArrayList<>();
        for (Map.Entry<Future<IRemoteProcess>, Optional<Future<IDevice>>> entry : cachedDevices.entrySet()) {
            if (entry.getValue().isPresent()) {
                final IDevice device = entry.getValue().get().get();
                if (device.isLoggedInUser(forUser)) {
                    result.add(device);
                }
            }
        }
        return result;
    }

    public UserDevicePool(String backendType, boolean otrOnly) {
        final Config config = ConfigFactory.load("actor_coordinator");
        final ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        this.coordinatorActorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        this.backendType = backendType;
        this.otrOnly = otrOnly;
        prefillCache();
    }

    public IDevice addDevice(ClientUser user) {
        return addDevice(user, "Device_" + CommonUtils.generateGUID().substring(0, 8));
    }

    public IDevice addDevice(ClientUser user, String deviceName) {
        LOG.info("Add new device for user " + user.getName() + " with device name " + deviceName);
        try {
            return putDeviceInCache(user, deviceName);
        } catch (Exception e) {
            Throwables.propagate(e);
            return null;
        }
    }

    public List<IDevice> getDevices(ClientUser user) throws Exception {
        return selectUserDevices(user);
    }

    public Optional<IDevice> getDevice(ClientUser user, String deviceName) throws Exception {
        final List<IDevice> userDevices = getDevices(user);
        for (IDevice device : userDevices) {
            if (device.name().equals(deviceName)) {
                LOG.info("Found device " + deviceName + " in device pool!");
                return Optional.of(device);
            }
        }
        return Optional.empty();
    }

    public IDevice getOrAddRandomDevice(ClientUser user) throws Exception {
        final List<IDevice> allUserDevices = getDevices(user);
        if (allUserDevices.isEmpty()) {
            return addDevice(user);
        } else {
            // chosen by fair dice roll: https://xkcd.com/221/
            return allUserDevices.get(0);
        }
    }

    public IDevice getOrAddDevice(ClientUser user, String deviceName) throws Exception {
        return getDevice(user, deviceName).orElseGet(() -> addDevice(user, deviceName));
    }

    public synchronized void shutdown() {
        if (this.coordinatorActorRef != null) {
            LOG.info("Shutting down device pool...");
            this.cachedDevices = null;
            coordinatorActorRef.tell(ReleaseRemotes$.MODULE$, null);
            this.coordinatorActorRef = null;
        } else {
            LOG.error("Trying to shut down the device pool for the second time! Skipping...");
        }
    }

    public void reset() throws Exception {
        resetCache();
    }
}
