package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.serialization.Serialization;

import com.google.common.base.Throwables;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.WaitUntilRegistered;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RemoteProcess extends RemoteEntity implements IRemoteProcess {

    private static final Logger LOG = ZetaLogger.getLog(RemoteProcess.class
            .getSimpleName());

    private final String backendType;
    private final boolean otrOnly;
    private ExecutorService pinger;
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.pinger != null) {
                try {
                    this.pinger.shutdownNow();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    // Default actor lib TTL equals to 30 seconds
    private static final int PING_INTERVAL_SECONDS = 20;

    public RemoteProcess(String processName, ActorRef coordinatorActorRef,
                         FiniteDuration actorTimeout, String backendType, boolean otrOnly) {
        super(coordinatorActorRef, processName, actorTimeout);
        this.backendType = backendType;
        this.otrOnly = otrOnly;
        if (!isConnected()) {
            throw new IllegalStateException(
                    "There is no connection to the CoordinatorActor at path: "
                            + coordinatorActorRef);
        }
        try {
            startProcess();
        } catch (Exception e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
        reconnect();
    }

    private void startProcess() throws Exception {
        final String serialized = Serialization.serializedActorPath(ref());
        final String[] cmd = {"java", "-jar", getActorsJarLocation(),
                this.name(), serialized, backendType, String.valueOf(otrOnly)};
        LOG.info(String.format("Executing actors using the command line: %s", Arrays.toString(cmd)));
        final ProcessBuilder pb = new ProcessBuilder(cmd);
        // ! Having a log file is mandatory
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(
                getLogPath())));
        LOG.info(String.format("Actor logs will be redirected to %s", getLogPath()));
        pb.start();
    }

    @Override
    public void reconnect() {
        try {
            final Object resp = askActor(this.ref(), new WaitUntilRegistered(this.name()));
            if (resp instanceof ActorRef) {
                this.setRef((ActorRef) resp);
                if (this.pinger != null) {
                    this.pinger.shutdownNow();
                }
                this.pinger = Executors.newSingleThreadExecutor();
                LOG.debug(String.format("Starting ping thread with interval %s seconds for the remote process '%s'...",
                        PING_INTERVAL_SECONDS, name()));
                this.pinger.submit(() -> {
                    while(!Thread.currentThread().isInterrupted()) {
                        if (!isConnected()) {
                            break;
                        }
                        try {
                            Thread.sleep(PING_INTERVAL_SECONDS * 1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    LOG.debug(String.format("Stopped ping thread for the remote process '%s'", name()));
                });
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
        if (!Files.exists(Paths.get(outputFile.getParent()))) {
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

    @Override
    public boolean isOtrOnly() {
        return this.otrOnly;
    }
}
