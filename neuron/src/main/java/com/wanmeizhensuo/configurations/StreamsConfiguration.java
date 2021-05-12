package com.wanmeizhensuo.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "quarkus.kafka-streams")
public class StreamsConfiguration {
    @ConfigProperty(name="bootstrap-servers")
    public String bootstrapServers;
}
