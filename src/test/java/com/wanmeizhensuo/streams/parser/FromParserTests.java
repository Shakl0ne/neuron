package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.sqlisp.ast.From;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Parsec;
import org.junit.Assert;
import org.junit.Test;
import com.wanmeizhensuo.streams.parser.FromParser;

import java.util.List;

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
        try{
            parser.parse(state);
        }catch (ParsecException e){
            System.out.println("passed");
        }
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("[\"from\",\"sample.topic1\",\"sample.topic2\"]");
        var state = new StreamState(data);
        var parser = new FromParser();
        try{
            parser.parse(state);
        }catch (ParsecException e){
            System.out.println("passed");
        }
    }
    @Test
    public void testSample3() throws Throwable {
        var data = Json.decodeValue("[\"from\",{ \"db\":\"sample.table.topic\"}]");
        var state = new StreamState(data);
        var parser = new FromParser();
        try{
            parser.parse(state);
        }catch (ParsecException e){
            System.out.println("passed");
        }
    }
    @Test
    public void testSample4() throws Throwable {
        var data = Json.decodeValue("[{\"db\":174},\"from\",\"sample.table.topic\"]");
        var state = new StreamState(data);
        var parser = new FromParser();
        try{
            parser.parse(state);
        }catch (ParsecException e){
            System.out.println("passed");
        }
    }

}
