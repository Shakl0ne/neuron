package com.wanmeizhensuo.streams.flow;

import com.wanmeizhensuo.streams.parser.FlowParser;
import com.wanmeizhensuo.streams.parser.FromParser;
import com.wanmeizhensuo.streams.parser.SelectParser;
import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.State;
import org.apache.kafka.common.serialization.Serdes;

import java.util.ArrayList;
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




    WorkFlow(State<Token> s) throws Throwable {
        var flowParser = new FlowParser();

        this.name = flowParser.parse(s);
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

    public WorkFlow select(State<Token> s, Select select) throws Throwable {
        if (bootstrapServers == null) {
            throw new IllegalStateException("need bootstrap servers");
        }
        if (tpc == null) {
            throw new IllegalStateException("need topic");
        }
        var selectParser = new SelectParser();
        var selectResult = selectParser.parse(s);
        var result = Select.select();
        selectResult.forEach(
                str -> {
                    var res = select.defines.get(str);
                    if (!res.columnName().isEmpty()) {
                        result.defines.put(str, res);
                    }
                }
        );
        this.select = result;
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


    public static WorkFlow flow() {
        return new WorkFlow();
    }
}