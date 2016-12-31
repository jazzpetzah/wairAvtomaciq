package com.wearezeta.auto.common.wire_actors;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class SEBridge {
    private static final Logger LOG = ZetaLogger.getLog(SEBridge.class.getSimpleName());

    private static class Device {
        private String uuid;
        private Optional<String> name = Optional.empty();
        private Optional<ClientUser> owner = Optional.empty();

        public Device(String uuid, String name) {
            this.uuid = uuid;
            this.name = Optional.of(name);
        }

        public Device(String uuid) {
            this.uuid = uuid;
        }

        public String getUUID() {
            return uuid;
        }

        public Optional<String> getName() {
            return name;
        }

        public void setOwner(ClientUser owner) {
            this.owner = Optional.of(owner);
        }

        public Optional<ClientUser> getOwner() {
            return this.owner;
        }

        public void setName(String name) {
            this.name = Optional.of(name);
        }
    }

    private List<Device> registeredDevices = new ArrayList<>();
    private Semaphore devicesGuard = new Semaphore(1);

    public SEBridge() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::reset));
    }

    private List<Device> getDevicesOwnedBy(ClientUser owner) throws InterruptedException {
        return getDevicesOwnedBy(owner, Optional.empty());
    }

    private Device getDeviceOwnedBy(ClientUser owner, String name) throws InterruptedException {
        final List<Device> devices = getDevicesOwnedBy(owner, Optional.empty());
        if (devices.isEmpty()) {
            throw new IllegalStateException(String.format("There is no device '%s' owned by user '%s'",
                    name, owner.getName()));
        }
        return devices.get(0);
    }

    private List<Device> getDevicesOwnedBy(ClientUser owner, Optional<String> byName) throws InterruptedException {
        devicesGuard.acquire();
        try {
            final List<Device> ownedDevices = registeredDevices.stream()
                    .filter(x -> x.getOwner().isPresent()
                            && x.getOwner().get().getEmail().equals(owner.getEmail()))
                    .collect(Collectors.toList());
            return byName.map(s -> ownedDevices.stream()
                    .filter(x -> x.getName().isPresent() && x.getName().get().equals(s))
                    .collect(Collectors.toList()))
                    .orElse(ownedDevices);
        } finally {
            devicesGuard.release();
        }
    }

    private Device fetchDeviceOwnedBy(ClientUser owner) throws Exception {
        return fetchDeviceOwnedBy(owner, Optional.empty());
    }

    /**
     * This function fetches the first existing device, which is registered
     * for the current user or creates a new one and returns in case it has not been created yet
     *
     * @param owner
     * @param name
     * @return
     * @throws InterruptedException
     */
    private Device fetchDeviceOwnedBy(ClientUser owner, Optional<String> name) throws Exception {
        final List<Device> existingDevices = getDevicesOwnedBy(owner, name);
        if (!existingDevices.isEmpty()) {
            return existingDevices.get(0);
        }
        final String uuid = ActorsRESTWrapper.createDevice(name);
        ActorsRESTWrapper.loginToDevice(uuid, owner);
        final Device newDevice = new Device(uuid);
        name.ifPresent(newDevice::setName);
        newDevice.setOwner(owner);
        devicesGuard.acquire();
        try {
            registeredDevices.add(newDevice);
        } finally {
            devicesGuard.release();
        }
        return newDevice;
    }

    public List<String> getDeviceIds(ClientUser owner) throws Exception {
        final List<Device> devices = getDevicesOwnedBy(owner);
        final List<String> ids = new ArrayList<>();
        for (Device device : devices) {
            ids.add(ActorsRESTWrapper.getDeviceId(device.getUUID()));
        }
        return ids;
    }

    public String getDeviceId(ClientUser owner, String deviceName) throws Exception {
        return ActorsRESTWrapper.getDeviceId(getDeviceOwnedBy(owner, deviceName).getUUID());
    }

    public String getDeviceFingerprint(ClientUser owner, String deviceName) throws Exception {
        return ActorsRESTWrapper.getDeviceFingerprint(getDeviceOwnedBy(owner, deviceName).getUUID());
    }

    public void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        ActorsRESTWrapper.sendMessage(fetchDeviceOwnedBy(userFrom).getUUID(), convId, message);
    }

    public void sendConversationMessage(ClientUser userFrom, String convId, String message, String deviceName) throws
            Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.sendMessage(dstDevice.getUUID(), convId, message);
    }

    public void addRemoteDeviceToAccount(ClientUser userFrom, String deviceName, String label) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        LOG.info("Set label for device " + deviceName + " to " + label);
        ActorsRESTWrapper.setDeviceLabel(dstDevice.getUUID(), label);
    }

    public void sendImage(ClientUser userFrom, String convId, String path) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.sendImage(dstDevice.getUUID(), convId, new File(path));
    }

    public void sendGiphy(ClientUser userFrom, String convId, String searchQuery, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.sendGiphy(dstDevice.getUUID(), convId, searchQuery);
    }

    public void sendPing(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.sendPing(dstDevice.getUUID(), convId);
    }

    public void typing(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.sendTyping(dstDevice.getUUID(), convId);
    }

    public void clearConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.clearConversation(dstDevice.getUUID(), convId);
    }

    public void muteConversation(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.muteConversation(dstDevice.getUUID(), convId);
    }

    public void muteConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.muteConversation(dstDevice.getUUID(), convId);
    }

    public void unmuteConversation(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.unmuteConversation(dstDevice.getUUID(), convId);
    }

    public void unmuteConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.unmuteConversation(dstDevice.getUUID(), convId);
    }

    public void archiveConversation(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.archiveConversation(dstDevice.getUUID(), convId);
    }

    public void archiveConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.archiveConversation(dstDevice.getUUID(), convId);
    }

    public void unarchiveConversation(ClientUser userFrom, String convId) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom);
        ActorsRESTWrapper.unarchiveConversation(dstDevice.getUUID(), convId);
    }

    public void unarchiveConversation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.unarchiveConversation(dstDevice.getUUID(), convId);
    }

    public void sendFile(ClientUser userFrom, String convId, String path, String mime, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.sendFile(dstDevice.getUUID(), convId, new File(path), mime);
    }

    public void sendLocation(ClientUser userFrom, String deviceName, String convId, float longitude,
                             float latitude, String locationName, int zoom) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.sendLocation(dstDevice.getUUID(), convId,
                new ActorsRESTWrapper.LocationInfo(longitude, latitude, locationName, zoom)
        );
    }

    public void deleteMessage(ClientUser userFrom, String convId, String messageId, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.deleteMessage(dstDevice.getUUID(), convId, messageId);
    }

    public void deleteMessageEverywhere(ClientUser userFrom, String convId, String messageId, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.deleteMessageEverywhere(dstDevice.getUUID(), convId, messageId);
    }

    public void updateMessage(ClientUser userFrom, String messageId, String newMessage, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.updateMessage(dstDevice.getUUID(), messageId, newMessage);
    }

    public void reactMessage(ClientUser userFrom, String convId, String messageId,
                             ActorsRESTWrapper.MessageReaction reactionType, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.reactMessage(dstDevice.getUUID(), convId, messageId, reactionType);
    }

    public void shareDefaultLocation(ClientUser userFrom, String convId, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.sendLocation(dstDevice.getUUID(), convId);
    }

    public void setEphemeralMode(ClientUser userFrom, String convId, Timedelta expirationTimeout, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setEphemeralTimeout(dstDevice.getUUID(), convId, expirationTimeout);
    }

    public void markEphemeralRead(ClientUser userFrom, String convId, String messageId, String deviceName)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.readEphemeralMessage(dstDevice.getUUID(), convId, messageId);
    }

    public List<ActorsRESTWrapper.MessageInfo> getConversationMessages(ClientUser userFrom, String convId,
                                                                       String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        return ActorsRESTWrapper.getMessagesInfo(dstDevice.getUUID(), convId);
    }

    public void setAssetToV3(ClientUser userFrom, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setDeviceAssetsVersion(dstDevice.getUUID(), ActorsRESTWrapper.AssetsVersion.V3);
    }

    public void setAssetToV2(ClientUser userFrom, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setDeviceAssetsVersion(dstDevice.getUUID(), ActorsRESTWrapper.AssetsVersion.V2);
    }

    public void cancelConnection(ClientUser userFrom, ClientUser userDst, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.cancelConnection(dstDevice.getUUID(), userDst.getId());
    }

    public String getUniqueUsername(ClientUser userFrom, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        return ActorsRESTWrapper.getUniqueUsername(dstDevice.getUUID());
    }

    public void updateUniqueUsername(ClientUser userFrom, String uniqueUserName, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setUniqueUsername(dstDevice.getUUID(), uniqueUserName);
    }

    public void reset() {
        try {
            final List<Device> registeredDevicesCopy = new ArrayList<>(registeredDevices);
            for (Device registeredDevice : registeredDevicesCopy) {
                try {
                    ActorsRESTWrapper.removeDevice(registeredDevice.getUUID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                devicesGuard.acquire();
                try {
                    registeredDevices.remove(registeredDevice);
                } finally {
                    devicesGuard.release();
                }
            }
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
