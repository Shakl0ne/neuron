package com.wanmeizhensuo.streams.parser;


import io.vertx.core.json.Json;
import jaskell.parsec.common.TxtState;
import org.junit.Assert;
import org.junit.Test;

public class StringParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("\"'sub'\" ");
        var state = new TxtState(data.toString());
        String result = "sub";
        var parser = new StringParser();
        Assert.assertEquals(result,parser.parse(state));

    }
}
