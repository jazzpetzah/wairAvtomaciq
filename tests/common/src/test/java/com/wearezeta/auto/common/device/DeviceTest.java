package com.wearezeta.auto.common.device;

import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class DeviceTest {

    private FiniteDuration testDuration = new FiniteDuration(5000, TimeUnit.MILLISECONDS);

    @Test(expected = IllegalStateException.class)
    public void deviceCantBeEstablishedWithoutConnectedRemote() {
        IRemoteProcess process = new RemoteProcessStub();
        new Device("device", process, testDuration);
    }
}