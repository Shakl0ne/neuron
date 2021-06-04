package com.wanmeizhensuo.streams.flow;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/24 20:06
 */
public class ColumnDefine {
    String key = null;
    String funcName;
    Function<JsonObject, ?> func;
    Type type;
    String column = null;

    public ColumnDefine(String key, String funcName, Function<JsonObject, ?> func, Type type) {
        this.key = key;
        this.funcName = funcName;
        this.func = func;
        this.type = type;
    }

    public String columnName() {
        if(StringUtils.isNoneBlank(column)){
            return column;
        } else {
            return key;
        }
    }

    public ColumnDefine(String key, String funcName, String column, Function<JsonObject, ?> func, Type type) {
        this.key = key;
        this.funcName = funcName;
        this.column = column;
        this.func = func;
        this.type = type;
    }

    public static enum Type {
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        DATE,
        TIMESTAMP,
        BOOLEAN,
        STRING,
    }
}
