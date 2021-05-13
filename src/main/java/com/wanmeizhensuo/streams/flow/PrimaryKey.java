package com.wanmeizhensuo.streams.flow;

import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/03/24 19:35
 */
public class PrimaryKey {
    Select select;

    public PrimaryKey(Select select) {
        this.select = select;
    }

    public Select int32(String name) {
        select.pKey.add(name);
        return select.int32(name);
    }
    public Select int32(String name, String column) {
        select.pKey.add(name);
        return select.int32(name, column);
    }

    public Select int32(String name, Function<JsonObject, Integer> func) {
        select.pKey.add(name);
        return select.int32(name, func);
    }

    public Select int64(String name) {
        select.pKey.add(name);
        return select.int64(name);
    }

    public Select int64(String name, String column) {
        select.pKey.add(name);
        return select.int64(name, column);
    }

    public Select int64(String name, Function<JsonObject, Long> func) {
        select.pKey.add(name);
        return select.int64(name, func);
    }

    public Select text(String name) {
        select.pKey.add(name);
        return select.text(name);
    }
    public Select text(String name, String column) {
        select.pKey.add(name);
        return select.text(name, column);
    }

    public Select text(String name, Function<JsonObject, String> func) {
        select.pKey.add(name);
        return select.text(name, func);
    }

}
