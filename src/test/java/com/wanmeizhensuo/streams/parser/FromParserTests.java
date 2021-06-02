package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;
import jaskell.parsec.ParsecException;

import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

public class FromParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"from\",\"simple.database.table.topic\"]");
        var state = new StreamState(data);
        String res = "simple.database.table.topic";
        var parser = new FromParser();
        Assert.assertEquals(res,parser.parse(state));
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"from\",758]");
        var state = new StreamState(data);
        var parser = new FromParser();
        try {
            parser.parse(state);
        }
        catch (ParsecException e) {
        }
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"from\",\"sample.topic1\",\"sample.topic2\"]");
        var state = new StreamState(data);
        var parser = new FromParser();
        try {
            parser.parse(state);
        }
        catch (ParsecException e) {
        }
    }
    @Test
    public void testSample3() throws Throwable {
        var data = Json.decodeValue("[\"from\",{ \"db\":\"sample.table.topic\"}]");
        var state = new StreamState(data);
        var parser = new FlowParser();
        try {
            parser.parse(state);
        }
        catch (ParsecException e) {
        }
    }
    @Test
    public void testSample4() throws Throwable {
        var data = Json.decodeValue("[{\"db\":174},\"from\",\"sample.table.topic\"]");
        var state = new StreamState(data);
        var parser = new FromParser();
        String res = "sample.table.topic";
        Assert.assertEquals(res, parser.parse(state));
    }
    @Test
    public void testSample5() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "simple.database.table.topic";
        var parser = new FromParser();
        Assert.assertEquals(res, parser.parse(state));
    }
    @Test
    public void testSample6() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        String res = "simple.database.table.topic";
        var parser = new FromParser();
        Assert.assertEquals(res, parser.parse(state));
    }
}
