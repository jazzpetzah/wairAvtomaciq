package com.wearezeta.auto.common.device;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.CoordinatorActor;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RemoteProcessTest {

    private FiniteDuration testDuration = new FiniteDuration(5000, TimeUnit.MILLISECONDS);

    @Test(expected = IllegalStateException.class)
    public void cantCreateProcessWithNullCoordinator() {
        ActorRef nullRef = null;
        new RemoteProcess("process", nullRef, testDuration);

        ActorRef terminatedRef = new ActorRef() {
            @Override
            public boolean isTerminated() {
                return true;
            }

            @Override
            public ActorPath path() {
                return null;
            }
        };

        new RemoteProcess("process", terminatedRef, testDuration);

    }

    @Test(expected = IllegalStateException.class)
    public void cantCreateProcessWithDisconnectedCoordinator() {
        //Start a coordinator
        ActorSystem system = ActorSystem.create("CoordinatorSystem");
        ActorRef disconnectedRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        //kill the system
        system.shutdown();
        //use it to start a new process
        new RemoteProcess("process", disconnectedRef, testDuration);
    }

    @Test
    public void testRemoteIsEstablishedWithValidCoordinator() {
        ActorRef coordinatorRef = DeviceTestUtils.createCoordinatorActor();
        RemoteProcess process = new RemoteProcess("process", coordinatorRef, testDuration);
        assertTrue("Process is not connected", process.isConnected());
    }
}