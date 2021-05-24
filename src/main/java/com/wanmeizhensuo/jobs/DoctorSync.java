package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
import com.wanmeizhensuo.streams.SyncVerticle;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.parser.*;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@Slf4j
@ApplicationScoped
public class DoctorSync {

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
    private String flowResult;
    private String fromResult;
    private List<Object> selectResult;
    private ImmutablePair<String, String> saveToResult;

    public void testSample0() throws Throwable {
        var jsonFile = Json.decodeValue("[\"flow\",\"doctor_sync\", [\"from\",\"topicConfiguration.doctorTopic\"]]");
        var flowState = new StreamState(jsonFile);
        var flowParser = new FlowParser();
        var flowResult = flowParser.parse(flowState);
        this.flowResult = flowResult;

        var fromFile = Json.decodeValue(("[\"from\",\"topicConfiguration.doctorTopic\"]"));
        var fromState = new StreamState(fromFile);
        var fromParser = new FromParser();
        var fromResult = fromParser.parse(fromState);
        this.fromResult = fromResult;

        var selectFile = Json.decodeValue("[\"select\",\"id\",\"doctor_name\",\"tag_id\"]");
        var selectState = new StreamState(selectFile);
        var selectParser = new SelectParser();
        var selectResult = selectParser.parse(selectState);
        this.selectResult = selectResult;

        var saveToFile = Json.decodeValue("[\"saveTo(PG)\", \"public.doctor_sync\"]");
        var saveToState = new StreamState(saveToFile);
        var saveToParser = new SaveToParser();
        var saveToResult = saveToParser.parse(saveToState);
        this.saveToResult = saveToResult;
    }
    public void ApiDoctorSync(@Observes StartupEvent startupEvent) {
        SyncVerticle.flow(flowResult)
                .topic(fromResult)
                .select(Select.select()
                    .pKey().int32(selectResult.get(0).toString())
                        .text(selectResult.get(1).toString())
                        .int32(selectResult.get(2).toString()))
                .saveTo().table(saveToResult.getRight());
    }

}
