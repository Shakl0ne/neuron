package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
import com.wanmeizhensuo.streams.Job;
import com.wanmeizhensuo.streams.SyncVerticle;
import com.wanmeizhensuo.streams.flow.Select;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class CitySync extends Job {
    @Inject
    StreamsConfiguration streamsConfiguration;
    @Inject
    TopicConfiguration topicConfiguration;
    @Inject
    GroupConfiguration groupConfiguration;

    @Inject
    @ReactiveDataSource("gmmerchant")
    PgPool gmmerchant;

    @Inject
    Vertx vertx;

    public void CitySync(@Observes StartupEvent startupEvent) {
        SyncVerticle.flow("city_sync")
                .bootstrapServers(streamsConfiguration.bootstrapServers)
                .topic(topicConfiguration.cityTopic)
                .groupId(groupConfiguration.cityGroup)
                .consumers(1)
                .select(Select.select()
                        .pKey().text("id")
                        .text("name")
                        .int32("display_in_filter")
                        .text("province_id")
                        .int32("tag_id")
                        .int32("is_online")
                        .int32("level")
                )
                .saveTo().schema("public").table("city_sync").pool(gmmerchant)
                .deploy(vertx);
    }
}
