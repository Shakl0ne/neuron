package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
import com.wanmeizhensuo.streams.Job;
import com.wanmeizhensuo.streams.SyncParser;
import com.wanmeizhensuo.streams.SyncVerticle;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.flow.Sink;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import com.wanmeizhensuo.streams.parser.*;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.simple.*;
import org.json.simple.parser.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import static com.wanmeizhensuo.streams.flow.WorkFlow.*;

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
        var obj = new JSONParser().parse(new FileReader("/Users/Shaco/neuron/src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var s = new StreamState(data);

    }


}


