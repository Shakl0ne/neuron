package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.flow.Sink;
import com.wanmeizhensuo.streams.flow.WorkFlow;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.processor.internals.TaskManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  表对表同步封装
 */
@Slf4j
public class SyncStream {

    private String syncName;
    private String bootstrapServers;
    private Class<?> defaultKeySerde = Serdes.String().getClass();
    private Class<?> defaultValueSerde = Serdes.String().getClass();
    private int consumerCount = 1;
    private StreamsBuilder builder;
    private KafkaStreams kafkaStreams;
    /**
     *  异常回调函数
     */
    private SyncExceptionFallback fallback;
    /**
     *  计数器
     */
    private final AtomicLong counts = new AtomicLong(0);
    /**
     *  定时日志打印
     */
    private final ScheduledExecutorService scheduleLogger = Executors.newScheduledThreadPool(1);


    Sink workflow;

    public SyncStream(String syncName, Sink workflow){
        this.syncName = syncName;
        this.workflow = workflow;
    }

    /**
     *  配置kafka server
     * @param bootstrapServers
     * @return
     */
    public SyncStream bootstrapServers(String bootstrapServers){
        this.bootstrapServers = bootstrapServers;
        return this;
    }

    /**
     *  配置 key Serde
     * @param keySerde
     * @return
     */
    public SyncStream keySerde(Class<?> keySerde){
        this.defaultKeySerde = keySerde;
        return this;
    }

    public SyncStream valueSerde(Class<?> valueSerde){
        this.defaultValueSerde = valueSerde;
        return this;
    }


    /**
     *  设置消费者数量
     * @param count
     * @return
     */
    public SyncStream consumerCount(int count){
        this.consumerCount = count;
        return this;
    }

    /**
     *  配置异常自定义处理 回调可自行实现
     * @param fallback
     * @return
     */
    public SyncStream exceptionHandler(SyncExceptionFallback fallback){
        this.fallback = fallback;
        return this;
    }

    public SyncStream start(){
        this.kafkaStreams = new KafkaStreams(this.workflow.topology(), this.workflow.properties());
        this.kafkaStreams.start();
        record();
        log.info("SyncStream [{}] start success", syncName);
        return this;
    }

    public SyncStream stop(){
        this.kafkaStreams.close();
        scheduleLogger.shutdown();
        log.info("SyncStream [{}] stop success", syncName);
        return this;
    }

    /**
     *  定时打印当前任务执行了多少
     */
    private void record(){
        scheduleLogger.scheduleAtFixedRate(()->{
            log.info("The SyncJob [{}] has executed {} messages", syncName, counts.get());
        }, 10, 10 , TimeUnit.MINUTES);
    }


    public Long getCounts(){
        return counts.get();
    }

    public TaskManager getTaskManager(){
        return null;
    }

    public enum Operations{
        /**
         *  增量创建
         */
        CREATE_C("c"),
        /**
         *  存量创建
         */
        CREATE_R("r"),
        /**
         *  更新
         */
        UPDATE("u"),
        /**
         *  删除
         */
        DELETE("d");

        public String name;

        Operations(String name){
            this.name = name;
        }

        public static boolean contains(String name){
            for (Operations value : Operations.values()) {
                if(value.name.equals(name)){
                    return true;
                }
            }
            return false;
        }
    }



    @FunctionalInterface
    public interface SyncExceptionFallback{
        void handle(JsonObject message, Exception ex);
    }

}
