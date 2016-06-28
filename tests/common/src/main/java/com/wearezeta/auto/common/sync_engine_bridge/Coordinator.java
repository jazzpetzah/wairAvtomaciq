package com.wearezeta.auto.common.sync_engine_bridge;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.base.Throwables;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.waz.provision.CoordinatorActor;

public class Coordinator {
    
    private static Coordinator instance = null;
    private final ActorRef actorRef;

    protected Coordinator() {
        final Config config = ConfigFactory.load("actor_coordinator");
        final ActorSystem system = ActorSystem.create("CoordinatorSystem", config);
        this.actorRef = system.actorOf(Props.create(CoordinatorActor.class), "coordinatorActor");
    }
    

    public static synchronized Coordinator getInstance() {
        if (instance == null) {
            try {
                instance = new Coordinator();
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        }
        return instance;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }
    
    
    
}
