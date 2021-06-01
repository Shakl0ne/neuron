package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;

import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\"]");
        var state = new StreamState(data);
        List<String> resList = new ArrayList<>();
        resList.add("field1");
        var parser = new SelectParser();
        Assert.assertEquals(resList,parser.parse(state));
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\",\"field2\"]");
        var state = new StreamState(data);
        List<String> resList = new ArrayList<>();
        resList.add("field1");
        resList.add("field2");
        var parser = new SelectParser();
        Assert.assertEquals(resList,parser.parse(state));
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\",[ \"field2\",\"field3\" ]]");
        var state = new StreamState(data);
        List<String> resList = new ArrayList<>();
        resList.add("field1");
        resList.add("[");
        resList.add("field2");
        resList.add("field3");
        resList.add("]");
        var parser = new SelectParser();
        Assert.assertEquals(resList,parser.parse(state));
    }

    @Test
    public void testSample3() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        var parser = new SelectParser();
        var res = parser.parse(state);
        String [] resArray = {"[, field1, field2, field3, [, named, field4, caption-0, ], [, named, [, replace, field5, 'sub', 'content', ], caption-1, ], ]"};
        System.out.println(res);
    }
    @Test
    public void testSample4() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/sample-elastic-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);
        var parser = new SelectParser();

        var res = parser.parse(state);
        System.out.println(res);
    }

}
