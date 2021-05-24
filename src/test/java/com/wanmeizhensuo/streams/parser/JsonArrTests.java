package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.streams.parser.common.JsonArr;
import io.vertx.core.json.Json;

import org.junit.Assert;
import org.junit.Test;

public class JsonArrTests {
    @Test
    public void testSample0() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, true,574,null]");
        var state = new StreamState(data);
        var parser = new FieldsParser();
        Object [] resArray ={"select",7.7,true,574,null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }
/*    @Test
    public void testSample1() throws Throwable {
        var data = Json.decodeValue("[\"select\",7.7, [true,574] ,null]");
        var state = new StreamState(data);
        var parser = new FieldsParser();
        Object [] resArray ={"select",7.7,true,574,null};
        Assert.assertArrayEquals(resArray,parser.parse(state).toArray());
    }*/
}
