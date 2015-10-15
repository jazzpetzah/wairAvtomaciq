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

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RemoteProcessIPC {

    private static final FiniteDuration duration = new FiniteDuration(30000, TimeUnit.MILLISECONDS);
    private static final Timeout akkaTimeout = new Timeout(30000);
    private static final Map<String, ActorRef> unregisteredDevices = new HashMap<>();
    private static final Map<String, ActorRef> registeredDevices = new HashMap<>();
    private static final ActorRef coordinator = setUpCoordinator();

    private static final String REMOTE_DEVICE_NAME = "Remote_device_";

    static {
        System.out.println("Setting up coordinator system");
        CoordinatorSystem.setUpCoordinatorSystem("actor_coordinator");
    }

    public static void startDevices(int numDevices) throws Exception {
        ActorRef processActor = registerProcess(coordinator, "RemoteProcess_" + new Random().nextInt(100));

        //TODO do this with futures, perhaps?
        System.out.println("Generating " + numDevices + " remotes");
        for (int i = 0; i < numDevices; i++) {
            String deviceName = REMOTE_DEVICE_NAME + i;
            ActorRef deviceActor = registerDevice(processActor, deviceName);
            if (deviceActor == null) {
                throw new NullPointerException();
            } else {
                System.out.println(deviceName + " created successfully, adding to map");
                unregisteredDevices.put(deviceName, deviceActor);
            }
        }
    }

    public static void killAllProcesses() {
        //TODO could also do this with futures?
        coordinator.tell(ActorMessage.TerminateRemotes$.MODULE$, null);
    }

    public static void loginToRemoteProcess(ClientUser user) throws Exception {

        if (unregisteredDevices.size() == 0) {
            throw new Exception("No remotes left!");
        }

        String nextProcessKey = REMOTE_DEVICE_NAME + (unregisteredDevices.size() - 1);
        ActorRef deviceActor = unregisteredDevices.get(nextProcessKey);

        Future<Object> future = Patterns.ask(deviceActor, new Login(user.getEmail(), user.getPassword()), duration.toMillis());

        Object resp = Await.result(future, duration);
        System.out.println("Response type: " + resp.getClass());
        if (resp instanceof Successful$) {
            System.out.println("Login successful");
            registeredDevices.put(user.getName(), deviceActor);
            unregisteredDevices.remove(nextProcessKey);
            Thread.sleep(3000); //just to allow the actor time to sign in
        } else {
            System.out.println("Login not successful, killing remote");
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        System.out.println("Sending message: " + message + " from user: " + userFrom + " to conversation: " + convId);
        ActorRef remote = registeredDevices.get(userFrom.getName());

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

        System.out.println("Actors path?: " + getActorsJarLocation());
        String[] cmd = {"java", "-jar", getActorsJarLocation(), processName, serialized, "staging"};

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

    public static String getActorsJarLocation() throws URISyntaxException {
        File file = new File(ActorMessage.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        return file.toString();
    }

    public static ActorRef registerDevice(ActorRef processActor, String deviceName) throws Exception {
        Future<Object> future = Patterns.ask(processActor, new ActorMessage.SpawnRemoteDevice(null, deviceName), duration.toMillis());
        Object resp = Await.result(future, duration);
        if (resp instanceof ActorRef) {
            return (ActorRef) resp;
        } else {
            return null;
        }
    }
}