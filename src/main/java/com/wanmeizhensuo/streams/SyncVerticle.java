package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.http.DoctorResource;
import com.wanmeizhensuo.streams.flow.Select;
import com.wanmeizhensuo.streams.flow.Sink;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import io.sentry.Sentry;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.kafka.client.consumer.KafkaConsumer;
import io.vertx.mutiny.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Transaction;
import io.vertx.mutiny.sqlclient.Tuple;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.microprofile.config.ConfigProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/27 15:28
 */
@Slf4j
public class SyncVerticle extends AbstractVerticle {
    Sink workflow;
    KafkaConsumer<Object, Object> consumer;
    boolean running = false;
    boolean success = false;
    String appName;
    String topic;

    Duration pullTimeout;
    int batchSize;
    int saveFreq;
    int pullFreq;

    LocalDateTime ticket = LocalDateTime.now();
    List<Pair<String, Tuple>> buffer = new ArrayList<>();
    List<Pair<String, Tuple>> preBuffer = new ArrayList<>();
    PgPool pgPool;

    long timerId;

    public SyncVerticle(Sink workflow) {

        this.workflow = workflow;
        this.topic = this.workflow.getFlow().getTopic();
        log.info("sync job {} select {} created", this.workflow.getFlow().getTopic(), this.workflow.getSelect());
    }


    @SneakyThrows
    @Override
    public void init(Vertx vertx, Context context) {

        super.init(vertx, context);

        var settings = workflow.settingsMap();

        consumer = KafkaConsumer.create(io.vertx.mutiny.core.Vertx.newInstance(vertx), settings);

//        log.info("sync job {} inited", this.workflow.getFlow().getTopic());
        appName = ConfigProvider.getConfig().getValue("quarkus.application.name", String.class);

        pullTimeout =
                Duration.ofMillis(ConfigProvider.getConfig().getValue("middleware.neuron.sync.pull-timeout.millis",
                        Integer.class));
        batchSize = ConfigProvider.getConfig().getValue("middleware.neuron.sync.batch-size",
                Integer.class);
        saveFreq = ConfigProvider.getConfig().getValue("middleware.neuron.sync.save.freq.seconds",
                Integer.class);
        pullFreq = ConfigProvider.getConfig().getValue("middleware.neuron.sync.pull.freq.millis",
                Integer.class);
    }

    @Override
    public Uni<Void> asyncStart() {
        //var delegate = workflow.getPool().getDelegate();
        //pgPool = PgPool.newInstance(delegate);
        pgPool = workflow.getPool();
        ticket = LocalDateTime.now();

        var topic = workflow.getFlow().getTopic();
        var sub = consumer.subscribe(topic);
        vertx.eventBus().localConsumer(topic).handler(message -> {
            this.commitIfy();
        });

        timerId = vertx.setPeriodic(pullFreq, id -> {
            if (!running) {
                vertx.eventBus().sendAndForget(topic, String.format("sync %s once", topic));
            }
        });
        consumer.handler(this::process);
        log.info("{} start with {}", workflow.getFlow().getName(), sub);
        return sub;
    }


    private void prepareBuffer() {
        preBuffer.clear();

        preBuffer.addAll(buffer);

        buffer.clear();
    }

    private void rollbackBuffer() {
        buffer.addAll(0, preBuffer);

        preBuffer.clear();
    }

    private void commitBuffer() {
        preBuffer.clear();
    }

    private int bufferSize() {
        return buffer.size();
    }

    private void commitIfy() {
        if ((bufferSize() >= batchSize || ticket.plusSeconds(saveFreq).isBefore(LocalDateTime.now())) && !running) {
            var bufferSize = bufferSize();
            info(bufferSize > 0, "{} commit jobs start of {} ticket {} ", bufferSize,this.workflow.getFlow().getTopic(), ticket);
//            log.info("{} commit jobs start of {} ticket {} ", bufferSize,
//                    this.workflow.getFlow().getTopic(), ticket);
            running = true;
            consumer.pause();
            this.save().onItem().transformToUni(Void -> this.doCommit()).subscribe().with(msg -> {
                info(bufferSize > 0, "sync {} once and {} data saved ", this.workflow.getFlow().getTopic(), bufferSize);
//                log.info("sync {} once and {} data saved ", this.workflow.getFlow().getTopic(), bufferSize);
                commitBuffer();
                running = false;
                success = true;
                consumer.resume();
            }, error -> {
                log.error("save records failed, topic, stop sync job {}", workflow.getFlow().getTopic(), error);
                Sentry.capture(error);
                rollbackBuffer();
                running = false;
                success = false;
                vertx.undeployAndForget(this.deploymentID());
            });
        }

    }

    private Uni<Void> doCommit() {
        if (success) {
            return consumer.commit().onItem().invoke(() -> {
//                var message = String.format("%d sync jobs of %s committed at %s",
//                        bufferSize(), this.workflow.getFlow().getTopic(), LocalDateTime.now()
//                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                ticket = LocalDateTime.now();
//            log.info(message);
                running = false;
            });
        } else {
            return Uni.createFrom().voidItem();
        }
    }

    private void process(KafkaConsumerRecord<Object, Object> record) {
        if (record.value() == null) {
            log.info("get a null message id {}, ignore it", record.value());
            return;
        }

        var syncRecord = record.key().toString()+" = "+record.value().toString();
        var doctorResource = new DoctorResource();
        doctorResource.post(syncRecord);
        System.out.println(syncRecord);
        JsonObject objKey = new JsonObject(record.key().toString());
        JsonObject objValue = new JsonObject(record.value().toString());
        JsonObject payload = objValue.getJsonObject("payload");
        var id = objKey.getValue("id");
        if (Objects.isNull(payload)) {
            payload = objValue;
        }
        if (Objects.isNull(id)) {
            id = objKey.getJsonObject("payload").getValue("id");
        }

        var flag = objValue.size() != 0 && Objects.nonNull(payload.getString("op"));
        if (!flag) {
            log.warn(" message {} structure invalid {}", id, payload.toString());
            return;
        }

        var operation = payload.getString("op");


        var select = this.workflow.getSelect();
        if (select == null) {
            select = Select.from(payload);
        }
        if (SyncStream.Operations.CREATE_C.name.equals(operation)
                || SyncStream.Operations.CREATE_R.name.equals(operation)) {
            buffer.add(Pair.of("create", select.upsertParameters(payload.getJsonObject("after"))));
        } else if (SyncStream.Operations.UPDATE.name.equals(operation)) {
            buffer.add(Pair.of("update", select.updateParameters(payload.getJsonObject("after"))));
        } else if (SyncStream.Operations.DELETE.name.equals(operation)) {
            buffer.add(Pair.of("delete", select.deleteParameters(payload.getJsonObject("before"))));
        } else {
            log.error("unknown operation {}", operation);
        }
        commitIfy();
    }


    public Uni<Void> save() {
        prepareBuffer();

        if (preBuffer.size() == 0) {
            return Uni.createFrom().voidItem();
        }

        var bufferSize = preBuffer.size();

        return pgPool.begin().onItem().transformToUni(tran -> {
            log.info("{} begin save for {}->{} data", workflow.getFlow().getName(), buffer.size(), preBuffer.size());
            Uni<?> result = Uni.createFrom().voidItem();
            List<Tuple> parameters = new ArrayList<>();
            int idx = 0;
            var current = preBuffer.get(0).getKey();
            do {
                var pair = preBuffer.get(idx);
                var op = pair.getKey();
                if (op.equals(current)) {
                    parameters.add(pair.getValue());
                }
                else {
                    log.info("save {} with {}", workflow.getFlow().getName(), current);
                    var params = new ArrayList<>(parameters);
                    var cur = current;
                    result = result.onItem().transformToUni(any -> saveBatch(tran, cur, params));
                    parameters.clear();
                    current = op;
                    parameters.add(pair.getValue());
                }
                idx++;
            } while (idx < preBuffer.size());

            if(parameters.size() > 0){
                String finalCurrent = current;
                result = result.onItem().transformToUni(any -> saveBatch(tran, finalCurrent, parameters));
            }

            return result
                    .onItem().transformToUni(any -> {
                        success = true;
                        return tran.commit().invoke(Void -> {
                            log.info("sync {} once and {} data committed ", this.workflow.getFlow().getTopic(), bufferSize);
                        });
                    })
                    .onFailure().recoverWithUni(error -> {
                        log.error("sync {} failed and {} data rollback ",
                                this.workflow.getFlow().getTopic(),
                                bufferSize, error);
                        success = false;
                        Sentry.capture(error);
                        return tran.rollback().onItem().transformToUni(Void->Uni.createFrom().failure(error));
                    });
        });
    }

    private Uni<?> saveBatch(Transaction tran, String operation, List<Tuple> parameters) {
        String sql = null;
        var select = workflow.getSelect();
        if ("create".equals(operation)) {
            sql = select.upsertSQL(workflow.tableName());
        } else if ("update".equals(operation)) {
            sql = select.updateSQL(workflow.tableName());
        } else if ("delete".equals(operation)) {
            sql = select.deleteSQL(workflow.tableName());
        } else {
            var message = String.format("unknown operation %s", operation);
            log.error(message);
            return Uni.createFrom().failure(new IllegalArgumentException(message));
        }
        log.info("Job:[{}], operation:[{}], sql:[{}], param:[{}]", this.workflow.getFlow().getName(), operation, sql, parameters.get(0).deepToString());
        return tran.preparedQuery(sql).executeBatch(parameters);
    }

    @Override
    public Uni<Void> asyncStop() {
        vertx.cancelTimer(timerId);
        pgPool.close();
        System.out.println(consumer);
        if (consumer != null) {
            return consumer.close();
        } else {
            return Uni.createFrom().voidItem();
        }
    }


    private void info(boolean enable, String format, Object ...params){
        if(enable){
            log.info(format, params);
        }
    }

}
