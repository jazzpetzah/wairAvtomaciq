package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.serialization.Serialization;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.WaitUntilRegistered;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class RemoteProcess {

    private String processName;

    private ActorRef remoteActorRef;

    public RemoteProcess(String processName, ActorRef coordinatorActorRef) {
        this.processName = processName;
        //TODO remove into a init() method of some sort?
        //TODO save the coordinatorRef?
        startProcess(coordinatorActorRef);
        establishConnection(coordinatorActorRef);
    }

    public String name() {
        return processName;
    }

    public ActorRef ref() {
        //TODO perform quick actor check to see the process is still alive?
        return remoteActorRef;
    }

    private void startProcess(ActorRef coordinatorActorRef) {
        String serialized = Serialization.serializedActorPath(coordinatorActorRef);
        String[] cmd = {"java", "-jar", getActorsJarLocation(), processName, serialized, "staging"};

        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.redirectErrorStream(true);
        File outputFile = createLogFile(processName);
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

    private void establishConnection(ActorRef coordinatorActorRef) {
        Object resp = DevicePool.askActor(coordinatorActorRef, new WaitUntilRegistered(processName));

        if (resp instanceof ActorRef) {
            remoteActorRef = (ActorRef) resp;
        } else {
            //TODO come up with a better exception?
            throw new IllegalStateException("The coordinator actor failed to establish a connection with the remote " +
                    "process: " + processName + ". The response was: " + resp);
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
