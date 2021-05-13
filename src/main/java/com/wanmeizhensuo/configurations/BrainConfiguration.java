package com.wanmeizhensuo.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;


/**
 * 与 Brain 相关的配置项
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/02/22 22:00
 */
@ConfigProperties(prefix = "middleware.brain")
public class BrainConfiguration {
    @ConfigProperty(name = "host")
    public String host;
    @ConfigProperty(name = "port")
    public Integer port;

    @ConfigProperty(name = "timeout")
    public Integer timeout;
}
