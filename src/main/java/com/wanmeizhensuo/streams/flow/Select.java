package com.wanmeizhensuo.streams.flow;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/24 18:36
 */
@Slf4j
public class Select {
    WorkFlow prev;

    LinkedHashMap<String, ColumnDefine> defines = new LinkedHashMap<>();
    LinkedHashSet<String> pKey = new LinkedHashSet<>();

    public Select identity(String column) {
        defines.put(column, new ColumnDefine(column, column,
                obj -> obj.getValue(column), ColumnDefine.Type.ANY));
        return this;
    }

    public Select int32(String name, String column) {
        defines.put(name, new ColumnDefine(name, "int32", column,
                obj -> obj.getInteger(name), ColumnDefine.Type.INTEGER));
        return this;
    }

    public Select int32(String name, Function<JsonObject, Integer> func) {
        defines.put(name, new ColumnDefine(name, "int32", func, ColumnDefine.Type.INTEGER));
        return this;
    }

    public Select int32(String name, String column, Function<JsonObject, Integer> func) {
        defines.put(name, new ColumnDefine(name, "int32", column, func, ColumnDefine.Type.INTEGER));
        return this;
    }

    public Select int64(String name, String column) {
        defines.put(name,
                new ColumnDefine(name, "int64", column,
                        obj -> obj.getLong(name), ColumnDefine.Type.LONG));
        return this;
    }

    public Select int64(String name, Function<JsonObject, Long> func) {
        defines.put(name, new ColumnDefine(name, "int64", func, ColumnDefine.Type.LONG));
        return this;
    }

    public Select int64(String name, String column, Function<JsonObject, Long> func) {
        defines.put(name, new ColumnDefine(name, "int64", column, func, ColumnDefine.Type.LONG));
        return this;
    }

    public Select flt(String name, Function<JsonObject, Float> func) {
        defines.put(name, new ColumnDefine(name, "float", func, ColumnDefine.Type.FLOAT));
        return this;
    }

    public Select dbl(String name, Function<JsonObject, Double> func) {
        defines.put(name, new ColumnDefine(name, "double", func, ColumnDefine.Type.DOUBLE));
        return this;
    }

    public Select text(String name, String column) {
        defines.put(name, new ColumnDefine(name, "text", column,
                obj -> {
                    try {
                        return obj.getString(name);
                    } catch (Exception error) {
                        log.error("try get string value {} from {} but failed",
                                name, obj);
                        throw error;
                    }
                }, ColumnDefine.Type.STRING));
        return this;
    }

    public Select text(String name, Function<JsonObject, String> func) {
        defines.put(name, new ColumnDefine(name, "text", func, ColumnDefine.Type.STRING));
        return this;
    }


    public Select bool(String name, Function<JsonObject, Boolean> func) {
        defines.put(name, new ColumnDefine(name, "bool", func, ColumnDefine.Type.BOOLEAN));
        return this;
    }

    public Select int2Bool(String name, Function<JsonObject, Boolean> func) {
        defines.put(name, new ColumnDefine(name, "int2Bool", func, ColumnDefine.Type.BOOLEAN));
        return this;
    }

    public Select long2Timestamp(String name, Function<JsonObject, LocalDateTime> func) {
        defines.put(name, new ColumnDefine(name, "long2Timestamp", func, ColumnDefine.Type.TIMESTAMP));
        return this;
    }

    public Select millSecond2Date(String name, Function<JsonObject, LocalDate> func) {
        defines.put(name, new ColumnDefine(name, "millSecond2Date", func, ColumnDefine.Type.DATE));
        return this;
    }

    public Select millSecond2Timestamp(String name, Function<JsonObject, LocalDateTime> func) {
        defines.put(name, new ColumnDefine(name, "millSecond2Timestamp", func, ColumnDefine.Type.TIMESTAMP));
        return this;
    }

    public Select microSecond2Timestamp(String name, Function<JsonObject, LocalDateTime> func) {
        defines.put(name, new ColumnDefine(name, "microSecond2Timestamp", func, ColumnDefine.Type.TIMESTAMP));
        return this;
    }

    public PrimaryKey pKey() {
        return new PrimaryKey(this);
    }

    public Tuple upsertParameters(JsonObject data) {
        var re = Tuple.tuple();
        defines.forEach((key, define) -> {
            addParameter(re, define, data);
        });
        defines.forEach((key, define) -> {
            if (pKey.contains(key)) {
                return;
            }
            addParameter(re, define, data);
        });
        return re;
    }

    public Tuple updateParameters(JsonObject data) {
        var re = Tuple.tuple();
        defines.forEach((key, define) -> {
            addParameter(re, define, data);
        });
        pKey.forEach(key -> {
            addParameter(re, defines.get(key), data);
        });
        return re;
    }

    public Tuple deleteParameters(JsonObject data) {
        var re = Tuple.tuple();
        pKey.forEach(key -> {
            addParameter(re, defines.get(key), data);
        });
        return re;
    }

    void addParameter(Tuple params, ColumnDefine define, JsonObject data) {

        switch (define.type) {
            case INTEGER:
                params.addInteger(extractValue(data, define.func, Integer.class));
                return;
            case LONG:
                params.addLong(extractValue(data, define.func, Long.class));
                return;
            case FLOAT:
                params.addFloat(extractValue(data, define.func, Float.class));
                return;
            case DOUBLE:
                params.addDouble(extractValue(data, define.func, Double.class));
                return;
            case TIMESTAMP:
                params.addLocalDateTime(extractValue(data, define.func, LocalDateTime.class));
                return;
            case BOOLEAN:
                params.addBoolean(extractValue(data, define.func, Boolean.class));
                return;
            case STRING:
                params.addString(extractValue(data, define.func, String.class));
                return;
            case DATE:
                params.addLocalDate(extractValue(data, define.func, LocalDate.class));
        }
    }

    public String updateSQL(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName).append(" set ");
        int i = 0;
        List<String> columns = new ArrayList<>();
        for (String column : defines.keySet()) {
            i++;
            var c = defines.get(column).columnName();
            columns.add(String.format("%s=$%d", c, i));
        }
        sql.append(String.join(", ", columns));
        sql.append(" where ");
        List<String> primaryKey = new ArrayList<>();
        for (String column : pKey) {
            i++;
            var c = defines.get(column).columnName();
            primaryKey.add(String.format("%s=$%d", c, i));
        }
        sql.append(String.join(" and ", primaryKey));
        return sql.toString();
    }

    public String upsertSQL(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append("(");
        List<String> columns = defines.keySet().stream().map(k -> defines.get(k).columnName())
                .collect(Collectors.toList());
        sql.append(String.join(", ", columns));
        sql.append(") values (");
        List<String> values = new ArrayList<>();
        int i = 0;
        for (var column : defines.keySet()) {
            i++;
            values.add(String.format("$%d", i));
        }
        sql.append(String.join(", ", values));
        sql.append(") on conflict(");
        List<String> primaryKey = pKey.stream().map(k -> defines.get(k).columnName()).collect(Collectors.toList());
        sql.append(String.join(", ", primaryKey)).append(") do update set ");
        List<String> expr = new ArrayList<>();
        for (String column : defines.keySet()) {
            if (pKey.contains(column)) {
                continue;
            }
            i++;
            var c = defines.get(column).columnName();
            expr.add(String.format("%s=$%d", c, i));
        }
        sql.append(String.join(", ", expr));
        return sql.toString();
    }

    public String deleteSQL(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableName).append(" where ");
        int i = 0;
        List<String> expr = new ArrayList<>();
        for (String key : pKey) {
            i++;
            var c = defines.get(key).columnName();
            expr.add(String.format("%s=$%d", c, i));
        }
        sql.append(String.join(" and ", expr));
        return sql.toString();
    }

    <T> T extractValue(JsonObject data, Function<JsonObject, ?> func, Class<T> cls) {
        var re = func.apply(data);
        if (re == null) {
            return null;
        } else {
            return cls.cast(re);
        }
    }

    public static Select select() {
        return new Select();
    }

    public static Select from(JsonObject body) {
        JsonObject data;
        if (body.containsKey("after") && (body.getJsonObject("after") != null)) {
            data = body.getJsonObject("after");
        } else if (body.containsKey("before") && (body.getJsonObject("before") != null)) {
            data = body.getJsonObject("before");
        } else {
            throw new IllegalArgumentException(String.format("unknown data %s", body.toString()));
        }
        var result = Select.select();

        data.forEach(entry -> {
            result.auto(entry.getKey(), entry.getValue());
            if (entry.getValue() == null) {
                return;
            }
            if (entry.getKey().equals("id")) {
                result.pKey.add("id");
            }
        });
        return result;
    }

    Select auto(String name, Object value) {
        if (value instanceof Integer) {
            return this.int32(name);
        }
        if (value instanceof Long) {
            return this.int64(name);
        }
        if (value instanceof String) {
            return this.text(name);
        }
        if (value instanceof Float) {
            return this.flt(name);
        }
        if (value instanceof Double) {
            return this.dbl(name);
        }
        if (value instanceof Byte) {
            return this.int32(name);
        }

        return this.text(name, obj -> {
            if (obj.getJsonObject(name) != null) {
                return obj.getJsonObject(name).toString();
            } else {
                return "";
            }
        });
    }

    public Select int32(String name) {
        return int32(name, obj -> obj.getInteger(name, 0));
    }

    public Select int64(String name) {
        return int64(name, obj -> obj.getLong(name, 0L));
    }

    public Select int64ToMicroSecond(String name){
        return int64(name, obj ->{
            var timestamp = obj.getLong(name, 0L);
            if(timestamp != null && timestamp != 0){
                timestamp = timestamp - 28800000000L;
            }
            return timestamp;
        });
    }

    public Select int64ToMilliSecond(String name){
        return int64(name, obj ->{
            var timestamp = obj.getLong(name, 0L);
            if(timestamp != null && timestamp != 0){
                timestamp = timestamp - 28800000;
            }
            return timestamp;
        });
    }

    public Select flt(String name) {
        return flt(name, obj -> obj.getFloat(name, 0f));
    }

    public Select dbl(String name) {
        return dbl(name, obj -> obj.getDouble(name, 0d));
    }

    public Select text(String name) {
        return text(name, obj -> obj.getString(name, ""));
    }

    public Select bool(String name) {
        return bool(name, obj -> obj.getBoolean(name, null));
    }

    public Select int2Bool(String name) {
        return int2Bool(name, obj -> {
            if (obj.containsKey(name) && obj.getValue(name) != null) {
                return BooleanUtils.toBoolean(obj.getInteger(name));
            } else {
                return null;
            }
        });
    }

    public Select int2Bool(String name, boolean defaultValue) {
        return int2Bool(name, obj -> {
            if (obj.containsKey(name) && obj.getValue(name) != null) {
                return BooleanUtils.toBoolean(obj.getInteger(name));
            } else {
                return defaultValue;
            }
        });
    }

    public Select millSecond2Date(String name) {
        return millSecond2Date(name,
                obj -> {
                    if (obj.getLong(name, 0L) != null)
                        return LocalDate.ofEpochDay(obj.getLong(name, 0L));
                    return null;
                });
    }

    public Select millSecond2Timestamp(String name) {
        return millSecond2Timestamp(name,
                obj -> LocalDateTime.ofEpochSecond(
                        obj.getLong(name, 0L) / 1000, 0,
                        ZoneOffset.UTC));
    }

    public Select microSecond2Timestamp(String name) {
        return microSecond2Timestamp(name,
                obj -> LocalDateTime.ofEpochSecond(
                        obj.getLong(name, 0L) / 1_000_000, 0,
                        ZoneOffset.UTC));
    }


    public Select millSecond2TimestampWithZone(String name) {
        return millSecond2Timestamp(name,
                obj -> {
                    if (obj.getLong(name, 0L) != null)
                        return LocalDateTime.ofEpochSecond(
                                obj.getLong(name, 0L) / 1000 - 28800, 0,
                                ZoneOffset.ofHours(8));
                    return null;
                });
    }

    public Select microSecond2TimestampWithZone(String name) {
        return microSecond2Timestamp(name,
                obj -> {
                    if (obj.getLong(name, 0L) != null)
                        return LocalDateTime.ofEpochSecond(
                                obj.getLong(name, 0L) / 1_000_000 - 28800, 0,
                                ZoneOffset.ofHours(8));
                    return null;
                });
    }

    public Select millSecond2TimestampWithZone(String name, ZoneOffset zoneOffset) {
        return millSecond2Timestamp(name,
                obj -> LocalDateTime.ofEpochSecond(
                        obj.getLong(name, 0L) / 1000, 0,
                        zoneOffset));
    }

    public Select microSecond2TimestampWithZone(String name, ZoneOffset zoneOffset) {
        return microSecond2Timestamp(name,
                obj -> LocalDateTime.ofEpochSecond(
                        obj.getLong(name, 0L) / 1_000_000, 0,
                        zoneOffset));
    }
}
