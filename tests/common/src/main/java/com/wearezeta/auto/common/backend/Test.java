package com.wearezeta.auto.common.backend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.waz.provision.RemoteActor;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello world");
        ActorSystem actorSystem = ActorSystem.create("Test System");
        ActorRef actorRef = actorSystem.actorOf(Props.create(RemoteActor.class));

        System.out.println("ActorRef created!: " + actorRef.toString());

//        Future<String> ask(actorRef, new ActorMessage.Echo("Hello", null), 1000);
    }
}
