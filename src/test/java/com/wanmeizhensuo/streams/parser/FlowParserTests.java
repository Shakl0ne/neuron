package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;

import org.junit.Assert;
import org.junit.Test;

public class FlowParserTests {
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"flow\",\"doctor_sync\"]");
        var state = new StreamState(data);
        String res = "doctor_sync";
        var parser = new FlowParser();
        Assert.assertEquals(res,parser.parse(state));
    }
}
