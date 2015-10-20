package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.waz.provision.CoordinatorActor;
import com.wearezeta.auto.common.sync_engine_bridge.RemoteProcess;

import org.junit.Test;

import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RemoteProcessTest {

	private FiniteDuration testDuration = new FiniteDuration(5000,
			TimeUnit.MILLISECONDS);

	@Test(expected = IllegalStateException.class)
	public void cantCreateProcessWithNullCoordinator() {
		ActorRef nullRef = null;
		new RemoteProcess("process", nullRef, testDuration);

		ActorRef terminatedRef = new ActorRef() {

			private static final long serialVersionUID = 6381383689960782524L;

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
		// Start a coordinator
		ActorSystem system = ActorSystem.create("CoordinatorSystem");
		ActorRef disconnectedRef = system.actorOf(
				Props.create(CoordinatorActor.class), "coordinatorActor");
		// kill the system
		system.shutdown();
		// use it to start a new process
		new RemoteProcess("process", disconnectedRef, testDuration);
	}

	@Test
	public void testRemoteIsEstablishedWithValidCoordinator() {
		ActorRef coordinatorRef = DeviceTestUtils.createCoordinatorActor();
		RemoteProcess process = new RemoteProcess("process", coordinatorRef,
				testDuration);
		assertTrue("Process is not connected", process.isConnected());
	}
}