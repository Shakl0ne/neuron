package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;

import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

public class FlowParserTests {
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"flow\",\"doctor_sync\"]");
        var state = new StreamState(data);
        String res = "doctor_sync";
        var parser = new FlowParser();
        Assert.assertEquals(res,parser.parse(state));
    }
    @Test
    public void testSample2() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "flow name";
        var parser = new FlowParser();
        Assert.assertEquals(res, parser.parse(state));
    }
    @Test
    public void testSample3() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "flow name";
        var parser = new FlowParser();
        Assert.assertEquals(res, parser.parse(state));
    }
}
