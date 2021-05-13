package com.wanmeizhensuo.jobs;

import com.wanmeizhensuo.configurations.GroupConfiguration;
import com.wanmeizhensuo.configurations.StreamsConfiguration;
import com.wanmeizhensuo.configurations.TopicConfiguration;
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

    public void ApiDoctorSync(@Observes StartupEvent startupEvent) {
        SyncVerticle.flow("doctor_sync")
                .bootstrapServers(streamsConfiguration.bootstrapServers)
                .topic(topicConfiguration.doctorTopic)
                .groupId(groupConfiguration.doctorGroup)
                .consumers(1)
                .select(Select.select()
                    .pKey().int32("id")
                        .text("doctor_name")
                        .int32("tag_id")
                )

                .saveTo().schema("public").table("doctor_sync").pool(gmmerchant)
                .deploy(vertx);
    }

}
