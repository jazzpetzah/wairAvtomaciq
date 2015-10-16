package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import com.waz.provision.ActorMessage.GetUser$;
import com.waz.provision.ActorMessage.Successful;
import com.waz.provision.ActorMessage.TerminateRemotes$;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.UserState;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class DeviceTest {

    private FiniteDuration testDuration = new FiniteDuration(5000, TimeUnit.MILLISECONDS);

    ClientUser availableTestUser = new ClientUser(
            "smoketester+dc2807@wearezeta.com",
            "aqa123456",
            null,
            "dc2807",
            UserState.Created
    );

    @Test(expected = IllegalStateException.class)
    public void deviceCantBeEstablishedWithoutConnectedRemote() {
        IRemoteProcess process = new RemoteProcessStub();
        new Device("device", process, testDuration);
    }

    @Test
    public void canEstablishDeviceWithValidProcess() {
        ActorRef validCoordinator = DeviceTestUtils.createCoordinatorActor();
        RemoteProcess validProcess = new RemoteProcess("test_process_1", validCoordinator, testDuration);

        Device device = new Device("test_device_1", validProcess, testDuration);

        assertTrue("Device was not connected", device.isConnected());

        validCoordinator.tell(TerminateRemotes$.MODULE$, null);
    }

    @Test
    public void canLogInWithClientUser() {
        ActorRef validCoordinator = DeviceTestUtils.createCoordinatorActor();
        RemoteProcess validProcess = new RemoteProcess("test_process_1", validCoordinator, testDuration);

        Device device = new Device("test_device_1", validProcess, testDuration);

        device.logInWithUser(availableTestUser);

        Object resp = DeviceTestUtils.askActor(device.ref, GetUser$.MODULE$, testDuration);
        assertTrue(resp instanceof Successful);

        String userId = ((Successful) resp).response();
        System.out.println("Returned userId: " + userId);

        assertTrue(userId.length() > 0);

        validCoordinator.tell(TerminateRemotes$.MODULE$, null);
    }
}