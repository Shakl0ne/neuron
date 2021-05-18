package com.wanmeizhensuo.streams.messages;

import io.vertx.core.json.JsonObject;
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
public class Start {
    JsonObject define;
}
