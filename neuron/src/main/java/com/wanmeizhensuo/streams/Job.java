package com.wanmeizhensuo.streams;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Slf4j
public class Job {

    @Inject
    public Vertx vertx;

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The " + "application is stoping...");
        vertx.deploymentIDs().forEach(vertx::undeployAndForget);
        Quarkus.asyncExit();
    }
}
