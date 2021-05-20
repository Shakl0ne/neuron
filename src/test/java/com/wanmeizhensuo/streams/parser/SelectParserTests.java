package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.sqlisp.ast.From;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Parsec;
import org.junit.Assert;
import org.junit.Test;
import com.wanmeizhensuo.streams.parser.SelectParser;

import java.util.ArrayList;
import java.util.List;
public class SelectParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\"]");
        var state = new StreamState(data);
        List<Object> resList = new ArrayList<Object>();
        resList.add("field1");
        var parser = new SelectParser();
        Assert.assertEquals(resList,parser.parse(state));
    }
    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\",\"field2\"]");
        var state = new StreamState(data);
        List<Object> resList = new ArrayList<Object>();
        resList.add("field1");
        resList.add("field2");
        var parser = new SelectParser();
        Assert.assertEquals(resList,parser.parse(state));
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"select\",\"field1\",[ \"field2\",\"field3\" ]]");
        var state = new StreamState(data);
        var parser = new SelectParser();
        System.out.println(parser.parse(state));
    }

}
