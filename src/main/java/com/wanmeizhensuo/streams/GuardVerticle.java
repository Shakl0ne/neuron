package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.streams.messages.Pause;
import com.wanmeizhensuo.streams.messages.Resume;
import com.wanmeizhensuo.streams.messages.Start;
import com.wanmeizhensuo.streams.messages.Stop;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.mutiny.core.eventbus.Message;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.function.Consumer;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/17 15:37
 */
@Slf4j
public class GuardVerticle extends AbstractVerticle {
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

    }

    @Override
    public Uni<Void> asyncStart() {
        String topic = ConfigProvider.getConfig().getValue("neuron.guard-topic", String.class);
        // String topic = "neuron.sync.guard";

        vertx.eventBus().consumer(topic,
                (Consumer<Message<Start>>) message -> log.info("try to start {} job", message.body().getDefine()));

        vertx.eventBus().consumer(topic,
                (Consumer<Message<Stop>>) message -> log.info("try to start {} job", message.body().getJobId()));

        vertx.eventBus().consumer(topic,
                (Consumer<Message<Pause>>) message -> log.info("try to start {} job", message.body().getJobId()));

        vertx.eventBus().consumer(topic,
                (Consumer<Message<Resume>>) message -> log.info("try to start {} job", message.body().getJobId()));

        return Uni.createFrom().voidItem();
    }
}
