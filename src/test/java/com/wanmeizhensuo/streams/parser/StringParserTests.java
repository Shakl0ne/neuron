package com.wanmeizhensuo.streams.parser;


import io.vertx.core.json.Json;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.Parsers.*;

public class StringParserTests implements Parsec<Token, List<String>>{

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
        var data = Json.decodeValue("\"'sub str\\\\'ing'\" ");
        var state = new StreamState(data);
        String result = "sub str'ing";
        var parser = new StringParser();
        Assert.assertEquals(result,parser.parse(state));
    }
    @Test
    public void testSample2() throws Throwable {
        var data = Json.decodeValue("\"'elon\\\\tmusk'\"");
        var state = new StreamState(data);
        String result = "elon\tmusk";
        var parser = new StringParser();
        Assert.assertEquals(result,parser.parse(state));
    }
    @Test
    public void testSample3() throws Throwable {
        var obj = new JSONParser().parse(new FileReader("src/test/resources/basic/simple-0.json"));
        var data = Json.decodeValue(obj.toString());
        var state = new StreamState(data);

        List<String> resList = new ArrayList<>();
        resList.add("sub");
        resList.add("content");

        Assert.assertEquals(resList, parse(state));
    }


    @Override
    public List<String> parse(State<Token> state) throws Throwable {
        var res = many1(new StringParser()).parse(state);
        while(res.remove(null));
        return res;
    }
}
