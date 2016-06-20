package com.wearezeta.auto.common.sync_engine_bridge;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import org.apache.log4j.Logger;

import scala.concurrent.duration.FiniteDuration;
import com.waz.provision.ActorMessage.ReleaseRemotes$;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class UserDevicePool {

    private static final Logger LOG = ZetaLogger.getLog(UserDevicePool.class.getSimpleName());
    private static final FiniteDuration ACTOR_DURATION = new FiniteDuration(60, TimeUnit.SECONDS);
    private static int INITIAL_CACHE_SIZE = 3;
    private static final int MAX_POOL_SIZE = 20;
    private static final int PROCESS_CREATION_TIMEOUT = 90; // seconds

    private Coordinator coordinator;
    private final String backendType;
    private final boolean otrOnly;
    private Map<Future<IRemoteProcess>, Optional<Future<IDevice>>> cachedDevices = new ConcurrentHashMap<>();
    private final Semaphore cachedDevicesGuard = new Semaphore(1);

    static {
        try {
            INITIAL_CACHE_SIZE = CommonUtils.getCachedOtrDevicesCount(UserDevicePool.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserDevicePool(String backendType, boolean otrOnly) {
        this.coordinator = Coordinator.getInstance();
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
            final IDevice device = putDeviceInCache(deviceName);
            device.logInWithUser(user);
            return device;
        } catch (Exception e) {
            Throwables.propagate(e);
            return null;
        }
    }

    public List<IDevice> getDevices(ClientUser user) throws Exception {
        final List<IDevice> result = new ArrayList<>();
        for (Map.Entry<Future<IRemoteProcess>, Optional<Future<IDevice>>> entry : cachedDevices.entrySet()) {
            if (entry.getValue().isPresent()) {
                final IDevice device = entry.getValue().get().get();
                if (device.isLoggedInUser(user)) {
                    result.add(device);
                }
            }
        }
        return result;
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
        if (this.coordinator.getActorRef() != null) {
            LOG.info("Shutting down device pool...");
            this.cachedDevices = null;
            this.coordinator.getActorRef().tell(ReleaseRemotes$.MODULE$, null);
            this.coordinator = null;
        } else {
            LOG.error("Trying to shut down the device pool for the second time! Skipping...");
        }
    }

    public void releaseDevices(Collection<IDevice> devices) throws Exception {
        for (IDevice device : devices) {
            releaseDevice(device);
        }
    }

    public void releaseDevice(IDevice device) throws Exception {
        cachedDevicesGuard.acquire();
        try {
            for (Future<IRemoteProcess> p : cachedDevices.keySet()) {
                if (cachedDevices.get(p).isPresent() && cachedDevices.get(p).get().get().getId().equals(device.getId())) {
                    try {
                        LOG.info(String.format("Releasing device %s of user %s", device.getId(), device.getLoggedInUser().
                                orElse(new ClientUser(null, null, null, "NONE")).getName()));
                        cachedDevices.get(p).get().get().destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        LOG.info("Detaching process from device");
                        cachedDevices.put(p, Optional.empty());
                    }
                }
            }
        } finally {
            cachedDevicesGuard.release();
        }
    }

    public void reset() throws Exception {
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
                    final ExecutorService innerPool = Executors.newSingleThreadExecutor();
                    final Future<IRemoteProcess> p = innerPool.submit(()
                            -> createNewProcess());
                    innerPool.shutdown();
                    cachedDevices.put(p, Optional.empty());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    private IDevice putDeviceInCache(String deviceName) throws Exception {
        Future<IRemoteProcess> targetProcess = null;
        Future<IDevice> targetDevice = null;
        cachedDevicesGuard.acquire();
        try {
            // Look for entry without device in cache
            for (Future<IRemoteProcess> p : cachedDevices.keySet()) {
                if (!cachedDevices.get(p).isPresent()) {
                    targetProcess = p;
                    break;
                }
            }
            // All entries are busy, let's create a new one
            if (targetProcess == null) {
                if (cachedDevices.size() < MAX_POOL_SIZE) {
                    final ExecutorService pool = Executors.newSingleThreadExecutor();
                    targetProcess = pool.submit(() -> createNewProcess());
                    pool.shutdown();
                } else {
                    throw new IllegalStateException(String.format(
                            "Cannot create more than %s devices. Make sure you've reset SE Bridge after the previous test",
                            MAX_POOL_SIZE));
                }
            }
            final Future<IRemoteProcess> keyProcess = targetProcess;
            final ExecutorService pool = Executors.newSingleThreadExecutor();
            targetDevice = pool.submit(()
                    -> createNewDevice(keyProcess.get(PROCESS_CREATION_TIMEOUT, TimeUnit.SECONDS), deviceName));
            pool.shutdown();
            cachedDevices.put(targetProcess, Optional.of(targetDevice));
        } finally {
            cachedDevicesGuard.release();
        }
        return targetDevice.get();
    }

    private IRemoteProcess createNewProcess() {
        return new RemoteProcess(CommonUtils.generateGUID().substring(0, 8), this.coordinator.getActorRef(), this.backendType,
                this.otrOnly);
    }

    private IDevice createNewDevice(IRemoteProcess process, String name) {
        return new Device(process, name, this.coordinator.getActorRef(), ACTOR_DURATION);
    }
}
