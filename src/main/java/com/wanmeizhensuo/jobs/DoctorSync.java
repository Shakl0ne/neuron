package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
import com.wanmeizhensuo.http.DoctorService;
import com.wanmeizhensuo.streams.Job;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.simple.parser.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileReader;
import java.util.List;

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
    @RestClient
    DoctorService doctorService;

    @Inject
    Vertx vertx;

    public void ApiDoctorSync(@Observes StartupEvent startupEvent) throws Throwable {
        var obj = new JSONParser().parse(new FileReader("/Users/Shaco/neuron/src/test/resources/basic/test-sample.json"));
        var data = Json.decodeValue(obj.toString());
        workFlow().flow(streamState(data))
                .bootstrapServers("localhost:9092")
                .topic(streamState(data))
                .groupId(groupConfiguration.doctorGroup)
                .consumers(1)
                .select(streamState(data))
                .saveTo().schema("").table("").pool(gmmerchant).deploy(vertx);

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<WorkFlow> postMutiny(WorkFlow flow) { return doctorService.postSync(flow);}


}


