package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.serialization.Serialization;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.Echo;
import com.waz.provision.ActorMessage.WaitUntilRegistered;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class RemoteProcess extends RemoteEntity implements IRemoteProcess {

    private final ActorRef coordinatorActorRef;

    //TODO make configurable
    private String backend = "staging";

    public RemoteProcess(String processName, ActorRef coordinatorActorRef, FiniteDuration actorTimeout) {
        super(actorTimeout);
        name = processName;
        this.coordinatorActorRef = coordinatorActorRef;
        if (!coordinatorConnected()) {
            throw new IllegalStateException("There is no connection to the CoordinatorActor at path: "
                    + coordinatorActorRef);
        }
        //TODO remove into a init() method of some sort?
        //TODO save the coordinatorRef?
        startProcess();
        establishConnection();
    }

    private boolean coordinatorConnected() {
        if (coordinatorActorRef == null) {
            return false;
        }
        Object resp = askActor(coordinatorActorRef, new Echo("test", "test"));
        if (resp instanceof Echo && ((Echo) resp).msg().equals("test")) {
            return true;
        }
        return false;
    }

    private void startProcess() {
        String serialized = Serialization.serializedActorPath(coordinatorActorRef);
        String[] cmd = {"java", "-jar", getActorsJarLocation(), name, serialized, backend};

        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);
        File outputFile = createLogFile(name);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(outputFile));

        try {
            pb.start();
        } catch (IOException e) {
            System.err.println("Command used to execute remote process: " + String.join(" ", cmd));
            System.err.println("Log file used for process: " + outputFile);
            e.printStackTrace();
        }
    }

    private File createLogFile(String processName) {
        File outputFile = new File("target/logcat/" + processName);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    private void establishConnection() {
        Object resp = askActor(coordinatorActorRef, new WaitUntilRegistered(name));

        if (resp instanceof ActorRef) {
            ref = (ActorRef) resp;
        } else {
            //TODO come up with a better exception?
            throw new IllegalStateException("The coordinator actor failed to establish a connection with the remote " +
                    "process: " + name + ". The response was: " + resp);
        }
    }

    private String getActorsJarLocation() {
        File file = null;
        try {
            //Any class from the zmessaging-actors jar will do. ActorMessage seemed like a good choice.
            file = new File(ActorMessage.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file.toString();
    }


}
