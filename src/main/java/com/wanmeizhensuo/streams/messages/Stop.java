package com.wanmeizhensuo.streams.messages;

import lombok.*;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/17 16:08
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stop {
    String jobId;
}
