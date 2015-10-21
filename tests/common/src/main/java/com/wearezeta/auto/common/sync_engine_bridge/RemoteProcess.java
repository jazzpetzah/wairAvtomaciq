package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.serialization.Serialization;

import com.google.common.base.Throwables;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.Echo;
import com.waz.provision.ActorMessage.WaitUntilRegistered;

import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

class RemoteProcess extends RemoteEntity implements IRemoteProcess {

	private final ActorRef coordinatorActorRef;

	// TODO make configurable
	private static final String BACKEND = "staging";

	public RemoteProcess(String processName, ActorRef coordinatorActorRef,
			FiniteDuration actorTimeout) {
		super(actorTimeout);
		this.name = processName;
		this.coordinatorActorRef = coordinatorActorRef;
		if (!coordinatorConnected()) {
			throw new IllegalStateException(
					"There is no connection to the CoordinatorActor at path: "
							+ coordinatorActorRef);
		}
		// TODO remove into a init() method of some sort?
		// TODO save the coordinatorRef?
		try {
			startProcess();
		} catch (Exception e) {
			e.printStackTrace();
			Throwables.propagate(e);
		}
		reconnect();
	}

	private boolean coordinatorConnected() {
		if (coordinatorActorRef == null) {
			return false;
		}
		try {
			final Object resp = askActor(coordinatorActorRef, new Echo("test",
					"test"));
			return ((resp instanceof Echo) && ((Echo) resp).msg()
					.equals("test"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void startProcess() throws Exception {
		final String serialized = Serialization
				.serializedActorPath(coordinatorActorRef);
		final String[] cmd = { "java", "-jar", getActorsJarLocation(), name,
				serialized, BACKEND };
		final ProcessBuilder pb = new ProcessBuilder(cmd);

		// ! Having a log file is mandatory
		pb.redirectErrorStream(true);
		pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(
				getLogPath())));
		pb.start();
	}

	@Override
	public void reconnect() {
		try {
			final Object resp = askActor(coordinatorActorRef,
					new WaitUntilRegistered(name));
			if (resp instanceof ActorRef) {
				this.ref = (ActorRef) resp;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new IllegalStateException(
				String.format(
						"The coordinator actor failed to establish a connection with the remote "
								+ "process: %s. Please check the log file %s for more details.",
						name(), getLogPath()));
	}

	private String getActorsJarLocation() throws URISyntaxException {
		final File file = new File(ActorMessage.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath());
		return file.toString();
	}

	@Override
	public String getLogPath() {
		final File outputFile = new File(String.format("target/logcat/%s.log",
				name()));
		if (outputFile.getParentFile() == null) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			return outputFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			Throwables.propagate(e);
			return null;
		}
	}
}
