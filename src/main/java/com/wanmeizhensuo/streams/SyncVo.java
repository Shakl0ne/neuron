package com.wanmeizhensuo.streams;

import com.wanmeizhensuo.data.ValidatorGroup;
import io.vertx.core.json.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncVo{

    /**
     *  同步名称
     */
    private String syncName = "Sync-Job";

    /**
     *  kafka
     */
    private String bootstrapServers;

    /**
     *  消费者数目
     */
    private int consumerCount = 1;

    private String from;

    /**
     *  sink 数据源  1 PG 2 Mysql
     */
    private Integer sinkPoolType = 1;

    private String database;

    /**
     *  sink schema名
     */
    private String schema = "public";

    private String table;

    private String applicationId;

    private List<Kv> select;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Kv {
        private String column;
        private String method;
    }

    public JsonArray toJsonArray(){

        var result = new JsonArray();

        result.add(new JsonArray(Arrays.asList("bootstrapServers", this.bootstrapServers)));
        result.add(new JsonArray(Arrays.asList("syncName", this.syncName)));
        result.add(new JsonArray(Arrays.asList("applicationId", this.applicationId)));
        result.add(new JsonArray(Arrays.asList("consumerCount", String.valueOf(this.consumerCount))));
        result.add(new JsonArray(Arrays.asList("sinkSchema", this.schema)));
        result.add(new JsonArray(Arrays.asList("sinkTable", this.table)));
        result.add(new JsonArray(Arrays.asList("sinkDatabase", this.database)));
        // topic
        result.add(new JsonArray(Arrays.asList("from", this.from)));
        // select
        if(Objects.nonNull(this.select) && select.size() > 0){
            var select = new JsonArray();
            select.add("select");
            result.add(select);
            // 字段方法
            this.select.forEach(kv->{
                result.add(new JsonArray(Arrays.asList(kv.getColumn(), kv.getMethod())));
            });
        }else {
            var select = new JsonArray();
            select.add("selectAll");
            result.add(select);
        }

        return result;
    }

}


