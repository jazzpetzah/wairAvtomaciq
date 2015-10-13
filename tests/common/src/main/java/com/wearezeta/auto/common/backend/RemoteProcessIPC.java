package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.serialization.Serialization;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.Login;
import com.waz.provision.ActorMessage.Successful$;
import com.waz.provision.ActorMessage.WaitUntilRegistered;
import com.waz.provision.CoordinatorActor;
import com.waz.provision.CoordinatorSystem;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RemoteProcessIPC {

    private static final FiniteDuration duration = new FiniteDuration(30000, TimeUnit.MILLISECONDS);
    private static final Timeout akkaTimeout = new Timeout(30000);
    private static final Map<String, ActorRef> unregisteredRemotes = new HashMap<>();
    private static final Map<String, ActorRef> registeredRemotes = new HashMap<>();
    private static final ActorRef coordinator = setUpCoordinator();

    private static final String REMOTE_PROCESS_NAME = "Remote_process_";

    static {
        System.out.println("Setting up coordinator system");
        CoordinatorSystem.setUpCoordinatorSystem("actor_coordinator");
    }

    public static void startProcesses(int numProcesses) throws Exception {
        //TODO do this with futures, perhaps?
        System.out.println("Generating " + numProcesses + " remotes");
        for (int i = 0; i < numProcesses; i++) {
            String processName = REMOTE_PROCESS_NAME + i;
            ActorRef remote = registerProcess(coordinator, processName);
            if (remote == null) {
                throw new NullPointerException();
            } else {
                System.out.println(processName + " created successfully, adding to map");
                unregisteredRemotes.put(processName, remote);
            }
        }
    }

    public static void killAllProcesses() {
        //TODO could also do this with futures?
        coordinator.tell(ActorMessage.TerminateRemotes$.MODULE$, null);
    }

    public static void loginToRemoteProcess(ClientUser user) throws Exception {

        if (unregisteredRemotes.size() == 0) {
            throw new Exception("No remotes left!");
        }

        String nextProcessKey = REMOTE_PROCESS_NAME + (unregisteredRemotes.size() - 1);
        ActorRef remote = unregisteredRemotes.get(nextProcessKey);

        Future<Object> future = Patterns.ask(remote, new Login(user.getEmail(), user.getPassword()), duration.toMillis());

        Object resp = Await.result(future, duration);
        System.out.println("Response type: " + resp.getClass());
        if (resp instanceof Successful$) {
            System.out.println("Login successful");
            registeredRemotes.put(user.getName(), remote);
            unregisteredRemotes.remove(nextProcessKey);
        } else {
            System.out.println("Login not successful, killing remote");
            remote.tell(ActorMessage.TerminateProcess$.MODULE$, null);
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        System.out.println("Sending message: " + message + " from user: " + userFrom + " to conversation: " + convId);
        ActorRef remote = registeredRemotes.get(userFrom.getName());

        Future<Object> future = Patterns.ask(remote, new ActorMessage.SendText(new RConvId(convId), message), duration.toMillis());
        Object resp = Await.result(future, duration);

        System.out.println(resp.getClass());
    }

    public static ActorRef setUpCoordinator() {
        Config config = ConfigFactory.load("actor_coordinator");
        ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        ActorRef actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
        return actorRef;
    }

    public static ActorRef registerProcess(ActorRef coordinator, String processName) throws Exception {
        String serialized = Serialization.serializedActorPath(coordinator);
        String[] cmd = {"java", "-jar", "/Users/deancook/.m2/repository/com/wearezeta/zmessaging-actor/45-SNAPSHOT/zmessaging-actor-45-SNAPSHOT.jar", processName, serialized, "staging"};

        ProcessBuilder pb = new ProcessBuilder(cmd);

        File outputFile = new File("target/logcat/" + processName);
        outputFile.getParentFile().mkdirs();

        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(outputFile));

        pb.start();

        Future<Object> future = Patterns.ask(coordinator, new WaitUntilRegistered(processName), duration.toMillis());

        Object resp = Await.result(future, duration);
        if (resp instanceof ActorRef) {
            return (ActorRef) resp;
        } else {
            return null;
        }
    }
}