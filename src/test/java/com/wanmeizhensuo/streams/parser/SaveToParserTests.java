package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Assert;
import org.junit.Test;

public class SaveToParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"saveTo(PG)\",\"source name\"]");
        var state = new StreamState(data);
        ImmutablePair res = new ImmutablePair("saveTo(PG)","source name");
        var parser = new SaveToParser();
        Assert.assertEquals(res,parser.parse(state));
    }
}
