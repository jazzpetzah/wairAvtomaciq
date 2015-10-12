package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.Login;
import com.waz.provision.ActorMessage.Successful$;
import com.waz.provision.CoordinatorSystem;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RemoteProcessIPC {

    private static final FiniteDuration duration = new FiniteDuration(30000, TimeUnit.MILLISECONDS);
    private static final Timeout akkaTimeout = new Timeout(30000);
    private static final Map<String, ActorRef> remotes = new HashMap<>();

    static {
        System.out.println("Setting up coordinator system");
        CoordinatorSystem.setUpCoordinatorSystem("actor_coordinator");
    }

    public static void startProcesses(int numProcesses) {

    }

    public static void loginToRemoteProcess(ClientUser user) throws Exception {
        System.out.println("Logging into remote process with user : " + user.getName());


        File testsFile = new File(System.getProperty("user.dir")).getParentFile();
        String hockeyLib = testsFile.getAbsolutePath() + "/common/lib/HockeySDK-3.6.0-rc.1.jar";
        System.out.println("HockeyLib: " + hockeyLib);

        String cp = System.getProperty("java.class.path") +
                ":" + hockeyLib +
                ":/Users/deancook/.m2/repository/com/ibm/icu/icu4j-shrunk/55.1/icu4j-shrunk-55.1.jar" +
                ":/Users/deancook/Documents/Github/zautomation/tests/android/target/classes/com/*" +
                ":/Users/deancook/Downloads/zmessaging-android-jar/cryptobox-jni-0.3.1.jar" +
                ":/Users/deancook/.m2/repository/com/wearezeta/generic-message-proto/1.3/generic-message-proto-1.3.jar" +
                ":/Users/deancook/.m2/repository/com/google/protobuf/protobuf-java/2.6.1/protobuf-java-2.6.1.jar";

        String lp = System.getProperty("java.library.path") +
                ":/Users/deancook/Downloads/zmessaging-android-jar/lib";



        ActorRef remote = CoordinatorSystem.registerProcess(user.getName(), true, duration, "staging", cp, lp, akkaTimeout);
        Future<Object> future = Patterns.ask(remote, new Login(user.getEmail(), user.getPassword()), duration.toMillis());

        Object resp = Await.result(future, duration);
        if (resp instanceof Successful$) {
            System.out.println("Login successful");
            remotes.put(user.getName(), remote);
        } else {
            System.out.println("Login not successful, killing remote");
            remote.tell(ActorMessage.TerminateProcess$.MODULE$, null);
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) throws Exception {
        System.out.println("Sending message: " + message + " from user: " + userFrom + " to conversation: " + convId);
        ActorRef remote = remotes.get(userFrom.getName());

        Future<Object> future = Patterns.ask(remote, new ActorMessage.SendText(new RConvId(convId), message), duration.toMillis());
        Object resp = Await.result(future, duration);

        System.out.println(resp.getClass());
    }
}