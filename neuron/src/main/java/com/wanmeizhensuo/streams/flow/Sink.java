package com.wanmeizhensuo.streams.flow;

import com.wanmeizhensuo.streams.SyncStream;
import com.wanmeizhensuo.streams.SyncVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/24 21:38
 */
@Slf4j
public class Sink {
    WorkFlow flow;
    String schema;
    String table;
    PgPool pool;
    String deploymentID;

    public WorkFlow getFlow() {
        return flow;
    }

    public Sink(WorkFlow prev) {
        this.flow = prev;
        this.deploymentID = "";
    }

    public Sink schema(String schema) {
        this.schema = schema;
        return this;
    }

    public Sink table(String table) {
        this.table = table;
        return this;
    }

    public Sink pool(PgPool pool) {
        this.pool = pool;
        return this;
    }

    public String tableName() {
        if (schema == null) {
            return table;
        } else {
            return String.format("%s.%s", schema, table);
        }
    }

    public Topology topology() {
        if (table == null) {
            throw new IllegalArgumentException("Table name must been define");
        }
        var builder = new StreamsBuilder();
        builder.stream(flow.tpc)
                .filter((message1, message2) -> Objects.nonNull(message1) && Objects.nonNull(message2))
                .map((message1, message2) -> {
                    JsonObject obj1 = new JsonObject(message1.toString());
                    JsonObject obj2 = new JsonObject(message2.toString());
                    var id = obj1.getValue("id");
                    if (Objects.isNull(id)) {
                        obj2 = obj2.getJsonObject("payload");
                        id = obj1.getJsonObject("payload").getValue("id");
                    }
                    return KeyValue.pair(id, obj2);
                }).filter((id, data) -> {
            var flag = !data.isEmpty() && Objects.nonNull(data.getString("op"));
            if (!flag) {
                log.info("filter message :{}", data.toString());
            }
            return flag;
        }).foreach((id, body) -> {
            var operation = body.getString("op");
            String sql = null;
            Tuple parameters = null;
            if (!SyncStream.Operations.contains(operation)) {
                return;
            }
            var select = flow.select;
            if (select == null) {
                select = Select.from(body);
            }
            if (SyncStream.Operations.CREATE_C.name.equals(operation)
                    || SyncStream.Operations.CREATE_R.name.equals(operation)) {
                sql = select.upsertSQL(tableName());
                parameters = select.upsertParameters(body.getJsonObject("after"));
            } else if (SyncStream.Operations.UPDATE.name.equals(operation)) {
                sql = select.updateSQL(tableName());
                parameters = select.updateParameters(body.getJsonObject("after"));

            } else if (SyncStream.Operations.DELETE.name.equals(operation)) {
                sql = select.deleteSQL(tableName());
                parameters = select.deleteParameters(body.getJsonObject("before"));
            } else {
                log.error("unknown operation {}", operation);
            }

            String finalSql = sql;
            Tuple finalParameters = parameters;
            pool.preparedQuery(sql).execute(parameters).onItem().invoke(rs -> {
                log.debug("data {} synced success", body);
            }).ifNoItem().after(Duration.ofMillis(1000))
                    .recoverWithItem(() -> {
                        log.error("operation timeout, sql [{}]", finalSql);
                        return null;
                    })
                    .onFailure()
                    .recoverWithItem(e -> {
                        log.error("operation error sql [{}], param[{}]:", finalSql, finalParameters.deepToString(), e);
                        return null;
                    }).await().indefinitely();
        });

        return builder.build();
    }

    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, flow.bootstrapServers);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, flow.defaultKeySerde);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, flow.defaultValueSerde);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, flow.groupId);
        if (flow.consumerCount > 1) {
            properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, flow);
        }
        return properties;
    }

    public Map<String, String> settingsMap() throws UnknownHostException {
        Map<String, String> result = new HashMap<>();
        result.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, flow.bootstrapServers);
        result.put(ConsumerConfig.GROUP_ID_CONFIG, flow.groupId);
        result.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        result.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        result.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        result.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return result;
    }

    public SyncStream sync() {
        return new SyncStream(flow.name, this);
    }

    public void deploy(Vertx vertx) {
        var limit = ConfigProvider.getConfig().getOptionalValue("middleware.neuron.sync.limit",
                String.class);
        var name = this.flow.getName();

        var flag = false;

        if (limit.isPresent()) {
            var limitSet =
                    Arrays.stream(limit.get().split(",")).map(String::trim).collect(Collectors.toSet());
            if (limitSet.contains(name)) {
                flag = true;
            } else {
                log.info("job {} not included in limit set [{}], exist", name, limit);
            }
        } else {
            flag = true;
        }

        if (flag) {
            vertx.deployVerticle(new SyncVerticle(this))
                    .subscribe().with(deploymentId -> {
                log.info("{} start as {}", name, deploymentId);
                this.deploymentID = deploymentId;
            }, error -> {
                log.error("{} start failed", name, error);
            });

        }
    }

    public void undeploy(Vertx vertx){
        vertx.undeploy(this.deploymentID);
    }

    public Select getSelect() {
        return flow.select;
    }


    public PgPool getPool() {
        return pool;
    }
}
