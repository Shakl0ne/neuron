package com.wanmeizhensuo.streams.parser;

import io.vertx.core.json.Json;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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
        var data = Json.decodeValue("[ \"select\", [\"field1\",\"field2\",\"field3\",[\"named\",\"field4\",\"caption-0\"],[\"named\",[\"replace\",\"field5\",\"'sub'\", \"'content'\"],\"caption-1\"]]]");
        var state = new StreamState(data);
        var parser = new SelectParser();
        String [] resArray = {"[","field1","field2","field3","[","named","field4","caption-0","]","[","named","[","replace","field5","'sub'", "'content'","]","caption-1","]","]"};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }

}
