package com.wanmeizhensuo.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "middleware.topic")
public class TopicConfiguration {

    @ConfigProperty(name = "doctor-topic")
    public String doctorTopic;

}
