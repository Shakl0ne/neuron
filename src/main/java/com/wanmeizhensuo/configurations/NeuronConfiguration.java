package com.wanmeizhensuo.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/30 18:41
 */
@ConfigProperties(prefix="middleware.neuron")
public class NeuronConfiguration {
    @ConfigProperty(name="sync.timeout.seconds")
    public int syncTimeout;

    @ConfigProperty(name="batch-max")
    public int batchMax;

    @ConfigProperty(name="sync.freq.mills")
    public int freq;

    @ConfigProperty(name = "db-timeout")
    public Duration dbTimeout;

}
