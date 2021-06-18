package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
import com.wanmeizhensuo.streams.Job;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.FileReader;

import static com.wanmeizhensuo.streams.flow.WorkFlow.workFlow;
import static com.wanmeizhensuo.streams.parser.StreamState.streamState;

@Slf4j
@ApplicationScoped
public class DoctorSync extends Job {

    @Inject
    TopicConfiguration topicConfiguration;
    @Inject
    StreamsConfiguration streamsConfiguration;
    @Inject
    GroupConfiguration groupConfiguration;

    @Inject
    @ReactiveDataSource("gmmerchant")
    PgPool gmmerchant;



    @Inject
    Vertx vertx;

    public void ApiDoctorSync(@Observes StartupEvent startupEvent) throws Throwable {
        var obj = new JSONParser().parse(new FileReader("/Users/Shaco/neuron/src/test/resources/basic/test-sample.json"));
        var data = Json.decodeValue(obj.toString());
        workFlow().flow(streamState(data))
                .bootstrapServers(streamsConfiguration.bootstrapServers)
                .topic(streamState(data))
                .groupId(groupConfiguration.doctorGroup)
                .consumers(1)
                .select(streamState(data))
                .saveTo().schema("public").table("doctor_sync").pool(gmmerchant).deploy(vertx);

    }




}


