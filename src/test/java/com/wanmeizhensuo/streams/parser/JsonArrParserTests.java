package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.streams.parser.combination.JsonArrParser;
import io.vertx.core.json.Json;

import org.junit.Assert;
import org.junit.Test;

public class JsonArrParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, true,574,null]");
        var state = new StreamState(data);
        var parser = new JsonArrParser();
        Object [] resArray ={"select",7.7,true,574,null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, [true,574] ,null]");
        var state = new StreamState(data);
        var parser = new JsonArrParser();
        Object [] resArray ={"select",7.7,"[",true,574,"]",null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, [true,[1,2,3],574] ,null, [\"option\"]]");
        var state = new StreamState(data);
        var parser = new JsonArrParser();
        Object [] resArray ={"select",7.7,"[",true,"[",1,2,3,"]",574,"]",null,"[","option","]"};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
    @Test
    public void testSample3() throws Throwable {
        var data = Json.decodeValue("[ [7.7, true,574] ,null]");
        var state = new StreamState(data);
        var parser = new JsonArrParser();
        Object [] resArray ={"[",7.7,true,574,"]",null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
}
