package com.wearezeta.auto.common.device;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.util.List;

/**
 *
 */
public interface IDevicePool {

    Boolean hasFreeDevices();

    Device getNextFreeDevice();

    List<Device> getDevicesWithLoggedInUser(ClientUser user);

    void killAllDevices();
}
