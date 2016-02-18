package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
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
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class RemoteProcess extends RemoteEntity implements IRemoteProcess {
    private static final Logger LOG = ZetaLogger.getLog(RemoteProcess.class.getSimpleName());

    private static final FiniteDuration ACTOR_DURATION = new FiniteDuration(90, TimeUnit.SECONDS);

    private final ActorRef coordinatorActorRef;
    private final String backendType;
    private final boolean otrOnly;
    private volatile Optional<ExecutorService> pinger = Optional.empty();
    private Optional<Process> currentProcess = Optional.empty();

    {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    // Default actor lib TTL equals to 30 seconds
    private static final int PING_INTERVAL_SECONDS = 20;

    public RemoteProcess(String processName, ActorRef coordinatorActorRef, String backendType, boolean otrOnly) {
        super(processName, ACTOR_DURATION);
        this.backendType = backendType;
        this.otrOnly = otrOnly;
        this.coordinatorActorRef = coordinatorActorRef;
        try {
            restart();
        } catch (Exception e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
    }

    private void restartProcess() throws Exception {
        if (currentProcess.isPresent()) {
            currentProcess.get().destroyForcibly();
        }
        final String serialized = Serialization.serializedActorPath(this.coordinatorActorRef);
        final String[] cmd = {"java", "-jar", getActorsJarLocation(),
                this.name(), serialized, backendType, String.valueOf(otrOnly)};
        LOG.info(String.format("Executing actors using the command line: %s", Arrays.toString(cmd)));
        final ProcessBuilder pb = new ProcessBuilder(cmd);
        // ! Having a log file is mandatory
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(getLogPath())));
        LOG.info(String.format("Actor logs will be redirected to %s", getLogPath()));
        this.currentProcess = Optional.of(pb.start());
    }

    @Override
    public void restart() throws Exception {
        if (this.pinger.isPresent()) {
            this.pinger.get().shutdownNow();
            this.pinger = Optional.empty();
        }

        if (this.ref() != null) {
            this.ref().tell(PoisonPill.getInstance(), null);
            this.setRef(null);
        }

        restartProcess();

        try {
            final Object resp = askActor(this.coordinatorActorRef, new WaitUntilRegistered(this.name()));
            if (resp instanceof ActorRef) {
                this.setRef((ActorRef) resp);
                this.pinger = Optional.of(Executors.newSingleThreadExecutor());
                LOG.debug(String.format(
                        "Starting ping thread with %s-seconds interval for the remote process '%s'...",
                        PING_INTERVAL_SECONDS, name()));
                this.pinger.get().submit(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(PING_INTERVAL_SECONDS * 1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        if (!isConnected()) {
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

    public void shutdown() {
        if (this.pinger.isPresent()) {
            try {
                this.pinger.get().shutdownNow();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (currentProcess.isPresent()) {
            currentProcess.get().destroy();
        }
    }
}
