package com.wanmeizhensuo.streams.parser;


import io.vertx.core.json.Json;

import org.junit.Assert;
import org.junit.Test;

public class StringParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("\"'sub'\" ");
        var state = new StreamState(data);
        String result = "sub";
        var parser = new StringParser();
        Assert.assertEquals(result,parser.parse(state));
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("\"'sub str'ing'\" ");
        var state = new StreamState(data);
        String result = "sub str'ing";
        var parser = new StringParser();
        Assert.assertEquals(result,parser.parse(state));
    }

}
