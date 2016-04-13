package com.wearezeta.auto.common.sync_engine_bridge;

import com.google.common.base.Throwables;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SEBridge {

    private volatile UserDevicePool devicePool;
    private static SEBridge instance = null;

    private static final Logger LOG = ZetaLogger.getLog(SEBridge.class.getSimpleName());

    static {
        try {
            CommonUtils.cleanupOutdatedMavenSnapshots(new File(ActorMessage.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SEBridge() throws Exception {
        this.devicePool = new UserDevicePool(CommonUtils.getBackendType(CommonUtils.class),
                CommonUtils.getOtrOnly(CommonUtils.class));
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

    private UserDevicePool getDevicePool() throws Exception {
        return this.devicePool;
    }

    private void login(ClientUser user, IDevice dstDevice) throws Exception {
        if (!(dstDevice.hasLoggedInUser() && dstDevice.isLoggedInUser(user))) {
            LOG.info("Login as user " + user.getName() + " with device " + dstDevice.name());
            dstDevice.logInWithUser(user);
        }
    }

    private IDevice getOrAddRandomDevice(ClientUser user) throws Exception {
        IDevice dstDevice = getDevicePool().getOrAddRandomDevice(user);
        this.login(user, dstDevice);
        return dstDevice;
    }

    private IDevice getOrAddDevice(ClientUser user, String deviceName) throws Exception {
        IDevice dstDevice = getDevicePool().getOrAddDevice(user, deviceName);
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

    public String getDeviceId(ClientUser user, String deviceName) throws Exception {
        return getDevicePool().getDevice(user, deviceName).orElseThrow(() ->
                new IllegalStateException(String.format("There is no device '%s' owned by user '%s'", deviceName,
                        user.getName()))
        ).getId();
    }

    public String getDeviceFingerprint(ClientUser user, String deviceName) throws Exception {
        return getDevicePool().getDevice(user, deviceName).orElseThrow(() ->
                new IllegalStateException(String.format("There is no device '%s' owned by user '%s'", deviceName,
                        user.getName()))
        ).getFingerprint();
    }

    private static void verifyPathExists(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("The file %s is not accessible", path));
        }
    }

    public void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        getOrAddRandomDevice(userFrom).sendMessage(convId, message);
    }

    public void sendConversationMessage(ClientUser userFrom, String convId, String message, String deviceName) throws
            Exception {
        if (deviceName == null) {
            sendConversationMessage(userFrom, convId, message);
        } else {
            getOrAddDevice(userFrom, deviceName).sendMessage(convId, message);
        }
    }

    public void addRemoteDeviceToAccount(ClientUser user, String deviceName, String label) throws Exception {
        IDevice dstDevice = this.getDevicePool().addDevice(user, deviceName);
        LOG.info("Set label for device " + deviceName + " to " + label);
        dstDevice.setLabel(label);
    }

    public void sendImage(ClientUser userFrom, String convId, String path) throws Exception {
        verifyPathExists(path);
        getOrAddRandomDevice(userFrom).sendImage(convId, path);
    }

    public void sendPing(ClientUser userFrom, String convId) throws Exception {
        getOrAddRandomDevice(userFrom).sendPing(convId);
    }

    public void clearConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        getOrAddDevice(userFrom, deviceName).clearConversation(convId);
    }

    public void sendFile(ClientUser userFrom, String convId, String path, String mime, String deviceName) throws Exception {
        getOrAddDevice(userFrom, deviceName).sendFile(convId, path, mime);
    }

    private void shutdown() {
        try {
            getDevicePool().shutdown();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void reset() throws Exception {
        this.getDevicePool().reset();
    }
}