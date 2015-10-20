package com.wearezeta.auto.common.backend;

import com.wearezeta.auto.common.device.Device;
import com.wearezeta.auto.common.device.DevicePool;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.List;

public class RemoteProcessIPC {

    private static DevicePool devicePool;

    public static void startDevices(int numDevices) throws Exception {
        devicePool = new DevicePool(numDevices);
    }

    /**
     * First checks to see if a user is logged into any remote devices. If not, then it will attempt to do so
     * This method should be safe to call before any operation where it is uncertain if the user is logged into
     * a device or not
     * @param user
     */
    public static void loginToSingleRemoteProcess(ClientUser user) {
        List<Device> loggedInDevices = devicePool.getDevicesWithLoggedInUser(user);
        if (loggedInDevices.size() == 0) {
            devicePool.getNextFreeDevice().logInWithUser(user);
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) {
        List<Device> devices = devicePool.getDevicesWithLoggedInUser(userFrom);
        if (devices.size() == 0) {
            throw new IllegalStateException("No devices with logged in user: " + userFrom.getName());
        }
        //use the first device for now:
        Device sendersDevice = devices.get(0);
        sendersDevice.sendMessage(convId, message);
    }

    public static void killAllDevices() {
        devicePool.killAllDevices();
    }
}