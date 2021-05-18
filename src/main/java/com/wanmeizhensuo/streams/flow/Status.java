package com.wanmeizhensuo.streams.flow;

import lombok.*;
import org.eclipse.microprofile.config.ConfigProvider;

import java.time.Duration;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/17 15:13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {
    float qps;
    Object lastMessageId;
    long count;
    long offset;
    String topic;
}
