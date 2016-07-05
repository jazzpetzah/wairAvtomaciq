package com.wearezeta.auto.common.sync_engine_bridge;

import com.google.common.base.Throwables;
import com.waz.model.MessageId;
import com.waz.provision.ActorMessage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

public class SEBridge {

    private volatile UserDevicePool devicePool;
    private static SEBridge instance = null;
    protected static int MAX_PROCESS_NUM = 25;

    private static final Logger LOG = ZetaLogger.getLog(SEBridge.class.getSimpleName());

    {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    protected SEBridge() throws Exception {
        this.devicePool = new UserDevicePool(CommonUtils.getBackendType(CommonUtils.class),
                CommonUtils.getOtrOnly(CommonUtils.class), MAX_PROCESS_NUM);
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

    public void muteConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        getOrAddDevice(userFrom, deviceName).muteConversation(convId);
    }

    public void unmuteConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        getOrAddDevice(userFrom, deviceName).unmuteConversation(convId);
    }

    public void sendFile(ClientUser userFrom, String convId, String path, String mime, String deviceName)
            throws Exception {
        getOrAddDevice(userFrom, deviceName).sendFile(convId, path, mime);
    }

    public void sendLocation(ClientUser userFrom, String deviceName, String convId, float longitude, float latitude, String locationName,
                             int zoom)
            throws Exception {
        getOrAddDevice(userFrom, deviceName).shareLocation(convId, longitude, latitude, locationName, zoom);
    }

    public void deleteMessage(ClientUser userFrom, String convId, MessageId messageId, String deviceName)
            throws Exception {
        getOrAddDevice(userFrom, deviceName).deleteMessage(convId, messageId);
    }

    public void shareDefaultLocation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        getOrAddDevice(userFrom, deviceName).shareLocation(convId);
    }

    public ActorMessage.MessageInfo[] getConversationMessages(ClientUser userFrom, String convId, String deviceName)
            throws Exception {
        return getOrAddDevice(userFrom, deviceName).getConversationMessages(convId);
    }

    public void releaseDevicesOfUsers(Collection<ClientUser> users) throws Exception {
        for (ClientUser user : users) {
            getDevicePool().releaseDevices(getDevicePool().getDevices(user));
        }
    }

    public void releaseDevicesOfUser(ClientUser user) throws Exception {
        getDevicePool().releaseDevices(getDevicePool().getDevices(user));
    }

    public void reset() throws Exception {
        this.getDevicePool().reset();
    }

    private void shutdown() {
        try {
            getDevicePool().shutdown();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private UserDevicePool getDevicePool() throws Exception {
        return this.devicePool;
    }

    private IDevice getOrAddRandomDevice(ClientUser user) throws Exception {
        return getDevicePool().getOrAddRandomDevice(user);
    }

    private IDevice getOrAddDevice(ClientUser user, String deviceName) throws Exception {
        return getDevicePool().getOrAddDevice(user, deviceName);
    }

    private static void verifyPathExists(String path) {
        if (!new File(path).exists()) {
            throw new IllegalArgumentException(String.format("The file %s is not accessible", path));
        }
    }
}
