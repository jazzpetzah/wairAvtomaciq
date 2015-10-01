package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.waz.model.RConvId;
import com.waz.provision.ActorMessage;
import com.waz.provision.ActorMessage.Login;
import com.waz.provision.ActorMessage.Successful;
import com.waz.provision.CoordinatorSystem;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RemoteProcessIPC {

    private static final FiniteDuration duration = new FiniteDuration(20000, TimeUnit.MILLISECONDS);
    private static final Timeout akkaTimeout = new Timeout(20000);
    private static final Map<String, ActorRef> remotes = new HashMap<>();

    static {
        System.out.println("Setting up coordinator system");
        CoordinatorSystem.setUpCoordinatorSystem("actor_coordinator");
    }

    public static void loginToRemoteProcess(ClientUser user) throws Exception {
        System.out.println("Logging into remote process with user : " + user.getName());
        ActorRef remote = CoordinatorSystem.registerProcess(user.getName(), false, duration, akkaTimeout);
        Future<Object> future = Patterns.ask(remote, new Login(user.getEmail(), user.getPassword()), duration.toMillis());

        Object resp = Await.result(future, duration);
        if (resp instanceof Successful) {
            System.out.println("Login successful");
            remotes.put(user.getName(), remote);
        } else {
            System.out.println("Login not successful, killing remote");
            remote.tell(ActorMessage.TerminateProcess$.MODULE$, null);
        }
    }

    public static void sendConversationMessage(ClientUser userFrom, String convId, String message) {
        System.out.println("Sending message: " + message + " from user: " + userFrom + " to conversation: " + convId);
        ActorRef remote = remotes.get(userFrom.getName());
        remote.tell(new ActorMessage.SendText(new RConvId(convId), message), null);
    }
}
