package com.wearezeta.auto.common.device;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.CoordinatorActor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RemoteProcessTest {
    @Test(expected = IllegalStateException.class)
    public void cantCreateProcessWithNullCoordinator() {
        ActorRef nullRef = null;
        new RemoteProcess("process", nullRef);

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

        new RemoteProcess("process", terminatedRef);

    }

    @Test(expected = IllegalStateException.class)
    public void cantCreateProcessWithDisconnectedCoordinator() {
        //Start a coordinator
        ActorSystem system = ActorSystem.create("CoordinatorSystem");
        ActorRef disconnectedRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        //kill the system
        system.shutdown();
        //use it to start a new process
        new RemoteProcess("process", disconnectedRef);
    }

    @Test
    public void testRemoteIsEstablishedWithValidCoordinator() {
        ActorRef coordinatorRef = createCoordinatorActor();
        RemoteProcess process = new RemoteProcess("process", coordinatorRef);
        assertTrue("Process is not connected", process.isConnected());
    }

    //TODO can I refactor this to use the method from DevicePool?
    private ActorRef createCoordinatorActor() {
        //TODO find out how to depend on resources from main
        Config config = ConfigFactory.load("actor_coordinator");
        ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        ActorRef actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");

        return actorRef;
    }
}