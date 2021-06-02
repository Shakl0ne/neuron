package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

public class Save2PGParserTests {
    @Test
    public void testSample0() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "source name";
        var parser = new Save2PGParser();
        Assert.assertEquals(res, parser.parse(state));
    }
}
