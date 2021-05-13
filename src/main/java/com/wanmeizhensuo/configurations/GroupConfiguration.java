package com.wanmeizhensuo.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "middleware.group")
public class GroupConfiguration {

    @ConfigProperty(name = "doctor-group")
    public String doctorGroup;

    @ConfigProperty(name = "city-group")
    public String cityGroup;
}
