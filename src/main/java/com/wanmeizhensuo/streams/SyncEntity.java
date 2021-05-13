package com.wanmeizhensuo.streams;

import io.vertx.core.json.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncEntity {

    private Integer id;

    private String name;

    private JsonArray meta;

    private Integer status;

}

