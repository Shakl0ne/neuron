package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.StreamState;
import com.wanmeizhensuo.streams.parser.common.SaveToAny;
import io.vertx.core.json.Json;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Assert;
import org.junit.Test;

public class SaveToAnyTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"saveTo()\",\"source name\"]");
        var state = new StreamState(data);
        var parser = new SaveToAny();
        try {
            var res = parser.parse(state);
        } catch (Throwable e) {
        }
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"saveTo\",\"source name\"]");
        var state = new StreamState(data);
        var parser = new SaveToAny();
        try {
            var res = parser.parse(state);
        } catch (Throwable e) {
        }
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"saveTo(PG)\",\"source name\"]");
        var state = new StreamState(data);
        ImmutablePair res = new ImmutablePair("PG","source name");
        var parser = new SaveToAny();
        Assert.assertEquals(res,parser.parse(state));
    }
    @Test
    public void testSample3() throws Throwable {
        var data = Json.decodeValue("[\"saveToES\",\"source name\"]");
        var state = new StreamState(data);
        ImmutablePair res = new ImmutablePair("ES","source name");
        var parser = new SaveToAny();
        Assert.assertEquals(res,parser.parse(state));
    }
    @Test
    public void testSample4() throws Throwable {
        var data = Json.decodeValue("[\"saveTo(db2)\",\"source name\"]");
        var state = new StreamState(data);
        ImmutablePair res = new ImmutablePair("db2","source name");
        var parser = new SaveToAny();
        Assert.assertEquals(res,parser.parse(state));
    }
    @Test
    public void testSample5() throws Throwable {
        var data = Json.decodeValue("[\"savTo(PG)\",\"source name\"]");
        var state = new StreamState(data);
        var parser = new SaveToAny();
        try {
            var res = parser.parse(state);
        } catch (Throwable e) {
        }
    }
    @Test
    public void testSample6() throws Throwable {
        var data = Json.decodeValue("[\"saveTo(PG)\",\"source name1\",\"source name2\"]");
        var state = new StreamState(data);
        var parser = new SaveToAny();
        try {
            var res = parser.parse(state);
        } catch (Throwable e) {
        }
    }
    @Test
    public void testSample7() throws Throwable {
        var data = Json.decodeValue("[\"saveTo(PG)\",44]");
        var state = new StreamState(data);
        var parser = new SaveToAny();
        try {
            var res = parser.parse(state);
        } catch (Throwable e) {
        }
    }
}
