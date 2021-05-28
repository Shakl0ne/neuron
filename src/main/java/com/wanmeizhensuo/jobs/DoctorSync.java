package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
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
import java.util.List;

import static com.wanmeizhensuo.streams.flow.WorkFlow.*;

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
    private List<String> selectResult;
    private ImmutablePair<String, String> saveToResult;

/*    private void jsonParser(String fileName) throws Throwable {
        JSONParser jsonParser = new JSONParser();
        File jsonFile = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        try (FileReader reader = new FileReader(jsonFile)) {
            var file = jsonParser.parse(reader);
            var f = Json.decodeValue(file.toString());
            var state = new StreamState(f);

            switch (fileName) {
                case "flow-example.json"   : flowResult = new FlowParser().parse(state);
                case "from-example.json"   : fromResult = new FromParser().parse(state);
                case "select-example.json" : selectResult = new SelectParser().parse(state);
                case "saveTo-example.json" : saveToResult = new SaveToParser().parse(state);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    public void ApiDoctorSync(@Observes StartupEvent startupEvent) throws Throwable {
        JSONParser jsonParser = new JSONParser();
        File jsonFile = new File(this.getClass().getClassLoader().getResource("flow-example.json").getFile());
        try (FileReader reader = new FileReader(jsonFile)) {
            var file = jsonParser.parse(reader);
            var f = Json.decodeValue(file.toString());
            var state = new StreamState(f);
            var s = new StreamState(jsonFile);
            Sink workFlow = new SyncParser(gmmerchant).parse(s);
            workFlow.deploy(vertx);

        }

    }
}


