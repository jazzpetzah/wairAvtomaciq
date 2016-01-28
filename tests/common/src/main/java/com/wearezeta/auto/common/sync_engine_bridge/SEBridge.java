package com.wearezeta.auto.common.sync_engine_bridge;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SEBridge {
    private static final long DEVICE_POOL_INIT_TIMEOUT_SECONDS = 60;
    private Future<UserDevicePool> devicePool;
    private static SEBridge instance = null;

    private static final Logger LOG = ZetaLogger.getLog(SEBridge.class.getSimpleName());

    private SEBridge() throws Exception {
        final Callable<UserDevicePool> task = () -> new UserDevicePool(CommonUtils.getBackendType(CommonUtils.class),
                CommonUtils.getOtrOnly(CommonUtils.class));
        this.devicePool = Executors.newSingleThreadExecutor().submit(task);
    }

    public static synchronized SEBridge getInstance() {
        if (instance == null) {
            try {
                instance = new SEBridge();
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        }
        return instance;
    }

    private synchronized UserDevicePool getDevicePool() throws Exception {
        return this.devicePool.get(DEVICE_POOL_INIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private void login(ClientUser user, IDevice dstDevice) throws Exception {
        if (dstDevice.hasLoggedInUser() && !dstDevice.isLoggedInUser(user)) {
            LOG.info("Logout as user " + user.getName() + " with device "
                    + dstDevice.name());
            this.logout(dstDevice);
        }
        if (!dstDevice.isLoggedInUser(user)) {
            LOG.info("Login as user " + user.getName() + " with device "
                    + dstDevice.name());
            dstDevice.logInWithUser(user);
        }
    }

    private void logout(IDevice dstDevice) throws Exception {
        if (dstDevice.hasLoggedInUser()) {
            dstDevice.logout();
        }
    }

    private IDevice getOrAddRandomDevice(ClientUser user) throws Exception {
        IDevice dstDevice = getDevicePool().getOrAddRandomDevice(user);
        this.login(user, dstDevice);
        return dstDevice;
    }

    public List<String> getDeviceIds(ClientUser user) throws Exception {
        List<IDevice> devices = getDevicePool().getDevices(user);
        List<String> ids = new ArrayList<>();
        for (IDevice device : devices) {
            try {
                ids.add(device.getId());
            } catch (Exception e) {
                LOG.error(String.format("Could not get ID from device of user '%s'", user.getName()), e);
            }
        }
        return ids;
    }

    public List<String> getDeviceFingerprints(ClientUser user) throws Exception {
        List<IDevice> devices = getDevicePool().getDevices(user);
        List<String> fingerprints = new ArrayList<>();
        for (IDevice device : devices) {
            try {
                fingerprints.add(device.getFingerprint());
            } catch (Exception e) {
                LOG.error(String.format("Could not get fingerprint from device of user '%s'", user.getName()), e);
            }
        }
        return fingerprints;
    }

    private static void verifyPathExists(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("The file %s is not accessible", path));
        }
    }

    public void sendConversationMessage(ClientUser userFrom, String convId,
                                        String message) throws Exception {
        getOrAddRandomDevice(userFrom).sendMessage(convId, message);
    }

    public void addRemoteDeviceToAccount(ClientUser user, String deviceName, String label)
            throws Exception {
        IDevice dstDevice = this.getDevicePool().addDevice(user, deviceName);
        this.login(user, dstDevice);
        LOG.info("Set label for device " + deviceName + " to " + label);
        dstDevice.setLabel(label);
    }

    public void sendImage(ClientUser userFrom, String convId, String path)
            throws Exception {
        verifyPathExists(path);
        getOrAddRandomDevice(userFrom).sendImage(convId, path);
    }

    public void sendPing(ClientUser userFrom, String convId) throws Exception {
        getOrAddRandomDevice(userFrom).sendPing(convId);
    }

    private synchronized void shutdown() {
        try {
            getDevicePool().shutdown();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public synchronized void reset() throws Exception {
        if (this.devicePool.isDone() && this.getDevicePool().size() > 0) {
            this.getDevicePool().shutdown();
            instance = new SEBridge();
        }
    }
}