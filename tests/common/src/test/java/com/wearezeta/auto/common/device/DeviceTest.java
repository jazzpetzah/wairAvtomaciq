package com.wearezeta.auto.common.device;

import org.junit.Test;

public class DeviceTest {

    @Test(expected = IllegalStateException.class)
    public void deviceCantBeEstablishedWithoutConnectedRemote() {
        IRemoteProcess process = new RemoteProcessStub();
        new Device("device", process);
    }
}