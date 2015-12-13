package com.wearezeta.auto.common.sync_engine_bridge;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SEBridge {
    private static final Future<DevicePool> devicePool;
    private static final ExecutorService pool = Executors.newFixedThreadPool(1);

	static {
		devicePool = pool.submit(() -> new DevicePool(CommonUtils
				.getBackendType(CommonUtils.class)));
	}

    private static SEBridge instance = null;
    private static final int POOL_CREATION_TIMEOUT = 60; // seconds

    public static synchronized SEBridge getInstance() {
        if (instance == null) {
            instance = new SEBridge();
        }
        return instance;
    }

    private SEBridge() {
    }

    private synchronized static void shutdown() {
        if (!wasPoolAccessed) {
            return;
        }
        if (devicePool != null) {
            if (devicePool.isDone()) {
                try {
                    devicePool.get().shutdown();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                devicePool.cancel(true);
            }
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    private static boolean wasPoolAccessed = false;

    private DevicePool getDevicePool() throws Exception {
        wasPoolAccessed = true;
        return devicePool.get(POOL_CREATION_TIMEOUT, TimeUnit.SECONDS);
    }

    private Map<ClientUser, List<IDevice>> usersMapping = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private void login(ClientUser user, IDevice dstDevice) throws Exception {
        if (dstDevice.hasLoggedInUser() && !dstDevice.isLoggedInUser(user)) {
            this.logout(user, dstDevice);
        }
        if (!dstDevice.isLoggedInUser(user)) {
            dstDevice.logInWithUser(user);
        }
        if (this.usersMapping.containsKey(user)) {
            final List<IDevice> mappedDevices = this.usersMapping.get(user);
            mappedDevices.add(dstDevice);
        } else {
            this.usersMapping.put(user,
                    Arrays.asList(new IDevice[]{dstDevice}));
        }
    }

    private void logout(ClientUser user, IDevice dstDevice) throws Exception {
        if (dstDevice.hasLoggedInUser()) {
            dstDevice.logout();
        }
        final List<IDevice> mappedDevices = this.usersMapping.get(user);
        if (mappedDevices != null && mappedDevices.contains(dstDevice)) {
            mappedDevices.remove(dstDevice);
        }
    }

    private IDevice getCachedDevice(ClientUser userFrom) throws Exception {
        IDevice dstDevice;
        if (this.usersMapping.containsKey(userFrom)) {
            // We don't care about multiple devices yet. Just take the first
            // available one
            dstDevice = this.usersMapping.get(userFrom).get(0);
        } else {
            dstDevice = this.getDevicePool().addDevice();
            this.login(userFrom, dstDevice);
        }
        return dstDevice;
    }

    private static void verifyPathExists(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format(
                    "The file %s is not accessible", path));
        }
    }

    public void sendConversationMessage(ClientUser userFrom, String convId,
                                        String message) throws Exception {
        getCachedDevice(userFrom).sendMessage(convId, message);
    }

	public void addRemoteDeviceToAccount(ClientUser user) throws Exception {
		getCachedDevice(user);
	}

    public void sendImage(ClientUser userFrom, String convId, String path)
            throws Exception {
        verifyPathExists(path);
        getCachedDevice(userFrom).sendImage(convId, path);
    }

    public synchronized void reset() throws Exception {
        if (!this.usersMapping.isEmpty()) {
            this.usersMapping.clear();
            this.getDevicePool().clear();
        }
    }

}