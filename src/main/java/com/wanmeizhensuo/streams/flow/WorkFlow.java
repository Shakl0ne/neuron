package com.wanmeizhensuo.streams.flow;

import com.wanmeizhensuo.http.DoctorService;
import com.wanmeizhensuo.streams.parser.FlowParser;
import com.wanmeizhensuo.streams.parser.FromParser;
import com.wanmeizhensuo.streams.parser.SelectParser;
import com.wanmeizhensuo.streams.parser.Token;
import io.smallrye.mutiny.Uni;
import jaskell.parsec.common.State;
import org.apache.kafka.common.serialization.Serdes;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

public class WorkFlow {
    String name;
    Select select;
    String groupId;
    String bootstrapServers;

    Class<?> defaultKeySerde = Serdes.String().getClass();
    Class<?> defaultValueSerde = Serdes.String().getClass();

    int consumerCount = 1;
    String tpc = null;

    @Inject
    @RestClient
    DoctorService doctorService;

    public WorkFlow flow(State<Token> s) throws Throwable {
        var flowParser = new FlowParser();
        this.name = flowParser.parse(s);
        return this;
    }

    public WorkFlow() {
        this.name = "sync-stream-" + UUID.randomUUID().toString();
    }

    public WorkFlow topic(State<Token> s) throws Throwable {
        var fromParser = new FromParser();
        this.tpc = fromParser.parse(s);
        return this;
    }

    public WorkFlow bootstrapServers(String bootstrapServers){
        this.bootstrapServers = bootstrapServers;
        return this;
    }

    public WorkFlow defaultKeySerde(Class<?> defaultKeySerde) {
        this.defaultKeySerde = defaultKeySerde;
        return this;
    }

    public WorkFlow defaultValueSerde(Class<?> defaultValueSerde) {
        this.defaultValueSerde = defaultValueSerde;
        return this;
    }

    public WorkFlow groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public WorkFlow consumers(int count){
        this.consumerCount = count;
        return this;
    }

    public WorkFlow select(State<Token> s) throws Throwable {
        if (bootstrapServers == null) {
            throw new IllegalStateException("need bootstrap servers");
        }
        if (tpc == null) {
            throw new IllegalStateException("need topic");
        }
        var selectParser = new SelectParser();
        var selectResult = selectParser.parse(s);
        var sel = Select.select();
        selectResult.forEach(
                list -> {
                    if (list.size() > 2 && list.get(2).equals("Primary Key")) {
                        switch (list.get(1)) {
                            case "Integer32" : sel.pKey().int32(list.get(0)); break;
                            case "Integer64" : sel.pKey().int64(list.get(0)); break;
                            case "String"    : sel.pKey().text(list.get(0)); break;
                            case "Text"      : sel.pKey().text(list.get(0)); break;
                        }
                    }
                    else {
                        switch (list.get(1)) {
                            case "Integer32" : sel.int32(list.get(0)); break;
                            case "Integer64" : sel.int64(list.get(0)); break;
                            case "String"    : sel.text(list.get(0)); break;
                            case "Float"     : sel.flt(list.get(0)); break;
                            case "Double"    : sel.dbl(list.get(0)); break;
                            case "Text"      : sel.text(list.get(0)); break;
                        }
                    }
                }
        );

        this.select = sel;
        return this;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public WorkFlow post(WorkFlow flow) {
        flow.select.defines.values().forEach(de -> {
            doctorService.postSync(de);
        });
        return this;

    }


    public String getName() {
        return name;
    }

    public Sink saveTo() {
        return new Sink(this);
    }

    public String getTopic() {
        return tpc;
    }


    public static WorkFlow workFlow() {
        return new WorkFlow();
    }
}