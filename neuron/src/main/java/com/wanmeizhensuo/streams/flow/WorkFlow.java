package com.wanmeizhensuo.streams.flow;

import org.apache.kafka.common.serialization.Serdes;

import java.util.UUID;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/24 18:27
 */
public class WorkFlow {
    String name;
    Select select;

    String groupId;
    /**
     *  kafka servers
     */
    String bootstrapServers;
    /**
     *
     */
    Class<?> defaultKeySerde = Serdes.String().getClass();
    /**
     *
     */
    Class<?> defaultValueSerde = Serdes.String().getClass();
    /**
     *  消费者数目
     */
    int consumerCount = 1;

    String tpc = null;

    WorkFlow(String name) {
        this.name = name;
    }

    WorkFlow() {
        this.name = "sync-stream-"+ UUID.randomUUID().toString();
    }

    public WorkFlow topic(String topic) {
        this.tpc = topic;
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

    public WorkFlow select(Select select) {
        if(bootstrapServers == null){
            throw new IllegalStateException("need bootstrap servers");
        }
        if(tpc == null){
            throw new IllegalStateException("need topic");
        }
        this.select = select;
        this.select.prev = this;
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

    public static WorkFlow flow(String name) {
        return new WorkFlow(name);
    }

    public static WorkFlow flow() {
        return new WorkFlow();
    }
}

