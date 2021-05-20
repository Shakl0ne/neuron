package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.sqlisp.ast.From;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Parsec;
import org.junit.Assert;
import org.junit.Test;
import com.wanmeizhensuo.streams.parser.FromParser;

import java.util.ArrayList;
import java.util.List;
public class FieldParserTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, true,574,null]");
        var state = new StreamState(data);
        var parser = new FieldParser();
        Object [] resArray ={"select",7.7,true,574,null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
}
