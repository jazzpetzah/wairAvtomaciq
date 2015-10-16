package com.wearezeta.auto.common.device;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class DevicePoolTest {

    @Test(expected = IllegalStateException.class)
    public void killingAllDevicesClosesPool() {
        DevicePool testPool = new DevicePool(2);
        testPool.killAllDevices();
        testPool.getNextFreeDevice();
    }

    @Test
    public void newPoolHasAllFreeDevices() {
        DevicePool testPool = new DevicePool(1);
        assertTrue("The Device Pool should have free devices", testPool.hasFreeDevices());
        testPool.killAllDevices();
    }

    @Test(expected = IllegalArgumentException.class)
    public void mustCreateAPoolWithAtLeatOneDevice() {
        new DevicePool(0);
    }

    @Test
    public void loggingIntoDevicesUsesUpFreeDevices() {
        List<ClientUser> users = DeviceTestUtils.generateRandomUsers(3);

        DevicePool testPool = new DevicePool(3);
        List<Device> devices = new ArrayList<>();

        int index = 0;
        while (testPool.hasFreeDevices()) {
            Device device = testPool.getNextFreeDevice();
            device.logInWithUser(users.get(index));
            devices.add(device);
            index++;
        }

        assertEquals("Unexpected number of logged in devices", devices.size(), 3);
        assertFalse("Device pool should have no more free devices", testPool.hasFreeDevices());
    }

}