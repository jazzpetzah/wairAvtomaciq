package com.wearezeta.auto.common.wire_actors;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.wire_actors.models.AssetsVersion;
import com.wearezeta.auto.common.wire_actors.models.LocationInfo;
import com.wearezeta.auto.common.wire_actors.models.MessageInfo;
import com.wearezeta.auto.common.wire_actors.models.MessageReaction;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;

public class RemoteDevicesManager {
    private static final Logger LOG = ZetaLogger.getLog(RemoteDevicesManager.class.getSimpleName());

    private static class Device {
        private Future<String> uuid;
        private Optional<String> name = Optional.empty();
        private Optional<ClientUser> owner = Optional.empty();

        Device(Future<String> uuid) {
            this.uuid = uuid;
        }

        String getUUID() {
            try {
                return uuid.get();
            } catch (Exception e) {
                Throwables.propagate(e);
                e.printStackTrace();
            }
            return "";
        }

        Optional<String> getName() {
            return name;
        }

        void setOwner(ClientUser owner) {
            this.owner = Optional.of(owner);
        }

        Optional<ClientUser> getOwner() {
            return this.owner;
        }

        void setName(String name) {
            this.name = Optional.of(name);
        }
    }

    private List<Device> registeredDevices = new ArrayList<>();
    private Semaphore devicesGuard = new Semaphore(1);

    public RemoteDevicesManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private List<Device> getDevicesOwnedBy(ClientUser owner) throws InterruptedException {
        return getDevicesOwnedBy(owner, Optional.empty());
    }

    private Device getDeviceOwnedBy(ClientUser owner, String name) throws InterruptedException {
        devicesGuard.acquire();
        try {
            return registeredDevices.stream()
                    .filter(x -> x.getOwner().isPresent() && x.getOwner().get().equals(owner)
                            && x.getName().isPresent() && x.getName().get().equals(name))
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalStateException(String.format("There is no device '%s' owned by user '%s'",
                                    name, owner.getName()))
                    );
        } finally {
            devicesGuard.release();
        }
    }

    private List<Device> getDevicesOwnedBy(ClientUser owner, Optional<String> byName) throws InterruptedException {
        devicesGuard.acquire();
        try {
            final List<Device> ownedDevices = registeredDevices.stream()
                    .filter(x -> x.getOwner().isPresent() && x.getOwner().get().equals(owner))
                    .collect(Collectors.toList());
            return byName.map(s -> ownedDevices.stream()
                    .filter(x -> x.getName().isPresent() && x.getName().get().equals(s))
                    .collect(Collectors.toList()))
                    .orElse(ownedDevices);
        } finally {
            devicesGuard.release();
        }
    }

    private void unregisterAllDevices() throws InterruptedException {
        devicesGuard.acquire();
        try {
            registeredDevices.clear();
        } finally {
            devicesGuard.release();
        }
    }

    private List<Device> getRegisteredDevices() throws InterruptedException {
        devicesGuard.acquire();
        try {
            return new ArrayList<>(registeredDevices);
        } finally {
            devicesGuard.release();
        }
    }

    private void unregisterDevice(Device device) throws InterruptedException {
        devicesGuard.acquire();
        try {
            if (registeredDevices.contains(device)) {
                registeredDevices.remove(device);
            }
        } finally {
            devicesGuard.release();
        }
    }

    private Device fetchDeviceOwnedBy(ClientUser owner) throws InterruptedException {
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
    private Device fetchDeviceOwnedBy(ClientUser owner, Optional<String> name) throws InterruptedException {
        devicesGuard.acquire();
        try {
            final Device result = registeredDevices.stream()
                    .filter(x -> x.getOwner().isPresent() && x.getOwner().get().equals(owner)
                            && (name.isPresent() && x.getName().isPresent() && x.getName().get().equals(name.get())
                            || !name.isPresent()))
                    .findFirst()
                    .orElse(null);
            if (result != null) {
                return result;
            }
            final ExecutorService pool = Executors.newSingleThreadExecutor();
            final Future<String> uuidPromise = pool.submit(
                    () -> {
                        final String uuid = ActorsRESTWrapper.createDevice(name);
                        ActorsRESTWrapper.loginToDevice(uuid, owner);
                        return uuid;
                    }
            );
            pool.shutdown();
            final Device newDevice = new Device(uuidPromise);
            name.ifPresent(newDevice::setName);
            newDevice.setOwner(owner);
            registeredDevices.add(newDevice);
            return newDevice;
        } finally {
            devicesGuard.release();
        }
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

    public void addRemoteDeviceToAccount(ClientUser userFrom, @Nullable String deviceName, Optional<String> label)
            throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        // !!! Do not remove this line. This is to wait until the device object is returned from the backend
        LOG.info(String.format("Successfully created a new device with uuid '%s'", dstDevice.getUUID()));
        if (label.isPresent()) {
            LOG.info(String.format("Setting label '%s' to device '%s'", label.get(), deviceName));
            ActorsRESTWrapper.setDeviceLabel(dstDevice.getUUID(), label.get());
        }
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
                new LocationInfo(longitude, latitude, locationName, zoom)
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
                             MessageReaction reactionType, String deviceName) throws Exception {
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

    public List<MessageInfo> getConversationMessages(ClientUser userFrom, String convId,
                                                     String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        return ActorsRESTWrapper.getMessagesInfo(dstDevice.getUUID(), convId);
    }

    public void setAssetToV3(ClientUser userFrom, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setDeviceAssetsVersion(dstDevice.getUUID(), AssetsVersion.V3);
    }

    public void setAssetToV2(ClientUser userFrom, String deviceName) throws Exception {
        final Device dstDevice = fetchDeviceOwnedBy(userFrom, Optional.ofNullable(deviceName));
        ActorsRESTWrapper.setDeviceAssetsVersion(dstDevice.getUUID(), AssetsVersion.V2);
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

    private static ExecutorService removeDevicesAsync(List<String> uuids) {
        final ExecutorService pool = Executors.newFixedThreadPool(uuids.size());
        for (String uuid : uuids) {
            pool.submit(() -> {
                try {
                    ActorsRESTWrapper.removeDevice(uuid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        return pool;
    }

    private static final Timedelta DEVICES_REMOVAL_TIMEOUT = Timedelta.fromSeconds(5);

    public void reset() throws InterruptedException {
        final List<String> uuids = getRegisteredDevices().stream()
                .map(Device::getUUID)
                .collect(Collectors.toList());
        if (uuids.isEmpty()) {
            return;
        }
        LOG.info(String.format("Cleaning %d devices from remote devices manager...", uuids.size()));
        try {
            unregisterAllDevices();
        } finally {
            removeDevicesAsync(uuids).awaitTermination(DEVICES_REMOVAL_TIMEOUT.asSeconds(), TimeUnit.SECONDS);
        }
    }

    public void releaseDevicesOfUsers(List<ClientUser> owners) throws InterruptedException {
        final List<String> uuids = new ArrayList<>();
        try {
            for (ClientUser owner : owners) {
                final List<Device> ownedDevices = getDevicesOwnedBy(owner);
                for (Device ownedDevice : ownedDevices) {
                    this.unregisterDevice(ownedDevice);
                    uuids.add(ownedDevice.getUUID());
                }
            }
        } finally {
            if (!uuids.isEmpty()) {
                LOG.info(String.format("Cleaning %d devices from remote devices manager...", uuids.size()));
                removeDevicesAsync(uuids).awaitTermination(DEVICES_REMOVAL_TIMEOUT.asSeconds(), TimeUnit.SECONDS);
            }
        }
    }
}
