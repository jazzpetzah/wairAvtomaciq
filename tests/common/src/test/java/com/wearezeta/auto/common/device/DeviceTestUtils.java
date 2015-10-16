package com.wearezeta.auto.common.device;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.ActorMessage;
import com.waz.provision.CoordinatorActor;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.RegistrationStrategy;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.List;

/**
 *
 */
public class DeviceTestUtils {

    public static ActorRef createCoordinatorActor() {
        //TODO find out how to depend on resources from main
        Config config = ConfigFactory.load("actor_coordinator");
        ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        ActorRef actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");

        return actorRef;
    }

    public static Object askActor(ActorRef actorRef, ActorMessage message, FiniteDuration duration) {
        Future<Object> future = Patterns.ask(actorRef, message, duration.toMillis());
        Object resp = null;

        try {
            resp = Await.result(future, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }



    public static List<ClientUser> generateRandomUsers(int numUsers) {
        ClientUsersManager usersManager = ClientUsersManager.getInstance();

        try {
            usersManager.createUsersOnBackend(numUsers, RegistrationStrategy.ByEmailOnly);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersManager.getCreatedUsers();
    }
}
